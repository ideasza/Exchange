package dev.teerayut.report;

import java.sql.ResultSet;
import java.util.List;

import dev.teerayut.model.ReceiptModel;

public interface ReportInterface {

	interface viewInterface{
		void showReport(List<ReceiptModel> listReport);
		void onSuccess(String str);
	}
	
	interface presentInterface{
		void getReport(String date);
		void exportToExcel(String date, List<ReceiptModel> listReport);
	}
}
