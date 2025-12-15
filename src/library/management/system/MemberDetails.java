package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class MemberDetails extends JInternalFrame implements ActionListener {

    JTable table;
    JTextField search;
    JButton print, cancel;

    MemberDetails() {
        super("Master List of Members", true, true, true, true);
        setSize(1000, 600);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel l1 = new JLabel("Search by Member ID / Name:");
        l1.setBounds(20, 20, 200, 20);
        l1.setForeground(Color.black);
        add(l1);

        search = new JTextField();
        search.setBounds(220, 20, 200, 20);
        add(search);

        search.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ae) {
                loadData(search.getText());
            }
        });

        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 100, 990, 450);
        add(jsp);

        print = new JButton("Print");
        print.setBounds(120, 60, 100, 30);
        print.addActionListener(this);
        add(print);

        cancel = new JButton("Close");
        cancel.setBounds(240, 60, 100, 30);
        cancel.addActionListener(this);
        add(cancel);

        loadData("");
        setVisible(true);
    }

    void loadData(String query) {
        try {
            Conn c = new Conn();
            String sql;
            if (query.isEmpty()) {
                sql = "select * from member";
            } else {
                sql = "select * from member where name like '%"+query+"%' or member_id like '%"+query+"%'";
            }
            ResultSet rs = c.s.executeQuery(sql);
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }
}