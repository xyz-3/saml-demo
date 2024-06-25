package mujina.idp;

import mujina.api.IdpConfiguration;
import mujina.Entity.User;
import mujina.dao.UserDao;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static mujina.api.AuthenticationMethod.ALL;

/*
提供身份验证服务的类
 */
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    private final UserDao userDao;

    private final IdpConfiguration idpConfiguration;

    public AuthenticationProvider(UserDao userDao, IdpConfiguration idpConfiguration) {
        this.userDao = userDao;
        this.idpConfiguration = idpConfiguration;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (StringUtils.isEmpty(authentication.getPrincipal())) {
            throw new InvalidAuthenticationException("Principal may not be empty");
        }
        if (idpConfiguration.getAuthenticationMethod().equals(ALL)) {
            return new FederatedUserAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER")));
        } else {
            User user = userDao.getUser(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
            if (user == null) {
                throw new InvalidAuthenticationException("User not found or bad credentials");
            }
            List<GrantedAuthority> authorities = user.getAuthorities()
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new FederatedUserAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    authorities);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
