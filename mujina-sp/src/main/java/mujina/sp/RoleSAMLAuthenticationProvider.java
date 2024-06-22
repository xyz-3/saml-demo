package mujina.sp;

import mujina.saml.SAMLPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLCredential;

import java.util.Collection;

public class RoleSAMLAuthenticationProvider extends SAMLAuthenticationProvider {

    @Override
    protected Collection<? extends GrantedAuthority> getEntitlements(SAMLCredential credential, Object userDetail) {
        return ((SAMLPrincipal) userDetail).getAuthorities();
    }
}
