package org.data.mujinaidpplatform.repository;

import jakarta.transaction.Transactional;
import org.data.mujinaidpplatform.Entity.UserGauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGauthRepository extends JpaRepository<UserGauth, Integer> {
    @Query("SELECT ug.secretkey FROM user_gauth ug WHERE ug.username = :username")
    String findSecretkeyByUsername(String username);

    @Modifying
    @Query("INSERT INTO user_gauth (username, userid, secretkey) VALUES (:username, :userid, :secretkey)")
    @Transactional
    void insertUserGauth(String username, Integer userid, String secretkey);
}
