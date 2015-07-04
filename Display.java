import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class Display extends JPanel implements MouseListener {
		
	private static final long serialVersionUID = 1L;
	
	public final static int WIDTH = 640;
	public final static int HEIGHT = 480;
	
	private Board m_board;
	private Palette m_palette;
	private WidthBox m_widthBox;
	private HeightBox m_heightBox;
	
	Display(Palette palette, WidthBox widthBox, HeightBox heightBox) {
		addMouseListener(this);
		m_board = new Board();
		m_palette = palette;
		m_widthBox = widthBox;
		m_heightBox = heightBox;
		repaint();
	}
	
	public void exportMap() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		File f;
		do {
			int saveState = fc.showSaveDialog(this);
			if (saveState == JFileChooser.ERROR_OPTION) {
				JOptionPane.showMessageDialog(this, "There was an error in exporting map.");
				return;
			}
			else if (saveState == JFileChooser.CANCEL_OPTION)
				return;
			else
				f = fc.getSelectedFile();
			
			if (f.exists() && !f.isDirectory())
				JOptionPane.showMessageDialog(this, "File already exists. Choose a different name.");
		} while (f.exists() && !f.isDirectory());
		
//		File f = fc.getSelectedFile();
//		while (f.exists() && !f.isDirectory()) {
//			
//		}
		
		PrintWriter outputStream;
		try {
			outputStream = new PrintWriter(f);
			StringBuilder output = new StringBuilder();
			boolean gotFirstTile = false;
			
			for (int row = 0; row < Board.ROWS; row++) {
				for (Board.Tile tile : m_board.getTilesAtRow(row)) {
					Integer r = row;
					Integer c = tile.m_col;
					Integer w = tile.m_width;
					Integer h = tile.m_height;
					Character v;
					switch (tile.m_color) {
					case GREEN:
						v = 'p';
						break;
					case RED:
						v = 'w';
						break;
					default:
						v = 's';
					}
					
					if (!gotFirstTile)
						gotFirstTile = true;
					else
						output.append('\n');
					
					output.append(v.toString());
					output.append(",");
					output.append(r.toString());
					output.append(",");
					output.append(c.toString());
					output.append(",");
					output.append(w.toString());
					output.append(",");
					output.append(h.toString());
					output.append(",");
				}
			}
			
			boolean firstLine = true;
			Scanner s = new Scanner(output.toString());
			while (s.hasNextLine()) {
				if (firstLine)
					firstLine = false;
				else
					outputStream.write('\n');
				String in = s.nextLine();
				outputStream.write(in);
			}
			
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "File not found exception");
			return;
		}
		
		outputStream.close();
	}
	
	private void doClickSquare(int row, int col) {
		Palette.FillColor color = m_palette.getSelectedColor();
		int width = m_widthBox.getSelectedWidth();
		int height = m_heightBox.getSelectedHeight();
		
		boolean addTile = m_board.addTile(row, col, color, width, height);
		if (addTile)
			repaint();
		else
			JOptionPane.showMessageDialog(this, "Cannot add tile here.");
	}
	
	private void doRightClickSquare(int row, int col) {
		boolean deleteTile = m_board.deleteTile(row, col);
		if (deleteTile)
			repaint();
	}
	
	public void mousePressed(MouseEvent e) {
		int row = e.getY() / Board.Tile.HEIGHT;
		int col = e.getX() / Board.Tile.WIDTH;
		
		if (col >= 0 && col < Board.COLS && row >= 0 && row < Board.ROWS) {
			if (e.isMetaDown())
				doRightClickSquare(row, col);
			else
				doClickSquare(row, col);
		}
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Display.WIDTH, Display.HEIGHT);
		
		for (int tileRow = 0; tileRow < Board.ROWS; tileRow++) {
			for (Board.Tile tile : m_board.getTilesAtRow(tileRow)) {
				switch (tile.m_color) {
				case GREEN:
					g.setColor(Color.GREEN);
					break;
				case RED:
					g.setColor(Color.RED);
					break;
				case YELLOW:
					g.setColor(Color.YELLOW);
					break;
				}
				
				int tileHeight = tile.m_height;
				int tileWidth = tile.m_width;
				int tileCol = tile.m_col;
					
				int tileX = tileCol * Board.Tile.WIDTH;
				int tileY = tileRow * Board.Tile.HEIGHT;
				g.fillRect(tileX, tileY, Board.Tile.WIDTH * tileWidth, Board.Tile.HEIGHT * tileHeight);
				
				// draw row, col label string
				g.setColor(Color.BLACK);
				Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 8);
				g.setFont(font);
				g.drawString(tileRow + "," + tileCol, tileX + 1, tileY + (Board.Tile.HEIGHT / 2));
			}
		}
		
		// draw grid lines
		g.setColor(Color.WHITE);
		for (int x = Board.Tile.WIDTH; x < Display.WIDTH; x += Board.Tile.WIDTH)
			g.drawLine(x, 0, x, Display.HEIGHT);
		for (int y = Board.Tile.HEIGHT; y < Display.HEIGHT; y += Board.Tile.HEIGHT)
			g.drawLine(0, y, Display.WIDTH, y);
	}
	
	public void mouseClicked(MouseEvent e) { }		
	public void mouseReleased(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	
	class Board {
		
		public final static int ROWS = Display.HEIGHT / Board.Tile.HEIGHT;
		public final static int COLS = Display.WIDTH / Board.Tile.WIDTH;
		
		private ArrayList< LinkedList<Tile> > m_tiles;
		private char[][] m_tileMap;
		
		private Board() {
			m_tiles = new ArrayList< LinkedList<Tile> >(ROWS);
			for (int r = 0; r < ROWS; r++)
				m_tiles.add(new LinkedList<Tile>());
			
			m_tileMap = new char[ROWS][COLS];
			for (int r = 0; r < ROWS; r++) {
				for (int c = 0; c < COLS; c++)
					m_tileMap[r][c] = '0';
			}
		}
		
		private LinkedList<Tile> getTilesAtRow(int r) {
			return m_tiles.get(r);
		}
		
		private Tile findTile(int tileRow, int tileCol) {
			for (Tile tile : m_tiles.get(tileRow)) {
				if (tile.m_col == tileCol)
					return tile;
			}
			
			return null;
		}
		
		private boolean addTile(int tileRow, int tileCol, Palette.FillColor color, int width, int height) {
			// check if possible to insert tile
			if (tileRow + height > ROWS || tileCol + width > COLS)
				return false;
			
			// check if another tile exists in region
			for (int r = tileRow; r < tileRow + height; r++) {
				for (int c = tileCol; c < tileCol + width; c++) {
					if (m_tileMap[r][c] != '0')
						return false;
				}
			}
			
			boolean added = m_tiles.get(tileRow).add(new Board.Tile(tileCol, color, width, height));
			if (added) {
				for (int r = tileRow; r < tileRow + height; r++) {
					for (int c = tileCol; c < tileCol + width; c++)
						m_tileMap[r][c] = '1';
				}
			}
			
			return added;
		}
		
		private boolean deleteTile(int tileRow, int tileCol) {
			Tile tile = findTile(tileRow, tileCol);
			if (tile == null)
				return false;
			
			int width = tile.m_width;
			int height = tile.m_height;
			
			boolean deleted = m_tiles.get(tileRow).remove(tile);
			if (deleted) {
				for (int r = tileRow; r < tileRow + height; r++) {
					for (int c = tileCol; c < tileCol + width; c++)
						m_tileMap[r][c] = '0';
				}
			}
			
			return deleted;
		}
		
		final class Tile {
			
			public final static int WIDTH = 32;
			public final static int HEIGHT = 24;
			
			private Palette.FillColor m_color;
			private int m_width;
			private int m_height;
			private int m_col;
			
			private Tile(int col, Palette.FillColor color, int width, int height) {
				m_col = col;
				m_color = color;
				m_width = width;
				m_height = height;
			}
		}
	}
}