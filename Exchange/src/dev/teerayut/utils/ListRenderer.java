package dev.teerayut.utils;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;


public class ListRenderer extends DefaultListCellRenderer {

	Font font = new Font("Angsana New", Font.BOLD, 34);
	private Map<String, ImageIcon> imageMap = null;
	private DefaultListModel defaultListModel;
	
	public ListRenderer(DefaultListModel defaultListModel) {
		this.defaultListModel = defaultListModel;
	}

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    	//imageMap = createImageMapModel(defaultListModel);
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        /*Image img = imageMap.get((String) value.toString().toLowerCase()).getImage(); 
		Image newimg = img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);*/
        
        //label.setIcon(new ImageIcon(newimg));
        label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        label.setIconTextGap(40);
        label.setFont(font);
        return label;
    }
    
    /*private Map<String, ImageIcon> createImageMapModel(DefaultListModel defaultListModel) {
    	Map<String, ImageIcon> map = new HashMap<>();
    	for (int i = 0; i < defaultListModel.size(); i++) {
    		map.put(defaultListModel.getElementAt(i), 
    				new ImageIcon(Config.ICON_PATH + currencyModel.get(i).getCurrencyDesc().toString().trim().toLowerCase() + ".png"));
    	}
    	return map;
    }*/
}
