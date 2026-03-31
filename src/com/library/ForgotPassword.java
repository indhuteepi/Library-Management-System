package com.library;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ForgotPassword extends JFrame {

    JTextField userField;

    public ForgotPassword() {

        setTitle("Forgot Password");
        setSize(300, 200);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userField = new JTextField(15);

        JButton getPassBtn = new JButton("Get Password");
        JButton backBtn = new JButton("Back");

        add(new JLabel("Enter Username:"));
        add(userField);

        add(getPassBtn);
        add(backBtn);

        getPassBtn.addActionListener(e -> getPassword());

        backBtn.addActionListener(e -> {
            new Login();
            dispose();
        });

        setVisible(true);
    }

    private void getPassword() {

        String username = userField.getText();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT password FROM users WHERE username=?");

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String password = rs.getString("password");

                JOptionPane.showMessageDialog(this,
                        "Your Password: " + password);

            } else {
                JOptionPane.showMessageDialog(this,
                        "User not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}