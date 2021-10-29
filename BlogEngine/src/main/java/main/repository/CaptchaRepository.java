package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {

    @Query(value = "SELECT * FROM captcha_codes WHERE time < (NOW() - INTERVAL 1 HOUR)",
            nativeQuery = true)
    List<CaptchaCode> findOldCaptcha();
}
