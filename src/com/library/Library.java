package com.library;

import javax.swing.*;
import java.sql.*;

public class Library {

    // ✅ ADD BOOK (FIXED)
    public void addBook(Book book) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO books VALUES (?, ?, ?, ?)");

            ps.setInt(1, book.id);
            ps.setString(2, book.title);
            ps.setString(3, book.author);
            ps.setBoolean(4, false);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Book Added Successfully!");

        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Book ID already exists!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding book!");
        }
    }

    // ✅ DELETE BOOK (FIXED)
    public void deleteBook(int id) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM books WHERE id=?");

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Book Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Book not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting book!");
        }
    }

    // ✅ ISSUE BOOK
    public void issueBook(int id) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE books SET isIssued=true WHERE id=? AND isIssued=false");

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Book Issued!");
            } else {
                JOptionPane.showMessageDialog(null, "Book not available!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ RETURN BOOK
    public void returnBook(int id) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE books SET isIssued=false WHERE id=? AND isIssued=true");

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Book Returned!");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Book ID!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ DASHBOARD
    public int getTotalBooks() {
        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM books");
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getIssuedBooks() {
        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM books WHERE isIssued=true");
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}