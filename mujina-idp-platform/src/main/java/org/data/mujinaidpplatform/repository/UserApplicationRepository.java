package org.data.mujinaidpplatform.repository;

import org.data.mujinaidpplatform.Entity.UserApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserApplicationRepository extends JpaRepository<UserApplication, Integer> {
    @Query("SELECT ua.applicationId FROM user_application ua WHERE ua.userId = :userId")
    List<Integer> findApplicationIdByUserId(Integer userId);

    @Query("SELECT a.name FROM application a WHERE a.id IN :applicationIds")
    List<String> findApplicationsByUserId(List<Integer> applicationIds);
}
