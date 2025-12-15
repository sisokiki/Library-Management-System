package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.formdev.flatlaf.FlatDarkLaf;

public class Login extends JFrame implements ActionListener {

    JTextField tfusername;
    JPasswordField tfpassword;
    JButton login, cancel;

    Login() {
        // Apply the FlatLaf Theme
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Library Management System - Login");
        setLayout(null);
        setSize(600, 300);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("icons/lojo.png")).getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setResizable(false);

        Font myFont = new Font("Segoe UI", Font.BOLD, 14);

        JLabel l1 = new JLabel("Username");
        l1.setBounds(150, 70, 100, 30);
        l1.setFont(myFont);
        l1.setForeground(Color.black);
        add(l1);

        tfusername = new JTextField();
        tfusername.setBounds(260, 70, 150, 30);
        add(tfusername);

        JLabel l2 = new JLabel("Password");
        l2.setBounds(150, 120, 100, 30);
        l2.setFont(myFont);
        l2.setForeground(Color.black);
        add(l2);

        tfpassword = new JPasswordField();
        tfpassword.setBounds(260, 120, 150, 30);
        add(tfpassword);

        login = new JButton("Login");
        login.setBounds(150, 180, 120, 30);
        login.addActionListener(this);
        add(login);

        cancel = new JButton("Cancel");
        cancel.setBounds(290, 180, 120, 30);
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            try {
                String username = tfusername.getText();
                String password = new String(tfpassword.getPassword()); // Get password safely

                Conn c = new Conn();
                String query = "select * from login where username = '"+username+"' and password = '"+password+"'";

                ResultSet rs = c.s.executeQuery(query);
                if (rs.next()) {
                    String role = rs.getString("role"); // 1. Fetch the role column from MySQL
                    setVisible(false);
                    new Main(role); // 2. Pass the role to the Main Dashboard
                    dispose(); // Close login window
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}