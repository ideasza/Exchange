package dev.teerayut.show;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

import dev.teerayut.model.CurrencyModel;
import dev.teerayut.utils.Convert;
import dev.teerayut.utils.Preferrence;
import dev.teerayut.utils.tableImageRenderrer;

public class showCurrencyActivity extends JFrame implements showCurrencyInterface.viewInterface, ActionListener{

	private static final int pageSize = 10;
	private int numOfpage = 0;
	private int page = 0;
	private int startIndex;
	
	private Preferrence prefs;
	
	private int panelW, panelH;
	
	private showCurrencyInterface.presentInterface present;
	
	private DefaultTableModel model;
	
	private Object[] objs;
	private Object[] columName = {"No.", "Flag", "Currency", "Buy", "Sell"};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					showCurrencyActivity frame = new showCurrencyActivity();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private javax.swing.JPanel Toppanel;
	private javax.swing.JPanel Leftpanel;
	private javax.swing.JPanel Rightpanel;
	private javax.swing.JPanel Bottompanel;
	private javax.swing.JLabel lblCompanyName;
	private javax.swing.JLabel lblCoID;
	private javax.swing.JLabel lbl;
	
	private javax.swing.JTable table;
	private javax.swing.JScrollPane scrollPane;
	
	private javax.swing.JLabel lblThank;

	/**
	 * Create the frame.
	 */
	public showCurrencyActivity() {
		prefs = new Preferrence();
		present = new showCurrencyPresenter(this);
		getContentPane().setBackground(new Color(51, 102, 255));
		Toppanel = new javax.swing.JPanel();
		Leftpanel = new javax.swing.JPanel();
		Rightpanel = new javax.swing.JPanel();
		Bottompanel = new javax.swing.JPanel();
		lblCompanyName = new javax.swing.JLabel();
		lblCoID = new javax.swing.JLabel();
		lbl = new javax.swing.JLabel();
		scrollPane = new javax.swing.JScrollPane();
		table = new javax.swing.JTable(model);
		lblThank = new javax.swing.JLabel("THANK YOU.");
		
		setTitle("Currency Exchange");
		setResizable(false);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) dimension.getWidth();
		int height = (int) dimension.getHeight();
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		setPreferredSize(new java.awt.Dimension(width, height));
		setBounds(0, 0, width, height);
		setIconImage(new ImageIcon(getClass().getResource("/icon/icon.png")).getImage());
		
		setBackground(new Color(102, 153, 255));
		getContentPane().setLayout(new java.awt.BorderLayout());
		Toppanel.setBackground(new Color(51, 102, 255));
		Toppanel.setLayout(new java.awt.BorderLayout());
		getContentPane().add(Toppanel, java.awt.BorderLayout.NORTH);
		
		Leftpanel.setBackground(new Color(51, 102, 255));
		Leftpanel.setPreferredSize(new java.awt.Dimension(30, height));
		getContentPane().add(Leftpanel, java.awt.BorderLayout.WEST);
		
		Rightpanel.setBackground(new Color(51, 102, 255));
		Rightpanel.setPreferredSize(new java.awt.Dimension(30, height));
		getContentPane().add(Rightpanel, java.awt.BorderLayout.EAST);
		
		Bottompanel.setBackground(new Color(51, 102, 255));
		Bottompanel.setPreferredSize(new java.awt.Dimension(width, 50));
		getContentPane().add(Bottompanel, java.awt.BorderLayout.SOUTH);
		
		lblThank.setForeground(new Color(255, 255, 255));
		lblThank.setFont(new Font("Serif", Font.BOLD, 40));
		lblThank.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		Bottompanel.add(lblThank);
		
		lblCompanyName.setForeground(new Color(255, 255, 255));
		lblCompanyName.setFont(new Font("Angsana New", Font.BOLD, 70));
		lblCompanyName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		Toppanel.add(lblCompanyName, java.awt.BorderLayout.NORTH);
		Border margin = new EmptyBorder(40, 0, 0, 0);
		lblCompanyName.setBorder(new CompoundBorder(null, margin));
		
		lblCoID.setForeground(new Color(255, 255, 255));
		lblCoID.setFont(new Font("Angsana New", Font.PLAIN, 40));
		lblCoID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		Toppanel.add(lblCoID);
		
		lbl.setPreferredSize(new java.awt.Dimension(width, 20));
		Toppanel.add(lbl, java.awt.BorderLayout.AFTER_LAST_LINE);
		
