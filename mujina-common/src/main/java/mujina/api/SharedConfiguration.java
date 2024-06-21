package mujina.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import mujina.saml.KeyStoreLocator;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.security.BasicSecurityConfiguration;
import org.opensaml.xml.signature.SignatureConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.saml.key.JKSKeyManager;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.util.Enumeration;

/*
抽象类，定义了共享配置的基本操作和属性。主要用于配置SAML（Security Assertion Markup Language）身份认证相关的参数。
Idp和Sp的配置都继承自该类。
 */
@Getter
@Setter
public abstract class SharedConfiguration {

    @JsonIgnore
    protected static final Logger LOG = LoggerFactory.getLogger(SharedConfiguration.class);
    @JsonIgnore
    private JKSKeyManager keyManager; //管理密钥的实例
    private String keystorePassword = "secret"; //密钥库密码
    private boolean needsSigning; //是否需要签名
    private String defaultSignatureAlgorithm = SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256; //默认签名算法
    private String signatureAlgorithm; //签名算法
    private String entityId; //实体ID,通常是IDP的标识符。

    // KeyStore - 管理密钥和证书的存储

    public SharedConfiguration(JKSKeyManager keyManager) {
        this.keyManager = keyManager;
    }

    /*
    抽象方法，用于重置配置
     */
    public abstract void reset();

    /*
    设置entityID/KeyStoreEntry/PwdProtection，将其存储到密钥库中
     */
    public void setEntityId(String newEntityId, boolean addTokenToStore) {
        if (addTokenToStore) {
            try {
                KeyStore keyStore = keyManager.getKeyStore();
                KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(keystorePassword.toCharArray());
                KeyStore.Entry keyStoreEntry = keyStore.getEntry(this.entityId, passwordProtection);
                keyStore.setEntry(newEntityId, keyStoreEntry, passwordProtection);
            } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
                throw new RuntimeException(e);
            }
        }
        this.entityId = newEntityId;
    }

    /*
    向密钥库注入签名凭据
     */
    public void injectCredential(final String certificate, final String pemKey) {
        try {
            KeyStore keyStore = keyManager.getKeyStore();
            if (keyStore.containsAlias(entityId)) {
                keyStore.deleteEntry(entityId);
            }
            // 为指定的实体ID添加私钥后，加入
            KeyStoreLocator.addPrivateKey(keyStore, entityId, pemKey, certificate, keystorePassword);
        } catch (Exception e) {
            throw new RuntimeException("Unable to append signing credential", e);
        }
    }

    /*
    重置KeyStoreLocator
     */
    protected void resetKeyStore(String alias, String privateKey, String certificate) {
        try {
            KeyStore keyStore = keyManager.getKeyStore();
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                keyStore.deleteEntry(aliases.nextElement());
            }
            KeyStoreLocator.addPrivateKey(keyStore, alias, privateKey, certificate, getKeystorePassword());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
    设置签名算法
     */
    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
        BasicSecurityConfiguration.class.cast(Configuration.getGlobalSecurityConfiguration())
                .registerSignatureAlgorithmURI("RSA", signatureAlgorithm);
    }
}
