package main.service;

import lombok.AllArgsConstructor;
import main.api.request.RegRequest;
import main.api.response.AuthCheckResponse;
import main.api.response.RegResponse;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthCheckService {
    public static final int PASSWORD_LENGTH = 6;
    public static final int MAX_LENGTH = 255;

    private final UserRepository userRepository;
    private final MapperService mapperService;
    private final CaptchaRepository captchaRepository;

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

    public RegResponse getRegResponse(RegRequest regRequest) {
        RegResponse regResponse = new RegResponse();
        Map<String, String> errors = new HashMap<>();
        List<String> emails = userRepository.findAll().stream()
                .map(User::getEmail).collect(Collectors.toList());
        String email = regRequest.getEmail();
        if (emails.contains(email)) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        String name = regRequest.getName();
        if (name.length() > MAX_LENGTH || !name.matches("[А-Яа-яA-Za-z]+([А-Яа-яA-Za-z\\s]+)?")) {
            errors.put("name", "Имя указано неверно");
        }
        String password = regRequest.getPassword();
        if (password.length() < PASSWORD_LENGTH) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        String captcha = regRequest.getCaptcha();
        String secret = regRequest.getCaptchaSecret();
        Optional<CaptchaCode> optionalCaptcha = captchaRepository.findCaptchaBySecretCode(secret);
        if (optionalCaptcha.isPresent()) {
            if (!optionalCaptcha.get().getCode().equals(captcha)) {
                errors.put("captcha", "Код с картинки введён неверно");
            }
        } else {
            errors.put("captcha", "код устарел");
        }
        if (errors.isEmpty()) {
            regResponse.setResult(true);
            User user = new User();
            user.setIsModerator((byte) 0);
            user.setRegTime(new Date());
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            userRepository.save(user);
        } else {
            regResponse.setResult(false);
            regResponse.setErrors(errors);
        }
        return regResponse;
    }
}
