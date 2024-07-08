package org.data.mujinaidpplatform.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "created_at")
    private String createdAt;
}
