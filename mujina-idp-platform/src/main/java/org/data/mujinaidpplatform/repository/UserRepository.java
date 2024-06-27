package org.data.mujinaidpplatform.repository;


import jakarta.transaction.Transactional;
import org.data.mujinaidpplatform.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNameAndPassword(String name, String password);

    Optional<User> findByName(String name);

    @Query("SELECT u.id FROM user u WHERE u.name = :username")
    Integer findIdByName(String username);

    @Transactional
    @Modifying
    @Query("UPDATE user u SET u.mfa_enabled = :mfaEnabled WHERE u.id = :id")
    void updateMfaEnabledById(Integer id, Boolean mfaEnabled);
}
