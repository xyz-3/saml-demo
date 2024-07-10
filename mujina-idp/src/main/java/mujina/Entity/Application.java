package mujina.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Data
@Entity(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "entity_id")
    private String entity_id;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(name = "acs_location_path")
    private String acsLocationPath;

    @Column(name = "slo_location_path")
    private String sloLocationPath;

    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "cert", length = 2047)
    private String cert;
}
