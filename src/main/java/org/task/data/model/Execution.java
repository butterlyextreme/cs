package org.task.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Execution {

    String instrumentId;
    int price;
    int quantity;
}
