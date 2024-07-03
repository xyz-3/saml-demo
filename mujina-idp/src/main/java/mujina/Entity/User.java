package mujina.Entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "mfa_enabled")
    private Boolean mfa_enabled;

    public User(String name, String password, String email) {
        new User(name, password, email, false);
    }

    public User(String name, String password, String email, Boolean mfaEnabled) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.mfa_enabled = mfaEnabled;
    }
}
