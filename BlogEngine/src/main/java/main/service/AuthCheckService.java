package main.service;

import lombok.AllArgsConstructor;
import main.api.response.AuthCheckResponse;
import main.model.User;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthCheckService {
    private final UserRepository userRepository;
    private final MapperService mapperService;

    public AuthCheckResponse getAuthCheck() {
        AuthCheckResponse authCheckResponse = new AuthCheckResponse();
        authCheckResponse.setResult(false);

        if (authCheckResponse.isResult()) {
            if (userRepository.findById(1).isPresent()) {
                User user = userRepository.findById(1).get();
                authCheckResponse.setUser(mapperService.convertUserToDto(user));
            }
        }
        return authCheckResponse;
    }
}
