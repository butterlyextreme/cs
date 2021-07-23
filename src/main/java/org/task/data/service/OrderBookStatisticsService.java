package org.task.data.service;

import org.task.data.model.OrderBookStat;

import java.util.List;

public interface OrderBookStatisticsService {

    List<OrderBookStat> retrieveAllOrderBookStats();
}
