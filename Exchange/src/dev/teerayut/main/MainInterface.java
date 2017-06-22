package dev.teerayut.main;

import java.sql.ResultSet;
import java.util.List;

import dev.teerayut.model.CalculateModel;

public class MainInterface {

	interface viewInterface{
		void onCancelReceipt(List<CalculateModel> calculateList);
		void onGenerateKey(ResultSet result);
		void onSuccess(String success);
		void onFail(String fail);
	}
	
	interface presentInterface{
		void getReceipt(String number);
		void cancelReceipt(String number, String status, List<CalculateModel> calculateList);
		void getLastKey();
		ResultSet getAllCurrency(String type);
		void insertReceipt(List<CalculateModel> calculateList);
	}
}
