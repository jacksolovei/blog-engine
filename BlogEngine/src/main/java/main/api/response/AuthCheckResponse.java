package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class AuthCheckResponse {
    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthUserResponse user;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public AuthUserResponse getUser() {
        return user;
    }

    public void setUser(AuthUserResponse user) {
        this.user = user;
    }
}
