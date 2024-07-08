package org.data.mujinaidpplatform.dao.impl;

import org.data.mujinaidpplatform.Entity.User;
import org.data.mujinaidpplatform.dao.UserDao;
import org.data.mujinaidpplatform.dto.UserDto;
import org.data.mujinaidpplatform.repository.UserApplicationRepository;
import org.data.mujinaidpplatform.repository.UserAuthoritiesRepository;
import org.data.mujinaidpplatform.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthoritiesRepository userAuthoritiesRepository;
    @Autowired
    private UserApplicationRepository userApplicationRepository;
    @Override
    public UserDto getUser(String name, String password) {
        Optional<User> user = userRepository.findByNameAndPassword(name, password);
        if(!user.isPresent()){
            return null;
        }
        List<String> authorities = userAuthoritiesRepository.findAllAuthoritiesByUserId(user.get().getId());
        List<Integer> applicationIds = userApplicationRepository.findApplicationIdByUserId(user.get().getId());
        List<String> applications = userApplicationRepository.findApplicationsByUserId(applicationIds);
        UserDto userDto = new UserDto(user.get(), applications, authorities);
        return userDto;
    }
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for(User user : users){
            List<String> authorities = userAuthoritiesRepository.findAllAuthoritiesByUserId(user.getId());
            List<Integer> applicationIds = userApplicationRepository.findApplicationIdByUserId(user.getId());
            List<String> applications = userApplicationRepository.findApplicationsByUserId(applicationIds);
            UserDto userDto = new UserDto(user, applications, authorities);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public void addAuthority(Integer id, String authority) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()){
            return;
        }
        userAuthoritiesRepository.addAuthority(user.get().getId(), authority);
    }

    @Override
    public void removeAuthority(Integer id, String authority) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()){
            return;
        }
        userAuthoritiesRepository.removeAuthority(user.get().getId(), authority);
    }
}
