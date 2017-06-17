package dev.teerayut.main;

import java.sql.ResultSet;
import java.util.List;

import dev.teerayut.model.CalculateModel;

public class MainInterface {

	interface viewInterface{
		void onSuccess(String success);
		void onFail(String fail);
	}
	
	interface presentInterface{
		ResultSet getAllCurrency(String type);
		ResultSet getCompName();
		void insertReceipt(List<CalculateModel> calculateList);
	}
}
