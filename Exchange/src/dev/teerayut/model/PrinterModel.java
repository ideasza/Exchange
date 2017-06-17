package dev.teerayut.model;

public class PrinterModel {

	private String printName;
	private String printStatus;
	
	public PrinterModel(String name, String status) {
		this.printName = name;
		this.printStatus = status;
	}
	
	public String getPrintName() {
		return printName;
	}
	public void setPrintName(String printName) {
		this.printName = printName;
	}
	public String getPrintStatus() {
		return printStatus;
	}
	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
	}
}
