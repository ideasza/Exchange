package dev.teerayut.settings;


public interface SettingsInterface {

	interface viewInterface{
		void onSuccess(String success);
		void onFail(String fail);
	}
	
	interface presentInterface{
		String getSettings(String key);
		void changeStatus(int id, String status);
		void saveSettings(String key, String value);
	}
}
