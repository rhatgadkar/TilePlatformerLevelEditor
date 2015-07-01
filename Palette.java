import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

// the palette is a comboBox

class Palette implements ActionListener {

	enum FillColor {
		GREEN, RED, YELLOW
	}
	
	private JComboBox<String> m_colorChoice;
	
	private Palette.FillColor m_selectedColor; 
	
	public Palette.FillColor getSelectedColor() {
		return m_selectedColor;
	}
	
	public JComboBox<String> getComboBox() {
		return m_colorChoice;
	}
	
	Palette() {
		m_colorChoice = new JComboBox<String>();
		m_colorChoice.addItem(FillColor.GREEN.toString());
		m_colorChoice.addItem(FillColor.RED.toString());
		m_colorChoice.addItem(FillColor.YELLOW.toString());
		
		m_colorChoice.setSelectedItem(FillColor.GREEN.toString());
		m_selectedColor = FillColor.GREEN;
		
		m_colorChoice.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unchecked")
		JComboBox<String> cb = (JComboBox<String>)e.getSource();
		String color = (String)cb.getSelectedItem();
		if (color == "GREEN")
			m_selectedColor = FillColor.GREEN;
		else if (color == "RED")
			m_selectedColor = FillColor.RED;
		else
			m_selectedColor = FillColor.YELLOW;
	}
}
