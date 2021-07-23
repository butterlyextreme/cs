package org.task.data.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class Order {

    UUID id;
    String instrumentId;
    Integer price;
    int quantity;
    Instant date;

    boolean valid;
    int ExecutionPrice;
    int executedQuantity;



}
