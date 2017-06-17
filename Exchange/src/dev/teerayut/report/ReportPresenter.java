package dev.teerayut.report;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import dev.teerayut.connection.ConnectionDB;
import dev.teerayut.model.ReceiptModel;
import dev.teerayut.utils.Config;
import dev.teerayut.utils.Convert;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ReportPresenter implements ReportInterface.presentInterface{

	private ResultSet resultSet;
	private ConnectionDB connectionDB;
	
	private float GrandTotal = 0;
	private float GrandAmountBuy = 0;
	private float GrandAmountSell = 0;
	private DecimalFormat df = new DecimalFormat("#,###.00");
	
	private String fileName;
	private ReportInterface.viewInterface view;
	
	private ReceiptModel receiptModel;
	private List<ReceiptModel> listReport = new ArrayList<ReceiptModel>();
	
	public ReportPresenter(ReportInterface.viewInterface view) {
		this.view = view;
	}
	
	@Override
	public void getReport(String date) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("SELECT RC_DATE, RC_NAME, RC_RATE, RC_AMOUNT, RC_TOTAL, RC_TYPE FROM RECEIPT ");
		sb.append("WHERE RC_DATE = '" + date + "' ORDER BY RC_ID ASC");
		
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				receiptModel = new ReceiptModel(resultSet.getString("RC_DATE"), 
						resultSet.getString("RC_NAME"), 
						resultSet.getString("RC_RATE"), 
						(resultSet.getString("RC_AMOUNT").equals(""))? "0.00" : resultSet.getString("RC_AMOUNT"), 
						(resultSet.getString("RC_TOTAL").equals(""))? "0.00" : resultSet.getString("RC_TOTAL"), 
						resultSet.getString("RC_TYPE"));
				listReport.add(receiptModel); 
			}
			view.showReport(listReport);
		} catch(Exception e) {
			System.out.println("Report query : " + e.getMessage());
		}
	}

	@Override
	public void exportToExcel(String date, List<ReceiptModel> listReport) {
		File file = new File(Config.REPORT_PATH);
		if (!file.exists()) {
			 if (file.mkdirs()) {
				 fileName = Config.REPORT_PATH + date + ".xls";
		    } else {
		    	final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/fail32.png"));
		    	JOptionPane.showMessageDialog(null, "ไม่สามารถสร้างโพลเดอร์ได้", "Alert", JOptionPane.ERROR_MESSAGE, icon);
		    }
		}
		
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
			
			//*** Create Font ***//
			WritableFont fontHeader = new WritableFont(WritableFont.TIMES, 14);
			fontHeader.setColour(Colour.WHITE);
			
			WritableFont fontReport = new WritableFont(WritableFont.TIMES, 12);
			fontReport.setColour(Colour.BLACK);
			
			WritableSheet ws1 = workbook.createSheet("SheetReport", 0);
			
			//*** Header ***//
            WritableCellFormat cellFormat1 = new WritableCellFormat(fontHeader);
            cellFormat1.setAlignment(Alignment.CENTRE);
            cellFormat1.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat1.setBorder(Border.ALL, BorderLineStyle.THIN);
            cellFormat1.setBackground(Colour.LIGHT_ORANGE);
            
            //*** Data ***//
            WritableCellFormat cellFormat2 = new WritableCellFormat(fontReport);
            cellFormat2.setAlignment(jxl.format.Alignment.CENTRE);
            cellFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat2.setWrap(true);
            cellFormat2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.HAIR,
            jxl.format.Colour.BLACK);
            
            WritableCellFormat cellFormatCurrency = new WritableCellFormat(fontReport);
            cellFormatCurrency.setAlignment(jxl.format.Alignment.RIGHT);
            cellFormatCurrency.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormatCurrency.setWrap(false);
            cellFormatCurrency.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.HAIR,
            jxl.format.Colour.BLACK);
            
            WritableCellFormat cellFormat3 = new WritableCellFormat(fontReport);
    	    cellFormat3.setAlignment(jxl.format.Alignment.LEFT);
    	    cellFormat3.setVerticalAlignment(VerticalAlignment.CENTRE);
    	    
    	    WritableCellFormat cellFormat4 = new WritableCellFormat(fontReport);
    	    cellFormat4.setAlignment(jxl.format.Alignment.RIGHT);
    	    cellFormat4.setVerticalAlignment(VerticalAlignment.CENTRE);
            
    	    ws1.mergeCells(0, 0, 6, 0);
    	    Label lable = new Label(0, 0,"Report " + date, cellFormat1);
    	    ws1.addCell(lable);
    	    
    	    //*** Header ***//
    	    ws1.setColumnView(0, 8); // Column CustomerID
    	    ws1.addCell(new Label(0,1,"No.",cellFormat1));
    	    
    	    ws1.setColumnView(1, 25); // Column Name
    	    ws1.addCell(new Label(1,1,"Currency",cellFormat1));
    	    
    	    ws1.setColumnView(2, 10); // Column Email
    	    ws1.addCell(new Label(2,1,"Buy rate",cellFormat1));
    	    
    	    ws1.setColumnView(3, 10); // Column CountryCode
    	    ws1.addCell(new Label(3,1,"Sell rate",cellFormat1));
    	    
    	    ws1.setColumnView(4, 12); // Column Budget
    	    ws1.addCell(new Label(4,1,"Amount buy",cellFormat1));
    	    
    	    ws1.setColumnView(5, 12); // Column Used
    	    ws1.addCell(new Label(5,1,"Amount sell",cellFormat1));
    	    
    	    ws1.setColumnView(6, 20); // Column Used
    	    ws1.addCell(new Label(6,1,"Total",cellFormat1));
    	    
    	    for (int i = 0; i < listReport.size(); i++) {
    	    	ReceiptModel m = listReport.get(i);
    	    	ws1.addCell(new Label(0, (i + 2), "" + (i + 1), cellFormat2));
	    	    ws1.addCell(new Label(1, (i + 2), m.getReceiptCurrencyName().trim(), cellFormat2));
	    	    ws1.addCell(new Label(2, (i + 2), (m.getReceiptType().trim().equals("buy") ? m.getReceiptCurrencyRate().trim() : "-"), cellFormat2));
	    	    ws1.addCell(new Label(3, (i + 2), (m.getReceiptType().trim().equals("sell") ? m.getReceiptCurrencyRate().trim() : "-"), cellFormat2));
	    	    ws1.addCell(new Label(4, (i + 2), (m.getReceiptType().trim().equals("buy") ? df.format(Float.parseFloat(m.getReceiptCurrencyAmount().trim())) : "-"), cellFormatCurrency));
	    	    ws1.addCell(new Label(5, (i + 2), (m.getReceiptType().trim().equals("sell") ? df.format(Float.parseFloat(m.getReceiptCurrencyAmount().trim())) : "-"), cellFormatCurrency)); 
	 	    	ws1.addCell(new Label(6, (i + 2), df.format(Double.parseDouble(m.getReceiptAmountTotal().trim())), cellFormatCurrency)); 
	    	    
	    	    ws1.addCell(new Label(0, (listReport.size() + 2), "Grand Total" , cellFormat3));
	    	    
	    	    float grandAmountBuy = (m.getReceiptType().trim().equals("buy") ? Float.parseFloat(m.getReceiptCurrencyAmount().trim()) : Float.parseFloat("0.00"));
	    	    float grandAmountSell = (m.getReceiptType().trim().equals("sell") ? Float.parseFloat(m.getReceiptCurrencyAmount().trim()) : Float.parseFloat("0.00"));
	    	    float grandTotal = Float.parseFloat(m.getReceiptAmountTotal().trim());
	    	    GrandAmountBuy += grandAmountBuy;
	    	    GrandAmountSell += grandAmountSell;
	    	    GrandTotal += grandTotal;
	    	    
	    	    ws1.addCell(new Label(4, (listReport.size() + 2), new Convert().formatDecimal(GrandAmountBuy), cellFormat4));
	    	    ws1.addCell(new Label(5, (listReport.size() + 2), new Convert().formatDecimal(GrandAmountSell), cellFormat4));
	    	    ws1.addCell(new Label(6, (listReport.size() + 2), new Convert().formatDecimal(GrandTotal), cellFormat4));
    	    }

    	    workbook.write();
    	    workbook.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		view.onSuccess("ส่งออกรายงานสำเร็จ");
		try {
			Desktop.getDesktop().open(new File(Config.REPORT_PATH));
		} catch (IOException e) {
			final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/fail32.png"));
            JOptionPane.showMessageDialog(null, e.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE, icon);
		}
	}
}
