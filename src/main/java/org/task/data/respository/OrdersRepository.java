package org.task.data.respository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.task.data.entity.OrdersEntity;

import java.util.UUID;


public interface OrdersRepository extends JpaRepository<OrdersEntity, UUID> {

}
