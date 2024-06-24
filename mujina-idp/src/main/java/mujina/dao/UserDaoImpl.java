package mujina.dao;

import mujina.Entity.User;
import mujina.repository.UserAuthoritiesRepository;
import mujina.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthoritiesRepository userAuthoritiesRepository;
    @Override
    public User getUser(String name, String password) {
        Optional<User> user = userRepository.findByNameAndPassword(name, password);
        if(!user.isPresent()){
            return null;
        }
        List<String> authorities = userAuthoritiesRepository.findAllAuthoritiesByUserId(user.get().getId());
        user.get().setAuthorities(authorities);
        return user.get();
    }
}
