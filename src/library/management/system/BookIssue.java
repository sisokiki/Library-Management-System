package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import com.toedter.calendar.JDateChooser;

public class BookIssue extends JInternalFrame implements ActionListener {

    Choice cBookId, cMemberId;
    JTextField tfBookName, tfMemberName;
    JDateChooser dateIssue, dateReturn;
    JButton issue, cancel;

    BookIssue() {
        super("Issue Book", true, true, true, true);
        setSize(900, 500);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Issue Book");
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(heading, gbc);
        gbc.gridwidth = 1;

        // --- Row 1: Book ID ---
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Book ID:"), gbc);
        cBookId = new Choice();
        gbc.gridx = 1; gbc.gridy = 1; add(cBookId, gbc);

        try {
            Conn c = new Conn();
            // Only show Available books
            ResultSet rs = c.s.executeQuery("select * from book where status = 'Available'");
            while(rs.next()) {
                cBookId.add(rs.getString("book_id"));
            }
        } catch (Exception e) { e.printStackTrace(); }

        // --- Row 2: Book Name ---
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Book Name:"), gbc);
        tfBookName = new JTextField(); tfBookName.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 2; add(tfBookName, gbc);

        // --- Row 3: Member ID ---
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Member ID:"), gbc);
        cMemberId = new Choice();
        gbc.gridx = 1; gbc.gridy = 3; add(cMemberId, gbc);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from member");
            while(rs.next()) {
                cMemberId.add(rs.getString("member_id"));
            }
        } catch (Exception e) { e.printStackTrace(); }

        // --- Row 4: Member Name ---
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Member Name:"), gbc);
        tfMemberName = new JTextField(); tfMemberName.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 4; add(tfMemberName, gbc);

        // --- Row 5: Dates ---
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Issue Date:"), gbc);
        dateIssue = new JDateChooser();
        gbc.gridx = 1; gbc.gridy = 5; add(dateIssue, gbc);

        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Return Date:"), gbc);
        dateReturn = new JDateChooser();
        gbc.gridx = 1; gbc.gridy = 6; add(dateReturn, gbc);

        // --- Buttons ---
        issue = new JButton("Issue Book");
        issue.addActionListener(this);
        gbc.gridx = 0; gbc.gridy = 7; add(issue, gbc);

        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        gbc.gridx = 1; gbc.gridy = 7; add(cancel, gbc);

        // --- Listeners & Initial Fetch ---
        cBookId.addItemListener(ie -> getBookDetails());
        cMemberId.addItemListener(ie -> getMemberDetails());

        // Initial load
        getBookDetails();
        getMemberDetails();

        setVisible(true);
    }

    private void getBookDetails() {
        try {
            Conn c = new Conn();
            String query = "select * from book where book_id = '"+cBookId.getSelectedItem()+"'";
            ResultSet rs = c.s.executeQuery(query);
            if(rs.next()) tfBookName.setText(rs.getString("name"));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void getMemberDetails() {
        try {
            Conn c = new Conn();
            String query = "select * from member where member_id = '"+cMemberId.getSelectedItem()+"'";
            ResultSet rs = c.s.executeQuery(query);
            if(rs.next()) tfMemberName.setText(rs.getString("name"));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == issue) {
            String bookID = cBookId.getSelectedItem();
            String memberID = cMemberId.getSelectedItem();
            String bookName = tfBookName.getText();
            String memberName = tfMemberName.getText();

            // 1. Basic Validation
            if (dateIssue.getDate() == null || dateReturn.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Dates are mandatory!");
                return;
            }

            // 2. Date Logic (The Critical Fix)
            LocalDate iDate = dateIssue.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate rDate = dateReturn.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();

            // Rule A: Cannot issue in the past
            if (iDate.isBefore(today)) {
                JOptionPane.showMessageDialog(null, "Issue Date cannot be in the past.");
                return;
            }

            // Rule B: Return date cannot be before Issue date
            if (rDate.isBefore(iDate)) {
                JOptionPane.showMessageDialog(null, "Return Date cannot be before Issue Date.");
                return;
            }

            // Rule C: Cannot be more than 15 days
            long daysBetween = ChronoUnit.DAYS.between(iDate, rDate);
            if (daysBetween > 15) {
                JOptionPane.showMessageDialog(null, "Return Date cannot be more than 15 days from Issue Date.");
                return; // STOP HERE
            }

            // 3. Database Insertion
            try {
                Conn c = new Conn();

                // Get String format for DB
                String sDate = ((JTextField) dateIssue.getDateEditor().getUiComponent()).getText();
                String eDate = ((JTextField) dateReturn.getDateEditor().getUiComponent()).getText();

                String query = "insert into issuebook values('"+bookID+"', '"+memberID+"', '"+bookName+"', '"+memberName+"', '"+sDate+"', '"+eDate+"')";
                c.s.executeUpdate(query);

                // Update Book Status
                c.s.executeUpdate("update book set status = 'Issued' where book_id = '"+bookID+"'");

                JOptionPane.showMessageDialog(null, "Book Issued Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }
}