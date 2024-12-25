import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class SortingVisualizer extends JPanel implements ActionListener {
    private static final int SCREEN_WIDTH = 910;
    private static final int SCREEN_HEIGHT = 750;
    private static final int ARR_SIZE = 130;
    private static final int RECT_SIZE = 7;

    private int[] arr = new int[ARR_SIZE];
    private int[] originalArr = new int[ARR_SIZE];
    private boolean complete = false;
    private String currentTheme = "Default";
    private int delay = 5;

    private Timer timer;

    public SortingVisualizer() {
        randomizeAndSaveArray();
        loadArray();
        timer = new Timer(delay, this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Apply theme
        if (currentTheme.equals("Dark Mode")) {
            setBackground(Color.BLACK);
        } else if (currentTheme.equals("Rainbow Mode")) {
            setBackground(Color.WHITE);
        } else {
            setBackground(new Color(240, 240, 240));
        }

        int x = 0;
        for (int i = 0; i < ARR_SIZE; i++) {
            if (currentTheme.equals("Rainbow Mode")) {
                g.setColor(Color.getHSBColor((float) i / ARR_SIZE, 1.0f, 1.0f));
            } else if (complete) {
                g.setColor(new Color(100, 180, 100));
            } else {
                g.setColor(new Color(170, 183, 184));
            }
            g.fillRect(x, SCREEN_HEIGHT - arr[i], RECT_SIZE, arr[i]);
            g.setColor(Color.BLACK);
            g.drawRect(x, SCREEN_HEIGHT - arr[i], RECT_SIZE, arr[i]);
            x += RECT_SIZE;
        }
    }

    public void randomizeAndSaveArray() {
        Random rand = new Random();
        for (int i = 0; i < ARR_SIZE; i++) {
            originalArr[i] = rand.nextInt(SCREEN_HEIGHT - 50) + 50;
        }
    }

    public void loadArray() {
        System.arraycopy(originalArr, 0, arr, 0, ARR_SIZE);
        complete = false;
        repaint();
    }

    public void selectionSort() {
        new Thread(() -> {
            for (int i = 0; i < ARR_SIZE - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < ARR_SIZE; j++) {
                    if (arr[j] < arr[minIndex]) {
                        minIndex = j;
                    }
                    repaint();
                    sleep(delay);
                }
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
                repaint();
            }
            complete = true;
        }).start();
    }

    public void bubbleSort() {
        new Thread(() -> {
            for (int i = 0; i < ARR_SIZE - 1; i++) {
                for (int j = 0; j < ARR_SIZE - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                        repaint();
                        sleep(delay);
                    }
                }
            }
            complete = true;
        }).start();
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    public void toggleTheme() {
        if (currentTheme.equals("Default")) {
            currentTheme = "Dark Mode";
        } else if (currentTheme.equals("Dark Mode")) {
            currentTheme = "Rainbow Mode";
        } else {
            currentTheme = "Default";
        }
        repaint();
    }

    public void increaseSpeed() {
        delay = Math.max(1, delay - 2);
    }

    public void decreaseSpeed() {
        delay += 2;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sorting Visualizer");
        SortingVisualizer panel = new SortingVisualizer();

        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_0:
                        panel.randomizeAndSaveArray();
                        panel.loadArray();
                        break;
                    case KeyEvent.VK_1:
                        panel.loadArray();
                        panel.selectionSort();
                        break;
                    case KeyEvent.VK_2:
                        panel.loadArray();
                        panel.bubbleSort();
                        break;
                    case KeyEvent.VK_T:
                        panel.toggleTheme();
                        break;
                    case KeyEvent.VK_PLUS:
                        panel.increaseSpeed();
                        break;
                    case KeyEvent.VK_MINUS:
                        panel.decreaseSpeed();
                        break;
                    case KeyEvent.VK_Q:
                        System.exit(0);
                        break;
                }
            }
        });
    }
}
