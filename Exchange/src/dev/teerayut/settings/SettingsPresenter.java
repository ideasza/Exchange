package dev.teerayut.settings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dev.teerayut.connection.ConnectionDB;
import dev.teerayut.model.CalculateModel;
import dev.teerayut.receipt.Receive;
import dev.teerayut.utils.DateFormate;

public class SettingsPresenter implements SettingsInterface.presentInterface{

	private ResultSet resultSet;
	private PreparedStatement psmt;
	private ConnectionDB connectionDB;
	private SettingsInterface.viewInterface view;
	
	public SettingsPresenter(SettingsInterface.viewInterface view) {
		this.view = view;
	}
	
	@Override
	public void changeStatus(int id, String status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveSettings(String key, String value) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("INSERT INTO SETTINGS (ST_KEY, ST_VALUE, ST_CREATED_DATE) ");
		sb.append("VALUES (?, ?, ?)");
		
		connectionDB = new ConnectionDB();
		try {
			psmt = connectionDB.dbInsert(sb.toString());
			psmt.setString(1, "printer");
			psmt.setString(2, value);
			psmt.setString(3, new DateFormate().getDateWithTime());
			if (psmt.executeUpdate() != 1) {
				view.onFail("ไม่สามารถบันทึกข้อมูลได้");
			} else {
				view.onSuccess("บันทึกการตั้งค่าแล้ว");
			}
		} catch(Exception e) {
			view.onFail("Insert receipt : " + e.getMessage());
			System.out.println(e.getMessage());
		}
		
	}

	@Override
	public String getSettings(String key) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("SELECT ST_KEY, ST_VALUE FROM SETTINGS ");
		sb.append("WHERE ST_KEY = '" + key + "' ");
		
		connectionDB = new ConnectionDB();
		ResultSet resultSet = null;
		String result = "";
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			if (resultSet.next()) {
				result = "T;" + resultSet.getString(1);
			} else {
				result = "F;";
			}
		} catch(Exception e) {
			result = "F;" + e.getMessage();
			view.onFail("Fail : " + e.getMessage());
		}
		return result;
	}

}
