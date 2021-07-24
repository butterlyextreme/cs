package org.task.data.service;

import org.task.data.model.Order;
import org.task.data.model.OrderBookStat;

import java.util.List;
import java.util.UUID;

public interface OrderBookStatisticsService {

    List<OrderBookStat> retrieveAllOrderBookStats();

    List<OrderBookStat> retrieveAllOrderBookStatsExecuted();

    Order getOrderById(final UUID id);
}
