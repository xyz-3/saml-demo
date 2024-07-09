package mujina.repository;

import com.warrenstrange.googleauth.ICredentialRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CredentialRepository implements ICredentialRepository {

    @Autowired
    private UserGauthRepository userGauthRepository;

    private final Map<String, UserTOTP> usersKeys = new HashMap<String, UserTOTP>() {{
        put("user0", null);
        put("user1", null);
        put("user2", null);
    }};

    @Override
    public String getSecretKey(String userName) {
        return userGauthRepository.findSecretkeyByUsername(userName);
    }

    @Override
    public void saveUserCredentials(String userName,
                                    String secretKey,
                                    int validationCode,
                                    List<Integer> scratchCodes) {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserTOTP {
        private String username;
        private String secretKey;
        private int validationCode;
        private List<Integer> scratchCodes;
    }
}
