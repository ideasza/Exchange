package dev.teerayut.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dev.teerayut.model.CalculateModel;
import dev.teerayut.model.CurrencyModel;
import dev.teerayut.report.ReportActivity;
import dev.teerayut.settings.SettingsActivity;
import dev.teerayut.show.showCurrencyActivity;
import dev.teerayut.update.updateCurrencyActivity;
import dev.teerayut.utils.Config;
import dev.teerayut.utils.Convert;
import dev.teerayut.utils.DateFormate;
import dev.teerayut.utils.ListRenderer;
import dev.teerayut.utils.Preferrence;
import dev.teerayut.utils.ScreenCenter;
import dev.teerayut.utils.tableButtonRenderrer;
import java.awt.event.MouseAdapter;

public class MainActivity extends JFrame implements MouseListener, MainInterface.viewInterface {

	private javax.swing.JPanel topPanel;
	private javax.swing.JPanel leftPanel;
	private javax.swing.JPanel bottomPanel;
	private javax.swing.JPanel centerPanel;
	private javax.swing.JLabel lblCompanyName;
	private javax.swing.JLabel lblCoID;
	private javax.swing.JLabel lblTime;
	private javax.swing.JLabel lblSource;
	private JList list;
	private javax.swing.JScrollPane scrollPane;
	private int tabIndex;
	private javax.swing.JTabbedPane tabPane;
	private DefaultTableModel model;
	private DefaultTableModel modelSell;
	private Object[] columName = {"สกุลเงิน", "เรท", "จำนวน", "รวม", "ลบ"};
	private Object[][] data;
	private int width, height, w, h, frmWidth, frmHeight;
	private DecimalFormat df = new DecimalFormat("#,###.00");
	private MainInterface.presentInterface present;
	private CurrencyModel currencyModel;
	private List<CurrencyModel> currencyModelList = new ArrayList<CurrencyModel>();
	private CalculateModel calculateModel;
	private List<CalculateModel> calculateModelList = new ArrayList<CalculateModel>();
	private Point moniter1 = null;
	private Point moniter2 = null;
	private JMenu edit, report;
	private Preferrence prefs;

	private static final String prefixName = "RWA";
	private String receiptNumber = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainActivity frame = new MainActivity();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void createFolder() {
		File file = new File(Config.DB_PATH);
		try {
			if (!file.exists()) {
			    if (file.mkdirs()) {
			    } else {
			    	final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/fail32.png"));
	                JOptionPane.showMessageDialog(null, "ไม่สามารถสร้างโฟลเดอร์ได้", "Alert", JOptionPane.ERROR_MESSAGE, icon);
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initWidget() {
		topPanel = new javax.swing.JPanel();
		leftPanel = new javax.swing.JPanel();
		bottomPanel = new javax.swing.JPanel();
		centerPanel = new javax.swing.JPanel();
		lblCompanyName = new javax.swing.JLabel();
		lblCoID = new javax.swing.JLabel();
		lblTime = new javax.swing.JLabel();
		lblSource = new javax.swing.JLabel();
		list = new JList();
		scrollPane = new javax.swing.JScrollPane();
		tabPane = new javax.swing.JTabbedPane();
	}
	
	private void setTime(JLabel lbTime) {
		Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	lbTime.setText(new DateFormate().getDateWithTime());
            }
        }, 0, 1000);
	}
	
	private void getScreenPoint() {	
		for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			if (moniter1 == null) {
				moniter1 = gd.getDefaultConfiguration().getBounds().getLocation();
			} else if (moniter2 == null) {
				moniter2 = gd.getDefaultConfiguration().getBounds().getLocation();
			}
		}
		if (moniter2 == null) {
			moniter2 = moniter1;
		}
	}

