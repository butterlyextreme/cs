package org.task.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.task.data.entity.OrderBookEntity;
import org.task.data.respository.OrderBookRepository;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

import static java.lang.String.format;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderBookServiceImpl implements OrderBookService {

    private final OrderBookRepository orderBookRepository;

    public String createBook(final String instrumentId) {
        log.debug("Creating order book for instrument [{}]", instrumentId);
        OrderBookEntity orderBookEntity = new OrderBookEntity();
        orderBookEntity.setInstrumentId(instrumentId);
        orderBookEntity.setState("OPEN");
        orderBookEntity = orderBookRepository.save(orderBookEntity);
        log.info("Successfully created order book for instrument ID [{}]", instrumentId);
        return orderBookEntity.getId().toString();
    }

    public void closeBook(UUID id) {
        OrderBookEntity orderBookEntity = orderBookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        format("OrderBook with id: %s has not been found", id)));

        orderBookEntity.setState("CLOSED");
        orderBookRepository.save(orderBookEntity);
        log.debug("Successfully closed order book for comment ID [{}]", id);
    }

}


