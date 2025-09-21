
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationCRUD {

    // ===== Application Model =====
    public static class Application implements Serializable {
        private String workId, applicantName, contact, message;

        public Application(String workId, String applicantName, String contact, String message) {
            this.workId = workId;
            this.applicantName = applicantName;
            this.contact = contact;
            this.message = message;
        }

        public String getWorkId() { return workId; }
        public String getApplicantName() { return applicantName; }
        public String getContact() { return contact; }
        public String getMessage() { return message; }

        public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
        public void setContact(String contact) { this.contact = contact; }
        public void setMessage(String message) { this.message = message; }
    }

    private static final String FILE_NAME = "applications.dat";
    private static List<Application> applications = new ArrayList<>();

    // ===== Create a JPanel for AdminDashboard =====
    public static JPanel getApplicationPanel() {
        loadApplications();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.white);

        // ===== Table =====
        String[] columns = {"Work ID", "Applicant Name", "Contact", "Message"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ===== Buttons =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.white);
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        styleButton(addBtn, new Color(33, 150, 243));
        styleButton(editBtn, new Color(255, 140, 0));
        styleButton(deleteBtn, new Color(220, 53, 69));

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // ===== Button Actions =====
        refreshTable(tableModel);

        addBtn.addActionListener(_ -> openApplicationPanelDialog(null, tableModel));
        editBtn.addActionListener(_ -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                openApplicationPanelDialog(applications.get(row), tableModel);
            } else {
                JOptionPane.showMessageDialog(panel, "Select an application to edit.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        deleteBtn.addActionListener(_ -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                applications.remove(row);
                saveApplications();
                refreshTable(tableModel);
            } else {
                JOptionPane.showMessageDialog(panel, "Select an application to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // ===== Panel-style Add/Edit Dialog =====
    private static void openApplicationPanelDialog(Application app, DefaultTableModel tableModel) {
        JPanel dialogPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField workIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField messageField = new JTextField();

        if (app != null) {
            workIdField.setText(app.getWorkId());
            workIdField.setEnabled(false);
            nameField.setText(app.getApplicantName());
            contactField.setText(app.getContact());
            messageField.setText(app.getMessage());
        }

        dialogPanel.add(new JLabel("Work ID:"));
        dialogPanel.add(workIdField);
        dialogPanel.add(new JLabel("Applicant Name:"));
        dialogPanel.add(nameField);
        dialogPanel.add(new JLabel("Contact:"));
        dialogPanel.add(contactField);
        dialogPanel.add(new JLabel("Message:"));
        dialogPanel.add(messageField);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel, 
                (app == null ? "Add Application" : "Edit Application"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (workIdField.getText().isEmpty() || nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Work ID and Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (app == null) {
                applications.add(new Application(workIdField.getText(), nameField.getText(), contactField.getText(), messageField.getText()));
            } else {
                app.setApplicantName(nameField.getText());
                app.setContact(contactField.getText());
                app.setMessage(messageField.getText());
            }
            saveApplications();
            refreshTable(tableModel);
        }
    }

    public static void addApplication(Application app) {
        loadApplications();
        applications.add(app);
        saveApplications();
    }


    public static int getApplicationsCount() {
        loadApplications();
        return applications.size();
    }


    // ===== Refresh table =====
    private static void refreshTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Application app : applications) {
            tableModel.addRow(new Object[]{app.getWorkId(), app.getApplicantName(), app.getContact(), app.getMessage()});
        }
    }

    // ===== Persistence =====
    private static void saveApplications() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(applications);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadApplications() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            applications = (List<Application>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Button styling =====
    private static void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
    }
}
