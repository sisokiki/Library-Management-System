package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class PayFine extends JInternalFrame implements ActionListener {

    Choice cBookId;
    JTextField tfBookId, tfMemberId, tfBookName, tfMemberName, tfDueDate, tfFine, tfTotalDays;
    JCheckBox chkPaid;
    JButton payBtn, cancel;
    String currentBookId = "";

    public PayFine() {
        this("", "");
    }

    public PayFine(String bookId, String memberId) {
        super("Pay Fine & Finalize", true, true, true, true);
        this.currentBookId = bookId;

        setSize(800, 500);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Pay Fine & Return Book");
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(heading, gbc);
        gbc.gridwidth = 1;

        // Row 1: Book Selection
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Select Book ID:"), gbc);

        if (bookId.isEmpty()) {
            cBookId = new Choice();
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery("select * from issuebook");
                while(rs.next()) cBookId.add(rs.getString("book_id"));
            } catch (Exception e) { e.printStackTrace(); }

            cBookId.addItemListener(ie -> {
                currentBookId = cBookId.getSelectedItem();
                fetchDetails(currentBookId);
            });
            gbc.gridx = 1; gbc.gridy = 1; add(cBookId, gbc);

            // Set initial ID
            if (cBookId.getItemCount() > 0) currentBookId = cBookId.getSelectedItem();
        } else {
            // FIXED: Variable name is strictly tfBookId now
            tfBookId = new JTextField(bookId);
            tfBookId.setEditable(false);
            gbc.gridx = 1; gbc.gridy = 1; add(tfBookId, gbc);
        }

        // Row 2: Book Name
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Book Name:"), gbc);
        tfBookName = new JTextField(); tfBookName.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 2; add(tfBookName, gbc);

        // Row 3: Member Name
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Member Name:"), gbc);
        tfMemberName = new JTextField(); tfMemberName.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 3; add(tfMemberName, gbc);

        // Row 4: Member ID
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Member ID:"), gbc);
        tfMemberId = new JTextField(memberId); tfMemberId.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 4; add(tfMemberId, gbc);

        // Row 5: Due Date
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Return Due Date:"), gbc);
        tfDueDate = new JTextField(); tfDueDate.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 5; add(tfDueDate, gbc);

        // Row 6: Overdue Days
        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Overdue Days:"), gbc);
        tfTotalDays = new JTextField("0"); tfTotalDays.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 6; add(tfTotalDays, gbc);

        // Row 7: Fine Amount
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel lblFine = new JLabel("Fine Amount (Rs. 10/day):");
        lblFine.setForeground(Color.RED);
        add(lblFine, gbc);

        tfFine = new JTextField("0"); tfFine.setEditable(false);
        tfFine.setForeground(Color.RED); tfFine.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 1; gbc.gridy = 7; add(tfFine, gbc);

        // Row 8: Checkbox
        chkPaid = new JCheckBox("Fine Paid");
        gbc.gridx = 1; gbc.gridy = 8; add(chkPaid, gbc);

        // Row 9: Buttons
        payBtn = new JButton("Complete Return");
        payBtn.addActionListener(this);
        gbc.gridx = 0; gbc.gridy = 9; add(payBtn, gbc);

        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        gbc.gridx = 1; gbc.gridy = 9; add(cancel, gbc);

        // Load details immediately
        if (!currentBookId.isEmpty()) {
            fetchDetails(currentBookId);
        } else if (cBookId != null && cBookId.getItemCount() > 0) {
            currentBookId = cBookId.getSelectedItem();
            fetchDetails(currentBookId);
        }

        setVisible(true);
    }

    private void fetchDetails(String bookId) {
        try {
            Conn c = new Conn();
            String query = "select * from issuebook where book_id = '"+bookId+"'";
            ResultSet rs = c.s.executeQuery(query);
            if(rs.next()) {
                tfBookName.setText(rs.getString("book_name"));
                tfMemberName.setText(rs.getString("member_name"));
                if(tfMemberId.getText().isEmpty()) tfMemberId.setText(rs.getString("member_id"));

                String dueStr = rs.getString("return_date");
                tfDueDate.setText(dueStr);

                calculateFine(dueStr);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void calculateFine(String dueDateStr) {
        try {
            LocalDate dueDate = null;
            try {
                DateTimeFormatter f1 = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
                dueDate = LocalDate.parse(dueDateStr, f1);
            } catch (Exception e) {
                try {
                    dueDate = LocalDate.parse(dueDateStr);
                } catch (Exception ex) {
                    System.out.println("Date Parse Error: " + dueDateStr);
                    return;
                }
            }

            LocalDate today = LocalDate.now();
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);

            if (daysOverdue > 0) {
                tfTotalDays.setText(String.valueOf(daysOverdue));
                tfFine.setText(String.valueOf(daysOverdue * 10));
            } else {
                tfTotalDays.setText("0");
                tfFine.setText("0");
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == payBtn) {
            int fine = 0;
            try { fine = Integer.parseInt(tfFine.getText()); } catch(Exception e){}

            if (fine > 0 && !chkPaid.isSelected()) {
                JOptionPane.showMessageDialog(null, "Fine Pending! Collect Rs." + fine + " and check box.");
                return;
            }

            try {
                Conn c = new Conn();
                String date = LocalDate.now().toString();
                String mID = tfMemberId.getText();

                // 1. Insert History
                String query = "insert into returnbook (book_id, member_id, return_date, fine) values('"+currentBookId+"', '"+mID+"', '"+date+"', '"+fine+"')";
                c.s.executeUpdate(query);

                // 2. Delete Issue
                c.s.executeUpdate("delete from issuebook where book_id = '"+currentBookId+"'");

                // 3. Update Book Status
                c.s.executeUpdate("update book set status = 'Available' where book_id = '"+currentBookId+"'");

                JOptionPane.showMessageDialog(null, "Transaction Completed Successfully.");
                setVisible(false);
            } catch (Exception e) { e.printStackTrace(); }
        } else {
            setVisible(false);
        }
    }
}