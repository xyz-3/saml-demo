package mujina.idp;

import mujina.api.IdpConfiguration;
import mujina.Entity.User;
import mujina.repository.UserAuthoritiesRepository;
import mujina.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static mujina.api.AuthenticationMethod.ALL;

public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    private final UserAuthoritiesRepository userAuthoritiesRepository;

    private final UserRepository userRepository;

    private final IdpConfiguration idpConfiguration;

    public AuthenticationProvider(UserAuthoritiesRepository userAuthoritiesRepository, UserRepository userRepository, IdpConfiguration idpConfiguration) {
        this.userAuthoritiesRepository = userAuthoritiesRepository;
        this.userRepository = userRepository;
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
            Optional<User> user = userRepository.findByNameAndPassword(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
            if (user.isPresent()) {
                List<GrantedAuthority> authorities = userAuthoritiesRepository.findAllAuthoritiesByUserId(user.get().getId())
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                FederatedUserAuthenticationToken token = new FederatedUserAuthenticationToken(
                        authentication.getPrincipal(),
                        authentication.getCredentials(),
                        authorities);
                return token;
            } else {
                throw new InvalidAuthenticationException("User not found or bad credentials");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
