package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import com.toedter.calendar.JDateChooser;

public class AddMember extends JInternalFrame implements ActionListener {

    JTextField tfName, tfFather, tfAddress, tfPhone, tfEmail, tfAadhar;
    JLabel lblMemberId;
    JDateChooser dateChooser;
    JButton add, cancel;

    // Auto-generate a random Member ID
    Random ran = new Random();
    long randomNum = Math.abs((ran.nextLong() % 9000L) + 1000L);

    AddMember() {
        super("Add New Member", true, true, true, true);
        setSize(900, 500);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Heading ---
        JLabel heading = new JLabel("Add New Member Details");
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(heading, gbc);

        gbc.gridwidth = 1; // Reset gridwidth

        // --- Row 1: Member ID ---
        JLabel lblId = new JLabel("Member ID:");
        lblId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblId, gbc);

        lblMemberId = new JLabel("MEM" + randomNum);
        lblMemberId.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblMemberId.setForeground(Color.RED);
        gbc.gridx = 1; gbc.gridy = 1;
        add(lblMemberId, gbc);

        // --- Row 2: Name ---
        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblName, gbc);

        tfName = new JTextField();
        gbc.gridx = 1; gbc.gridy = 2;
        add(tfName, gbc);

        // --- Row 3: Father's Name ---
        JLabel lblFather = new JLabel("Father's Name:");
        lblFather.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3;
        add(lblFather, gbc);

        tfFather = new JTextField();
        gbc.gridx = 1; gbc.gridy = 3;
        add(tfFather, gbc);

        // --- Row 4: Date of Birth ---
        JLabel lblDob = new JLabel("Date of Birth:");
        lblDob.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 4;
        add(lblDob, gbc);

        dateChooser = new JDateChooser();
        gbc.gridx = 1; gbc.gridy = 4;
        add(dateChooser, gbc);

        // --- Row 5: Phone ---
        JLabel lblPhone = new JLabel("Phone No:");
        lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 5;
        add(lblPhone, gbc);

        tfPhone = new JTextField();
        gbc.gridx = 1; gbc.gridy = 5;
        add(tfPhone, gbc);

        // --- Row 6: Email ---
        JLabel lblEmail = new JLabel("Email ID:");
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 6;
        add(lblEmail, gbc);

        tfEmail = new JTextField();
        gbc.gridx = 1; gbc.gridy = 6;
        add(tfEmail, gbc);

        // --- Row 7: Address ---
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 7;
        add(lblAddress, gbc);

        tfAddress = new JTextField();
        gbc.gridx = 1; gbc.gridy = 7;
        add(tfAddress, gbc);

        // --- Row 8: Aadhar ---
        JLabel lblAadhar = new JLabel("Aadhar No:");
        lblAadhar.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 8;
        add(lblAadhar, gbc);

        tfAadhar = new JTextField();
        gbc.gridx = 1; gbc.gridy = 8;
        add(tfAadhar, gbc);

        // --- Buttons ---
        add = new JButton("Add Member");
        add.addActionListener(this);
        gbc.gridx = 0; gbc.gridy = 9;
        add(add, gbc);

        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        gbc.gridx = 1; gbc.gridy = 9;
        add(cancel, gbc);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            String id = lblMemberId.getText();
            String name = tfName.getText();
            String father = tfFather.getText();
            String dob = "";
            if (dateChooser.getDate() != null) {
                dob = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText();
            }
            String phone = tfPhone.getText();
            String email = tfEmail.getText();
            String address = tfAddress.getText();
            String aadhar = tfAadhar.getText();

            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Name and Phone are mandatory!");
                return;
            }

            try {
                Conn c = new Conn();
                // Matches the table: member_id, name, father_name, dob, address, phone, email, aadhar
                String query = "insert into member values('"+id+"', '"+name+"', '"+father+"', '"+dob+"', '"+address+"', '"+phone+"', '"+email+"', '"+aadhar+"')";
                c.s.executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Member Added Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }
}