package dev.teerayut.receipt;

import java.text.DecimalFormat;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import dev.teerayut.main.MainActivity;
import dev.teerayut.model.CalculateModel;
import dev.teerayut.utils.Convert;
import dev.teerayut.utils.Preferrence;

public class Receive {	
	private String date, time;
	private float grandTotal = 0;
	private DecimalFormat df = new DecimalFormat("#,###.00");
	
	private PrinterServiceClass printerService;
	private PrinterOptionsClass printerOptions;
	static String printerName = "";
	
	private Preferrence prefs;
	
	public Receive() {
		prefs = new Preferrence();
		if (prefs.getPreferrence("printer") == null) {
			final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/fail32.png"));
	        JOptionPane.showMessageDialog(null, "กรุณาตั้งค่าปริ้นเตอร์", "Alert", JOptionPane.ERROR_MESSAGE, icon);
		} else {
			printerName = prefs.getPreferrence("printer");
		}
	}
	
	public void printReceipt(List<CalculateModel> calModel) {
		for(CalculateModel m : calModel) {
			date = m.getReceiveDate().trim().toString();
			time = m.getReceiptTime().trim().toString();
		}
		printerService = new PrinterServiceClass();
		PrinterOptionsClass p = new PrinterOptionsClass();
		
        p.resetAll();
        p.initialize();
        p.newLine();
        p.alignCenter();
        p.setText((prefs.getPreferrence("compName") != null)? prefs.getPreferrence("compName") : "Company Name");
        p.newLine();
        p.alignCenter();
        p.setText((prefs.getPreferrence("compID") != null)? "เลขประจำตัวผู้เสียภาษี : " + prefs.getPreferrence("compID") : "Company License");
        p.newLine();
        p.setText("Date : " +  date + " " + time);
        p.newLine();
        p.alignCenter();
        p.setText(" - RECEIPT BOUGHT - ");
        p.newLine();
        p.alignLeft();
        p.addLineSeperator();
        p.newLine();
        p.setText("Currency    Rate\tAmount\tTotal");
        p.newLine();
        p.addLineSeperator();
        p.newLine();
        for (int i = 0 ; i < calModel.size(); i++) {
        	String currency = calModel.get(i).getReceiveCurrency().trim();
       	 	if(currency.contains(" ")){
       	 		currency= currency.substring(0, currency.indexOf(" ")); 
            }
        	
        	p.setText(" " + currency 
        			+ "\t    " + new Convert().formatDecimal(Float.parseFloat(calModel.get(i).getReceiveRate().trim()))
        			+ "\t" + new Convert().formatDecimal(Float.parseFloat(calModel.get(i).getReceiveAmount().trim()))
        			+ "\t" + new Convert().formatDecimal(Float.parseFloat(calModel.get(i).getReceiveTotal().trim()))
        	);
        	
        	float total = Float.parseFloat(calModel.get(i).getReceiveTotal().trim());
        	grandTotal += total;
        	p.newLine();
        }
        p.newLine();
	    p.setText("\t\tTotal" + "\t\t" + new Convert().formatDecimal(grandTotal));
	    p.newLine();
        p.addLineSeperator2();
        p.newLine();
        p.alignCenter();
        p.setText(" - THANK YOU - ");
        p.feed((byte)3);
        p.finit();

        feedPrinter(p.finalCommandSet());
        
        byte[] cutPaper = new byte[] {27, 105};
		printerService.printBytes(printerName, cutPaper);
	}
	
	private static boolean feedPrinter(String p) {
		System.out.println(p);
        try {
            AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(printerName, null));
            DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();
	        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
	        byte[] bytes = p.getBytes("TIS-620");
	        //byte[] bytes = p.getBytes("CP437");
	        Doc doc = new SimpleDoc(bytes, flavor, null);
	        System.out.println(job);
	        job.print(doc, null);
	        System.out.println("Done !");
	    } catch(javax.print.PrintException pex) {
	        System.out.println("Printer Error " + pex.getMessage());
	        final ImageIcon icon = new ImageIcon(MainActivity.class.getResource("/icon/fail32.png"));
	        JOptionPane.showMessageDialog(null, "Printer Error" + pex.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE, icon);
	        return false;
	    } catch(Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	        return true;
    }	
}
