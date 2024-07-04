package org.data.mujinaidpplatform.repository;

import org.data.mujinaidpplatform.Entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Query("SELECT a FROM application a WHERE a.entity_id = :entityId")
    Application findByEntityId(String entityId);
}
