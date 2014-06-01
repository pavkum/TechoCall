package com.techostic.techocall.modal;

public class Settings {

	private long settingsID;
	
	private String name;
	
	private String value;

	public long getSettingsID() {
		return settingsID;
	}

	public void setSettingsID(long settingsID) {
		this.settingsID = settingsID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		String data = "SettingsID : name : value :: " + this.getSettingsID() + " : " + this.getName() + " : " + this.value;
		return data;
	}
}
