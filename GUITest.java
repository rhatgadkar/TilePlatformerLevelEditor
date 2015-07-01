import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

// make a 640x480 grid with empty squares. Fill colors in square when clicked on.
// TILE_WIDTH = 32  TILE_HEIGHT = 24
// for 640x480: num_rows = 20  num_cols = 20
// mouse click a grid fill a color in it, right click deletes the color
// color palette on right side to choose color

public class GUITest extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public final static int HEIGHT = 600;
	public final static int WIDTH = 800;
	
	private Palette m_palette;
	private Display m_display;
	
	public GUITest() {
		setLayout(new BorderLayout());
		
		m_palette = new Palette();
		m_display = new Display(m_palette);
		
		m_display.setBounds(0, 0, Display.WIDTH, Display.HEIGHT);
		m_palette.getComboBox().setBounds(660, 100, 100, 100);
		
		add(m_palette.getComboBox());
		add(m_display);
	}

	public static void main(String[] args) {
		JFrame window = new JFrame("GUI Test");
		GUITest gui = new GUITest();
		window.setContentPane(gui);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setSize(WIDTH, HEIGHT);
		window.setLocation((screenSize.width - WIDTH) / 2, 
				           (screenSize.height - HEIGHT) / 2);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setVisible(true);
	}
}
