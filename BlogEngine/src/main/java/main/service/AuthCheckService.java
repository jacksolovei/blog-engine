package main.service;

import main.api.response.AuthCheckResponse;
import main.api.response.AuthUserResponse;
import main.model.User;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthCheckService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    public AuthCheckResponse getAuthCheck() {
        AuthCheckResponse authCheckResponse = new AuthCheckResponse();
        authCheckResponse.setResult(true);


        if (authCheckResponse.isResult()) {
            Optional<User> user = userRepository.findById(1);
            if (user.isPresent()) {
                User user2 = user.get();
                AuthUserResponse authUserResponse = new AuthUserResponse();
                authUserResponse.setId(user2.getId());
                authUserResponse.setName(user2.getName());
                authUserResponse.setEmail(user2.getEmail());
                authUserResponse.setPhoto(user2.getPhoto());
                authUserResponse.setModeration(user2.getIsModerator() == 1);

                if (authUserResponse.isModeration()) {
                    authUserResponse.setModerationCount(postRepository.findUnmoderatedPostsCount());
                    authUserResponse.setSettings(true);
                } else {
                    authUserResponse.setModerationCount(0);
                    authUserResponse.setSettings(false);
                }
                authCheckResponse.setUser(authUserResponse);
            }
        }
        return authCheckResponse;
    }
}
