package com.lab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class PongGame extends JPanel implements KeyListener, ActionListener {
    private int ballX = 250, ballY = 250, ballDX = 3, ballDY = 3;
    private int paddle1Y = 200, paddle2Y = 200;
    private int player1Score = 0, player2Score = 0;
    private final int PADDLE_WIDTH = 10, PADDLE_HEIGHT = 80;
    private Timer timer;
    private boolean isSinglePlayer = false;
    private String player1Name = "Player 1", player2Name = "Player 2";

    private boolean keyWPressed = false, keySPressed = false, keyUpPressed = false, keyDownPressed = false;

    private Random random = new Random();
    private Image backgroundImage;

    private int aiDifficulty = 1;  // 0 = Easy, 1 = Medium, 2 = Hard
    private int ballSpeed = 2; // ความเร็วของลูกบอลเริ่มต้นเป็น 2
    private boolean gameOver = false;  // ตัวแปรใหม่เพื่อใช้ในการหยุดเกม
    private long gameOverTime = 0;  // เวลาที่เกมจบ

    public PongGame() {
        this.setPreferredSize(new Dimension(800, 600));
        this.setFocusable(true);
        this.addKeyListener(this);
        timer = new Timer(10, this);
    }

    public void setSinglePlayer(boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
    }

    public void setPlayerNames(String player1, String player2) {
        this.player1Name = player1;
        this.player2Name = player2;
    }

    // ฟังก์ชันให้เลือกความเร็วของลูกบอลในโหมด 2 ผู้เล่น
    public void setBallSpeed() {
        if (!isSinglePlayer) {  // ให้เลือกความเร็วในโหมด 2 ผู้เล่นเท่านั้น
            String[] options = {"Slow", "Medium", "Fast"};
            int choice = JOptionPane.showOptionDialog(this, "Select Ball Speed", "Ball Speed",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);

            if (choice == 0) {
                ballSpeed = 1;  // Slow
            } else if (choice == 1) {
                ballSpeed = 2;  // Medium
            } else {
                ballSpeed = 3;  // Fast
            }
        }
    }

    public void start() {
        if (!isSinglePlayer) {
            player1Name = JOptionPane.showInputDialog("Enter Player 1 Name:");
            player2Name = JOptionPane.showInputDialog("Enter Player 2 Name:");
            setBallSpeed(); // เลือกความเร็วของลูกบอลในโหมด 2 ผู้เล่น
        } else {
            setAIDifficulty();  // ให้ผู้เล่นเลือกระดับความยาก AI
        }
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, 10, 10);
        g.fillRect(30, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(760, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString(player1Name + ": " + player1Score, 30, 20);
        g.drawString(player2Name + ": " + player2Score, 700, 20);

        // แสดงผลเมื่อเกมจบ
        if (gameOver) {
            String winner = (player1Score == 11) ? player1Name : player2Name;
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.RED);
            g.drawString(winner + " Wins!", 320, 300);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - gameOverTime >= 5000) {  // 15 วินาทีหลังจากเกมจบ
                // แสดงกล่องข้อความให้เลือกว่าจะเล่นใหม่หรือไม่
                int option = JOptionPane.showConfirmDialog(this, "END", "Game Over", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    resetGame();  // รีเซ็ตเกมและเริ่มเกมใหม่
                } else if (option == JOptionPane.NO_OPTION) {
                    // กด "No" รีเซ็ตเกมและกลับไปที่หน้าเลือกโหมดการเล่น
                    int modeOption = JOptionPane.showOptionDialog(this, "Mode", "Select Mode",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                            new String[] {"Single Player", "Two Player"}, "Single Player");

                    if (modeOption == 0) {
                        isSinglePlayer = true;
                        start();
                    } else if (modeOption == 1) {
                        isSinglePlayer = false;
                        start();
                    }
                }
            }
            return;  // หากเกมจบแล้วไม่ทำการอัปเดตเกมต่อ
        }

        ballX += ballDX * ballSpeed;  // ใช้ความเร็วที่เลือกในการเคลื่อนที่ของลูกบอล
        ballY += ballDY * ballSpeed;  // ใช้ความเร็วที่เลือกในการเคลื่อนที่ของลูกบอล

        if (ballY <= 0 || ballY >= getHeight() - 10) ballDY = -ballDY;

        if ((ballX <= 40 && ballY >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) ||
            (ballX >= 750 && ballY >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT)) {
            ballDX = -ballDX;
        }

        if (ballX <= 0 || ballX >= getWidth()) {
            if (ballX <= 0) player2Score++;
            if (ballX >= getWidth()) player1Score++;
            ballX = 400;
            ballY = 300;
        }

        if (player1Score == 11 || player2Score == 11) {
            gameOver = true;  // เมื่อมีผู้ชนะ คะแนนถึง 11 ให้จบเกม
            gameOverTime = System.currentTimeMillis();  // จดเวลาที่เกมจบ
        }

        if (isSinglePlayer) {
            int aiSpeed = 4;
            if (aiDifficulty == 0) aiSpeed = 2;  
            else if (aiDifficulty == 1) aiSpeed = 4;  
            else aiSpeed = 6;  

            if (paddle2Y + PADDLE_HEIGHT / 2 < ballY) {
                paddle2Y += aiSpeed;
            } else if (paddle2Y + PADDLE_HEIGHT / 2 > ballY) {
                paddle2Y -= aiSpeed;
            }
        }

        if (keyWPressed && paddle1Y > 0) paddle1Y -= 8;
        if (keySPressed && paddle1Y < getHeight() - PADDLE_HEIGHT) paddle1Y += 8;

        if (!isSinglePlayer) {
            if (keyUpPressed && paddle2Y > 0) paddle2Y -= 8;
            if (keyDownPressed && paddle2Y < getHeight() - PADDLE_HEIGHT) paddle2Y += 8;
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) keyWPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_S) keySPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_UP) keyUpPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) keyDownPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) keyWPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_S) keySPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_UP) keyUpPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) keyDownPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void resetGame() {
        player1Score = 0;
        player2Score = 0;
        ballX = 400;
        ballY = 300;
        paddle1Y = 200;
        paddle2Y = 200;
        gameOver = false;  // รีเซ็ตสถานะเกมจบ
        timer.start();  // เริ่มเวลาใหม่เพื่อให้เกมเริ่มเล่นใหม่
    }

    // ฟังก์ชันสำหรับการเลือกระดับความยาก AI
    public void setAIDifficulty() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(this, "Select AI Difficulty", "AI Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) aiDifficulty = 0;  // Easy
        else if (choice == 1) aiDifficulty = 1;  // Medium
        else aiDifficulty = 2;  // Hard
    }
}
