import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

class WidthBox implements ActionListener {

	private JComboBox<Integer> m_widthChoice;
	
	private int m_selectedWidth;
	
	public int getSelectedWidth() {
		return m_selectedWidth;
	}
	
	public JComboBox<Integer> getComboBox() {
		return m_widthChoice;
	}
	
	WidthBox() {
		m_widthChoice = new JComboBox<Integer>();
		for (int c = 1; c <= Display.Board.COLS; c++)
			m_widthChoice.addItem(c);
		
		m_widthChoice.setSelectedItem(1);
		m_selectedWidth = 1;
		
		m_widthChoice.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unchecked")
		JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
		int width = (int)cb.getSelectedItem();
		m_selectedWidth = width;
	}

}
