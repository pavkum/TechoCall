package com.techostic.techocall.settings;

import java.util.List;

import com.techostic.techocall.modal.Settings;


public interface SettingsAPI {

	public List<Settings> getAllSettings();

	public boolean updateSettings(Settings settings);
	
	public Settings getSettingsBySettingsName(String name);
}
