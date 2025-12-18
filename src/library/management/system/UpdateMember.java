package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;

public class UpdateMember extends JInternalFrame implements ActionListener {

    JTextField tfName, tfFather, tfAddress, tfPhone, tfEmail, tfAadhar;
    Choice cMemberId;
    JDateChooser dateChooser;
    JButton update, cancel;

    UpdateMember() {
        super("Update Member Details", true, true, true, true);
        setSize(900, 500);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Update Member Details");
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(heading, gbc);
        gbc.gridwidth = 1;

        // --- Row 1: Member ID ---
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Select Member ID:"), gbc);
        cMemberId = new Choice();
        gbc.gridx = 1; gbc.gridy = 1; add(cMemberId, gbc);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from member");
            while(rs.next()) {
                cMemberId.add(rs.getString("member_id"));
            }
        } catch (Exception e) { e.printStackTrace(); }

        // --- Row 2: Name ---
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Name:"), gbc);
        tfName = new JTextField();
        gbc.gridx = 1; gbc.gridy = 2; add(tfName, gbc);

        // --- Row 3: Father's Name ---
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Father's Name:"), gbc);
        tfFather = new JTextField();
        gbc.gridx = 1; gbc.gridy = 3; add(tfFather, gbc);

        // --- Row 4: DOB ---
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Date of Birth:"), gbc);
        dateChooser = new JDateChooser();
        gbc.gridx = 1; gbc.gridy = 4; add(dateChooser, gbc);

        // --- Row 5: Phone ---
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Phone:"), gbc);
        tfPhone = new JTextField();
        gbc.gridx = 1; gbc.gridy = 5; add(tfPhone, gbc);

        // --- Row 6: Email ---
        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Email:"), gbc);
        tfEmail = new JTextField();
        gbc.gridx = 1; gbc.gridy = 6; add(tfEmail, gbc);

        // --- Row 7: Address ---
        gbc.gridx = 0; gbc.gridy = 7; add(new JLabel("Address:"), gbc);
        tfAddress = new JTextField();
        gbc.gridx = 1; gbc.gridy = 7; add(tfAddress, gbc);

        // --- Row 8: Aadhar ---
        gbc.gridx = 0; gbc.gridy = 8; add(new JLabel("Aadhar No:"), gbc);
        tfAadhar = new JTextField();
        gbc.gridx = 1; gbc.gridy = 8; add(tfAadhar, gbc);

        // --- Buttons ---
        update = new JButton("Update Details");
        update.addActionListener(this);
        gbc.gridx = 0; gbc.gridy = 9; add(update, gbc);

        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        gbc.gridx = 1; gbc.gridy = 9; add(cancel, gbc);

        // --- Load Data Listener ---
        cMemberId.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                loadMemberDetails();
            }
        });

        loadMemberDetails();
        setVisible(true);
    }

    private void loadMemberDetails() {
        try {
            Conn c = new Conn();
            String query = "select * from member where member_id = '"+cMemberId.getSelectedItem()+"'";
            ResultSet rs = c.s.executeQuery(query);
            if(rs.next()) {
                tfName.setText(rs.getString("name"));
                tfFather.setText(rs.getString("father_name"));
                tfAddress.setText(rs.getString("address"));
                tfPhone.setText(rs.getString("phone"));
                tfEmail.setText(rs.getString("email"));
                tfAadhar.setText(rs.getString("aadhar"));

                // Date Parsing
                String dob = rs.getString("dob");
                if(dob != null && !dob.isEmpty()) {
                    try {
                        Date date = new SimpleDateFormat("MMM d, yyyy").parse(dob);
                        dateChooser.setDate(date);
                    } catch (Exception e) {
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
                            dateChooser.setDate(date);
                        } catch(Exception ex) {}
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == update) {
            String id = cMemberId.getSelectedItem();
            String name = tfName.getText();
            String father = tfFather.getText();
            String address = tfAddress.getText();
            String phone = tfPhone.getText();
            String email = tfEmail.getText();
            String aadhar = tfAadhar.getText();
            String dob = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText();

            try {
                Conn c = new Conn();
                String query = "update member set name='"+name+"', father_name='"+father+"', address='"+address+"', phone='"+phone+"', email='"+email+"', dob='"+dob+"', aadhar='"+aadhar+"' where member_id='"+id+"'";
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Member Details Updated Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }
}