package dev.teerayut.model;

public class ReceiptModel {

	private String receiptDate;
	private String receiptCurrencyName;
	private String receiptCurrencyRate;
	private String receiptCurrencyAmount;
	private String receiptAmountTotal;
	private String receiptType;
	
	public ReceiptModel(String date, String name, String rate, String amount, String total, String type) {
		this.setReceiptDate(date);
		this.setReceiptCurrencyName(name);
		this.setReceiptCurrencyRate(rate);
		this.setReceiptCurrencyAmount(amount);
		this.setReceiptAmountTotal(total);
		this.setReceiptType(type);
	}

	public String getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getReceiptCurrencyName() {
		return receiptCurrencyName;
	}

	public void setReceiptCurrencyName(String receiptCurrencyName) {
		this.receiptCurrencyName = receiptCurrencyName;
	}

	public String getReceiptCurrencyRate() {
		return receiptCurrencyRate;
	}

	public void setReceiptCurrencyRate(String receiptCurrencyRate) {
		this.receiptCurrencyRate = receiptCurrencyRate;
	}

	public String getReceiptCurrencyAmount() {
		return receiptCurrencyAmount;
	}

	public void setReceiptCurrencyAmount(String receiptCurrencyAmount) {
		this.receiptCurrencyAmount = receiptCurrencyAmount;
	}

	public String getReceiptAmountTotal() {
		return receiptAmountTotal;
	}

	public void setReceiptAmountTotal(String receiptAmountTotal) {
		this.receiptAmountTotal = receiptAmountTotal;
	}

	public String getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}
}
