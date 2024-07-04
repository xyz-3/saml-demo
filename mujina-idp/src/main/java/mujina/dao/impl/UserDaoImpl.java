package mujina.dao.impl;

import mujina.Entity.User;
import mujina.dao.UserDao;
import mujina.dto.UserDto;
import mujina.repository.ApplicationRepository;
import mujina.repository.UserApplicationRepository;
import mujina.repository.UserAuthoritiesRepository;
import mujina.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public UserDto getUser(String name, String password) {
        Optional<User> user = userRepository.findByNameAndPassword(name, password);
        if(user.isEmpty()){
            return null;
        }
        List<String> authorities = userAuthoritiesRepository.findAllAuthoritiesByUserId(user.get().getId());
        return new UserDto(user.get(), authorities);
    }

    @Override
    public Boolean checkUserAppAccess(Integer userId, String entity) {
        List<Integer> applicationIds = userApplicationRepository.findApplicationIdByUserId(userId);
        Integer appId = applicationRepository.findByEntityId(entity).getId();
        return applicationIds.contains(appId);
    }
}
