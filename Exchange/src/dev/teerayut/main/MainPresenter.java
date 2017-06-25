package dev.teerayut.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.teerayut.connection.ConnectionDB;
import dev.teerayut.model.CalculateModel;
import dev.teerayut.receipt.Receive;
import dev.teerayut.utils.DateFormate;

public class MainPresenter implements MainInterface.presentInterface {

	private ResultSet resultSet;
	private PreparedStatement psmt;
	private ConnectionDB connectionDB;
	private MainInterface.viewInterface view;
	private CalculateModel calculateModel;
	private List<CalculateModel> calList = new ArrayList<CalculateModel>();
	
	public MainPresenter(MainInterface.viewInterface view) {
		this.view = view;
	}
	
	@Override
	public ResultSet getAllCurrency(String type) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		switch (type) {
		case "buy":
			sb.append("SELECT CR_ID, CR_NAME, CR_BUY_RATE ");
			sb.append("FROM CURRENCY ");
			sb.append("WHERE CR_STATUS = 'Y' AND CR_BUY_RATE != '-'");
			break;
		case "sell":
			sb.append("SELECT CR_ID, CR_NAME, CR_SELL_RATE ");
			sb.append("FROM CURRENCY ");
			sb.append("WHERE CR_STATUS = 'Y' AND CR_SELL_RATE != '-'");
			break;
		default:
			break;
		}
		
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			return resultSet;
		} catch(Exception e) {
			resultSet = null;
			view.onFail("Fail : " + e.getMessage());
			connectionDB.closeAllTransaction();
		}
		connectionDB.closeAllTransaction();
		return null;
	}

	@Override
	public void insertReceipt(List<CalculateModel> calculateList) {
		this.calList = calculateList;
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("INSERT INTO RECEIPT (RC_DATE, RC_NAME, RC_RATE, RC_AMOUNT, RC_TOTAL, RC_TYPE, RC_CREATED_DATE, RC_NUMBER, RC_STATUS) ");
		sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		connectionDB = new ConnectionDB();
		int is = 0;
		try {
			psmt = connectionDB.dbInsert(sb.toString());
			for (int i = 0; i < calculateList.size(); i++) {
				CalculateModel model = calculateList.get(i);
				psmt.setString(1, model.getReceiveDate());
				psmt.setString(2, model.getReceiveCurrency());
				psmt.setString(3, model.getReceiveRate());
				psmt.setString(4, model.getReceiveAmount());
				psmt.setString(5, model.getReceiveTotal());
				psmt.setString(6, model.getReceiveType());
				psmt.setString(7, new DateFormate().getDateWithTime());
				psmt.setString(8, model.getReceiptNumber());
				psmt.setString(9, "Print");
			}
			is = psmt.executeUpdate();
			if (is == 1) {
				new Receive().printReceipt(calList);
				view.onSuccess("บันทึกรายการซื้อขายแล้ว");
				calculateList.clear();
				connectionDB.closeAllTransaction();
			} else {
				view.onFail("ไม่สามารถบันทึกข้อมูลได้");
			}
		} catch(Exception e) {
			view.onFail("Insert receipt : " + e.getMessage());
			System.out.println(e.getMessage());
			connectionDB.closeAllTransaction();
		}
	}

	@Override
	public void getLastKey() {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("SELECT RC_NUMBER ");
		sb.append("FROM RECEIPT ");
		//sb.append("WHERE RC_DATE ='" + new DateFormate().getTime() + "'");
		sb.append("ORDER BY RC_ID DESC LIMIT 1");
		
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			view.onGenerateKey(resultSet);
		} catch(Exception e) {
			resultSet = null;
			view.onFail("Fail : " + e.getMessage());
		}
		connectionDB.closeAllTransaction();
	}

	private String RCStatus = null;
	@Override
	public void getReceipt(String number) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("SELECT * FROM RECEIPT ");
		sb.append("WHERE RC_NUMBER ='" + number + "'");
		
		connectionDB = new ConnectionDB();
		try {
			calList.clear();
			resultSet = connectionDB.dbQuery(sb.toString());
			while (resultSet.next()) {
				calculateModel = new CalculateModel(resultSet.getString("RC_NUMBER"), 
						resultSet.getString("RC_DATE"), "", resultSet.getString("RC_NAME"), 
						resultSet.getString("RC_RATE"), resultSet.getString("RC_AMOUNT"), 
						resultSet.getString("RC_TOTAL"), resultSet.getString("RC_TYPE"));
				calList.add(calculateModel);
				
				RCStatus = resultSet.getString("RC_STATUS");
			}
			
			
			if (RCStatus.equals("Print")) {
				view.onCancelReceipt(calList);
				connectionDB.closeAllTransaction();
			} else {
				view.onFail("ใบเสร็จเลขที่ " + number + " ถูกยกเลิกแล้ว");
				connectionDB.closeAllTransaction();
			}
			//view.onCancelReceipt(calList);
			//connectionDB.closeAllTransaction();
		} catch(Exception e) {
			resultSet = null;
			view.onFail("getReceipt : " + e.getMessage());
		}
	}

	@Override
	public void cancelReceipt(String number, String status, List<CalculateModel> calculateList) {
		this.calList = calculateList;
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("UPDATE RECEIPT ");
		sb.append("SET RC_STATUS = ? ");
		sb.append("WHERE RC_NUMBER = ? ");
		
		connectionDB = new ConnectionDB();
		int is = 0;
		try {
			psmt = connectionDB.dbInsert(sb.toString());
			psmt.setString(1, status);
			psmt.setString(2, number);
			is = psmt.executeUpdate();
			if (is == 1) {
				new Receive().printReceiptCancel(calList);
				view.onSuccess("ยกเลิกใบเสร็จเลขที่ " + number + " แล้ว");
				connectionDB.closeAllTransaction();
			} else {
				view.onFail("ไม่สามารถบันทึกข้อมูลได้");
			}
		} catch (Exception e) {
			view.onFail("Cancel receipt : " + e.getMessage());
			System.out.println(e.getMessage());
			connectionDB.closeAllTransaction();
		}
	}
}
