package com.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MusicGUI extends JFrame {

    JTextArea textArea;

    public MusicGUI() {
        super("Music Advisor");
        prepareGUI();

    }

    private void prepareGUI() {

        // JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
        setLocation(1500,300); // where the JFrame appears on the desktop
        setSize(600, 400);

        // JLabel
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setVisible(true);

        // JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Adding components
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public void setTextArea(String content) {
        this.textArea.setText(content);
    }

    public static void exit() {
        System.exit(0);
    }
}
