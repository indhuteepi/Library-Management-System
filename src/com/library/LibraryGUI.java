package com.library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LibraryGUI extends JFrame {

    Library lib = new Library();
    boolean isAdmin;
    String username;

    JTextField idField, titleField, authorField, searchField;
    JTable table;
    DefaultTableModel model;

    public LibraryGUI(boolean isAdmin, String username) {

        this.isAdmin = isAdmin;
        this.username = username;

        setTitle("Library Management System");
        setSize(950, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(180, 0));
        sidebar.setBackground(new Color(0, 120, 215));
        sidebar.setLayout(new GridLayout(9, 1, 10, 10));

        JLabel userLabel = new JLabel("👤 " + username, JLabel.CENTER);
        userLabel.setForeground(Color.WHITE);

        JButton addBtn = createSideButton("➕ Add");
        JButton showBtn = createSideButton("📋 Show");
        JButton issueBtn = createSideButton("📖 Issue");
        JButton returnBtn = createSideButton("🔄 Return");
        JButton searchBtn = createSideButton("🔍 Search");
        JButton deleteBtn = createSideButton("🗑 Delete");
        JButton dashBtn = createSideButton("📊 Dashboard");
        JButton logoutBtn = createSideButton("🚪 Logout");

        if (!isAdmin) {
            addBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }

        sidebar.add(userLabel);
        sidebar.add(addBtn);
        sidebar.add(showBtn);
        sidebar.add(issueBtn);
        sidebar.add(returnBtn);
        sidebar.add(searchBtn);
        sidebar.add(deleteBtn);
        sidebar.add(dashBtn);
        sidebar.add(logoutBtn);

        // ================= CONTENT =================
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idField = new JTextField();
        titleField = new JTextField();
        authorField = new JTextField();
        searchField = new JTextField();

        topPanel.add(new JLabel("ID:"));
        topPanel.add(idField);
        topPanel.add(new JLabel("Title:"));
        topPanel.add(titleField);

        topPanel.add(new JLabel("Author:"));
        topPanel.add(authorField);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);

        String[] columns = {"ID", "Title", "Author", "Issued"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        content.add(topPanel, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(content, BorderLayout.CENTER);

        add(mainPanel);

        // ================= ACTIONS =================

        addBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                lib.addBook(new Book(id, titleField.getText(), authorField.getText()));
                loadTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        showBtn.addActionListener(e -> loadTable());

        issueBtn.addActionListener(e -> {
            try {
                lib.issueBook(Integer.parseInt(idField.getText()));
                loadTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid ID!");
            }
        });

        returnBtn.addActionListener(e -> {
            try {
                lib.returnBook(Integer.parseInt(idField.getText()));
                loadTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid ID!");
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                lib.deleteBook(Integer.parseInt(idField.getText()));
                loadTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID!");
            }
        });

        searchBtn.addActionListener(e -> {
            try {
                model.setRowCount(0);
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM books WHERE title LIKE ?");
                ps.setString(1, "%" + searchField.getText() + "%");

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getBoolean("isIssued")
                    });
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        dashBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "📊 Total Books: " + lib.getTotalBooks() +
                            "\n📖 Issued Books: " + lib.getIssuedBooks());
        });

        // 🔐 LOGOUT
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this, "Are you sure you want to logout?", "Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                new Login();
                dispose();
            }
        });

        setVisible(true);
    }

    private void loadTable() {
        try {
            model.setRowCount(0);
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM books");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("isIssued")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createSideButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(0, 120, 215));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }
}
