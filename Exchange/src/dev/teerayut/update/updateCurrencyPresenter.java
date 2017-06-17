package dev.teerayut.update;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.teerayut.connection.ConnectionDB;
import dev.teerayut.model.CurrencyModel;

public class updateCurrencyPresenter implements updateCurrencyInterface.presentInterface {

	private ResultSet resultSet;
	private ConnectionDB connectionDB;
	private PreparedStatement psmt;
	
	private CurrencyModel model;
	private List<CurrencyModel> modelList = new ArrayList<CurrencyModel>();
	private updateCurrencyInterface.viewInterface view;
	
	public updateCurrencyPresenter(updateCurrencyInterface.viewInterface view) {
		this.view = view;
	}
	
	@Override
	public void updateCurrency(int id, String value1, String value2, String value3, boolean value4) {
		String status = "";
		if (value4) {
			status = "Y";
		} else {
			status = "F";
		}
		
		String result;
		
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("UPDATE CURRENCY SET CR_NAME = ?, ");
		sb.append("CR_BUY_RATE = ?, ");
		sb.append("CR_SELL_RATE = ?, ");
		sb.append("CR_STATUS = ? ");
		sb.append("WHERE CR_ID = ? ");
		
		connectionDB = new ConnectionDB();
		try {
			psmt = connectionDB.dbUpdate(sb.toString());
			psmt.setString(1, value1);
			psmt.setString(2, value2);
			psmt.setString(3, value3);
			psmt.setString(4, status);
			psmt.setInt(5, id);
			if (psmt.executeUpdate() != 1) {
				view.onFail("ไม่สามารถอัพเดทฐานข้อมูลได้");
			}
		} catch(Exception e) {
			view.onFail("Update : " + e.getMessage());
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void LoadCurrency() {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("SELECT * FROM CURRENCY ORDER BY CR_ID ASC");
		
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				model = new CurrencyModel(resultSet.getInt("CR_ID"), resultSet.getString("CR_NAME")/*, resultSet.getString("CR_DESC")*/, 
						resultSet.getString("CR_BUY_RATE"), resultSet.getString("CR_SELL_RATE"), resultSet.getString("CR_STATUS"));
            	modelList.add(model);
            	
			}
			view.showCurrency(modelList);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
