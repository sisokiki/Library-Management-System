package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;

public class UpdateBook extends JInternalFrame implements ActionListener {

    JTextField tfName, tfAuthor, tfCost, tfCategory, tfStatus;
    Choice cBookId;
    JDateChooser dateChooser;
    JButton update, cancel;

    UpdateBook() {
        super("Update Book Details", true, true, true, true);
        setSize(900, 500);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Update Book Details");
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(heading, gbc);
        gbc.gridwidth = 1;

        // --- Row 1: Select Book ID ---
        JLabel lblId = new JLabel("Select Book ID:");
        lblId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1; add(lblId, gbc);

        cBookId = new Choice();
        gbc.gridx = 1; gbc.gridy = 1; add(cBookId, gbc);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from book");
            while(rs.next()) {
                cBookId.add(rs.getString("book_id"));
            }
        } catch (Exception e) { e.printStackTrace(); }

        // --- Row 2: Name ---
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Book Name:"), gbc);
        tfName = new JTextField();
        gbc.gridx = 1; gbc.gridy = 2; add(tfName, gbc);

        // --- Row 3: Author ---
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Author Name:"), gbc);
        tfAuthor = new JTextField();
        gbc.gridx = 1; gbc.gridy = 3; add(tfAuthor, gbc);

        // --- Row 4: Cost ---
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Cost:"), gbc);
        tfCost = new JTextField();
        gbc.gridx = 1; gbc.gridy = 4; add(tfCost, gbc);

        // --- Row 5: Date ---
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Procurement Date:"), gbc);
        dateChooser = new JDateChooser();
        gbc.gridx = 1; gbc.gridy = 5; add(dateChooser, gbc);

        // --- Buttons ---
        update = new JButton("Update");
        update.addActionListener(this);
        gbc.gridx = 0; gbc.gridy = 6; add(update, gbc);

        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        gbc.gridx = 1; gbc.gridy = 6; add(cancel, gbc);

        // --- Load Data Listener ---
        cBookId.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                loadBookDetails();
            }
        });

        // Load first item initially
        loadBookDetails();

        setVisible(true);
    }

    private void loadBookDetails() {
        try {
            Conn c = new Conn();
            String query = "select * from book where book_id = '"+cBookId.getSelectedItem()+"'";
            ResultSet rs = c.s.executeQuery(query);
            if(rs.next()) {
                tfName.setText(rs.getString("name"));
                tfAuthor.setText(rs.getString("author"));
                tfCost.setText(rs.getString("cost"));

                // Date Parsing Logic
                String dateStr = rs.getString("date");
                if(dateStr != null && !dateStr.isEmpty()) {
                    try {
                        // Try various formats just in case
                        Date date = new SimpleDateFormat("MMM d, yyyy").parse(dateStr);
                        dateChooser.setDate(date);
                    } catch (Exception e) {
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                            dateChooser.setDate(date);
                        } catch (Exception ex) {} // Ignore parse errors
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == update) {
            String name = tfName.getText();
            String author = tfAuthor.getText();
            String cost = tfCost.getText();
            String date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText();
            String id = cBookId.getSelectedItem();

            try {
                Conn c = new Conn();
                String query = "update book set name='"+name+"', author='"+author+"', cost='"+cost+"', date='"+date+"' where book_id='"+id+"'";
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Book Updated Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }
}