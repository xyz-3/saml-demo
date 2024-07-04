package mujina.repository;

import mujina.Entity.UserApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserApplicationRepository extends JpaRepository<UserApplication, Integer> {
    @Query("SELECT ua.applicationId FROM user_application ua WHERE ua.userId = :userId")
    List<Integer> findApplicationIdByUserId(Integer userId);
}
