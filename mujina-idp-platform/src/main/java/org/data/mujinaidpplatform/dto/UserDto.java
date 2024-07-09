package org.data.mujinaidpplatform.dto;

import lombok.Getter;
import lombok.Setter;
import org.data.mujinaidpplatform.Entity.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private Integer id;
    private String name;
    private String password;
    private String email;
    private List<String> applications;
    private List<String> authorities;

    public UserDto(Integer id, String name, String password, String email, List<String> applications, List<String> authorities) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.applications = applications;
        this.authorities = authorities;
    }

    public UserDto(User user, List<String> applications, List<String> authorities) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.applications = applications;
        this.authorities = authorities;
    }
}
