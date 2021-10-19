package main.service;

import lombok.AllArgsConstructor;
import main.api.response.SettingsResponse;
import main.repository.SettingsRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SettingsService {
    private final SettingsRepository settingsRepository;

    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(settingsRepository.findSettingValue("MULTIUSER_MODE").equals("YES"));
        settingsResponse.setPostPremoderation(settingsRepository.findSettingValue("POST_PREMODERATION").equals("YES"));
        settingsResponse.setStatisticsIsPublic(settingsRepository.findSettingValue("STATISTICS_IS_PUBLIC").equals("YES"));
        return settingsResponse;
    }
}
