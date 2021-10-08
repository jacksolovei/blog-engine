package main.service;

import main.api.response.SettingsResponse;
import main.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    @Autowired
    private SettingsRepository settingsRepository;

    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(settingsRepository.findSettingValue("MULTIUSER_MODE").equals("YES"));
        settingsResponse.setPostPremoderation(settingsRepository.findSettingValue("POST_PREMODERATION").equals("YES"));
        settingsResponse.setStatisticsIsPublic(settingsRepository.findSettingValue("STATISTICS_IS_PUBLIC").equals("YES"));
        return settingsResponse;
    }
}
