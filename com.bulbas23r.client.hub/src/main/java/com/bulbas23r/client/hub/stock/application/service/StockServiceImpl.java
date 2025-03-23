package com.bulbas23r.client.hub.stock.application.service;

import com.bulbas23r.client.hub.stock.domain.model.Stock;
import com.bulbas23r.client.hub.stock.domain.repository.StockRepository;
import common.event.CreateStockEventDto;
import common.event.OrderProductEventDto;
import common.event.UpdateStockEventDto;
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

    @Override
    @Transactional
    public Stock createStock(CreateStockEventDto eventDto) {
        Stock stock = Stock.fromEventDto(eventDto);

        return stockRepository.save(stock);
    }

    @Override
    @Transactional
    public void updateStock(UpdateStockEventDto eventDto) {
        List<UUID> itemIds =
            eventDto.getProducts().stream().map(OrderProductEventDto::getProductId).toList();
        List<Stock> stockList =
            stockRepository.findById_HubIdAndId_ItemIdIn(eventDto.getHubId(), itemIds);

        eventDto.getProducts().forEach(dto ->
            stockList.stream()
                .filter(stock -> dto.getProductId().equals(stock.getId().getProductId()))
                .findFirst()
                .ifPresent(stock -> {
                    int reducedQuantity = stock.getQuantity() - dto.getQuantity();
                    if (reducedQuantity < 0) {
                        log.error("허브의 재고를 초과한 주문");
                        // TODO 주문 실패 처리 핸들링
                    } else {
                        stock.setQuantity(reducedQuantity);
                    }
                })
        );
    }

    @Override
    @Transactional
    public void deleteStocksByHubId(UUID hubId) {
        List<Stock> stockList = stockRepository.findAllByHubId(hubId);

        stockList.forEach(Stock::setDeleted);
    }

    @Override
    public void deleteStocksByProductId(UUID productId) {
        List<Stock> stockList = stockRepository.findAllByProductId(productId);

        stockList.forEach(Stock::setDeleted);
    }
}
