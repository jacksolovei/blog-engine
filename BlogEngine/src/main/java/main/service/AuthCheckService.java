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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthCheckService {
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
        if (name.length() > 255 || !name.matches("[А-Яа-яA-Za-z]+([А-Яа-яA-Za-z\\s]+)?")) {
            errors.put("name", "Имя указано неверно");
        }
        String password = regRequest.getPassword();
        if (password.length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        String captcha = regRequest.getCaptcha();
        String secret = regRequest.getCaptchaSecret();
        List<CaptchaCode> codes = captchaRepository.findAll().stream()
                .filter(c -> c.getSecretCode().equals(secret))
                .collect(Collectors.toList());
        if (!codes.get(0).getCode().equals(captcha)) {
            errors.put("captcha", "Код с картинки введён неверно");
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
