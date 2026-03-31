package com.library;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Signup extends JFrame {

    JTextField userField;
    JPasswordField passField;
    JComboBox<String> roleBox;

    public Signup() {

        setTitle("Signup");
        setSize(320, 280);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userField = new JTextField(15);
        passField = new JPasswordField(15);

        roleBox = new JComboBox<>(new String[]{"user", "admin"});

        JButton signupBtn = new JButton("Create Account");
        JButton backBtn = new JButton("Back");

        // 👁 Show Password
        JCheckBox showPassword = new JCheckBox("Show Password");

        add(new JLabel("Username:"));
        add(userField);

        add(new JLabel("Password:"));
        add(passField);

        add(showPassword);

        add(new JLabel("Role:"));
        add(roleBox);

        add(signupBtn);
        add(backBtn);

        // 👁 Toggle password
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passField.setEchoChar((char) 0);
            } else {
                passField.setEchoChar('*');
            }
        });

        signupBtn.addActionListener(e -> signup());

        backBtn.addActionListener(e -> {
            new Login();
            dispose();
        });

        setVisible(true);
    }

    private void signup() {

        String username = userField.getText();
        String password = new String(passField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            // ⚠ Check duplicate username
            PreparedStatement check = con.prepareStatement(
                    "SELECT * FROM users WHERE username=?");
            check.setString(1, username);

            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
                return;
            }

            // Insert new user
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (username, password, role) VALUES (?, ?, ?)");

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account Created Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }
}