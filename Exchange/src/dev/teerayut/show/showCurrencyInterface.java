package dev.teerayut.show;

import java.util.List;

import dev.teerayut.model.CurrencyModel;

public interface showCurrencyInterface {

	interface viewInterface{
		void showCurrency(List<CurrencyModel> modelList);
	}
	
	interface presentInterface{
		void LoadCurrency();
		void LoadCurrencyOffset(int limit, int offset);
	}
}
