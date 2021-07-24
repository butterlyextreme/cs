package org.task.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.task.data.entity.ExecutionsEntity;
import org.task.data.entity.OrdersEntity;
import org.task.data.model.Order;
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
import java.util.stream.Stream;

import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderBookStatisticsServiceImplTest {

    public static final int MARKET_ORDER = -1;
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
        when(ordersRepository.findAll()).thenReturn(generateOrders());
        List<OrderBookStat> stats = orderBookStatisticsService.retrieveAllOrderBookStats();
        String result = "[OrderBookStat(id=INSTRUMENT_0NE, lastOrder=2020-12-22T10:16:30Z, firstOrder=2020-12-22T10:15:30Z, biggest=70, smallest=50, demand=120, executionPrice=0, accumulatedExecution=0, limitBreakdown={100=120}), OrderBookStat(id=INSTRUMENT_TWO, lastOrder=2020-12-22T10:25:30Z, firstOrder=2020-12-22T10:15:30Z, biggest=10, smallest=10, demand=20, executionPrice=0, accumulatedExecution=0, limitBreakdown={70=10, 1000=10})]";
        assertEquals(result, stats.toString());
    }

    @Test
    void testRetrieveExecutedOrders() {
        when(ordersRepository.findAll()).thenReturn(generateOrders());
        when(executionsRepository.findAll()).thenReturn(generateExecutions(INSTRUMENT_ONE));
        List<OrderBookStat> stats = orderBookStatisticsService.retrieveAllOrderBookStatsExecuted();
        String result = "[OrderBookStat(id=INSTRUMENT_0NE, lastOrder=2020-12-22T10:16:30Z, firstOrder=2020-12-22T10:15:30Z, biggest=70, smallest=50, demand=120, executionPrice=100, accumulatedExecution=120, limitBreakdown={100=120})]";
        assertEquals(result, stats.toString());
    }

    @ParameterizedTest
    @MethodSource("provisionOrderTypes")
    void testRetrieveExecutedOrder(Integer orderPrice, int executionPrice, String expected) {
        UUID id = UUID.fromString("e4944dab-2846-44d5-891a-0ba3a78fb1cc");
        OrdersEntity ordersEntity = OrdersEntity.builder()
                .id(id)
                .instrumentId(INSTRUMENT_ONE)
                .date(Instant.now(clock))
                .price(orderPrice)
                .quantity(50)
                .build();

        ExecutionsEntity executionsEntity = ExecutionsEntity.builder()
                .id(UUID.randomUUID())
                .instrumentId(INSTRUMENT_ONE)
                .quantity(70)
                .price(executionPrice)
                .build();

        when(ordersRepository.findAll()).thenReturn(Arrays.asList(ordersEntity));
        when(executionsRepository.findAll()).thenReturn(Arrays.asList(executionsEntity));

        Order order = orderBookStatisticsService.getOrderById(id);

        assertEquals(order.toString(), expected);

    }

    private static Stream<Arguments> provisionOrderTypes() {
        return Stream.of(
                Arguments.of(100, 70, "Order(id=e4944dab-2846-44d5-891a-0ba3a78fb1cc, instrumentId=INSTRUMENT_0NE, price=100, quantity=50, date=2020-12-22T10:15:30Z, valid=true, ExecutionPrice=70, executedQuantity=50)"),
                Arguments.of(100, 100, "Order(id=e4944dab-2846-44d5-891a-0ba3a78fb1cc, instrumentId=INSTRUMENT_0NE, price=100, quantity=50, date=2020-12-22T10:15:30Z, valid=true, ExecutionPrice=100, executedQuantity=50)"),
                Arguments.of(70, 100, "Order(id=e4944dab-2846-44d5-891a-0ba3a78fb1cc, instrumentId=INSTRUMENT_0NE, price=70, quantity=50, date=2020-12-22T10:15:30Z, valid=false, ExecutionPrice=0, executedQuantity=0)"),
                Arguments.of(MARKET_ORDER, 100, "Order(id=e4944dab-2846-44d5-891a-0ba3a78fb1cc, instrumentId=INSTRUMENT_0NE, price=-1, quantity=50, date=2020-12-22T10:15:30Z, valid=true, ExecutionPrice=100, executedQuantity=50)")
        );
    }

    private List<OrdersEntity> generateOrders() {
        List<OrdersEntity> ordersEntities = new ArrayList<>();

        ordersEntities.add(OrdersEntity.builder()
                .id(UUID.randomUUID())
                .instrumentId(INSTRUMENT_ONE)
                .date(Instant.now(clock))
                .price(100)
                .quantity(50)
                .build());

        ordersEntities.add(OrdersEntity.builder()
                .id(UUID.randomUUID())
                .instrumentId(INSTRUMENT_ONE)
                .date(Instant.now(clock).plus(1, ChronoUnit.MINUTES))
                .price(100)
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
