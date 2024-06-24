package mujina.Entity;

import javax.persistence.*;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "user_authorities")
public class UserAuthorities implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "authority")
    private String authority;
}
