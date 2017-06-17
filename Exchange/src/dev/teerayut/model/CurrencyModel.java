package dev.teerayut.model;

public class CurrencyModel {

	private int currencyID;
	private String currencyName;
	private String currencyDesc;
	private String currencyBuyRate;
	private String currencySellRate;
	private String currencyStatus;
	
	public CurrencyModel(int id, String currencyName/*, String currencyDesc*/, String currencyBuyRate, String currencySellRate, String currencyStatus) {
		this.setCurrencyID(id);
		this.currencyName = currencyName;
		//this.currencyDesc = currencyDesc;
		this.currencyBuyRate = currencyBuyRate;
		this.currencySellRate =  currencySellRate;
		this.currencyStatus = currencyStatus;
	}
	
	public CurrencyModel(){
		
	}
	
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	/*public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}*/
	public String getCurrencyBuyRate() {
		return currencyBuyRate;
	}
	public void setCurrencyBuyRate(String currencyBuyRate) {
		if (currencyBuyRate.isEmpty()) {
			this.currencyBuyRate = "0.00";
		} else {
			this.currencyBuyRate = currencyBuyRate;
		}
	}
	public String getCurrencySellRate() {
		return currencySellRate;
	}
	public void setCurrencySellRate(String currencySellRate) {
		if (currencySellRate.isEmpty()) {
			this.currencySellRate = "0.00";
		} else {
			this.currencySellRate = currencySellRate;
		}
	}

	public int getCurrencyID() {
		return currencyID;
	}

	public void setCurrencyID(int currencyID) {
		this.currencyID = currencyID;
	}

	public String getCurrencyStatus() {
		return currencyStatus;
	}

	public void setCurrencyStatus(String currencyStatus) {
		this.currencyStatus = currencyStatus;
	}
}
