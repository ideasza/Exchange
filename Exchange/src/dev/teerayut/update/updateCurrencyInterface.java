package dev.teerayut.update;

import java.util.List;

import dev.teerayut.model.CurrencyModel;

public interface updateCurrencyInterface {
	
	interface viewInterface{
		void showCurrency(List<CurrencyModel> modelList);
		void onFail(String fail);
	}

	interface presentInterface{
		void LoadCurrency();
		void updateCurrency(int id, String value1, String value2, String value3, boolean value4);
	}
}
