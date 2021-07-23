package org.task.data.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Builder
@Data
public class OrderBookStat {
    @JsonProperty("instrument")
    final String id;
    @JsonProperty("last_order_entry")
    final Instant lastOrder;
    @JsonProperty("earliest_order_entry")
    final Instant firstOrder;
    final int biggest;
    final int smallest;
    final int demand;
    @JsonProperty("Execution_Price")
    final int executionPrice;
    @JsonProperty("Accumulated_Execution")
    final int accumulatedExecution;

    @JsonProperty("limit_breakdown")
    Map<Integer, Integer> limitBreakdown;

}
