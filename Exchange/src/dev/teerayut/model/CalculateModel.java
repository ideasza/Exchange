package dev.teerayut.model;

public class CalculateModel {

	private String receiptDate;
	private String receiptTime;
	private String receiptCurrency;
	private String receiptRate;
	private String receiptAmount;
	private String receiptTotal;
	private String receiptType;
	
	public CalculateModel(String date, String time, String currency, String rate, String amount, String total, String type) {
		this.receiptDate = date;
		this.receiptTime = time;
		this.receiptCurrency = currency;
		this.receiptRate = rate;
		this.receiptAmount = amount;
		this.receiptTotal = total;
		this.receiptType = type;
	}
	
	public String getReceiveDate() {
		return receiptDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiptDate = receiveDate;
	}
	public String getReceiveCurrency() {
		return receiptCurrency;
	}
	public void setReceiveCurrency(String receiveCurrency) {
		this.receiptCurrency = receiveCurrency;
	}
	public String getReceiveRate() {
		return receiptRate;
	}
	public void setReceiveRate(String receiveRate) {
		this.receiptRate = receiveRate;
	}
	public String getReceiveAmount() {
		return receiptAmount;
	}
	public void setReceiveAmount(String receiveAmount) {
		this.receiptAmount = receiveAmount;
	}
	public String getReceiveTotal() {
		return receiptTotal;
	}
	public void setReceiveTotal(String receiveTotal) {
		this.receiptTotal = receiveTotal;
	}
	public String getReceiveType() {
		return receiptType;
	}
	public void setReceiveType(String receiveType) {
		this.receiptType = receiveType;
	}

	public String getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(String receiptTime) {
		this.receiptTime = receiptTime;
	}
}
