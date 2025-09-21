import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class WorkerDashboard {
    private Frame frame;
    private Panel sidebar;
    private Panel content;

    public WorkerDashboard() {
        frame = new Frame("Construction Management System - Worker Dashboard");
        frame.setSize(1380, 740);
        frame.setLayout(new BorderLayout());
        frame.setBackground(new Color(240, 242, 245));

        // =============== SIDEBAR ===============
        sidebar = new Panel(new GridLayout(6, 1, 0, 15));
        sidebar.setBackground(new Color(40, 44, 52));
        sidebar.setPreferredSize(new Dimension(220, 700));

        Label title = new Label("Worker Dashboard", Label.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.white);
        sidebar.add(title);

        Button homeBtn = createNavButton(" Home");
        Button worksBtn = createNavButton(" Works");
        Button logoutBtn = createNavButton(" Logout");

        sidebar.add(homeBtn);
        sidebar.add(worksBtn);
        sidebar.add(new Label("")); // spacer
        sidebar.add(logoutBtn);

        // =============== CONTENT AREA ===============
        content = new Panel(new BorderLayout(15, 15));
        content.setBackground(new Color(240, 242, 245));
        showWelcomePage();

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(content, BorderLayout.CENTER);

        // =============== EVENTS ===============
        homeBtn.addActionListener(_ -> showWelcomePage());
        worksBtn.addActionListener(_ -> showWorks());
        logoutBtn.addActionListener(_ -> showLogoutConfirmation());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        frame.setVisible(true);
    }

    //=============== BUTTON STYLES ==================
    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.white);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(30, 136, 229)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(33, 150, 243)); }
        });

        return btn;
    }

    private Button createButton(String text, Color bgColor) {
        Button btn = new Button(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.white);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bgColor); }
        });

        return btn;
    }

    // =============== WELCOME PAGE ===============
    private void showWelcomePage() {
        content.removeAll();

        Panel container = new Panel(null);
        int containerWidth = 1150;
        int containerHeight = 700;

        Panel mainCard = new Panel(null);
        mainCard.setBounds((1380 - containerWidth) / 15, 30, containerWidth, containerHeight);
        mainCard.setBackground(Color.white);

        Label title = new Label("Welcome, Worker!", Label.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 150, 243));
        title.setBounds(0, 10, containerWidth, 40);
        mainCard.add(title);

        Label subtitle = new Label("View available works and apply easily.", Label.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setBounds(0, 60, containerWidth, 30);
        mainCard.add(subtitle);

        Label footer = new Label("Construction Management System", Label.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        footer.setForeground(new Color(120, 120, 120));
        footer.setBounds(0, 640, containerWidth, 20);
        mainCard.add(footer);

        container.add(mainCard);
        content.add(container);
        content.revalidate();
        content.repaint();
    }

    // =============== WORKS PAGE (READ-ONLY FOR WORKER) ===============
    private void showWorks() {
        content.removeAll();

        Panel worksPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        worksPanel.setBackground(new Color(240, 242, 245));

        List<WorkCRUD.Work> works = WorkCRUD.getAllWorks();
        for (WorkCRUD.Work w : works) {
            Panel card = new Panel(new BorderLayout());
            card.setBackground(Color.white);
            card.setPreferredSize(new Dimension(250, 220));

            // Title
            Label title = new Label(w.getWorkName(), Label.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 16));
            title.setBackground(new Color(227, 242, 253));
            title.setForeground(new Color(25, 118, 210));
            card.add(title, BorderLayout.NORTH);

            // Details
            Panel details = new Panel(new GridLayout(0, 1));
            details.add(new Label("Owner: " + w.getOwnerName()));
            details.add(new Label("Contact: " + w.getContactNumber()));
            details.add(new Label("Workers: " + w.getWorkerWanted()));
            details.add(new Label("Location: " + w.getLocation()));
            details.add(new Label("End: " + w.getEndDate()));
            details.add(new Label("Status: " + w.getStatus()));
            card.add(details, BorderLayout.CENTER);

            // Actions
            Panel actions = new Panel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            Button viewBtn = createButton("View", new Color(33, 150, 243));
            Button applyBtn = createButton("Apply", new Color(76, 175, 80));
            actions.add(viewBtn);
            actions.add(applyBtn);

            viewBtn.addActionListener(_ -> showWorkDetails(w));
            applyBtn.addActionListener(_ -> openApplicationForm(w));

            card.add(actions, BorderLayout.SOUTH);

            worksPanel.add(card);
        }

        ScrollPane scroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scroll.add(worksPanel);

        content.add(scroll, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    // Worker views work details
    private void showWorkDetails(WorkCRUD.Work w) {
        String info = "Work Name: " + w.getWorkName() +
                "\nOwner: " + w.getOwnerName() +
                "\nContact: " + w.getContactNumber() +
                "\nWorkers Wanted: " + w.getWorkerWanted() +
                "\nLocation: " + w.getLocation() +
                "\nDate Created: " + w.getDateCreated() +
                "\nEnd Date: " + w.getEndDate() +
                "\nStatus: " + w.getStatus();

        Dialog dlg = new Dialog(frame, "Work Details", true);
        dlg.setSize(350, 300);
        dlg.setLayout(new BorderLayout());
        TextArea txt = new TextArea(info);
        txt.setEditable(false);
        dlg.add(txt, BorderLayout.CENTER);
        Button ok = createButton("Close", new Color(25, 118, 210));
        ok.addActionListener(_ -> dlg.dispose());
        dlg.add(ok, BorderLayout.SOUTH);
        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true);
    }

    // =============== APPLICATION FORM ===============
    private void openApplicationForm(WorkCRUD.Work w) {
        Dialog dlg = new Dialog(frame, "Apply for " + w.getWorkName(), true);
        dlg.setSize(400, 300);
        dlg.setLayout(new GridLayout(5, 2, 10, 10));

        Label nameLbl = new Label("Name:");
        TextField nameField = new TextField();
        Label contactLbl = new Label("Contact:");
        TextField contactField = new TextField();
        Label messageLbl = new Label("Message:");
        TextField messageField = new TextField();

        Button submitBtn = createButton("Submit", new Color(76, 175, 80));
        Button cancelBtn = createButton("Cancel", new Color(244, 67, 54));

        dlg.add(nameLbl); dlg.add(nameField);
        dlg.add(contactLbl); dlg.add(contactField);
        dlg.add(messageLbl); dlg.add(messageField);
        dlg.add(submitBtn); dlg.add(cancelBtn);

        submitBtn.addActionListener(_ -> {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String message = messageField.getText().trim();

            if (name.isEmpty() || contact.isEmpty()) {
                showMessage("Name and Contact are required!");
                return;
            }

            // Save application to ApplicationCRUD
            ApplicationCRUD.Application app = new ApplicationCRUD.Application(
                    w.getWorkName(), name, contact, message
            );
            ApplicationCRUD.addApplication(app);

            showMessage("Application submitted successfully!");
            dlg.dispose();
        });

        cancelBtn.addActionListener(_ -> dlg.dispose());

        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true);
    }

    private void showMessage(String msg) {
        Dialog dlg = new Dialog(frame, "Message", true);
        dlg.setSize(300, 150);
        dlg.setLayout(new BorderLayout());
        Label lbl = new Label(msg, Label.CENTER);
        dlg.add(lbl, BorderLayout.CENTER);
        Button ok = createButton("OK", new Color(33, 150, 243));
        ok.addActionListener(_ -> dlg.dispose());
        dlg.add(ok, BorderLayout.SOUTH);
        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true);
    }

    // =============== LOGOUT CONFIRMATION ===============
    private void showLogoutConfirmation() {
        Dialog dlg = new Dialog(frame, "Confirm Logout", true);
        dlg.setSize(350, 180);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setBackground(Color.white);

        Label message = new Label("Are you sure you want to logout?", Label.CENTER);
        message.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dlg.add(message, BorderLayout.CENTER);

        Panel buttonPanel = new Panel(new FlowLayout());
        Button yesBtn = new Button("Yes");
        Button cancelBtn = new Button("Cancel");

        yesBtn.setBackground(new Color(244, 67, 54));
        yesBtn.setForeground(Color.white);
        cancelBtn.setBackground(new Color(158, 158, 158));
        cancelBtn.setForeground(Color.white);

        buttonPanel.add(yesBtn);
        buttonPanel.add(cancelBtn);
        dlg.add(buttonPanel, BorderLayout.SOUTH);

        yesBtn.addActionListener(_ -> {
            dlg.dispose();
            frame.dispose();
            System.out.println("✅ Worker logged out.");
        });

        cancelBtn.addActionListener(_ -> dlg.dispose());

        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true);
    }

    public static void main(String[] args) {
        new WorkerDashboard();
    }
}
