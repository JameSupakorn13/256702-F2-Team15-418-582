package com.lab;

import javax.swing.*;
import java.awt.*;

public class PongMenu extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private PongGame pongGame;
    private JTextField player1NameField;
    private JTextField player2NameField;

    public PongMenu() {
        setTitle("Pong Game - Main Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel menuPanel = new BackgroundPanel("/background.jpg");
        menuPanel.setLayout(new GridBagLayout());

        mainPanel.add(menuPanel, "Menu");
        add(mainPanel);
        
        player1NameField = new JTextField("Player 1", 15);
        player2NameField = new JTextField("Player 2", 15);
        
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 18);
        player1NameField.setFont(thaiFont);
        player2NameField.setFont(thaiFont);

        JButton singlePlayerButton = createStyledButton("Single Player");
        singlePlayerButton.addActionListener(e -> startGame(true));

        JButton multiplayerButton = createStyledButton("Multiplayer");
        multiplayerButton.addActionListener(e -> startGame(false));
        
        JButton howToPlayButton = createStyledButton("How to Play");
        howToPlayButton.addActionListener(e -> showHowToPlay());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(player1NameField);
        buttonPanel.add(player2NameField);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(singlePlayerButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(multiplayerButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(howToPlayButton);

        player1NameField.setVisible(false);
        player2NameField.setVisible(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        menuPanel.add(buttonPanel, gbc);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 100, 100));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50));
            }
        });
        return button;
    }

    private void startGame(boolean isSinglePlayer) {
        String player1Name = player1NameField.getText();
        String player2Name = player2NameField.getText();

        if (isSinglePlayer) {
            player1NameField.setVisible(true);
            player2NameField.setVisible(false);
        } else {
            player1NameField.setVisible(true);
            player2NameField.setVisible(true);
        }

        JOptionPane.showMessageDialog(this, "Starting " + (isSinglePlayer ? "Single Player" : "Multiplayer") + " mode... 🚀");

        if (pongGame != null) {
            mainPanel.remove(pongGame);
        }
        pongGame = new PongGame();
        pongGame.setSinglePlayer(isSinglePlayer);
        pongGame.setPlayerNames(player1Name, player2Name);

        mainPanel.add(pongGame, "Game");
        cardLayout.show(mainPanel, "Game");
        mainPanel.revalidate();
        mainPanel.repaint();
        pongGame.start();
    }

    private void showHowToPlay() {
        JTextArea textArea = new JTextArea("\n    - How to Play -\n\n"
                + ": Player 1: Use W/S to move up and down.\n"
                + ": Player 2: Use Up/Down arrows to move.\n"
                + ": Score points by getting the ball past your opponent.\n"
                + ": The first player to reach the target score wins!\n\n"
                + ": enjoy the game! ");
        textArea.setFont(new Font("Arial", Font.BOLD, 16));
        textArea.setEditable(false);
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "How to Play", JOptionPane.INFORMATION_MESSAGE);
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String filePath) {
            try {
                backgroundImage = new ImageIcon(getClass().getResource(filePath)).getImage();
            } catch (Exception e) {
                System.out.println("❌ Error loading background image: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PongMenu::new);
    }
}