		scrollPane.setBackground(new Color(51, 102, 255));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerifyInputWhenFocusTarget(false);
		scrollPane.setRequestFocusEnabled(false);
		getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);
		
		panelW = width;
		panelH = height;
		
		table.setFocusable(false);
		table.setForeground(Color.WHITE);
		table.setBackground(Color.BLACK);
		table.setFillsViewportHeight(true); 
		table.setRowSelectionAllowed(false);
		table.setEnabled(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
            	getCurrency();
            	lblCompanyName.setText((prefs.getPreferrence("compName") != null)? prefs.getPreferrence("compName") : "Company Name");
            	lblCoID.setText((prefs.getPreferrence("compID") != null)? "เลขประจำตัวผู้เสียภาษี : " + prefs.getPreferrence("compID") : "Company License");
            }
        }, 0, 8000);
		
		scrollPane.setViewportView(table);
	}

	@Override
	public void showCurrency(List<CurrencyModel> modelList) {
		model = new DefaultTableModel(new Object[0][0], columName);
		for (int i = 0; i < modelList.size(); i++) {
			CurrencyModel m = modelList.get(i);
			model.addRow(new Object[] {startIndex + (i + 1), "/flag/" + m.getCurrencyName().trim().toLowerCase().substring(0, 3) + ".png"
					, m.getCurrencyName().trim(), 
					Float.parseFloat(m.getCurrencyBuyRate().trim()), 
					Float.parseFloat(m.getCurrencySellRate().trim())
			});
		}
		table.setModel(model);
    	table.setFont(new Font("Angsana New", Font.BOLD, 90));
    	
    	int screenHeight = (int) this.getSize().getHeight();
		int topHeight = (int) Toppanel.getSize().getHeight();
		int bottomHeight = (int) Bottompanel.getSize().getHeight();
		int rowHeight = (screenHeight - (topHeight + bottomHeight));
		int scrollWidth = (int) this.getSize().getWidth();
		int colWidth = (scrollWidth - 80);
		int columnsWidth = panelW;
		columnsWidth = (colWidth / 5);
		int smCol = (columnsWidth / 4);
		int mdCol = (columnsWidth / 2);
    	
    	JTableHeader header = table.getTableHeader();
    	header.setPreferredSize(new Dimension(95, (rowHeight / 13)));
    	
		table.getTableHeader().setFont(new Font("Angsana New", Font.BOLD, 95));
		table.getTableHeader().setBackground(Color.WHITE);
		table.getTableHeader().setForeground(new Color(0, 51, 102));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(mdCol);
		table.getColumnModel().getColumn(1).setPreferredWidth(mdCol);
		table.getColumnModel().getColumn(2).setPreferredWidth(columnsWidth + mdCol);
		table.getColumnModel().getColumn(3).setPreferredWidth(columnsWidth + smCol);
		table.getColumnModel().getColumn(4).setPreferredWidth(columnsWidth + smCol);
		table.setRowHeight(rowHeight / 11);
		
		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRender);
		
		DefaultTableCellRenderer renderrerG = new DefaultTableCellRenderer();
		renderrerG.setHorizontalAlignment(JLabel.CENTER);
		renderrerG.setForeground(Color.GREEN);
		renderrerG.setBackground(Color.BLACK);
		table.getColumnModel().getColumn(3).setCellRenderer(renderrerG);
		
		DefaultTableCellRenderer renderrerR = new DefaultTableCellRenderer();
		renderrerR.setHorizontalAlignment(JLabel.CENTER);
		renderrerR.setForeground(Color.RED);
		renderrerR.setBackground(Color.BLACK);
		table.getColumnModel().getColumn(4).setCellRenderer(renderrerR);
		
		table.getColumnModel().getColumn(1).setCellRenderer(new tableImageRenderrer());
		
		DefaultTableCellRenderer leftRender = new DefaultTableCellRenderer();
		leftRender.setHorizontalAlignment(JLabel.LEFT);
		table.getColumnModel().getColumn(2).setCellRenderer(leftRender);
		
		if (prefs.getPreferrence("compName") != null) {
			lblCompanyName.setText(prefs.getPreferrence("compName"));
		} else if (prefs.getPreferrence("compID") != null) {
			lblCoID.setText("เลขประจำตัวผู้เสียภาษี : " + prefs.getPreferrence("compID"));
		}
	}
	
	private void getCurrency() {
		if (page == 0) {
			startIndex = 0;
			page++;
		} else if (page == 1) {
			startIndex = startIndex + pageSize;
			page++;
		} else if (page == 2) {
			startIndex = startIndex + pageSize;
			page = 0;
		}
		present.LoadCurrencyOffset(pageSize, startIndex);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub	
	}
}
