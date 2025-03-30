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

        // ✅ โหลดภาพจาก resources/
        String imagePath = "/background.jpg"; // รูปต้องอยู่ใน resources/
        JPanel menuPanel = new BackgroundPanel(imagePath);
        menuPanel.setLayout(new GridBagLayout());

        pongGame = new PongGame();
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(pongGame, "Game");

        player1NameField = new JTextField("Player 1", 15);
        player2NameField = new JTextField("Player 2", 15);

        // ตั้งฟอนต์ให้รองรับภาษาไทย
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 18); // หรือเลือกฟอนต์อื่นที่รองรับภาษาไทย
        player1NameField.setFont(thaiFont);
        player2NameField.setFont(thaiFont);

        // ✅ ปุ่มเล่นเกม
        JButton singlePlayerButton = createStyledButton("Single Player");
        singlePlayerButton.addActionListener(e -> startGame(true));

        JButton multiplayerButton = createStyledButton("Multiplayer");
        multiplayerButton.addActionListener(e -> startGame(false));

        // ✅ จัดปุ่มให้อยู่ตรงกลาง
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(player1NameField);
        buttonPanel.add(player2NameField);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(singlePlayerButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(multiplayerButton);

        // ซ่อนช่องกรอกชื่อ
        player1NameField.setVisible(false);
        player2NameField.setVisible(false);

        // ✅ ใช้ GridBagLayout เพื่อจัดปุ่มไว้กลางจอ
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        menuPanel.add(buttonPanel, gbc);

        add(mainPanel);
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
        pongGame.setSinglePlayer(isSinglePlayer);
        pongGame.setPlayerNames(player1Name, player2Name);
        cardLayout.show(mainPanel, "Game");
        pongGame.start();
    }

    // ✅ คลาสสำหรับแสดงภาพพื้นหลังจาก resources/
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String filePath) {
            try {
                // โหลดภาพจาก resources โดยใช้ getResource
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
