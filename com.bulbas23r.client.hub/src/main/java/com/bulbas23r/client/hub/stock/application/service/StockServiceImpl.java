package com.bulbas23r.client.hub.stock.application.service;

import com.bulbas23r.client.hub.stock.domain.model.Stock;
import com.bulbas23r.client.hub.stock.domain.repository.StockRepository;
import com.bulbas23r.client.hub.stock.infrastructure.messaging.TransactionEventProducer;
import common.event.CreateOrderEventDto;
import common.event.CreateProductEventDto;
import common.event.OrderProductEventDto;
import common.exception.BadRequestException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final TransactionEventProducer transactionEventProducer;

    @Override
    @Transactional
    public Stock createStock(CreateProductEventDto eventDto) {
        Stock stock = Stock.fromEventDto(eventDto);

        return stockRepository.save(stock);
    }

    @Override
    @Transactional
    public void updateStock(CreateOrderEventDto eventDto) {
        List<UUID> productIds =
            eventDto.getProducts().stream().map(OrderProductEventDto::getProductId).toList();
        List<Stock> stockList =
            stockRepository.findByHubIdAndProductId(eventDto.getHubId(), productIds);

        eventDto.getProducts().forEach(dto ->
            stockList.stream()
                .filter(stock -> dto.getProductId().equals(stock.getId().getProductId()))
                .findFirst()
                .ifPresent(stock -> {
                    int reducedQuantity = stock.getQuantity() - dto.getQuantity();
                    if (reducedQuantity < 0) {
                        log.error("허브의 재고를 초과한 주문");
                        transactionEventProducer.sendOrderFailedEvent(eventDto.getOrderId());
                        throw new BadRequestException("허브의 재고를 초과한 주문입니다!");
                    } else {
                        stock.setQuantity(reducedQuantity);
                    }
                })
        );
    }
}
