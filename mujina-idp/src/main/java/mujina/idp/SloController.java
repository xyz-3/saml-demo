package mujina.idp;

import io.micrometer.core.instrument.config.validate.ValidationException;
import mujina.api.IdpConfiguration;
import mujina.saml.SAMLPrincipal;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.core.LogoutRequest;
import org.opensaml.saml2.core.NameIDType;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.signature.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Controller
public class SloController {
    @Autowired
    private SAMLMessageHandler samlMessageHandler;

    @Autowired
    private IdpConfiguration idpConfiguration;

    @GetMapping("/SingleLogoutService")
    public void singleLogoutServiceGet(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws MarshallingException, MessageEncodingException, ValidationException,
            SecurityException, MessageDecodingException, MetadataProviderException, org.opensaml.xml.validation.ValidationException, SignatureException {
        doSLO(request, response, authentication, false);
    }

    @PostMapping("/SingleLogoutService")
    public void singleLogoutServicePost(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws MarshallingException, MessageEncodingException, ValidationException,
            SecurityException, MessageDecodingException, MetadataProviderException, org.opensaml.xml.validation.ValidationException, SignatureException {
        doSLO(request, response, authentication, true);
    }

    private void doSLO(HttpServletRequest request, HttpServletResponse response, Authentication authentication, boolean postRequest)
            throws MessageDecodingException, MarshallingException, MessageEncodingException, MetadataProviderException, SecurityException, SignatureException, org.opensaml.xml.validation.ValidationException {
        SAMLMessageContext messageContext = samlMessageHandler.extractSAMLMessageContext(request, response, postRequest);
        LogoutRequest logoutRequest = (LogoutRequest) messageContext.getInboundSAMLMessage();
        String destination = idpConfiguration.getSlsEndpoint();

        SAMLPrincipal principal = new SAMLPrincipal(
                authentication.getName(),
                NameIDType.UNSPECIFIED,
                Collections.emptyList(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                logoutRequest.getIssuer().getValue(),
                logoutRequest.getID(),
                destination,
                messageContext.getRelayState()
        );

        samlMessageHandler.sendLogoutResponse(principal, response);
    }
}
