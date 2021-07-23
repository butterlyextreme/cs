package org.task.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.task.data.model.OrderBookStat;
import org.task.data.service.OrderBookStatisticsService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderBookStatisticsService orderBookStatisticsService;

    @GetMapping(value = "/stats")
    public ResponseEntity<List<OrderBookStat>> getStats() {
        return new ResponseEntity<>(orderBookStatisticsService.retrieveAllOrderBookStats(), HttpStatus.OK);
    }

}
