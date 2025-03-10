package com.lab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.IOException;
import java.net.URL;

public class PongGame extends JPanel implements KeyListener, ActionListener {
    private int ballX = 250, ballY = 250, ballDX = 2, ballDY = 2;
    private int paddle1Y = 200, paddle2Y = 200;
    private int player1Score = 0, player2Score = 0;
    private final int PADDLE_WIDTH = 10, PADDLE_HEIGHT = 60;
    private Timer timer;
    private boolean isSinglePlayer = false;
    private String player1Name = "ผู้เล่น 1", player2Name = "ผู้เล่น 2";

    private boolean keyWPressed = false, keySPressed = false, keyUpPressed = false, keyDownPressed = false;

    private Random random = new Random();
    private Image backgroundImage;

    public PongGame() {
        this.setPreferredSize(new Dimension(800, 600));
        this.setFocusable(true);
        this.addKeyListener(this);
        timer = new Timer(10, this);

        // โหลดภาพพื้นหลัง
        try {
            URL backgroundUrl = getClass().getResource("/path/to/your/background-image.jpg");
            backgroundImage = new ImageIcon(backgroundUrl).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSinglePlayer(boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
    }

    public void setPlayerNames(String player1, String player2) {
        this.player1Name = player1;
        this.player2Name = player2;
    }

    public void start() {
        // ถ้าเป็นโหมด 2 คน ให้ขอชื่อผู้เล่นทั้งสอง
        if (!isSinglePlayer) {
            player1Name = JOptionPane.showInputDialog(" PLS Name 1:");
            player2Name = JOptionPane.showInputDialog(" PLS Name 2:");
        }
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // วาดภาพพื้นหลัง
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.BLACK); // ถ้าไม่สามารถโหลดภาพได้ ใช้พื้นหลังดำ
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // วาดบอลและแผ่นพาย
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, 10, 10);
        g.fillRect(30, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(760, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // แสดงชื่อผู้เล่น
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString(player1Name + ": " + player1Score, 30, 20); // ชื่อผู้เล่น 1 พร้อมคะแนน
        g.drawString(player2Name + ": " + player2Score, 760 - g.getFontMetrics().stringWidth(player2Name + ": " + player2Score), 20); // ชื่อผู้เล่น 2 พร้อมคะแนน
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        ballX += ballDX;
        ballY += ballDY;
    
        // บอลชนผนังบนและล่าง
        if (ballY <= 0 || ballY >= getHeight() - 10) ballDY = -ballDY;
    
        // บอลชนแผ่นพาย
        if ((ballX <= 40 && ballY >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) ||
            (ballX >= 750 && ballY >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT)) {
            ballDX = -ballDX;
        }
    
        // ถ้าได้คะแนนแล้ว (ฝั่งซ้ายหรือขวา)
        if (ballX <= 0 || ballX >= getWidth()) {
            if (ballX <= 0) player2Score++;
            if (ballX >= getWidth()) player1Score++;
    
            ballX = 400; // รีเซ็ตตำแหน่งบอล
            ballY = 300;
    
            // เช็คหากฝ่ายใดฝ่ายหนึ่งได้ 11 คะแนน
            if (player1Score == 12) {
                timer.stop(); // หยุดเกม
                JOptionPane.showMessageDialog(this, player1Name + " ชนะ!", "เกมจบ", JOptionPane.INFORMATION_MESSAGE);
                resetGame(); // รีเซ็ตเกม
            } else if (player2Score == 11 ) {
                timer.stop(); // หยุดเกม
                JOptionPane.showMessageDialog(this, player2Name + " ชนะ!", "เกมจบ", JOptionPane.INFORMATION_MESSAGE);
                resetGame(); // รีเซ็ตเกม
            }
        }
    
        // หากเป็นโหมดผู้เล่นเดี่ยว (AI)
        if (isSinglePlayer) {
            int aiMove = random.nextInt(100);
            if (aiMove < 70) {
                if (paddle2Y + PADDLE_HEIGHT / 2 < ballY) {
                    paddle2Y += 3;
                } else if (paddle2Y + PADDLE_HEIGHT / 2 > ballY) {
                    paddle2Y -= 3;
                }
            }
        }
    
        // การควบคุมแผ่นพายของผู้เล่น
        if (keyWPressed && paddle1Y > 0) paddle1Y -= 10;
        if (keySPressed && paddle1Y < getHeight() - PADDLE_HEIGHT) paddle1Y += 10;
    
        if (!isSinglePlayer) {
            if (keyUpPressed && paddle2Y > 0) paddle2Y -= 10;
            if (keyDownPressed && paddle2Y < getHeight() - PADDLE_HEIGHT) paddle2Y += 10;
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

    // ฟังก์ชันสำหรับรีเซ็ตเกม
    private void resetGame() {
        player1Score = 0;
        player2Score = 0;
        ballX = 400;
        ballY = 300;
        paddle1Y = 200;
        paddle2Y = 200;
        timer.start(); // เริ่มเกมใหม่
    }
}
