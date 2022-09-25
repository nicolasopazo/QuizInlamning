package com.nico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread implements ActionListener {
    Socket socket;
    ObjectInputStream input;
    PrintWriter out;
    Object object;
    String[] options = null;
    String question = null;
    String player;
    String correctAnswer;

    private final JTextField title = new JTextField();
    private final JTextField textField = new JTextField();
    private final JButton buttonA = new JButton();
    private final JButton buttonB = new JButton();
    private final JButton buttonC = new JButton();
    private final JButton buttonD = new JButton();
    private final JLabel answerA = new JLabel();
    private final JLabel answerB = new JLabel();
    private final JLabel answerC = new JLabel();
    private final JLabel answerD = new JLabel();


    public Client() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 435);
        frame.getContentPane().setBackground(new Color(255, 255, 255));
        frame.setLayout(null);
        frame.setResizable(false);

        title.setBounds(0, 0, 400, 50);
        title.setBackground(new Color(255, 255, 255));
        title.setForeground(new Color(0, 0, 0));
        title.setFont(new Font("HELVETICA", Font.BOLD, 25));
        title.setHorizontalAlignment(JTextField.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder());
        title.setEditable(false);
        title.setText("QUIZ");

        textField.setBounds(0, 50, 400, 50);
        textField.setBackground(new Color(255, 255, 255));
        textField.setForeground(new Color(0, 0, 0));
        textField.setFont(new Font("HELVETICA", Font.BOLD, 17));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setEditable(false);

        answerA.setBounds(0, 75, 400, 100);
        answerA.setBackground(new Color(255, 255, 255));
        answerA.setForeground(new Color(0, 0, 0));
        answerA.setFont(new Font("HELVETICA", Font.BOLD, 17));
        answerA.setHorizontalAlignment(JTextField.CENTER);

        answerB.setBounds(0, 125, 400, 100);
        answerB.setBackground(new Color(255, 255, 230));
        answerB.setForeground(new Color(0, 0, 0));
        answerB.setFont(new Font("HELVETICA", Font.BOLD, 17));
        answerB.setHorizontalAlignment(JTextField.CENTER);

        answerC.setBounds(0, 175, 400, 100);
        answerC.setBackground(new Color(255, 255, 230));
        answerC.setForeground(new Color(0, 0, 0));
        answerC.setFont(new Font("HELVETICA", Font.BOLD, 17));
        answerC.setHorizontalAlignment(JTextField.CENTER);

        answerD.setBounds(0, 225, 400, 100);
        answerD.setBackground(new Color(255, 255, 230));
        answerD.setForeground(new Color(0, 0, 0));
        answerD.setFont(new Font("HELVETICA", Font.BOLD, 17));
        answerD.setHorizontalAlignment(JTextField.CENTER);

        buttonA.setBounds(0, 300, 90, 90);
        buttonA.setFont(new Font("HELVETICA", Font.BOLD, 40));
        buttonA.setBorder(BorderFactory.createEmptyBorder());
        buttonA.addActionListener(this);
        buttonA.setText("A");

        buttonB.setBounds(100, 300, 90, 90);
        buttonB.setFont(new Font("HELVETICA", Font.BOLD, 40));
        buttonB.addActionListener(this);
        buttonB.setBorder(BorderFactory.createEmptyBorder());
        buttonB.setText("B");

        buttonC.setBounds(200, 300, 90, 90);
        buttonC.setFont(new Font("HELVETICA", Font.BOLD, 40));
        buttonC.addActionListener(this);
        buttonC.setBorder(BorderFactory.createEmptyBorder());
        buttonC.setText("C");

        buttonD.setBounds(300, 300, 90, 90);
        buttonD.setFont(new Font("HELVETICA", Font.BOLD, 40));
        buttonD.addActionListener(this);
        buttonD.setBorder(BorderFactory.createEmptyBorder());
        buttonD.setText("D");

        frame.add(answerA);
        frame.add(answerB);
        frame.add(answerC);
        frame.add(answerD);
        frame.add(buttonA);
        frame.add(buttonB);
        frame.add(buttonC);
        frame.add(buttonD);
        frame.add(textField);
        frame.add(title);
        frame.setVisible(true);
        frame.add(title);
        frame.setVisible(true);
    }

    public void showQuestion() {
        textField.setText(question);
        answerA.setText(options[0]);
        answerB.setText(options[1]);
        answerC.setText(options[2]);
        answerD.setText(options[3]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonA) {
            //ternary (operator)
            sendMessageToServer(buttonA.getText().equals(correctAnswer) ? "correct" : "false");
        }
        if (e.getSource() == buttonB) {
            sendMessageToServer(buttonB.getText().equals(correctAnswer) ? "correct" : "false");
        }
        if (e.getSource() == buttonC) {
            sendMessageToServer(buttonC.getText().equals(correctAnswer) ? "correct" : "false");
        }
        if (e.getSource() == buttonD) {
            sendMessageToServer(buttonD.getText().equals(correctAnswer) ? "correct" : "false");
        }
        options = null;
    }

    public void sendMessageToServer(String message) {
        out.println(message);
    }

    public void run() {
        try{
            socket = new Socket("localhost", 55556);
            input = new ObjectInputStream(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            receiveMessage();
            if(question !=null && options!=null) {
                showQuestion();
            } else if(question !=null) {
                textField.setText(question);
            }
        }
    }

    public void showResult() {
        textField.setText(question);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exitGame() {
        textField.setText("Tack för att du spelade, hejdå!");
        System.out.println("The game ended");
        try {
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void receiveMessage() {
            try {
                if (((object = input.readObject())!=null)) {
                    if (object instanceof String) {
                        String s = (String) object;
                        if (s.startsWith("player")) {
                            player = s;
                        } else if (s.startsWith("Correct")) {
                            correctAnswer = s.substring(8);
                        } else if (s.startsWith("Result")) {
                            question = s.substring(7);
                            showResult();
                        } else if(s.startsWith("EXIT")) {
                            exitGame();
                        } else {
                            question = s;
                        }
                    } else if (object instanceof String[]) {
                        options = (String[]) object;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

    public static void main(String[] args) {
        new Client().start();
    }
}
