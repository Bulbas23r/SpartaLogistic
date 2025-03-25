package com.bulbas23r.client.hub.route.domain.service;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.route.domain.model.Route;
import common.exception.InternalServerErrorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PathFinderServiceImpl implements PathFinderService {

    static class Node implements Comparable<Node> {

        UUID id;
        int cost;

        public Node(UUID id, int cost) {
            this.id = id;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.cost, o.cost);
        }
    }

    @Override
    public List<UUID> findShortestPath(List<Hub> hubs, List<Route> routes, UUID departureHubId,
        UUID arrivalHubId) {

        if (hubs.isEmpty() || routes.isEmpty()) {
            throw new InternalServerErrorException("허브나 경로 데이터가 존재하지 않습니다!");
        }

        Map<UUID, List<Node>> graph = hubs.stream()
            .collect(Collectors.toMap(Hub::getId, hub -> new ArrayList<>()));

        for (Route route : routes) {
            graph.get(route.getId().getDepartureHubId())
                .add(new Node(route.getId().getArrivalHubId(), route.getTransitDistance()));
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(departureHubId, 0));

        Map<UUID, Integer> distance = hubs.stream()
            .collect(Collectors.toMap(Hub::getId, hub -> Integer.MAX_VALUE));
        distance.put(departureHubId, 0);
        Map<UUID, UUID> previous = new HashMap<>();

        while (!pq.isEmpty()) {
            Node cur = pq.poll();

            if (distance.get(cur.id) < cur.cost) {
                continue;
            }

            for (Node next : graph.get(cur.id)) {
                Integer cost = distance.get(cur.id) + next.cost;
                if (distance.get(next.id) > cost) {
                    distance.put(next.id, cost);
                    previous.put(next.id, cur.id);
                    pq.offer(new Node(next.id, cost));
                }
            }
        }

        List<UUID> path = new ArrayList<>();
        UUID current = arrivalHubId;

        while (current != null && current != departureHubId) {
            path.add(current);
            current = previous.get(current);
        }
        path.add(departureHubId);

        Collections.reverse(path);

        return path.get(0).equals(departureHubId) ? path : List.of();
    }
}
