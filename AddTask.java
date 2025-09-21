
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

public class AddTask {

    // ====== Inner Task class (replaces Task.java) ======
    public static class Task implements Serializable {
        private String id;
        private String name;
        private String worker;
        private String status;

        public Task(String id, String name, String worker, String status) {
            this.id = id;
            this.name = name;
            this.worker = worker;
            this.status = status;
        }

        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getWorker() { return worker; }
        public String getStatus() { return status; }

        // Setters
        public void setName(String name) { this.name = name; }
        public void setWorker(String worker) { this.worker = worker; }
        public void setStatus(String status) { this.status = status; }
    }

    // ====== Show Add Task Dialog ======
    public static void showAddWorkDialog(Frame parentFrame) {
        Dialog dlg = new Dialog(parentFrame, "Add Task", true);
        dlg.setSize(400, 220);
        dlg.setLayout(new BorderLayout());

        Label header = new Label("Add New Task", Label.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBackground(new Color(33, 150, 243));
        header.setForeground(Color.white);
        dlg.add(header, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(2, 2, 10, 10));
        form.setBackground(Color.white);

        TextField taskName = new TextField();
        TextField worker = new TextField();

        form.add(new Label("Task Name:")); form.add(taskName);
        form.add(new Label("Worker:")); form.add(worker);

        dlg.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(new FlowLayout());
        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        saveBtn.setBackground(new Color(76, 175, 80));
        saveBtn.setForeground(Color.white);
        cancelBtn.setBackground(new Color(244, 67, 54));
        cancelBtn.setForeground(Color.white);

        buttons.add(saveBtn); buttons.add(cancelBtn);
        dlg.add(buttons, BorderLayout.SOUTH);

        saveBtn.addActionListener(_ -> {
            if (taskName.getText().trim().isEmpty()) {
                showMessage(parentFrame, "Please enter a task name!");
                return;
            }

            WorkerTasksCRUD.addTask(taskName.getText().trim(), worker.getText().trim());

            dlg.dispose();
        });


        cancelBtn.addActionListener(_ -> dlg.dispose());

        dlg.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dlg.dispose(); }
        });

        dlg.setLocationRelativeTo(parentFrame);
        dlg.setVisible(true);
    }

    private static void showMessage(Frame parentFrame, String message) {
        Dialog msg = new Dialog(parentFrame, "Notice", true);
        msg.setSize(300, 150);
        msg.setLayout(new FlowLayout());
        msg.add(new Label(message));
        Button ok = new Button("OK");
        ok.addActionListener(_ -> msg.dispose());
        msg.add(ok);
        msg.setLocationRelativeTo(parentFrame);
        msg.setVisible(true);
    }
}