	private showCurrencyActivity sc;
	/**
	 * Create the frame.
	 */
	public MainActivity() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				prefs = new Preferrence();
				sc = new showCurrencyActivity();
				File database = new File(Config.DB_PATH + Config.DB_FILE);
				if (database.exists()) {
					buyRate();
					lblSource.setText(" jdbc:sqlite: " + new File(Config.DB_PATH + Config.DB_FILE).getAbsolutePath().toString());
	                lblSource.setIcon(new ImageIcon(getClass().getResource("/icon/database_connect.png")));
	                prefs.setPreferrence("settings_open", "1");
	                sc.setLocation(moniter2);
	                sc.setVisible(true);
				} else {
					prefs.setPreferrence("settings_open", "0");
					edit.setEnabled(false);
	                report.setEnabled(false);
	                lblSource.setText(" jdbc:sqlite: Disconnect");
	                lblSource.setIcon(new ImageIcon(getClass().getResource("/icon/database_not_connect.png")));
					final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/warning.png"));
		            JOptionPane.showMessageDialog(null, "ไม่พบฐานข้อมูล", "Warning", JOptionPane.WARNING_MESSAGE, icon);
				}
				
				lblCompanyName.setText((prefs.getPreferrence("compName") != null)? prefs.getPreferrence("compName") : "Company Name");
				lblCoID.setText((prefs.getPreferrence("compID") != null)? "เลขประจำตัวผู้เสียภาษี : " + prefs.getPreferrence("compID") : "Company License");
			}
		});
		
		initWidget();
		getScreenPoint();
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) dimension.getWidth();
		height = (int) dimension.getHeight();
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		h = gd.getDisplayMode().getHeight();
		w = gd.getDisplayMode().getHeight();
		/************************ Main screen ***************************/
		setTitle("Currency Exchange");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new java.awt.Dimension(1280, 720));
		setBounds(0, 0, 1280, 720);
		setExtendedState(MAXIMIZED_BOTH);
		setIconImage(new ImageIcon(getClass().getResource("/icon/icon.png")).getImage());
		getContentPane().setLayout(new java.awt.BorderLayout());
		new ScreenCenter().centreWindow(this);
		frmWidth = getSize().width;
		frmHeight = getSize().height;
		/************************End Main screen ***************************/
		
		/****************************Bottom panel****************************/
		bottomPanel.setPreferredSize(new java.awt.Dimension(w, 50));
		bottomPanel.setLayout(new java.awt.BorderLayout());
		getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);
		
		lblTime.setForeground(new Color(0, 51, 102));
		lblTime.setFont(new Font("Angsana New", Font.BOLD, 24));
		lblTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		setTime(lblTime);
		bottomPanel.add(lblTime, java.awt.BorderLayout.LINE_END);
		
		lblSource.setForeground(new Color(0, 51, 102));
		lblSource.setFont(new Font("Angsana New", Font.BOLD, 24));
		lblSource.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		bottomPanel.add(lblSource, java.awt.BorderLayout.LINE_START);
		/****************************End Bottom panel****************************/
		
		createFolder();
		topPanel();
		leftPanel();
		centerPanel();
		menu();
		pack();
		setLocationRelativeTo(null);
	}
	
	private void topPanel() {
		topPanel.setLayout(new java.awt.BorderLayout());
		getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);
		
		lblCompanyName.setForeground(new Color(0, 51, 102));
		lblCompanyName.setFont(new Font("Angsana New", Font.BOLD, 60));
		lblCompanyName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		topPanel.add(lblCompanyName, java.awt.BorderLayout.CENTER);
		
		lblCoID.setForeground(new Color(0, 51, 102));
		lblCoID.setFont(new Font("Angsana New", Font.PLAIN, 30));
		lblCoID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		topPanel.add(lblCoID, java.awt.BorderLayout.AFTER_LAST_LINE);
	}
	
	private void leftPanel() {
		int y = (h - 70);
		leftPanel.setBorder(BorderFactory.createTitledBorder("Currency"));
		leftPanel.setPreferredSize(new java.awt.Dimension(350, (h - 70)));
		getContentPane().add(leftPanel, java.awt.BorderLayout.WEST);
		leftPanel.setLayout(new java.awt.BorderLayout());
		
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.addMouseListener(this);
		scrollPane.setViewportView(list);
		
		scrollPane.setVerifyInputWhenFocusTarget(false);
		scrollPane.setRequestFocusEnabled(false);
		leftPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
	}
	
	private void centerPanel() {
		present = new MainPresenter(this);
		centerPanel.setLayout(new java.awt.BorderLayout());
		centerPanel.setPreferredSize(new java.awt.Dimension((w - 400), (h - 70)));
		centerPanel.setBorder(BorderFactory.createTitledBorder("Exchange"));
		getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);
		
		tabPane.addTab( "Buy", panelBuy());
		tabPane.addTab( "Sell", panelSell());
		centerPanel.add(tabPane, java.awt.BorderLayout.CENTER);	
		
		tabPane.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	            if(tabPane.getSelectedIndex() == 0) {
	            	tabIndex = tabPane.getSelectedIndex();
	            	buyRate();
	            } else if (tabPane.getSelectedIndex() == 1) {
	            	tabIndex = tabPane.getSelectedIndex();
	            	sellRate();
	            }
	        }
	    });
	}
	
	private JPanel panelBuy() {
		calculateModelList.clear();
		javax.swing.JPanel panel = new javax.swing.JPanel();
		javax.swing.JPanel panelTop = new javax.swing.JPanel();
		javax.swing.JPanel panelCenter = new javax.swing.JPanel();
		javax.swing.JPanel panelBottom = new javax.swing.JPanel();
		javax.swing.JLabel lblTotal = new javax.swing.JLabel("0.00");
		javax.swing.JButton buttonClear = new javax.swing.JButton("CLEAR");
		javax.swing.JButton buttonOK = new javax.swing.JButton("OK");
		javax.swing.JScrollPane tableScroll = new javax.swing.JScrollPane();
		
		model = new DefaultTableModel(data, columName);
		javax.swing.JTable table = new javax.swing.JTable(model) {
			public boolean isCellEditable(int rowIndex, int colIndex) {
		    	if(colIndex == 0) {
		    		return false;
		    	} else if (colIndex == 1) {
		    		return false;
		    	} else if (colIndex == 3) {
		    		return false;
		    	} else {
		    		return true;
		    	}
		    }
		};
		
		table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		    	int row = table.rowAtPoint(evt.getPoint());
		        int col = table.columnAtPoint(evt.getPoint());
		        if (table.getRowCount() > 0) {
		        	if (col == 4) {
			        	model.removeRow(row);
			        	calculate(table, lblTotal);
			        }
		        }
		    }
		});
		
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==(TableModelEvent.UPDATE)) {
					int col = e.getColumn();
					int row = e.getFirstRow();
					float sum = (Float.parseFloat(table.getValueAt(row, 1).toString()) * Float.parseFloat(table.getValueAt(row, 2).toString()));
					calculate(table, lblTotal);
					if (col == 2) {
						table.setValueAt(new Convert().formatDecimal(sum), row, 3);
		            }
				}
			}
		});
		
		panel.setLayout(new java.awt.BorderLayout());
		panelTop.setLayout(new java.awt.BorderLayout());
		panelTop.setPreferredSize(new java.awt.Dimension((w - 400), 140));
		panel.add(panelTop, java.awt.BorderLayout.NORTH);
		
		lblTotal.setOpaque(true);
		lblTotal.setForeground(new Color(255, 255, 255));
		lblTotal.setBackground(new Color(0, 51, 102));
		lblTotal.setBorder(new TitledBorder(null, "Total", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(255, 255, 255)));
		lblTotal.setFont(new Font("Angsana New", Font.BOLD, 100));
		lblTotal.setPreferredSize(new java.awt.Dimension(380, 140));
		lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		panelTop.add(lblTotal, java.awt.BorderLayout.LINE_END);
		
		panelBottom.setLayout(null);
		panelBottom.setPreferredSize(new java.awt.Dimension(w, 60));
		panel.add(panelBottom, java.awt.BorderLayout.SOUTH);
		
		buttonOK.setBorder(new LineBorder(new Color(255, 255, 255), 2, true));
		buttonOK.setForeground(new Color(255, 255, 255));
		buttonOK.setFont(new Font("Tahoma", Font.BOLD, 25));
		buttonOK.setOpaque(true);
		buttonOK.setBackground(new Color(0, 102, 255));
		buttonOK.setBounds((width / 5), 0, (width / 6), 60);
		buttonOK.setPreferredSize(new java.awt.Dimension(100, 40));
		buttonOK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		panelBottom.add(buttonOK);
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int row = table.getRowCount();
					for (int i = 0; i < row; i++) {
						if (table.getValueAt(i, 2) == ""){
							final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/fail32.png"));
					        JOptionPane.showMessageDialog(null, "กรุณาใส่จำนวนเงินหรือลบแถวที่ไม่ต้องการออก", "Alert", JOptionPane.ERROR_MESSAGE, icon);
					        calculateModelList.clear();
					        return;
						} else {
							present.getLastKey();
							calculateModel = new CalculateModel(receiptNumber, new DateFormate().getDate(), new DateFormate().getTime(), 
									table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString(), 
									table.getValueAt(i, 2).toString(), table.getValueAt(i, 3).toString().replaceAll(",", ""), "buy");
							calculateModelList.add(calculateModel);
						}
					}
					present.insertReceipt(calculateModelList);
					calculateModelList.clear();
					buttonClear.doClick();
				} catch (Exception ex) {
					System.out.println("Error : " + ex.getMessage());
				}
			}
		});
		
		buttonClear.setBorder(new LineBorder(new Color(255, 255, 255), 2, true));
		buttonClear.setFont(new Font("Tahoma", Font.BOLD, 25));
		buttonClear.setForeground(new Color(255, 255, 255));
		buttonClear.setOpaque(true);
		buttonClear.setBackground(new Color(255, 51, 0));
		buttonClear.setBounds(0, 0, (width / 6), 60);
		buttonClear.setPreferredSize(new java.awt.Dimension(100, 40));
		buttonClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		panelBottom.add(buttonClear);
		buttonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblTotal.setText("0.00");
				for (int i = model.getRowCount() - 1; i >= 0; i--) {
				    model.removeRow(i);
				}
			}
		});
		
		panel.add(panelCenter, java.awt.BorderLayout.CENTER);
		
		int centerWidth = w;
		int centerHeight = h - 268;

		panelCenter.setLayout(new java.awt.BorderLayout());
		tableScroll.setPreferredSize(new java.awt.Dimension(centerWidth, centerHeight));
		tableScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tableScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelCenter.add(tableScroll);
		tableScroll.setBorder(BorderFactory.createTitledBorder("รายการ"));
		
		int centerPanelWidth = (int) tableScroll.getPreferredSize().getWidth();
		int tableWidth = (centerPanelWidth / 10);
		JTableHeader header = table.getTableHeader();
    	header.setPreferredSize(new Dimension(tableWidth, 60));
		table.getTableHeader().setFont(new Font("Angsana New", Font.BOLD, 50));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setFillsViewportHeight(true); 
		table.setRowSelectionAllowed(false);
		table.getPreferredScrollableViewportSize();
		table.getColumnModel().getColumn(0).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(1).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(2).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(3).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(4).setPreferredWidth(tableWidth);
		table.setRowHeight(45);
		table.getColumn("ลบ").setCellRenderer(new tableButtonRenderrer());
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFont(new Font("Angsana New", Font.PLAIN, 40));
		tableScroll.setViewportView(table);
		table.setCellSelectionEnabled(true);
		table.changeSelection(0, 2, false, false);
		table.requestFocus();
		
		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRender);
		
		DefaultTableCellRenderer rightRender = new DefaultTableCellRenderer();
		rightRender.setHorizontalAlignment(JLabel.RIGHT);
		table.getColumnModel().getColumn(2).setCellRenderer(rightRender);
		table.getColumnModel().getColumn(3).setCellRenderer(rightRender);

		return panel;
	}
	
	public void calculate(JTable table, JLabel lblTotal) {
		int rowCounth = table.getRowCount();
	    float sum = 0;
	    float value = 0;
	    for (int i = 0; i < rowCounth; i++) {
	    	value = Float.parseFloat(table.getValueAt(i, 3).toString().replaceAll(",", ""));
	    	sum += value;
	    }
	    lblTotal.setText(String.valueOf(new Convert().formatDecimal(sum)));
	}
	
	private JPanel panelSell() {
		calculateModelList.clear();
		javax.swing.JPanel panel = new javax.swing.JPanel();
		javax.swing.JPanel panelTop = new javax.swing.JPanel();
		javax.swing.JPanel panelCenter = new javax.swing.JPanel();
		javax.swing.JPanel panelBottom = new javax.swing.JPanel();
		javax.swing.JLabel lblTotal = new javax.swing.JLabel("0.00");
		javax.swing.JButton buttonClear = new javax.swing.JButton("CLEAR");
		javax.swing.JButton buttonOK = new javax.swing.JButton("OK");
		javax.swing.JScrollPane tableScroll = new javax.swing.JScrollPane();
		
		modelSell = new DefaultTableModel(data, columName);
		javax.swing.JTable table = new javax.swing.JTable(modelSell) {
			public boolean isCellEditable(int rowIndex, int colIndex) {
		    	if(colIndex == 0) {
		    		return false;
		    	} else if (colIndex == 1) {
		    		return false;
		    	} else if (colIndex == 3) {
		    		return false;
		    	} else {
		    		return true;
		    	}
		    }
		};
		
		table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		    	int row = table.rowAtPoint(evt.getPoint());
		        int col = table.columnAtPoint(evt.getPoint());
		        if (table.getRowCount() > 0) {
		        	if (col == 4) {
		        		modelSell.removeRow(row);
			        }
		        }
		    }
		});
		
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==(TableModelEvent.UPDATE)) {
					int col = e.getColumn();
					int row = e.getFirstRow();
					float rate = Float.parseFloat(table.getValueAt(row, 1).toString());
					float amount = Float.parseFloat(table.getValueAt(row, 2).toString());
					float sum = (rate * amount);
					
					calculate(table, lblTotal);
					if (col == 2) {
						table.setValueAt(new Convert().formatDecimal(sum), row, 3);
		            }
				}
			}
		});
		
		panel.setLayout(new java.awt.BorderLayout());
		
		panelTop.setLayout(new java.awt.BorderLayout());
		panelTop.setPreferredSize(new java.awt.Dimension(w, 140));
		panel.add(panelTop, java.awt.BorderLayout.NORTH);
		
		lblTotal.setOpaque(true);
		lblTotal.setForeground(new Color(255, 255, 255));
		lblTotal.setBackground(new Color(0, 51, 102));
		lblTotal.setBorder(new TitledBorder(null, "Total", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(255, 255, 255)));
		lblTotal.setFont(new Font("Angsana New", Font.BOLD, 100));
		lblTotal.setPreferredSize(new java.awt.Dimension(380, 140));
		lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		panelTop.add(lblTotal, java.awt.BorderLayout.LINE_END);
		
		panelBottom.setLayout(null);
		panelBottom.setPreferredSize(new java.awt.Dimension(w, 60));
		panel.add(panelBottom, java.awt.BorderLayout.SOUTH);
		
		buttonOK.setBorder(new LineBorder(new Color(255, 255, 255), 2, true));
		buttonOK.setForeground(new Color(255, 255, 255));
		buttonOK.setFont(new Font("Tahoma", Font.BOLD, 25));
		buttonOK.setOpaque(true);
		buttonOK.setBackground(new Color(0, 102, 255));
		buttonOK.setBounds((width / 5), 0, (width / 6), 60);
		buttonOK.setPreferredSize(new java.awt.Dimension(100, 40));
		buttonOK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		panelBottom.add(buttonOK);
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int row = table.getRowCount();
					for (int i = 0; i < row; i++) {
						if (table.getValueAt(i, 2) == ""){
							final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/fail32.png"));
					        JOptionPane.showMessageDialog(null, "กรุณาใส่จำนวนเงินหรือลบแถวที่ไม่ต้องการออก", "Alert", JOptionPane.ERROR_MESSAGE, icon);
					        calculateModelList.clear();
					        return;
						} else {
							calculateModel = new CalculateModel(receiptNumber, new DateFormate().getDate(), new DateFormate().getTime(), 
									table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString(), 
									table.getValueAt(i, 2).toString(), table.getValueAt(i, 3).toString(), "sell");
							
							calculateModelList.add(calculateModel);
						}
					}
					present.insertReceipt(calculateModelList);
					calculateModelList.clear();
					buttonClear.doClick();
				} catch (Exception ex) {
					System.out.println("Error : " + ex.getMessage());
				}
			}
		});
		
		buttonClear.setBorder(new LineBorder(new Color(255, 255, 255), 2, true));
		buttonClear.setFont(new Font("Tahoma", Font.BOLD, 25));
		buttonClear.setForeground(new Color(255, 255, 255));
		buttonClear.setOpaque(true);
		buttonClear.setBackground(new Color(255, 51, 0));
		buttonClear.setBounds(0, 0, (width / 6), 60);
		buttonClear.setPreferredSize(new java.awt.Dimension(100, 40));
		buttonClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		panelBottom.add(buttonClear);
		buttonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblTotal.setText("0.00");
				for (int i = modelSell.getRowCount() - 1; i >= 0; i--) {
				    modelSell.removeRow(i);
				}
			}
		});
		
		int centerWidth = w;
		int centerHeight = h - 268;
		
		panel.add(panelCenter, java.awt.BorderLayout.CENTER);
		panelCenter.setLayout(new java.awt.BorderLayout());
		tableScroll.setPreferredSize(new java.awt.Dimension(centerWidth, centerHeight));
		tableScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tableScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelCenter.add(tableScroll, java.awt.BorderLayout.CENTER);
		tableScroll.setBorder(BorderFactory.createTitledBorder("รายการ"));
		
		int centerPanelWidth = (int) tableScroll.getSize().getWidth();
		int tableWidth = (centerPanelWidth / 5) - 10;
		
		JTableHeader header = table.getTableHeader();
    	header.setPreferredSize(new Dimension(tableWidth, 60));
		table.getTableHeader().setFont(new Font("Angsana New", Font.BOLD, 50));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setFillsViewportHeight(true); 
		table.setRowSelectionAllowed(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(1).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(2).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(3).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(4).setPreferredWidth(tableWidth);
		table.setRowHeight(45);
		table.getColumn("ลบ").setCellRenderer(new tableButtonRenderrer());
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFont(new Font("Angsana New", Font.PLAIN, 40));
		tableScroll.setViewportView(table);
		table.setCellSelectionEnabled(true);
		table.changeSelection(0, 2, false, false);
		table.requestFocus();
		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRender);
		
		DefaultTableCellRenderer rightRender = new DefaultTableCellRenderer();
		rightRender.setHorizontalAlignment(JLabel.RIGHT);
		table.getColumnModel().getColumn(2).setCellRenderer(rightRender);
		table.getColumnModel().getColumn(3).setCellRenderer(rightRender);
		
		return panel;
	}
	
	private void menu() {
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		edit = new JMenu("\u0E41\u0E01\u0E49\u0E44\u0E02\u0E40\u0E23\u0E17");
		edit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				updateCurrencyActivity update = new updateCurrencyActivity(MainActivity.this);
				if(update.doModal() == updateCurrencyActivity.ID_OK) {
					for (int i = model.getRowCount() - 1; i >= 0; i--) {
					    model.removeRow(i);
					}
					for (int i = modelSell.getRowCount() - 1; i >= 0; i--) {
					    modelSell.removeRow(i);
					}
		        } else {
		        	buyRate();
		        }
				update.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				update.setModal(true);
			}
		});
		menu.add(edit);
		
		report = new JMenu("\u0E23\u0E32\u0E22\u0E07\u0E32\u0E19");
		report.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ReportActivity report = new ReportActivity(MainActivity.this);
				if(report.doModal() == ReportActivity.ID_OK) {}
				report.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				report.setModal(true);
			}
		});
		menu.add(report);
		
		JMenu settings = new JMenu("\u0E15\u0E31\u0E49\u0E07\u0E04\u0E48\u0E32");
		settings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				SettingsActivity settings = new SettingsActivity(MainActivity.this);
				if(settings.doModal() == SettingsActivity.ID_OK) {
					System.out.println("modal");
					
		        } else {
		        	File database = new File(Config.DB_PATH + Config.DB_FILE);
					if (database.exists() && prefs.getPreferrence("settings_open") != null) {
						buyRate();
						lblSource.setText(" jdbc:sqlite: " + new File(Config.DB_PATH + Config.DB_FILE).getAbsolutePath().toString());
		                lblSource.setIcon(new ImageIcon(getClass().getResource("/icon/database_connect.png")));
		                edit.setEnabled(true);
		                report.setEnabled(true);
		                prefs.setPreferrence("settings_open", "1");
		                
		                if (!sc.isShowing()) {
							sc.setLocation(moniter2);
			                sc.setVisible(true);
						}
					}
					
					lblCompanyName.setText((prefs.getPreferrence("compName") != null)? prefs.getPreferrence("compName") : "Company Name");
					lblCoID.setText((prefs.getPreferrence("compID") != null)? "เลขประจำตัวผู้เสียภาษี : " + prefs.getPreferrence("compID") : "Company License");
		        }
				settings.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				settings.setLocationRelativeTo(null);
				settings.setModal(true);
			}
		});
		menu.add(settings);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == list) {
			int[] selFromList = list.getSelectedIndices();
			for(int index : selFromList) {
				CurrencyModel cModel = currencyModelList.get(index);
				if (tabIndex == 0) {
					model.addRow(new Object[] {cModel.getCurrencyName(), 
							new Convert().formatDecimal(Float.parseFloat(cModel.getCurrencyBuyRate())), "", "0.00"});
				} else if (tabIndex == 1) {
					modelSell.addRow(new Object[] {cModel.getCurrencyName(), 
							new Convert().formatDecimal(Float.parseFloat(cModel.getCurrencySellRate())), "", "0.00"});
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void onFail(String fail) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/fail32.png"));
        JOptionPane.showMessageDialog(null, fail, "Alert", JOptionPane.ERROR_MESSAGE, icon);
	}

	@Override
	public void onSuccess(String success) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/success32.png"));
		JOptionPane.showMessageDialog(null, success, "Success", JOptionPane.ERROR_MESSAGE, icon);
	}

	public void buyRate() {
		currencyModelList.clear();
		DefaultListModel listModel = new DefaultListModel();
		try {
			ResultSet result = present.getAllCurrency("buy");
			while(result.next()) {
				listModel.addElement(result.getString("CR_NAME").trim().toString());
				currencyModel = new CurrencyModel();
				currencyModel.setCurrencyID(result.getInt("CR_ID"));
				currencyModel.setCurrencyName(result.getString("CR_NAME").trim().toString());
				currencyModel.setCurrencyBuyRate(result.getString("CR_BUY_RATE"));
				currencyModelList.add(currencyModel);
			}
			list.setModel(listModel);
			list.setCellRenderer(new ListRenderer(listModel));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void sellRate() {
		currencyModelList.clear();
		DefaultListModel listModel = new DefaultListModel();
		try {
			ResultSet result = present.getAllCurrency("sell");
			while(result.next()) {
				listModel.addElement(result.getString("CR_NAME").trim().toString());
				currencyModel = new CurrencyModel();
				currencyModel.setCurrencyID(result.getInt("CR_ID"));
				currencyModel.setCurrencyName(result.getString("CR_NAME").trim().toString());
				currencyModel.setCurrencySellRate(result.getString("CR_SELL_RATE"));
				currencyModelList.add(currencyModel);
			}
			list.setModel(listModel);
			list.setCellRenderer(new ListRenderer(listModel));
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}

	@Override
	public void onGenerateKey(ResultSet result) {
		try {
			if (result.next()) {
				receiptNumber = generateKey(result.getString("RC_NO"));
			} 
		} catch (Exception e) {
			receiptNumber = generateKey(null);
			System.out.println("No number!" + receiptNumber);
		}
		
	}
	
	private String generateKey(String lastkey) {
		String[] key;
		String prefixKey;
		int running;
		String generateNumber = null;
		
		if (lastkey != null) {
			key = lastkey.split("-");
			prefixKey = key[0];
			running = Integer.parseInt(key[1]);
			running++;
			generateNumber = prefixKey + "-" + String.format("%04d", running);
		} else {
			running = 0000;
			running++;
			generateNumber = prefixName + new DateFormate().getDateForBill() + "-" + String.format("%04d", running);
		}
		
		//System.out.println(String.format("%04d", running));
		
		return generateNumber;
	}
}
