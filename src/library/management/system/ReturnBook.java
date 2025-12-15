package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReturnBook extends JInternalFrame implements ActionListener {

    Choice cBookId;
    JTextField tfBookName, tfMemberId, tfIssueDate, tfReturnDate;
    JButton nextBtn, cancel;

    ReturnBook() {
        super("Return Book", true, true, true, true);
        setSize(800, 500);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Return Book (Step 1)");
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(heading, gbc);
        gbc.gridwidth = 1;

        // Select Issued Book
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Select Book ID:"), gbc);
        cBookId = new Choice();
        gbc.gridx = 1; gbc.gridy = 1; add(cBookId, gbc);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from issuebook");
            while(rs.next()) cBookId.add(rs.getString("book_id"));
        } catch (Exception e) { e.printStackTrace(); }

        // Auto-Populated Fields
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Book Name:"), gbc);
        tfBookName = new JTextField(); tfBookName.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 2; add(tfBookName, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Member ID:"), gbc);
        tfMemberId = new JTextField(); tfMemberId.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 3; add(tfMemberId, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Original Return Date:"), gbc);
        tfReturnDate = new JTextField(); tfReturnDate.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 4; add(tfReturnDate, gbc);

        nextBtn = new JButton("Confirm & Pay Fine");
        nextBtn.addActionListener(this);
        gbc.gridx = 0; gbc.gridy = 5; add(nextBtn, gbc);

        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        gbc.gridx = 1; gbc.gridy = 5; add(cancel, gbc);

        // Listener & Initial Call ---
        cBookId.addItemListener(ie -> loadIssueDetails());

        // Load details for the first item immediately
        loadIssueDetails();

        setVisible(true);
    }

    private void loadIssueDetails() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from issuebook where book_id = '"+cBookId.getSelectedItem()+"'");
            if(rs.next()) {
                tfBookName.setText(rs.getString("book_name"));
                tfMemberId.setText(rs.getString("member_id"));
                tfReturnDate.setText(rs.getString("return_date"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == nextBtn) {
            String bookID = cBookId.getSelectedItem();
            String memberID = tfMemberId.getText();

            if(bookID == null || memberID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No Book Selected!");
                return;
            }

            setVisible(false);
            PayFine fineFrame = new PayFine(bookID, memberID);
            Main.desktop.add(fineFrame);
            fineFrame.setVisible(true);
        } else {
            setVisible(false);
        }
    }
}