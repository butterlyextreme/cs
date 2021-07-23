package org.task.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.task.data.entity.ExecutionsEntity;
import org.task.data.entity.OrdersEntity;
import org.task.data.model.OrderBookStat;
import org.task.data.respository.ExecutionsRepository;
import org.task.data.respository.OrdersRepository;
import org.task.data.service.OrderBookStatisticsServiceImpl;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.lang.Boolean.TRUE;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderBookStatisticsServiceImplTest {

    final String INSTRUMENT_ONE = "INSTRUMENT_0NE";
    final String INSTRUMENT_TWO = "INSTRUMENT_TWO";

    List<String> instruments = Arrays.asList(INSTRUMENT_ONE, INSTRUMENT_TWO);

    String firstOrderTime = "2020-12-22T10:15:30Z";
    Clock clock = Clock.fixed(Instant.parse(firstOrderTime), ZoneId.of("UTC"));

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private ExecutionsRepository executionsRepository;

    @InjectMocks
    private OrderBookStatisticsServiceImpl orderBookStatisticsService;

    @Test
    void testRetrieveOrdersNoFilter() {
        when(ordersRepository.findAll()).thenReturn(generateOrders(null));
        List<OrderBookStat> stats = orderBookStatisticsService.retrieveAllOrderBookStats();
        String result = "[OrderBookStat(id=INSTRUMENT_0NE, lastOrder=2020-12-22T10:16:30Z, firstOrder=1970-01-01T00:00:00Z, biggest=70, smallest=50, demand=120, limitBreakdown={100=120}, accumulatedExecution=null), OrderBookStat(id=INSTRUMENT_TWO, lastOrder=2020-12-22T10:25:30Z, firstOrder=1970-01-01T00:00:00Z, biggest=10, smallest=10, demand=20, limitBreakdown={70=10, 1000=10}, accumulatedExecution=null)]";

//        assertEquals(result, stats.toString());

        System.err.println(stats);
    }

    @Test
    void testRetrieveExecutedOrders() {
        when(ordersRepository.findAll()).thenReturn(generateOrders(TRUE));
        when(executionsRepository.findAll()).thenReturn(generateExecutions(INSTRUMENT_ONE));
        List<OrderBookStat> stats = orderBookStatisticsService.retrieveAllOrderBookStatsExecuted();
        String result = "[OrderBookStat(id=INSTRUMENT_0NE, lastOrder=2020-12-22T10:16:30Z, firstOrder=1970-01-01T00:00:00Z, biggest=70, smallest=50, demand=120, limitBreakdown={100=120}, accumulatedExecution={100=70, 75=70})]";

        System.err.println(stats);

       // assertEquals(result, stats.toString());
    }

    private List<OrdersEntity> generateOrders(Boolean filter) {
        List<OrdersEntity> ordersEntities = new ArrayList<>();

        ordersEntities.add(OrdersEntity.builder()
                .id(UUID.randomUUID())
                .instrumentId(INSTRUMENT_ONE)
                .date(Instant.now(clock))
                .price(100)
                .invalid(filter)
                .quantity(50)
                .build());

        ordersEntities.add(OrdersEntity.builder()
                .id(UUID.randomUUID())
                .instrumentId(INSTRUMENT_ONE)
                .date(Instant.now(clock).plus(1, ChronoUnit.MINUTES))
                .price(100)
                .invalid(filter)
                .quantity(70)
                .build());

        ordersEntities.add(OrdersEntity.builder()
                .id(UUID.randomUUID())
                .instrumentId(INSTRUMENT_TWO)
                .date(Instant.now(clock))
                .price(70)
                .quantity(10)
                .build());

        ordersEntities.add(OrdersEntity.builder()
                .id(UUID.randomUUID())
                .instrumentId(INSTRUMENT_TWO)
                .date(Instant.now(clock).plus(10, ChronoUnit.MINUTES))
                .price(1000)
                .quantity(10)
                .build());

        return ordersEntities;
    }

    private List<ExecutionsEntity> generateExecutions(String instrumentId) {
        List<ExecutionsEntity> executionsEntities = new ArrayList<>();

        executionsEntities.add(ExecutionsEntity.builder()
                .id(UUID.randomUUID())
                .instrumentId(instrumentId)
                .quantity(70)
                .price(100)
                .build());

        executionsEntities.add(ExecutionsEntity.builder()
                .id(UUID.randomUUID())
                .instrumentId(instrumentId)
                .quantity(70)
                .price(100)
                .build());

        return executionsEntities;

    }

}
