package org.task.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.task.data.entity.ExecutionsEntity;
import org.task.data.entity.OrdersEntity;
import org.task.data.model.Order;
import org.task.data.model.OrderBookStat;
import org.task.data.respository.ExecutionsRepository;
import org.task.data.respository.OrdersRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderBookStatisticsServiceImpl implements OrderBookStatisticsService {

    private final OrdersRepository ordersRepository;
    private final ExecutionsRepository executionsRepository;

    public List<OrderBookStat> retrieveAllOrderBookStats() {
        log.debug("About to retrieve order book stats for all order books");

        List<OrderBookStat> stats = getOrderBookStats(ordersRepository.findAll()
                .stream()
                .map(this::toOrder)
                .collect(Collectors.toList()));

        log.info("Completed the retrieval of the order book stats for all order books");
        return stats;
    }

    public List<OrderBookStat> retrieveAllOrderBookStatsExecuted() {
        log.debug("About to retrieve order book stats for orders with executions");

        final List<Order> allOrders = processOrderExecutions();
        List<OrderBookStat> stats = getOrderBookStats(allOrders);

        log.info("Completed the retrieval of the order book stats for orders with executions");
        return stats;
    }

    public Order getOrderById(UUID id){
        log.debug("About to retrieve order for id [{}]",id);
        return processOrderExecutions().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst().orElseThrow(() -> new RuntimeException("Order not found for id :" +  id));
    }

    private List<Order> processOrderExecutions() {
        Map<String, List<Order>> orderMap = ordersRepository.findAll()
                .stream()
                .map(this::toOrder)
                .collect(Collectors.toList()).stream()
                .collect(Collectors.groupingBy(Order::getInstrumentId));

        final List<Order> allOrders = new ArrayList<>();

        orderMap.forEach((k, v) -> {
            // Retrieve the total price execution demand
            Map<Integer, Integer> totalPriceExecutionDemand = toAccumulatedExecution(k);

            // Only execute orders if we have executions
            if (totalPriceExecutionDemand.size() == 1) {

                // Get orders per instrument
                List<Order> orders = orderMap.get(k);
                Map.Entry<Integer, Integer> entry = totalPriceExecutionDemand.entrySet().iterator().next();

                allOrders.addAll(orders.stream()
                        .map(o -> executeOrder(o, entry)).collect(Collectors.toList()));
            }
        });
        return allOrders;
    }

    private List<OrderBookStat> getOrderBookStats(List<Order> allOrders) {
        return allOrders.stream()
                .collect(Collectors.groupingBy(Order::getInstrumentId))
                .entrySet().stream()
                .map(e -> toOrderBookStatics(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private Order executeOrder(Order order, Map.Entry<Integer, Integer> totalExecutionDemand) {
        int demand = totalExecutionDemand.getValue();
        int price = totalExecutionDemand.getKey();
        int quantity = order.getQuantity();
        order.setValid(true);

        if (order.getPrice() < price) {
            order.setValid(false);
            return order;
        }
        if (demand == 0) {
            return order;
        }
        if (demand > order.getQuantity()) {
            order.setExecutedQuantity(quantity);
            demand -= quantity;
        } else {
            order.setExecutedQuantity(demand);
            demand = 0;
        }
        totalExecutionDemand.setValue(demand);
        order.setExecutionPrice(price);

        return order;
    }

    private OrderBookStat toOrderBookStatics(final String id, final List<Order> orders) {
        int smallestOrder = 0;
        int biggestOrder = 0;
        Instant lastOrder = Instant.EPOCH;
        Instant firstOrder = Instant.EPOCH;
        int demand = 0;
        int accumulatedExecution = 0;
        int executionPrice = 0;

        for (Order order : orders) {
            Instant orderDate = order.getDate();
            int quantity = order.getQuantity();

            if (quantity > biggestOrder) {
                biggestOrder = quantity;
            }
            if (quantity < smallestOrder || smallestOrder == 0) {
                smallestOrder = quantity;
            }
            if (orderDate.isAfter(lastOrder)) {
                lastOrder = orderDate;
            }
            if (orderDate.isBefore(firstOrder)) {
                firstOrder = orderDate;
            }
            demand += quantity;
            accumulatedExecution += order.getExecutedQuantity();
            executionPrice = Math.max(executionPrice, order.getExecutionPrice());
        }

        Map<Integer, Integer> limitBreakdown = orders.stream()
                .collect(Collectors.groupingBy(Order::getPrice,
                        Collectors.summingInt(Order::getQuantity)));

        return OrderBookStat.builder().id(id)
                .biggest(biggestOrder)
                .smallest(smallestOrder)
                .demand(demand)
                .firstOrder(firstOrder)
                .lastOrder(lastOrder)
                .limitBreakdown(limitBreakdown)
                .accumulatedExecution(accumulatedExecution)
                .executionPrice(executionPrice)
                .build();
    }

    private Map<Integer, Integer> toAccumulatedExecution(final String id) {
        return executionsRepository.findAll()
                .stream()
                .filter(e -> e.getInstrumentId().equals(id))
                .collect(Collectors.groupingBy(ExecutionsEntity::getPrice,
                        Collectors.summingInt(ExecutionsEntity::getQuantity)));
    }

    private Order toOrder(final OrdersEntity ordersEntity) {
        return Order.builder().instrumentId(ordersEntity.getInstrumentId())
                .id(ordersEntity.getId())
                .date(ordersEntity.getDate())
                .price(ordersEntity.getPrice())
                .quantity(ordersEntity.getQuantity())
                .build();
    }
}
