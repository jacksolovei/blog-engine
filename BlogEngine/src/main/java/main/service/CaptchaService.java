package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import lombok.AllArgsConstructor;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.repository.CaptchaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CaptchaService {

    private final CaptchaRepository captchaRepository;

    public CaptchaResponse getCaptchaCode() {
        CaptchaResponse captchaResponse = new CaptchaResponse();
        Cage cage = new GCage();
        String secret = UUID.randomUUID().toString();
        String code = cage.getTokenGenerator().next();
        String image = "data:image/png;base64, " + Base64.getEncoder().encodeToString(cage.draw(code));
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCode(code);
        captchaCode.setSecretCode(secret);
        captchaCode.setTime(new Date());
        captchaRepository.save(captchaCode);
        captchaResponse.setSecret(secret);
        captchaResponse.setImage(image);
        return captchaResponse;
    }

    @Scheduled(fixedRate = 3_600_000)
    public void deleteCaptchaCode() {
        List<CaptchaCode> codes = captchaRepository.findOldCaptcha();
        if (codes.size() > 0) {
            codes.forEach(captchaRepository::delete);
        }
    }
}
