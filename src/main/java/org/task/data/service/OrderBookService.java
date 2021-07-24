package org.task.data.service;

import java.util.UUID;

public interface OrderBookService {

    String createBook(String instrumentId);

    void closeBook(UUID id);

}
