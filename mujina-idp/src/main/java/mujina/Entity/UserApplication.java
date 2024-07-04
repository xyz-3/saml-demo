package mujina.Entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "user_application")
@Getter
@Setter
public class UserApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "application_id")
    private Integer applicationId;

    @Column(name = "create_at")
    private String createAt;
}
