import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard {
    private Frame frame;
    private Panel sidebar;
    private Panel content;
    private PieChartPanel pieChartPanel;

    public AdminDashboard() {
        frame = new Frame("Construction Management System - Admin Dashboard");
        frame.setSize(1380, 740);
        frame.setLayout(new BorderLayout());
        frame.setBackground(new Color(240, 242, 245));

        // =============== SIDEBAR ===============
        sidebar = new Panel(new GridLayout(8, 1, 0, 15));
        sidebar.setBackground(new Color(40, 44, 52));
        sidebar.setPreferredSize(new Dimension(220, 700));

        Label title = new Label("Admin Dashboard", Label.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.white);
        sidebar.add(title);

        Button homeBtn = createNavButton(" Home");
        Button worksBtn = createNavButton(" Works");
        Button tasksBtn = createNavButton(" Worker Tasks");
        Button applyBtn = createNavButton(" Worker Applied");
        Button logoutBtn = createNavButton(" Logout");

        sidebar.add(homeBtn);
        sidebar.add(worksBtn);
        sidebar.add(applyBtn);
        sidebar.add(tasksBtn);
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
        tasksBtn.addActionListener(_ -> showWorkerTasks());
        applyBtn.addActionListener(_ -> showApplied());
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

    // =============== WELCOME PAGE ===============
    private void showWelcomePage() {
        content.removeAll();

        Panel container = new Panel(null);
        int containerWidth = 1150;
        int containerHeight = 800;

        Panel mainCard = new Panel(null);
        mainCard.setBounds((1380 - containerWidth) / 15, 30, containerWidth, containerHeight);
        mainCard.setBackground(Color.white);

        Label title = new Label("Welcome, Admin!", Label.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 150, 243));
        title.setBounds(0, 10, containerWidth, 40);
        mainCard.add(title);

        // ===== Stats cards (3 in one row) =====
        int totalWorks = WorkCRUD.getWorksCount();
        int totalTasks = WorkerTasksCRUD.getTasksCount();
        int totalApplied = ApplicationCRUD.getApplicationsCount();

        Panel statsPanel = new Panel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBounds(20, 60, containerWidth - 40, 120);
        statsPanel.setBackground(mainCard.getBackground());

        Panel workCard = createStatCard("Total Works", String.valueOf(totalWorks), new Color(255, 193, 7));
        Panel taskCard = createStatCard("Total Tasks", String.valueOf(totalTasks), new Color(76, 175, 80));
        Panel appliedCard = createStatCard("Total Applied", String.valueOf(totalApplied), new Color(33, 150, 243));

        statsPanel.add(workCard);
        statsPanel.add(taskCard);
        statsPanel.add(appliedCard);
        mainCard.add(statsPanel);

        // ===== Pie Chart Panel =====
        pieChartPanel = new PieChartPanel(totalWorks, totalTasks, totalApplied);
        pieChartPanel.setBounds(20, 220, containerWidth - 40, 350);
        mainCard.add(pieChartPanel);

        // Timer refresh
        Timer timer = new Timer(2000, e -> {
            int works = WorkCRUD.getWorksCount();
            int tasks = WorkerTasksCRUD.getTasksCount();
            int applied = ApplicationCRUD.getApplicationsCount();

            pieChartPanel.updateData(works, tasks, applied);

            ((Label) workCard.getComponent(1)).setText(String.valueOf(works));
            ((Label) taskCard.getComponent(1)).setText(String.valueOf(tasks));
            ((Label) appliedCard.getComponent(1)).setText(String.valueOf(applied));
        });
        timer.start();


        // Footer
        Label footer = new Label("Construction Management System", Label.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        footer.setForeground(new Color(100, 100, 100));
        footer.setBounds(0, 640, containerWidth, 20);
        mainCard.add(footer);

        container.add(mainCard);
        content.add(container);
        content.revalidate();
        content.repaint();
    }

    private Panel createStatCard(String title, String value, Color bgColor) {
        Panel card = new Panel(new BorderLayout());
        card.setBackground(bgColor);
        card.setPreferredSize(new Dimension(180, 120));

        Label titleLabel = new Label(title, Label.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.white);

        Label valueLabel = new Label(value, Label.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(Color.white);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // =============== WORKS PANEL ===============
    private void showWorks() {
        content.removeAll();
        Panel worksPanel = WorkCRUD.getWorkPanel(frame);

        Panel wrapper = new Panel(new BorderLayout());
        wrapper.setBackground(Color.white);
        wrapper.add(worksPanel, BorderLayout.CENTER);

        content.add(wrapper, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    // =============== WORKER TASKS PANEL ===============
    private void showWorkerTasks() {
        content.removeAll();
        Panel taskPanel = WorkerTasksCRUD.getWorkerTasksPanel(frame);

        Panel wrapper = new Panel(new BorderLayout());
        wrapper.setBackground(Color.white);
        wrapper.add(taskPanel, BorderLayout.CENTER);

        content.add(wrapper, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    // =============== APPLICATION PANEL ===============
    private void showApplied() {
        content.removeAll();
        JPanel appPanel = ApplicationCRUD.getApplicationPanel(); // now works (both Swing)
        appPanel.setBackground(Color.white);
        content.add(appPanel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
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
            System.out.println("✅ Logged out.");
        });

        cancelBtn.addActionListener(_ -> dlg.dispose());

        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true);
    }

    // =============== PIE CHART PANEL CLASS ===============
    class PieChartPanel extends Canvas {
        private int works;
        private int tasks;
        private int applied;

        public PieChartPanel(int works, int tasks, int applied) {
            this.works = works;
            this.tasks = tasks;
            this.applied = applied;
            setSize(400, 350);
        }

        public void updateData(int works, int tasks, int applied) {
            this.works = works;
            this.tasks = tasks;
            this.applied = applied;
            repaint();
        }

        public void paint(Graphics g) {
            int total = works + tasks + applied;
            if (total == 0) total = 1;

            int startAngle = 0;

            // Works slice
            int worksAngle = (int) Math.round(((double) works / total) * 360);
            g.setColor(new Color(255, 193, 7));
            g.fillArc(300, 20, 300, 300, startAngle, worksAngle);
            startAngle += worksAngle;

            // Tasks slice
            int tasksAngle = (int) Math.round(((double) tasks / total) * 360);
            g.setColor(new Color(76, 175, 80));
            g.fillArc(300, 20, 300, 300, startAngle, tasksAngle);
            startAngle += tasksAngle;

            // Applied slice
            int appliedAngle = (int) Math.round(((double) applied / total) * 360);
            g.setColor(new Color(33, 150, 243));
            g.fillArc(300, 20, 300, 300, startAngle, appliedAngle);

            // ===== Legend =====
            g.setColor(Color.black);
            g.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            g.drawRect(650, 100, 20, 20);
            g.setColor(new Color(255, 193, 7));
            g.fillRect(650, 100, 20, 20);
            g.setColor(Color.black);
            g.drawString("Works: " + works, 680, 115);

            g.drawRect(650, 140, 20, 20);
            g.setColor(new Color(76, 175, 80));
            g.fillRect(650, 140, 20, 20);
            g.setColor(Color.black);
            g.drawString("Tasks: " + tasks, 680, 155);

            g.drawRect(650, 180, 20, 20);
            g.setColor(new Color(33, 150, 243));
            g.fillRect(650, 180, 20, 20);
            g.setColor(Color.black);
            g.drawString("Applied: " + applied, 680, 195);
        }
    }


    public static void main(String[] args) {
        new AdminDashboard();
    }
}
