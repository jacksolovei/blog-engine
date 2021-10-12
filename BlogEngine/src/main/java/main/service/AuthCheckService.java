package main.service;

import main.api.response.AuthCheckResponse;
import main.model.User;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthCheckService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MapperService mapperService;

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
