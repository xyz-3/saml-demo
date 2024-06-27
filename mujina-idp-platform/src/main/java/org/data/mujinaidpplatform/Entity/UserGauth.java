package org.data.mujinaidpplatform.Entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity(name = "user_gauth")
public class UserGauth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "userid")
    private Integer userid;

    @Column(name = "secretkey")
    private String secretkey;
}
