package org.data.mujinaidpplatform.repository;

import jakarta.transaction.Transactional;
import org.data.mujinaidpplatform.Entity.UserAuthorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAuthoritiesRepository extends JpaRepository<UserAuthorities, Integer> {
    @Query("SELECT ua.authority FROM user_authorities ua WHERE ua.user_id = :userId")
    List<String> findAllAuthoritiesByUserId(Integer userId);

    @Modifying
    @Transactional
    @Query("INSERT INTO user_authorities (user_id, authority) VALUES (:user_id, :authority)")
    void addAuthority(Integer user_id, String authority);

    @Transactional
    @Modifying
    @Query("DELETE FROM user_authorities WHERE user_id = :user_id AND authority = :authority")
    void removeAuthority(Integer user_id, String authority);
}
