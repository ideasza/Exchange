package dev.teerayut.update;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dev.teerayut.model.CurrencyModel;
import dev.teerayut.utils.Preferrence;
import dev.teerayut.utils.ScreenCenter;
import dev.teerayut.utils.tableImageRenderrer;

public class updateCurrencyActivity extends JDialog implements updateCurrencyInterface.viewInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static final int ID_OK = 1;
    public static final int ID_CANCEL = 0;
    private int exitCode = ID_CANCEL;
    
    private int panelW;
	
	private DefaultTableModel model;
	
	private Object[] objs;
	private Object[] columName = {"No.", "Flag", "Currency", "Buy", "Sell", "Show/Hide"};
	
    private Preferrence prefs;
    private updateCurrencyPresenter presentUpdate;
    
    public updateCurrencyActivity(Frame owner) {
        super(owner);
        createGUI();
    }
	
	/**
     * @wbp.parser.constructor
     */
    public updateCurrencyActivity(Dialog owner) {
        super(owner);        
        createGUI();
    }

    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JPanel bottomPanel;
    
    private javax.swing.JLabel lblCompanyName;
	private javax.swing.JLabel lblCoID;
	
	private javax.swing.JTable table;
	private javax.swing.JScrollPane scrollPane;
	
	private void initWidget() {
		topPanel = new javax.swing.JPanel();
		bottomPanel = new javax.swing.JPanel();
		centerPanel = new javax.swing.JPanel();
		
		lblCompanyName = new javax.swing.JLabel();
		lblCoID = new javax.swing.JLabel();
		
		scrollPane = new javax.swing.JScrollPane();
	}
	/**
	 * Create the frame.
	 */
	public void createGUI() {
		initWidget();
		prefs = new Preferrence();
		presentUpdate = new updateCurrencyPresenter(this);
		new ScreenCenter().centreWindow(this);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
    	int height = screenSize.height;
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int h = gd.getDisplayMode().getHeight();
		int w = gd.getDisplayMode().getHeight();
        setPreferredSize(new Dimension(width, height - 100));
        setTitle("Currency Exchange - Update");
        setIconImage(new ImageIcon(getClass().getResource("/icon/icon.png")).getImage());
        
        topPanel.setLayout(new java.awt.BorderLayout());
		getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);
		
		bottomPanel.setPreferredSize(new java.awt.Dimension(w, 20));
		bottomPanel.setLayout(new java.awt.BorderLayout());
		getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);
		
		lblCompanyName.setText((prefs.getPreferrence("compName") != null)? prefs.getPreferrence("compName") : "Company Name");
		lblCompanyName.setForeground(new Color(0, 51, 102));
		lblCompanyName.setFont(new Font("Angsana New", Font.BOLD, 44));
		lblCompanyName.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
		topPanel.add(lblCompanyName, java.awt.BorderLayout.LINE_START);
		
		lblCoID.setText((prefs.getPreferrence("compID") != null)? "เลขประจำตัวผู้เสียภาษี : " + prefs.getPreferrence("compID") : "Company License");
		lblCoID.setForeground(new Color(0, 51, 102));
		lblCoID.setFont(new Font("Angsana New", Font.PLAIN, 20));
		lblCoID.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
		topPanel.add(lblCoID, java.awt.BorderLayout.AFTER_LAST_LINE);
		
		centerPanel.setLayout(new java.awt.BorderLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("Update price"));
		getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);
		
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerifyInputWhenFocusTarget(false);
		scrollPane.setRequestFocusEnabled(false);
		centerPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
		
		table =  new javax.swing.JTable() {
		    public boolean isCellEditable(int rowIndex, int colIndex) {
		    	if(colIndex == 0) {
		    		return false; // Disallow Column 0
		    	} else if (colIndex == 1) {
		    		return false;   // Allow the editing 
		    	} else if (colIndex == 2) {
		    		return true;   // Allow the editing 
		    	} else {
		    		return true;
		    	}
		    }
		    
		    @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Integer.class;
                    case 1:
                        return ImageIcon.class;
                    case 2:
                        return String.class;
                    case 3:
                        return Float.class;
                    case 4:
                        return Float.class;
                    default:
                        return Boolean.class;
                }
            }
		};
		table.setFocusable(false);
		table.setForeground(Color.BLACK);
		table.setFillsViewportHeight(true); 
		table.setRowSelectionAllowed(false);
		table.setEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		presentUpdate.LoadCurrency();
		
		scrollPane.setViewportView(table);
		pack();
        setLocationRelativeTo(getParent());
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

	@Override
	public void showCurrency(List<CurrencyModel> modelList) {
		model = new DefaultTableModel(new Object[0][0], columName);
		for (CurrencyModel m : modelList) {
			objs = new Object[6];
			objs[0] = m.getCurrencyID();
			objs[1] = "/flag/" + m.getCurrencyName().trim().toLowerCase().substring(0, 3) + ".png";
			objs[2] = m.getCurrencyName().trim();
			objs[3] = m.getCurrencyBuyRate().trim();
			objs[4] = m.getCurrencySellRate().trim();
			objs[5] = (m.getCurrencyStatus().trim().equals("Y"))? true : false;
			model.addRow(objs);
		}
		table.setModel(model);
		table.setFont(new Font("Angsana New", Font.BOLD, 44));
		JTableHeader header = table.getTableHeader();
		header.setPreferredSize(new Dimension(50, 50));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		table.getTableHeader().setFont(new Font("Angsana New", Font.BOLD, 46));
		table.getTableHeader().setForeground(Color.BLACK);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int centerPanelWidth = (int) screenSize.getWidth();
		int tableWidth = (centerPanelWidth / 5);
		table.getColumnModel().getColumn(0).setPreferredWidth(tableWidth / 4);
		table.getColumnModel().getColumn(1).setPreferredWidth(tableWidth / 2);
		table.getColumnModel().getColumn(2).setPreferredWidth(tableWidth + 50);
		table.getColumnModel().getColumn(3).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(4).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(5).setPreferredWidth(tableWidth);
		table.setRowHeight(50);
		
		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRender);
		
		DefaultTableCellRenderer renderrerG = new DefaultTableCellRenderer();
		renderrerG.setHorizontalAlignment(JLabel.CENTER);
		renderrerG.setForeground(Color.GREEN);
		table.getColumnModel().getColumn(3).setCellRenderer(renderrerG);
		
		DefaultTableCellRenderer renderrerR = new DefaultTableCellRenderer();
		renderrerR.setHorizontalAlignment(JLabel.CENTER);
		renderrerR.setForeground(Color.RED);
		table.getColumnModel().getColumn(4).setCellRenderer(renderrerR);
		
		table.getColumnModel().getColumn(1).setCellRenderer(new tableImageRenderrer());
		
		DefaultTableCellRenderer leftRender = new DefaultTableCellRenderer();
		leftRender.setHorizontalAlignment(JLabel.LEFT);
		table.getColumnModel().getColumn(2).setCellRenderer(leftRender);
		
		table.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==(TableModelEvent.UPDATE)) {
					int col = e.getColumn();
					int row = e.getFirstRow();
					presentUpdate.updateCurrency(Integer.parseInt(table.getValueAt(row, 0).toString()), table.getValueAt(row, 2).toString(),
							table.getValueAt(row, 3).toString(), table.getValueAt(row, 4).toString(), (boolean) table.getValueAt(row, 5));
				}
			}
		});
	}

	@Override
	public void onFail(String fail) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/fail32.png"));
        JOptionPane.showMessageDialog(null, fail, "Alert", JOptionPane.ERROR_MESSAGE, icon);
	}
}
