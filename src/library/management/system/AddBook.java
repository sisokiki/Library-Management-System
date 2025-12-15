package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import com.toedter.calendar.JDateChooser;

public class AddBook extends JInternalFrame implements ActionListener {

    JTextField tfName, tfAuthor, tfCost;
    JLabel lblSerial;
    JDateChooser dateChooser;
    JButton add, cancel;

    // Auto-generate a random Serial No
    Random ran = new Random();
    long randomNum = Math.abs((ran.nextLong() % 9000L) + 1000L);

    AddBook() {
        super("Add New Book", true, true, true, true);
        setSize(900, 500);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Heading ---
        JLabel heading = new JLabel("Add New Book Details");
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(heading, gbc);

        gbc.gridwidth = 1;

        // --- Row 1: Book ID ---
        JLabel lblS = new JLabel("Book ID:");
        lblS.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblS, gbc);

        lblSerial = new JLabel("BK" + randomNum);
        lblSerial.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblSerial.setForeground(Color.RED);
        gbc.gridx = 1; gbc.gridy = 1;
        add(lblSerial, gbc);

        // --- Row 2: Name ---
        JLabel lblName = new JLabel("Book Name:");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblName, gbc);

        tfName = new JTextField();
        gbc.gridx = 1; gbc.gridy = 2;
        add(tfName, gbc);

        // --- Row 3: Author ---
        JLabel lblAuthor = new JLabel("Author Name:");
        lblAuthor.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3;
        add(lblAuthor, gbc);

        tfAuthor = new JTextField();
        gbc.gridx = 1; gbc.gridy = 3;
        add(tfAuthor, gbc);

        // --- Row 4: Cost ---
        JLabel lblCost = new JLabel("Cost:");
        lblCost.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 4;
        add(lblCost, gbc);

        tfCost = new JTextField();
        gbc.gridx = 1; gbc.gridy = 4;
        add(tfCost, gbc);

        // --- Row 5: Procurement Date ---
        JLabel lblDate = new JLabel("Procurement Date:");
        lblDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 5;
        add(lblDate, gbc);

        dateChooser = new JDateChooser();
        gbc.gridx = 1; gbc.gridy = 5;
        add(dateChooser, gbc);

        // --- Buttons ---
        add = new JButton("Add Book");
        add.addActionListener(this);
        gbc.gridx = 0; gbc.gridy = 6;
        add(add, gbc);

        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        gbc.gridx = 1; gbc.gridy = 6;
        add(cancel, gbc);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            String name = tfName.getText();
            String author = tfAuthor.getText();
            String serial = lblSerial.getText();
            String category = "Book";
            String status = "Available";
            String cost = tfCost.getText();

            String date = "";
            if (dateChooser.getDate() != null) {
                date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText();
            }

            if (name.isEmpty() || author.isEmpty() || cost.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are mandatory!");
                return;
            }

            try {
                Conn c = new Conn();
                String query = "insert into book values('"+serial+"', '"+name+"', '"+author+"', '"+category+"', '"+status+"', '"+cost+"', '"+date+"')";
                c.s.executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Book Details Added Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }
}