package dev.teerayut.show;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dev.teerayut.connection.ConnectionDB;
import dev.teerayut.model.CurrencyModel;

public class showCurrencyPresenter implements showCurrencyInterface.presentInterface {

	private ConnectionDB connectionDB;
	private showCurrencyInterface.viewInterface view;
	
	private CurrencyModel model;
	private List<CurrencyModel> modelList = new ArrayList<CurrencyModel>();
	
	public showCurrencyPresenter(showCurrencyInterface.viewInterface view) {
		this.view = view;
	}
	@Override
	public void LoadCurrency() {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("SELECT CR_ID, CR_NAME, CR_DESC, CR_BUY_RATE, CR_SELL_RATE ");
		sb.append("FROM CURRENCY ");
		sb.append("WHERE CR_STATUS = 'Y' AND CR_BUY_RATE != '-' AND CR_SELL_RATE != '-'");
		
		connectionDB = new ConnectionDB();
		CurrencyModel model = null;
		List<CurrencyModel> modelList = new ArrayList<CurrencyModel>();
		try {
			ResultSet resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				model = new CurrencyModel(resultSet.getInt(1), resultSet.getString(2)/*, resultSet.getString(3)*/, resultSet.getString(4),
						resultSet.getString(5), "");
            	modelList.add(model);
			}
			view.showCurrency(modelList);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void LoadCurrencyOffset(int limit, int offset) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("SELECT CR_ID, CR_NAME, CR_DESC, CR_BUY_RATE, CR_SELL_RATE ");
		sb.append("FROM CURRENCY ");
		sb.append("WHERE CR_STATUS = 'Y' AND CR_BUY_RATE != '-' AND CR_SELL_RATE != '-' ");
		sb.append("LIMIT " + limit + " OFFSET " + offset);
		
		connectionDB = new ConnectionDB();
		CurrencyModel model = null;
		List<CurrencyModel> modelList = new ArrayList<CurrencyModel>();
		try {
			ResultSet resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				model = new CurrencyModel(resultSet.getInt(1), resultSet.getString(2)/*, resultSet.getString(3)*/, resultSet.getString(4),
						resultSet.getString(5), "");
            	modelList.add(model);
			}
			view.showCurrency(modelList);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
