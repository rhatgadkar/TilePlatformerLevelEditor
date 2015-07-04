import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class HeightBox implements ActionListener {
	
	private JComboBox<Integer> m_heightChoice;
	
	private int m_selectedHeight;
	
	public int getSelectedHeight() {
		return m_selectedHeight;
	}
	
	public JComboBox<Integer> getComboBox() {
		return m_heightChoice;
	}
	
	HeightBox() {
		m_heightChoice = new JComboBox<Integer>();
		for (int r = 1; r <= Display.Board.ROWS; r++)
			m_heightChoice.addItem(r);
		
		m_heightChoice.setSelectedItem(1);
		m_selectedHeight = 1;
		
		m_heightChoice.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unchecked")
		JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
		int height = (int)cb.getSelectedItem();
		m_selectedHeight = height;
	}

}
