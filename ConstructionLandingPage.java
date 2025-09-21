
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConstructionLandingPage extends JFrame {

    private JButton loginButton;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel description;

    public ConstructionLandingPage() {
        // ===== Frame setup =====
        setTitle("Construction Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== Gradient background =====
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(36, 59, 85),
                        getWidth(), getHeight(), new Color(58, 213, 176)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        // ===== Card container (white panel with shadow) =====
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(new Color(255, 255, 255, 220));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(40, 60, 40, 60),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== Title =====
        titleLabel = new JLabel("Construction Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(new Color(33, 150, 243));
        gbc.gridx = 0;
        gbc.gridy = 0;
        cardPanel.add(titleLabel, gbc);

        // ===== Subtitle =====
        subtitleLabel = new JLabel("Manage projects, workers, and resources with ease", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        subtitleLabel.setForeground(new Color(70, 70, 70));
        gbc.gridy = 1;
        cardPanel.add(subtitleLabel, gbc);

        // ===== Description (HTML for formatting) =====
        description = new JLabel(
                "<html><div style='text-align: center; font-size: 16px; color: #333;'>" +
                        "Welcome to the Construction Management System!<br><br>" +
                        "✔ Assign and track workers<br>" +
                        "✔ Manage project timelines<br>" +
                        "✔ Monitor resources and materials<br>" +
                        "✔ Generate reports for better decision making<br><br>" +
                        "Get started by logging in below." +
                        "</div></html>",
                SwingConstants.CENTER
        );
        gbc.gridy = 2;
        cardPanel.add(description, gbc);

        // ===== Login Button =====
        loginButton = new JButton("Sign in");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        loginButton.setBackground(new Color(255, 140, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(255, 120, 0));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(255, 140, 0));
            }
        });

        loginButton.addActionListener(_ -> new LoginFrame(this));

        gbc.gridy = 3;
        cardPanel.add(loginButton, gbc);

        // ===== Add card to background =====
        backgroundPanel.add(cardPanel);

        add(backgroundPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConstructionLandingPage::new);
    }
}
