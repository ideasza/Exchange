package dev.teerayut.settings;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterState;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import dev.teerayut.model.PrinterModel;
import dev.teerayut.utils.Config;
import dev.teerayut.utils.Preferrence;
import dev.teerayut.utils.ScreenCenter;

public class SettingsActivity extends JDialog{

	public static final int ID_OK = 1;
    public static final int ID_CANCEL = 0;
    private int exitCode = ID_CANCEL;
    
    public SettingsActivity(Frame owner) {
        super(owner);
        createGUI();
    }

    /**
     * @wbp.parser.constructor
     */
    public SettingsActivity(Dialog owner) {
        super(owner);        
        createGUI();
    }
    
    private Preferrence prefs;
    
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel companyPanel;
    
    private JComboBox comboBoxPrinter;
    
    private JFileChooser chooser = new JFileChooser();;
    private File file;
    
    private PrinterModel model;
    private List<PrinterModel> modelList = new ArrayList<PrinterModel>();
    private JTextField textField;
    private JTextField textField1;
    private JTextField textField2;
    private void createGUI() {
    	prefs = new Preferrence();
    	int width = 680;
    	int height = 480;
    	setBounds(0, 0, 681, 523);
    	setTitle("ตั้งค่า");
    	setIconImage(new ImageIcon(getClass().getResource("/icon/icon.png")).getImage());
    	new ScreenCenter().centreWindow(this);
    	getContentPane().setLayout(null);
    	
    	topPanel = new javax.swing.JPanel();
    	topPanel.setBounds(0, 0, 660, 135);
    	topPanel.setBorder(BorderFactory.createTitledBorder("Database"));
    	getContentPane().add(topPanel);
    	topPanel.setLayout(null);
    	
    	textField = new JTextField();
    	textField.setFont(new Font("Angsana New", Font.PLAIN, 25));
    	textField.setBounds(60, 53, 425, 40);
    	topPanel.add(textField);
    	textField.setColumns(10);
    	
    	JButton btnNewButton = new JButton("Browse");
    	btnNewButton.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent arg0) {
    			FileNameExtensionFilter filter = new FileNameExtensionFilter("SQLite", "sqlite");
    			chooser.setFileFilter(filter);
    			int returnVal = chooser.showOpenDialog(null);
                if(returnVal == chooser.APPROVE_OPTION){
                	file = chooser.getSelectedFile();
                    String filename = file.getName();
                    textField.setText(file.getAbsolutePath().toString());
                    copyFile(file.getAbsolutePath().toString(), Config.DB_PATH + Config.DB_FILE);
                }
    		}
    	});
    	btnNewButton.setBounds(497, 53, 97, 40);
    	topPanel.add(btnNewButton);
    	
    	bottomPanel = new javax.swing.JPanel();
    	bottomPanel.setBounds(0, 140, 660, 135);
    	bottomPanel.setBorder(BorderFactory.createTitledBorder("Printer"));
    	getContentPane().add(bottomPanel);
    	
    	comboBoxPrinter = new JComboBox();
        comboBoxPrinter.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		JComboBox comboBox = (JComboBox) event.getSource();
                Object selected = comboBox.getSelectedItem();
                //System.out.println(selected.toString());
                //System.out.println("action Performed : " + selected.toString());
                /*if (selected.equals("Ready")) {
                	lblStatus.setForeground(Color.GREEN);
                } else {
                	lblStatus.setForeground(Color.RED);
                }*/
        	}
        });
        comboBoxPrinter.addPropertyChangeListener(new PropertyChangeListener() {
        	public void propertyChange(PropertyChangeEvent arg0) {
        		//System.out.println("Changed : " + comboBoxPrinter.getSelectedIndex());
        	}
        });
        bottomPanel.setLayout(null);
        comboBoxPrinter.setBounds(60, 55, 532, 40);
        bottomPanel.add(comboBoxPrinter);
        
        getPrinter();
        
        File database = new File(Config.DB_PATH + Config.DB_FILE);
    	if (database.exists()) {
    		textField.setText(database.getAbsolutePath().toString());	
    	}
    	
    	companyPanel = new javax.swing.JPanel();
    	companyPanel.setBounds(0, 280, 660, 135);
    	companyPanel.setBorder(BorderFactory.createTitledBorder("Company"));
    	getContentPane().add(companyPanel);
    	companyPanel.setLayout(null);
    	
    	textField1 = new JTextField();
    	textField1.setFont(new Font("Angsana New", Font.PLAIN, 25));
    	textField1.setBounds(133, 26, 297, 40);
    	companyPanel.add(textField1);
    	
    	textField2 = new JTextField();
    	textField2.setFont(new Font("Angsana New", Font.PLAIN, 25));
    	textField2.setBounds(133, 76, 390, 40);
    	companyPanel.add(textField2);
    	
    	JLabel lblNewLabel = new JLabel("ชื่อบริษัท");
    	lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    	lblNewLabel.setFont(new Font("Angsana New", Font.PLAIN, 25));
    	lblNewLabel.setBounds(50, 26, 71, 40);
    	companyPanel.add(lblNewLabel);
    	
    	JLabel label = new JLabel("เลขที่ผู้เสียภาษี");
    	label.setHorizontalAlignment(SwingConstants.RIGHT);
    	label.setFont(new Font("Angsana New", Font.PLAIN, 25));
    	label.setBounds(24, 76, 97, 40);
    	companyPanel.add(label);
    	
    	
    	JButton button = new JButton("ตกลง");
    	button.setBounds(540, 423, 97, 40);
    	getContentPane().add(button);
    	button.setPreferredSize(new Dimension(90, 35));
    	button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!textField1.getText().toString().equals("") && textField2.getText().toString().equals("")) {
					prefs.setPreferrence("compName", textField1.getText().toString().trim());
				} else if (textField1.getText().toString().equals("") && !textField2.getText().toString().equals("")) {
					prefs.setPreferrence("compID", textField2.getText().toString().trim());
				} else if (!textField1.getText().toString().equals("") && !textField2.getText().toString().equals("")) {
					prefs.setPreferrence("compName", textField1.getText().toString().trim());
					prefs.setPreferrence("compID", textField2.getText().toString().trim());
				}
				
				prefs.setPreferrence("printer", comboBoxPrinter.getSelectedItem().toString());
        		final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/success32.png"));
		        JOptionPane.showMessageDialog(null, "บันทึกการตั้งค่าแล้ว", "Complete", JOptionPane.ERROR_MESSAGE, icon);
		        
			}
		});    	
    	
    	getProperties();
    }
    
    private void getProperties() {
    	try {
    		File configFile = new File(Config.CONFIG_PATH + "config.properties");
    		if (configFile.exists()) {
		    	if (prefs.getPreferrence("printer") != null) {
		        	comboBoxPrinter.setSelectedItem(prefs.getPreferrence("printer"));
		        }
		    	
		    	if (prefs.getPreferrence("compName") != null) {
		        	textField1.setText(prefs.getPreferrence("compName"));
		        } else {
		        	textField1.setText("");
		        }
		        
		        if (prefs.getPreferrence("compID") != null) {
		        	textField2.setText(prefs.getPreferrence("compID"));
		        } else {
		        	textField2.setText("");
		        }
    		}
    	} catch (Exception e) {
    		
    	}
    }
    
    @Override
    public void dispose() {
        super.dispose();
    }

    public int doModal() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setVisible(true);
        return exitCode;
    }
    
    private void copyFile(String from, String to){
    	Path FROM = Paths.get(from);
		Path TO = Paths.get(to);
		CopyOption[] options = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES}; 
		try {
			java.nio.file.Files.copy(FROM, TO, options);
		} catch (IOException e) {
			final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/fail32.png"));
            JOptionPane.showMessageDialog(null, "Copy file : " + e.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE, icon);
		}
		
		final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/success32.png"));
		JOptionPane.showMessageDialog(null, "เพิ่มไฟล์ฐานข้อมูลแล้ว", "Success", JOptionPane.ERROR_MESSAGE, icon);
    }
    
    private void getPrinter() {
    	PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
    	
        for (PrintService printer : printServices) {
        	 comboBoxPrinter.addItem(printer.getName());
        	 PrintServiceAttributeSet printServiceAttributes = printer.getAttributes();
         	 PrinterState printerState = (PrinterState) printServiceAttributes.get(PrinterState.class);
         	 //System.out.println(printerState);
         	 model = new PrinterModel(printer.getName(), (printerState != null) ? "Ready" : "Offline");
         	 modelList.add(model);
         	 
         	 AttributeSet attributes = printer.getAttributes();
             for (javax.print.attribute.Attribute a : attributes.toArray()) {
                 String name = a.getName();
                 String value = attributes.get(a.getClass()).toString();
                 //System.out.println(name + " : " + value);
             }
        }
    }
}
