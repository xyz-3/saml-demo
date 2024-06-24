package mujina.repository;

import mujina.Entity.UserAuthorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAuthoritiesRepository extends JpaRepository<UserAuthorities, Integer> {
    @Query("SELECT ua.authority FROM user_authorities ua WHERE ua.user_id = :userId")
    List<String> findAllAuthoritiesByUserId(Integer userId);
}
