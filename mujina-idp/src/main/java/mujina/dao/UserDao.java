package mujina.dao;

import mujina.dto.UserDto;

public interface UserDao {
    UserDto getUser(String name, String password);

    Boolean checkUserAppAccess(Integer userId, String entity);
}