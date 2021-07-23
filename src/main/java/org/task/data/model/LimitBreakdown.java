package org.task.data.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LimitBreakdown {
  final int price;
  final int quantity;
}
