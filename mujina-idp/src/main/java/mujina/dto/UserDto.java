package mujina.dto;

import lombok.Getter;
import lombok.Setter;
import mujina.Entity.User;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private Integer id;
    private String name;
    private String password;
    private String email;
    private Boolean mfaEnabled;
    private List<String> authorities;

    public UserDto(Integer id, String name, String password, Boolean mfaEnabled, List<String> authorities) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.mfaEnabled = mfaEnabled;
        this.authorities = authorities;
    }

    public UserDto(User user, List<String> authorities) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.mfaEnabled = user.getMfaEnabled();
        this.authorities = authorities;
    }
}
