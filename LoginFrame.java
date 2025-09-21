
import java.awt.*;
import java.awt.event.*;

public class LoginFrame {
    private Dialog dialog;
    private TextField usernameField;
    private TextField passwordField;

    public LoginFrame(Frame parent) {
        dialog = new Dialog(parent, "Construction Management System - Login", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());
        dialog.setBackground(new Color(240, 242, 245));

        // ===== CENTER CARD =====
        Panel card = new Panel(new BorderLayout(15, 15));
        card.setBackground(Color.white);
        card.setLayout(new BorderLayout(20, 20));

        // ========== Header =========== 
        Label header = new Label("Login", Label.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setBackground(new Color(33, 150, 243));
        header.setForeground(Color.white);
        card.add(header, BorderLayout.NORTH);

        Panel form = new Panel();
        form.setLayout(new GridLayout(4, 1, 5, 10)); 
        form.setBackground(Color.white);

        // =================== USERNAME ===================
        Label userLabel = new Label("Username (Email):");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameField = new TextField();
        form.add(userLabel);
        form.add(usernameField);

        // =================== PASSWORD ===================
        Label passLabel = new Label("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordField = new TextField();
        passwordField.setEchoChar('*');
        form.add(passLabel);
        form.add(passwordField);

        Panel formWrapper = new Panel(new FlowLayout(FlowLayout.CENTER));
        formWrapper.setBackground(Color.white);
        formWrapper.add(form);
        card.add(formWrapper, BorderLayout.CENTER);

        // ===================== LOGIN BUTTON =====================
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        Button loginBtn = new Button("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setBackground(new Color(33, 150, 243));
        loginBtn.setForeground(Color.white);

        // Hover effect
        loginBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { loginBtn.setBackground(new Color(30, 136, 229)); }
            public void mouseExited(MouseEvent e) { loginBtn.setBackground(new Color(33, 150, 243)); }
        });

        buttonPanel.setBackground(Color.white);
        buttonPanel.add(loginBtn);
        card.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(card, BorderLayout.CENTER);

        // ===== Login listener =====
        loginBtn.addActionListener(_ -> handleLogin());

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dialog.dispose(); }
        });

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    // ===== LOGIN HANDLER =====
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showDialog("⚠ Please fill in all fields!");
            return;
        }

        // ===== Check credentials =====
        if (username.equals("admin@gmail.com") && password.equals("admin")) {
            showDialog("Admin logged in successfully!");
            dialog.dispose();
            new AdminDashboard();
        } else if (username.equals("worker@gmail.com") && password.equals("worker")) {
            showDialog("Worker logged in successfully!");
            dialog.dispose();
            new WorkerDashboard();
        } else {
            showDialog("❌ Invalid username or password!");
        }
    }

    // ===== SHOW DIALOG =====
    private void showDialog(String message) {
        Dialog dlg = new Dialog(dialog, "Login Status", true);
        dlg.setSize(300, 150);
        dlg.setLayout(new FlowLayout());
        dlg.setBackground(Color.white);

        Label msg = new Label(message, Label.CENTER);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dlg.add(msg);

        Button ok = new Button("OK");
        ok.setBackground(new Color(33, 150, 243));
        ok.setForeground(Color.white);
        ok.addActionListener(_ -> dlg.dispose());
        dlg.add(ok);

        dlg.setLocationRelativeTo(dialog);
        dlg.setVisible(true);
    }

    // ====== MAIN ======
    public static void main(String[] args) {
        new LoginFrame(new Frame());
    }
}

// ====== ADMIN DASHBOARD ======
class AdminDashboard extends Frame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(600, 400);
        setLayout(new BorderLayout());

        Label lbl = new Label("Welcome to Admin Dashboard!", Label.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lbl, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        setVisible(true);
    }
}


