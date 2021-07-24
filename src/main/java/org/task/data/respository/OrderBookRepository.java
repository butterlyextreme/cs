package org.task.data.respository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.task.data.entity.OrderBookEntity;
import org.task.data.entity.OrdersEntity;

import java.util.List;
import java.util.UUID;


public interface OrderBookRepository extends JpaRepository<OrderBookEntity, UUID> {

    List<OrderBookEntity> findByState(String state);

}
