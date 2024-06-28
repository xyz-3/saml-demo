package org.data.mujinaidpplatform.repository;

import com.warrenstrange.googleauth.ICredentialRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.data.mujinaidpplatform.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CredentialRepository implements ICredentialRepository {

    @Autowired
    private UserGauthRepository userGauthRepository;
    @Autowired
    private UserRepository userRepository;

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
        Optional<User> user = userRepository.findByName(userName);
        if (!user.isPresent()) {
            return;
        }
        if (user.get().getMfa_enabled()) return;
        Integer userid = user.get().getId();
        userGauthRepository.insertUserGauth(userName, userid, secretKey);
        userRepository.updateMfaEnabledById(userid, true);
    }

    public UserTOTP getUser(String username) {
        return usersKeys.get(username);
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
