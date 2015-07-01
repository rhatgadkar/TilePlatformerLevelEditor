import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

class Display extends JPanel implements MouseListener {
		
	private static final long serialVersionUID = 1L;
	
	public final static int WIDTH = 640;
	public final static int HEIGHT = 480;
	
	private Board m_board;
	private Palette m_palette;
	
	Display(Palette palette) {
		addMouseListener(this);
		m_board = new Board();
		m_palette = palette;
		repaint();
	}
	
	private void doClickSquare(int row, int col) {
		boolean fillTile = m_board.fillTile(row, col);
		if (fillTile)
			repaint();
	}
	
	private void doRightClickSquare(int row, int col) {
		boolean emptyTile = m_board.emptyTile(row, col);
		if (emptyTile)
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
		
		for (int r = 0; r < Board.ROWS; r++) {
			for (int c = 0; c < Board.COLS; c++) {
				if (m_board.isFilled(r, c)) {
					Palette.FillColor color = m_board.getTileColor(r, c);
					switch (color) {
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
					g.fillRect(c * Board.Tile.WIDTH, r * Board.Tile.HEIGHT, 
							   Board.Tile.WIDTH, Board.Tile.HEIGHT);
				}
			}
		}
	}
	
	public void mouseClicked(MouseEvent e) { }		
	public void mouseReleased(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	
	class Board {
		
		public final static int ROWS = Display.HEIGHT / Board.Tile.HEIGHT;
		public final static int COLS = Display.WIDTH / Board.Tile.WIDTH;
		
		private Tile[][] m_tiles;
		
		private Board() {
			m_tiles = new Tile[Board.ROWS][Board.COLS];
			
			for (int r = 0; r < Board.ROWS; r++) {
				for (int c = 0; c < Board.COLS; c++)
					m_tiles[r][c] = new Tile(r, c);
			}
		}
		
		private boolean fillTile(int r, int c) {
			if (r >= Board.ROWS || r < 0)
				return false;
			if (c >= Board.COLS || c < 0)
				return false;
			
			if (m_tiles[r][c].m_fill) // only fill if not already filled
				return false;
			
			m_tiles[r][c].m_fill = true; 
			m_tiles[r][c].m_color = m_palette.getSelectedColor(); // color = whatever is selected on palette
			
			return true;
		}
		
		private Palette.FillColor getTileColor(int r, int c) {
			if (r >= Board.ROWS || r < 0)
				return null;
			if (c >= Board.COLS || c < 0)
				return null;
			
			return m_tiles[r][c].getColor();
		}
		
		private boolean emptyTile(int r, int c) {
			if (r >= Board.ROWS || r < 0)
				return false;
			if (c >= Board.COLS || c < 0)
				return false;
			
			if (!m_tiles[r][c].m_fill) // only empty if not already empty
				return false;
			
			m_tiles[r][c].m_fill = false; 
			
			return true;
		}
		
		private boolean isFilled(int r, int c) {
			if (r >= Board.ROWS || r < 0)
				return false;
			if (c >= Board.COLS || c < 0)
				return false;
			
			if (m_tiles[r][c].m_fill)
				return true;
			
			return false;
		}
		
		final class Tile {
			
			private boolean m_fill;
			
			public final static int WIDTH = 32;
			public final static int HEIGHT = 24;
			
			private Palette.FillColor m_color;
			
			public Palette.FillColor getColor() {
				return m_color;
			}
			
			private Tile(int row, int col) {
				m_fill = false;
				m_color = Palette.FillColor.GREEN; // default color
			}
		}
	}
}