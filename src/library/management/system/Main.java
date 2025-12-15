package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class Main extends JFrame implements ActionListener {

    public static JDesktopPane desktop;
    private String userRole;

    public Main(String role) {
        super("Library Management System");

        this.userRole = (role != null) ? role.trim() : "user";
        this.setTitle("Library Management System - Role: " + this.userRole);

        setSize(1540, 850);
        setLocationRelativeTo(null); // Center it on screen

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // --- Background Image ---
        desktop = new JDesktopPane() {
            URL imgUrl = ClassLoader.getSystemResource("icons/library_back.jpg");
            Image bgImage = (imgUrl != null) ? new ImageIcon(imgUrl).getImage() : null;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        add(desktop);

        // --- Menu Bar ---
        JMenuBar mb = new JMenuBar();
        setJMenuBar(mb);

        // === MENU 1: MAINTENANCE (ADMIN ONLY) ===
        if (this.userRole.equalsIgnoreCase("admin")) {
            JMenu maintenance = new JMenu("Maintenance");
            maintenance.setForeground(Color.BLUE);
            mb.add(maintenance);

            addMenuItem(maintenance, "Add New Book");
            addMenuItem(maintenance, "Add New Member");
        }

        // === MENU 2: TRANSACTIONS ===
        JMenu transactions = new JMenu("Transactions");
        transactions.setForeground(Color.RED);
        mb.add(transactions);

        addMenuItem(transactions, "Issue Book");
        addMenuItem(transactions, "Return Book");
        addMenuItem(transactions, "Pay Fine");

        // === MENU 3: REPORTS ===
        JMenu reports = new JMenu("Reports");
        reports.setForeground(Color.BLUE);
        mb.add(reports);

        addMenuItem(reports, "View Book Details");
        addMenuItem(reports, "View Member Details");

        // === MENU 4: EXIT ===
        JMenu exit = new JMenu("Exit");
        exit.setForeground(Color.RED);
        mb.add(exit);

        addMenuItem(exit, "Logout");

        setVisible(true);
    }

    private void addMenuItem(JMenu menu, String name) {
        JMenuItem item = new JMenuItem(name);
        item.setBackground(Color.WHITE);
        item.addActionListener(this);
        menu.add(item);
    }

    public void actionPerformed(ActionEvent ae) {
        String msg = ae.getActionCommand();

        if (msg.equals("Logout")) {
            setVisible(false);
            new Login();
        }
        else if (msg.equals("Add New Book")) openFrame(new AddBook());
        else if (msg.equals("Add New Member")) openFrame(new AddMember());
        else if (msg.equals("Issue Book")) openFrame(new BookIssue());
        else if (msg.equals("Return Book")) openFrame(new ReturnBook());
        else if (msg.equals("Pay Fine")) openFrame(new PayFine());
        else if (msg.equals("View Book Details")) openFrame(new BookDetails());
        else if (msg.equals("View Member Details")) openFrame(new MemberDetails());
    }

    private void openFrame(JInternalFrame frame) {
        desktop.add(frame);
        Dimension desktopSize = desktop.getSize();
        Dimension frameSize = frame.getSize();
        // Centering Logic
        frame.setLocation((desktopSize.width - frameSize.width)/2, (desktopSize.height - frameSize.height)/2);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}