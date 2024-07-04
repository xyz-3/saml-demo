package org.data.mujinaidpplatform.dao;

import org.data.mujinaidpplatform.dto.UserDto;

import java.util.List;

public interface UserDao {
    UserDto getUser(String name, String password);

    List<UserDto> getAllUsers();

    void addAuthority(Integer id, String authority);

    void removeAuthority(Integer id, String authority);
}
