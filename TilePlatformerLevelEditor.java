import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

// make a 640x480 grid with empty squares. Fill colors in square when clicked on.
// TILE_WIDTH = 32  TILE_HEIGHT = 24
// for 640x480: num_rows = 20  num_cols = 20
// mouse click a grid fill a color in it, right click deletes the color
// color palette on right side to choose color
// gridlines on display

public class TilePlatformerLevelEditor extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public final static int HEIGHT = 600;
	public final static int WIDTH = 800;
	
	public TilePlatformerLevelEditor() {
		setLayout(null);
		
		Palette palette = new Palette();
		WidthBox widthBox = new WidthBox();
		HeightBox heightBox = new HeightBox();
		Display display = new Display(palette, widthBox, heightBox);
		
		display.setPreferredSize(new Dimension(Display.WIDTH, Display.HEIGHT));
		JScrollPane displayScroller = new JScrollPane(display);
		display.setAutoscrolls(true);
		displayScroller.setPreferredSize(new Dimension(640, 500));
		displayScroller.getHorizontalScrollBar().setUnitIncrement(Display.Board.Tile.WIDTH);
		
		JLabel widthLabel = new JLabel("Width:");
		JLabel heightLabel = new JLabel("Height:");
		
		JButton exportButton = new JButton("Export");
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				display.exportMap();
			}
		});
		
		displayScroller.setBounds(0, 0, 640, 500);
		palette.getComboBox().setBounds(660, 100, 100, 50);
		widthBox.getComboBox().setBounds(660, 200, 50, 50);
		widthLabel.setBounds(660, 170, 50, 30);
		heightBox.getComboBox().setBounds(660, 300, 50, 50);
		heightLabel.setBounds(660, 270, 60, 30);
		exportButton.setBounds(660, 400, 100, 50);
		
		add(displayScroller);
		add(exportButton);
		add(heightLabel);
		add(heightBox.getComboBox());
		add(widthLabel);
		add(widthBox.getComboBox());
		add(palette.getComboBox());
	}

	public static void main(String[] args) {
		JFrame window = new JFrame("GUI Test");
		TilePlatformerLevelEditor gui = new TilePlatformerLevelEditor();
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
