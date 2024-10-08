import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
// import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
	// screen size, game size, snake speed, snake size, apple variables, etc.
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75;
	final int[] x = new int [GAME_UNITS];
	final int[] y = new int [GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	// panel constructor, bg color, window size, in focus and keys
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(new Color(168, 224, 0));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	// start game method
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	//
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	// grid size, apple color and coords
	public void draw(Graphics g) {
		//this for loop draws the grid to make it easier to see
		if (running) {	
			/*for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}*/
			g.setColor(Color.black);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for(int i = 0; i < bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.darkGray);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}else{
					g.setColor(Color.black);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.darkGray);
			g.setFont(new Font("Impact", Font.ITALIC, 30));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten)) / 2, g.getFont().getSize());

		}
		else {
			gameOver(g);
		}
	}
	// snake movement
	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}

		switch (direction) {
			case 'U':
				y[0] = y[0] - UNIT_SIZE;
				break;
		
			case 'D':
				y[0] = y[0] + UNIT_SIZE;
				break;

			case 'L':
				x[0] = x[0] - UNIT_SIZE;
				break;

			case 'R':
				x[0] = x[0] + UNIT_SIZE;
				break;

			default:
				break;
		}

	}
	//apple spawn random coords
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

	}
	//if apple is eaten bodypart increments and new apple spawns
	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}

	}
	public void checkCollisions() {
		//checks if head eats its own body
		for (int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//checks if head touches left border
		if(x[0] < 0) {
			running = false;
		}
		//checks if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//checks if head touches top border
		if (y[0] < 0) {
			running = false;
		}
		//checks if head touches bottom border
		if (y[0] > SCREEN_HEIGHT) {
			running = false;
		}

		if(!running) {
			timer.stop();
		}

	}
	public void gameOver(Graphics g) {
		//show score on game over screen
		g.setColor(Color.darkGray);
		g.setFont(new Font("Impact", Font.ITALIC, 30));
		FontMetrics metrics_Score = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics_Score.stringWidth("Score: "+applesEaten)) / 2, g.getFont().getSize());

		//game over gg
		g.setColor(Color.darkGray);
		g.setFont(new Font("Impact", Font.BOLD, 75));
		FontMetrics metrics_gameOver = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics_gameOver.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);


	}
	
	//check if all works
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}	
		repaint();
	}
	//arrow keys to control your snakey
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (direction != 'R') {
						direction = 'L';
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (direction != 'L') {
						direction = 'R';
					}
					break;
				
				case KeyEvent.VK_UP:
					if (direction != 'D') {
						direction = 'U';
					}
					break;
				
				case KeyEvent.VK_DOWN:
					if (direction != 'U') {
						direction = 'D';
					}
					break;		
			}

		}
	}

}
