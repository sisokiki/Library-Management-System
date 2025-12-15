package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class BookDetails extends JInternalFrame implements ActionListener {

    JTable table;
    JTextField search;
    JButton print, cancel;

    BookDetails() {
        super("Master List of Books", true, true, true, true);
        setSize(1000, 600);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // --- Search Bar ---
        JLabel l1 = new JLabel("Search by Book ID / Name:");
        l1.setBounds(20, 20, 180, 20);
        l1.setForeground(Color.black);
        add(l1);

        search = new JTextField();
        search.setBounds(200, 20, 200, 20);
        add(search);

        // Search Listener (Updates table as you type *feature)
        search.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ae) {
                String query = search.getText();
                loadData(query);
            }
        });

        // --- Table Setup ---
        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 100, 990, 450);
        add(jsp);

        // --- Buttons ---
        print = new JButton("Print");
        print.setBounds(120, 60, 100, 30);
        print.addActionListener(this);
        add(print);

        cancel = new JButton("Close");
        cancel.setBounds(240, 60, 100, 30);
        cancel.addActionListener(this);
        add(cancel);

        // Load initial data
        loadData("");

        setVisible(true);
    }

    // Helper method to fetch data
    void loadData(String query) {
        try {
            Conn c = new Conn();
            String sql;
            if (query.isEmpty()) {
                sql = "select * from book";
            } else {
                // Search logic
                sql = "select * from book where name like '%"+query+"%' or book_id like '%"+query+"%'";
            }
            ResultSet rs = c.s.executeQuery(sql);
            table.setModel(DbUtils.resultSetToTableModel(rs)); // Magic line!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == print) {
            try {
                table.print(); // Native Printer Dialog
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }
}