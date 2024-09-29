import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class Puzzle extends JFrame implements ActionListener {

    JButton[] buttons;
    JButton shuffle;
    int counter = 0;
    JLabel counterLabel, levelLabel;
    int level = 1;
    int gridSize = 3;
    final int BUTTON_WIDTH = 60, BUTTON_HEIGHT = 50;

    // Constructor to build the puzzle
    Puzzle() {
        // Set up the window (frame)
        setSize(600, 600);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create elements
        shuffle = new JButton("Shuffle!");
        counterLabel = new JLabel("Clicks: 0");
        levelLabel = new JLabel("Level: 1");

        // Attach elements to window
        Container contentPane = this.getContentPane();
        contentPane.add(counterLabel);
        contentPane.add(levelLabel);
        add(shuffle);

        // Place elements
        shuffle.setBounds(250, 500, 100, 40);
        counterLabel.setBounds(100, 20, 180, 40);
        levelLabel.setBounds(350, 20, 180, 40);

        // Customize elements
        shuffle.setBackground(Color.LIGHT_GRAY);
        shuffle.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        counterLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        levelLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));

        // Add an event listener
        shuffle.addActionListener(this);

        // Create the initial 3x3 grid
        createGrid();
    }

    // Method to create a grid based on the current level
    private void createGrid() {
        // Remove any existing buttons (when level changes)
        if (buttons != null) {
            for (JButton button : buttons) {
                remove(button);
            }
        }

        // Set the grid size based on the level
        gridSize = (level == 1) ? 3 : 4;

        // Initialize button array
        buttons = new JButton[gridSize * gridSize];

        // Generate a list of numbers from 1 to (gridSize * gridSize - 1)
        ArrayList<String> numbers = new ArrayList<>();
        for (int i = 1; i < gridSize * gridSize; i++) {
            numbers.add(String.valueOf(i));
        }
        numbers.add(" "); // Empty space

        // Shuffle the numbers
        Collections.shuffle(numbers);

        // Calculate the starting point to center the grid
        int startX = 90;
        int startY = 60;
        int horizontalSpacing = BUTTON_WIDTH + 10; // 10 is the gap between buttons
        int verticalSpacing = BUTTON_HEIGHT + 10; // 10 is the gap between buttons

        // Create buttons and add them to the frame
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(numbers.get(i));
            buttons[i].setBounds(startX + (i % gridSize) * horizontalSpacing, startY + (i / gridSize) * verticalSpacing,
                    BUTTON_WIDTH, BUTTON_HEIGHT);
            buttons[i].setBackground(Color.decode("#5adbb5"));
            buttons[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }

        revalidate();
        repaint();
    }

    private boolean isSolved() {
        for (int i = 0; i < buttons.length - 1; i++) {
            if (!buttons[i].getText().equals(String.valueOf(i + 1))) {
                return false;
            }
        }
        return buttons[buttons.length - 1].getText().equals(" ");
        // return true;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == shuffle) {
            // Shuffle the puzzle and reset the counter
            createGrid();
            counter = 0;
            counterLabel.setText("Clicks: 0");
        } else {
            // Button click logic for moving tiles
            for (int i = 0; i < buttons.length; i++) {
                if (e.getSource() == buttons[i]) {
                    String currentText = buttons[i].getText();

                    // Check if the clicked button is adjacent to the empty space
                    if (i > 0 && buttons[i - 1].getText().equals(" ")) { // Left
                        buttons[i - 1].setText(currentText);
                        buttons[i].setText(" ");
                    } else if (i < buttons.length - 1 && buttons[i + 1].getText().equals(" ")) { // Right
                        buttons[i + 1].setText(currentText);
                        buttons[i].setText(" ");
                    } else if (i >= gridSize && buttons[i - gridSize].getText().equals(" ")) { // Up
                        buttons[i - gridSize].setText(currentText);
                        buttons[i].setText(" ");
                    } else if (i < buttons.length - gridSize && buttons[i + gridSize].getText().equals(" ")) { // Down
                        buttons[i + gridSize].setText(currentText);
                        buttons[i].setText(" ");
                    }

                    counter++;
                    counterLabel.setText("Clicks: " + counter);

                    // Check if the user has won the game
                    if (isSolved()) {
                        JOptionPane.showMessageDialog(Puzzle.this,
                                "YOU WON!\n" + "You clicked: " + counter + " times.");
                        
                        if (level == 2) {
                            level = 1; // Reset to level 1 if currently at level 2
                            counter = 0; // Reset click counter to zero
                            counterLabel.setText("Clicks: 0");
                        } else {
                            level++; // Move to the next level
                        }
                        
                        levelLabel.setText("Level: " + level);
                        createGrid(); // Create the grid for the new level
                    }

                    break;
                }
            }
        }
    }
}


