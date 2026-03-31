package com.library;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {

    JTextField userField;
    JPasswordField passField;

    public Login() {

        setTitle("Library Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Labels
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        // Fields
        userField = new JTextField(15);
        passField = new JPasswordField(15);

        // Buttons
        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Signup");
        JButton forgotBtn = new JButton("Forgot Password");

        // Show Password
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(Color.WHITE);

        showPassword.addActionListener(e -> {
            passField.setEchoChar(showPassword.isSelected() ? (char) 0 : '*');
        });

        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(showPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(loginBtn, gbc);

        gbc.gridx = 1;
        panel.add(signupBtn, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(forgotBtn, gbc);

        add(panel);

        // ================= ACTIONS =================

        loginBtn.addActionListener(e -> login());

        signupBtn.addActionListener(e -> {
            new Signup();
            dispose();
        });

        forgotBtn.addActionListener(e -> {
            new ForgotPassword();
            dispose();
        });

        setVisible(true);
    }

    private void login() {

        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter all fields!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT role FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String role = rs.getString("role");

                if (role == null || role.trim().isEmpty()) {
                    role = "user";
                }

                role = role.trim().toLowerCase();

                boolean isAdmin = role.equals("admin");

                JOptionPane.showMessageDialog(this, "Login Successful!");

                // 🔥 IMPORTANT CHANGE (PASS USERNAME ALSO)
                new LibraryGUI(isAdmin, username);

                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }
}