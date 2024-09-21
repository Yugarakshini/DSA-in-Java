import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class MazeGame extends JPanel implements KeyListener {
    private static final int SIZE = 5;
    private static final int[][] maze = {
            {0, 1, 0, 0, 0},
            {0, 1, 0, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0}
    };
    
    private int playerX = 0, playerY = 0; // Player start position
    private final int exitX = 4, exitY = 4; // Exit position
    private int moveCount = 0; // Track moves
    private Timer timer;
    private int elapsedTime = 0; // Track time in seconds


    public MazeGame() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        startTimer();
    }

    // Start timer for tracking time
    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTime++;
                repaint();
            }
        }, 1000, 1000);
    }

    // Override the paintComponent method to draw the maze and player
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellSize = 400 / SIZE;

        // Draw the maze
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (maze[i][j] == 1) {
                    g.setColor(Color.BLACK); // Wall
                } else {
                    g.setColor(Color.WHITE); // Path
                }
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }

        // Draw the player
        g.setColor(Color.BLUE);
        g.fillOval(playerY * cellSize, playerX * cellSize, cellSize, cellSize);

        // Draw the exit
        g.setColor(Color.GREEN);
        g.fillRect(exitY * cellSize, exitX * cellSize, cellSize, cellSize);

        // Display move count and time
        g.setColor(Color.BLACK);
        g.drawString("Moves: " + moveCount, 10, 10);
        g.drawString("Time: " + elapsedTime + "s", 10, 25);
    }

    // Key press events for WASD keys
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W -> // Move up
                movePlayer(-1, 0);
            case KeyEvent.VK_A -> // Move left
                movePlayer(0, -1);
            case KeyEvent.VK_S -> // Move down
                movePlayer(1, 0);
            case KeyEvent.VK_D -> // Move right
                movePlayer(0, 1);
        }

        // Check if player reached the exit
        if (playerX == exitX && playerY == exitY) {
            timer.cancel(); // Stop the timer
            JOptionPane.showMessageDialog(this, "Congratulations! You reached the exit in " + moveCount + " moves and " + elapsedTime + " seconds.");
            resetGame();
        }

        repaint();
    }

    // Move the player and check for valid moves
    private void movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;

        // Check boundaries and if it's a valid path
        if (newX >= 0 && newX < SIZE && newY >= 0 && newY < SIZE && maze[newX][newY] == 0) {
            playerX = newX;
            playerY = newY;
            moveCount++;
        }
    }

    // Reset game after winning
    private void resetGame() {
        playerX = 0;
        playerY = 0;
        moveCount = 0;
        elapsedTime = 0;
        startTimer();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    // Main method to set up the JFrame and game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Solver Game");
        MazeGame mazeGame = new MazeGame();
        frame.add(mazeGame);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
