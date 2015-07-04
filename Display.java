import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;

class Display extends JPanel implements MouseListener {
		
	private static final long serialVersionUID = 1L;
	
	public final static int WIDTH = 640;
	public final static int HEIGHT = 480;
	
	private Board m_board;
	private Palette m_palette;
	private WidthBox m_widthBox;
	
	Display(Palette palette, WidthBox widthBox) {
		addMouseListener(this);
		m_board = new Board();
		m_palette = palette;
		m_widthBox = widthBox;
		repaint();
	}
	
	private void doClickSquare(int row, int col) {
		Palette.FillColor color = m_palette.getSelectedColor();
		int width = m_widthBox.getSelectedWidth();
		
		boolean addTile = m_board.addTile(row, col, color, width);
		if (addTile)
			repaint();
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
				
				//int tileHeight = tile.m_height;
				int tileWidth = tile.m_width;
				int tileCol = tile.m_col;
					
				int tileX = tileCol * Board.Tile.WIDTH;
				int tileY = tileRow * Board.Tile.HEIGHT;
				g.fillRect(tileX, tileY, Board.Tile.WIDTH * tileWidth, Board.Tile.HEIGHT /** tileHeight*/);
				
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
		
		private Board() {
			m_tiles = new ArrayList< LinkedList<Tile> >(ROWS);
			for (int r = 0; r < ROWS; r++)
				m_tiles.add(new LinkedList<Tile>());
		}
		
		private LinkedList<Tile> getTilesAtRow(int r) {
			return m_tiles.get(r);
		}
		
		private Tile findTile(int r, int c) {
			for (Tile tile : m_tiles.get(r)) {
				if (tile.m_col == c)
					return tile;
			}
			
			return null;
		}
		
		private boolean addTile(int r, int c, Palette.FillColor color, int width) {
			// check if another tile exists in region
			Tile tile = findTile(r, c);
			if (tile != null)
				return false;
			
			m_tiles.get(r).add(new Board.Tile(c, color, width));
			return true;
		}
		
		private boolean deleteTile(int r, int c) {
			Tile tile = findTile(r, c);
			if (tile != null)
				return false;
			
			return m_tiles.get(r).remove(tile);
		}
		
		final class Tile {
			
			public final static int WIDTH = 32;
			public final static int HEIGHT = 24;
			
			private Palette.FillColor m_color;
			private int m_width;
			private int m_col;
			
			private Tile(int col, Palette.FillColor color, int width) {
				m_col = col;
				m_color = color;
				m_width = width;
			}
		}
	}
}