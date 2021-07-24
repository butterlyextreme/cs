package org.task.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.task.data.model.Order;
import org.task.data.model.OrderBookStat;
import org.task.data.service.OrderBookService;
import org.task.data.service.OrderBookStatisticsService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderBookController {

    private final OrderBookStatisticsService orderBookStatisticsService;
    private final OrderBookService orderBookService;

    @GetMapping(value = "/stats")
    public ResponseEntity<List<OrderBookStat>> getStats(@RequestParam(required = false) Boolean closed) {
        if (closed != null) {
            return new ResponseEntity<>(orderBookStatisticsService.retrieveAllOrderBookStatsExecuted(), HttpStatus.OK);
        }
        return new ResponseEntity<>(orderBookStatisticsService.retrieveAllOrderBookStats(), HttpStatus.OK);
    }

    @GetMapping(value = "/order/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable final UUID id) {
        Order order = orderBookStatisticsService.getOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping(value = "/orderbook/{instrumentId}")
    public ResponseEntity<String> createBook(
            @PathVariable final String instrumentId) {
        String id = orderBookService.createBook(instrumentId);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/orderbook/{id}")
    public ResponseEntity<String> closeBook(
            @PathVariable final UUID id) {
        orderBookService.closeBook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
