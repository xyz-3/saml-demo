package mujina.sp;

import org.opensaml.common.SAMLException;
import org.opensaml.saml2.core.LogoutRequest;
import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml2.metadata.SPSSODescriptor;
import org.opensaml.saml2.metadata.SingleLogoutService;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.storage.SAMLMessageStorage;
import org.springframework.security.saml.util.SAMLUtil;
import org.springframework.security.saml.websso.SingleLogoutProfileImpl;


public class ConfigurableSingleLogoutProfile extends SingleLogoutProfileImpl {
    @Override
    public void sendLogoutRequest(SAMLMessageContext context, SAMLCredential credential) throws SAMLException, MetadataProviderException, MessageEncodingException {
        if (credential != null) {
            IDPSSODescriptor idpDescriptor = (IDPSSODescriptor)context.getPeerEntityRoleMetadata();
            SPSSODescriptor spDescriptor = (SPSSODescriptor)context.getLocalEntityRoleMetadata();
            String binding = SAMLUtil.getLogoutBinding(idpDescriptor, spDescriptor);
            SingleLogoutService logoutServiceIDP = SAMLUtil.getLogoutServiceForBinding(idpDescriptor, binding);
            LogoutRequest logoutRequest = this.getLogoutRequest(context, credential, logoutServiceIDP);
            logoutRequest.setDestination("http://localhost:8081/SingleLogoutService");
            context.setCommunicationProfileId(this.getProfileIdentifier());
            context.setOutboundMessage(logoutRequest);
            context.setOutboundSAMLMessage(logoutRequest);
            context.setPeerEntityEndpoint(logoutServiceIDP);
            SAMLMessageStorage messageStorage = context.getMessageStorage();
            if (messageStorage != null) {
                messageStorage.storeMessage(logoutRequest.getID(), logoutRequest);
            }

            boolean signMessage = context.getPeerExtendedMetadata().isRequireLogoutRequestSigned();
            this.sendMessage(context, signMessage);
        }
    }
}
