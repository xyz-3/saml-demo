package org.data.mujinaidpplatform.dto;

import org.data.mujinaidpplatform.Entity.User;

import java.util.List;

public class UserDto {
    private Integer id;
    private String name;
    private String password;
    private List<String> authorities;

    public UserDto(Integer id, String name, String password, List<String> authorities) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.authorities = authorities;
    }

    public UserDto(User user, List<String> authorities) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.authorities = authorities;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}
