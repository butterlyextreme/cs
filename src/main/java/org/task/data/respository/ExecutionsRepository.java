package org.task.data.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.task.data.entity.ExecutionsEntity;

import java.util.UUID;

public interface ExecutionsRepository extends JpaRepository<ExecutionsEntity, UUID> {
}
