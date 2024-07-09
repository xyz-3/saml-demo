package mujina.sp;

import mujina.api.SpConfiguration;
import org.opensaml.common.SAMLException;
import org.opensaml.saml2.metadata.AssertionConsumerService;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.SPSSODescriptor;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessorImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

public class ConfigurableSAMLProcessor extends SAMLProcessorImpl {

    private final SpConfiguration spConfiguration;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public ConfigurableSAMLProcessor(Collection<SAMLBinding> bindings, SpConfiguration spConfiguration, HttpServletRequest request, HttpServletResponse response) {
        super(bindings);
        this.spConfiguration = spConfiguration;
        this.request = request;
        this.response = response;
    }

    @Override
    public SAMLMessageContext sendMessage(SAMLMessageContext samlContext, boolean sign)
            throws SAMLException, MetadataProviderException, MessageEncodingException {

        Endpoint endpoint = samlContext.getPeerEntityEndpoint();
        String location = endpoint.getLocation();
        SAMLBinding binding = getBinding(endpoint);

        samlContext.setLocalEntityId(spConfiguration.getEntityId());
        samlContext.getLocalEntityMetadata().setEntityID(spConfiguration.getEntityId());
        samlContext.getPeerEntityEndpoint().setLocation(location);
        // set relay state to the request URL
        samlContext.setRelayState(request.getRequestURL().toString());

        SPSSODescriptor roleDescriptor = (SPSSODescriptor) samlContext.getLocalEntityMetadata().getRoleDescriptors().get(0);
        AssertionConsumerService assertionConsumerService = roleDescriptor.getAssertionConsumerServices().stream().filter(service -> service.isDefault()).findAny().orElseThrow(() -> new RuntimeException("No default ACS"));
        assertionConsumerService.setBinding(spConfiguration.getProtocolBinding());

        if (location.equals(spConfiguration.getSloUrl())) {
            assertionConsumerService.setBinding(spConfiguration.getSingleLogoutServiceURL());
            return super.sendMessage(samlContext, spConfiguration.isNeedsSigning(), binding);
        }

        assertionConsumerService.setLocation(spConfiguration.getAssertionConsumerServiceURL());

        return super.sendMessage(samlContext, spConfiguration.isNeedsSigning(), binding);

    }
}
