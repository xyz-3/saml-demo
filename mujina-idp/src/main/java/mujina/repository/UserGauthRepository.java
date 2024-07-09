package mujina.repository;

import mujina.Entity.UserGauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGauthRepository extends JpaRepository<UserGauth, Integer> {
    @Query("SELECT ug.secretkey FROM user_gauth ug WHERE ug.username = :username")
    String findSecretkeyByUsername(String username);
}
