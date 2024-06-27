package mujina.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RedirectResponse {
    private String redirectUrl;

    private String status;

    private List<String> roles;

    public RedirectResponse(String redirectUrl, String status, List<String> roles) {
        this.redirectUrl = redirectUrl;
        this.status = status;
        this.roles = roles;
    }
}
