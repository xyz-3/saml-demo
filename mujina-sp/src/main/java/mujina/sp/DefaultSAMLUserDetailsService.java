package mujina.sp;

import mujina.saml.SAMLAttribute;
import mujina.saml.SAMLBuilder;
import mujina.saml.SAMLPrincipal;
import org.opensaml.saml2.core.NameID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class DefaultSAMLUserDetailsService implements SAMLUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSAMLUserDetailsService.class);

    @Override
    public Principal loadUserBySAML(SAMLCredential credential) {
        LOG.debug("loadUserBySAML {}", credential);

        List<SAMLAttribute> attributes = credential.getAttributes().stream().map(attribute ->
                new SAMLAttribute(
                        attribute.getName(),
                        attribute.getAttributeValues().stream().map(SAMLBuilder::getStringValueFromXMLObject)
                                .filter(Optional::isPresent).map(Optional::get).collect(toList()))).collect(toList());
        List<GrantedAuthority> authorities = attributes.stream()
                .filter(attr -> "roles".equals(attr.getName()))
                .flatMap(attr -> attr.getValues().stream())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        attributes.sort(comparing(SAMLAttribute::getName));
        NameID nameID = credential.getNameID();
        String relayState = credential.getRelayState();
        SAMLPrincipal principal = new SAMLPrincipal(nameID.getValue(), nameID.getFormat(), attributes, authorities);
        principal.setRelayState(relayState);
        return principal;
    }

}
