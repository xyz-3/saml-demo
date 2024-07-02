package mujina.sp;

import mujina.api.SpConfiguration;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.processor.SAMLProcessor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SAMLSingleLogoutHandler {
    @Autowired
    private SpConfiguration spConfiguration;

    @Autowired
    @Qualifier("metadata")
    private MetadataManager metadataManager;

    @Autowired
    private SAMLProcessor samlProcessor;
}
