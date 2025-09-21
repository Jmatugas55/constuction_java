
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkerTasksCRUD {

    // ====== Task storage ======
    private static final String FILE_NAME = "tasks.dat";
    private static final List<AddTask.Task> tasks = new ArrayList<>();
    private static Panel table;  // table panel to refresh
    private static Frame parentRef;
    private static ScrollPane scrollPane;

    public static Panel getWorkerTasksPanel(Frame parentFrame) {
        parentRef = parentFrame;
        loadTasks(); // load from file

        Panel mainPanel = new Panel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // =============== HEADER ===============
        Label header = new Label("Worker tasks records", Label.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBackground(new Color(33, 150, 243));
        header.setForeground(Color.white);
        mainPanel.add(header, BorderLayout.NORTH);

        // =============== SEARCH + ADD TASK PANEL ===============
        Panel searchPanel = new Panel(new BorderLayout());
        searchPanel.setBackground(new Color(230, 230, 230));

        // Left side (search)
        Panel leftPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        Label searchLabel = new Label("Search Task:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        TextField searchField = new TextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        Button searchBtn = createButton("Search", new Color(76, 175, 80));

        leftPanel.add(searchLabel);
        leftPanel.add(searchField);
        leftPanel.add(searchBtn);

        // Right side (Add Task button)
        Panel rightPanel = new Panel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        Button addTaskBtn = createButton("Add Task", new Color(33, 150, 243));
        rightPanel.add(addTaskBtn);

        searchPanel.add(leftPanel, BorderLayout.WEST);
        searchPanel.add(rightPanel, BorderLayout.EAST);

        // =============== TABLE PANEL (Scrollable) ===============
        table = new Panel(new GridLayout(0, 5, 2, 2));
        table.setBackground(new Color(240, 240, 240));

        // Wrap table in a container
        Panel tableContainer = new Panel(new BorderLayout());
        tableContainer.add(table, BorderLayout.CENTER);

        // Give tableContainer a default size (will expand with rows)
        tableContainer.setPreferredSize(new Dimension(800, 400));

        // Add scrollbars if needed
        scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollPane.add(tableContainer);
        scrollPane.setPreferredSize(new Dimension(700, 350)); // visible window size

        refreshTable();

        // =============== FOOTER ===============
        Label footer = new Label("© 2025 Construction Management System", Label.CENTER);
        footer.setBackground(new Color(200, 200, 200));
        footer.setFont(new Font("Arial", Font.PLAIN, 12));

        Panel centerPanel = new Panel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        // ====== SEARCH BUTTON EVENT ======
        searchBtn.addActionListener(_ -> {
            String input = searchField.getText().trim();
            if (input.isEmpty()) {
                showMessage(parentFrame, "Search Error", "Please enter a task to search.", 300, 200);
                return;
            }

            boolean found = false;
            for (AddTask.Task row : tasks) {
                if (row.getName().equalsIgnoreCase(input)) {
                    found = true;
                    showDetails(parentFrame, row);
                    break;
                }
            }

            if (!found) {
                showMessage(parentFrame, "Task Not Found", "No task found with name: " + input, 350, 150);
            }
        });

        // ====== ADD TASK BUTTON EVENT ======
        addTaskBtn.addActionListener(_ -> AddTask.showAddWorkDialog(parentFrame));

        return mainPanel;
    }

    // ====== Add task to storage ======
    public static void addTask(String taskName, String worker) {
        // Check duplicate by name (case-insensitive)
        for (AddTask.Task existing : tasks) {
            if (existing.getName().equalsIgnoreCase(taskName)) {
                showMessage(parentRef, "Duplicate Task", 
                    "Task with name \"" + taskName + "\" already exists.", 350, 150);
                return; // do not add
            }
        }

        String id = String.valueOf(tasks.size() + 1);
        AddTask.Task task = new AddTask.Task(id, taskName, worker, "Ongoing");
        tasks.add(task);
        saveTasks();
        refreshTable();
    }

    // ====== Refresh table ======
    private static void refreshTable() {
        table.removeAll();

        // Headers
        String[] cols = {"Task ID", "Task Name", "Worker", "Status", "Actions"};
        for (String col : cols) {
            Label lbl = new Label(col, Label.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            lbl.setBackground(new Color(55, 71, 79));
            lbl.setForeground(Color.white);
            table.add(lbl);
        }

        // Rows
        for (AddTask.Task row : tasks) {
            table.add(new Label(row.getId(), Label.CENTER));
            table.add(new Label(row.getName(), Label.CENTER));
            table.add(new Label(row.getWorker(), Label.CENTER));
            table.add(new Label(row.getStatus(), Label.CENTER));

            Panel actions = new Panel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            actions.setBackground(Color.white);

            Button view = createButton("View", new Color(33, 150, 243));
            Button edit = createButton("Edit", new Color(255, 193, 7));
            Button process = createButton("Process", new Color(255, 152, 0));
            Button complete = createButton("Complete", new Color(76, 175, 80));
            Button delete = createButton("Delete", new Color(244, 67, 54));

            actions.add(view);
            actions.add(edit);
            actions.add(process);
            actions.add(complete);
            actions.add(delete);

            // ====== Actions ======
            view.addActionListener(_ -> showDetails(parentRef, row));
            edit.addActionListener(_ -> editTask(parentRef, row));
            process.addActionListener(_ -> {
                row.setStatus("Processing");
                saveTasks();
                refreshTable();
            });
            complete.addActionListener(_ -> {
                row.setStatus("Completed");
                saveTasks();
                refreshTable();
            });
            delete.addActionListener(_ -> {
                tasks.remove(row);
                saveTasks();
                refreshTable();
            });

            table.add(actions);
        }

        // Expand container size if rows exceed visible height
        int rowHeight = 40;
        int totalHeight = (tasks.size() + 1) * rowHeight;
        table.getParent().setPreferredSize(new Dimension(800, Math.max(400, totalHeight)));

        table.revalidate();
        table.repaint();
    }

    // ====== Helpers ======
    private static Button createButton(String text, Color bg) {
        Button btn = new Button(text);
        btn.setBackground(bg);
        btn.setForeground(Color.white);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        return btn;
    }

    private static void showMessage(Frame frame, String title, String msg, int w, int h) {
        Dialog dlg = new Dialog(frame, title, true);
        dlg.setSize(w, h);
        dlg.setLayout(new FlowLayout());
        dlg.add(new Label(msg));
        Button ok = new Button("OK");
        ok.addActionListener(_ -> dlg.dispose());
        dlg.add(ok);
        dlg.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dlg.dispose(); }
        });
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }

    private static void showDetails(Frame frame, AddTask.Task row) {
        String details = "Task: " + row.getName() + "\nWorker: " + row.getWorker() + "\nStatus: " + row.getStatus();
        Dialog dlg = new Dialog(frame, "Task Details", true);
        dlg.setSize(300, 200);
        dlg.setLayout(new BorderLayout());
        TextArea text = new TextArea(details);
        text.setEditable(false);
        dlg.add(text, BorderLayout.CENTER);
        Button ok = new Button("OK");
        ok.addActionListener(_ -> dlg.dispose());
        Panel pnl = new Panel();
        pnl.add(ok);
        dlg.add(pnl, BorderLayout.SOUTH);
        dlg.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dlg.dispose(); }
        });
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }

    public static int getTasksCount() {
        loadTasks(); // optional, ensures tasks are loaded from file
        return tasks.size();
    }


    private static void editTask(Frame frame, AddTask.Task row) {
        Dialog dlg = new Dialog(frame, "Edit Task", true);
        dlg.setSize(350, 200);
        dlg.setLayout(new GridLayout(3, 2, 10, 10));

        TextField name = new TextField(row.getName());
        TextField worker = new TextField(row.getWorker());

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        saveBtn.addActionListener(_ -> {
            row.setName(name.getText());
            row.setWorker(worker.getText());
            saveTasks();
            dlg.dispose();
            refreshTable();
            showMessage(frame, "Updated", "Task updated successfully.", 300, 150);
        });

        cancelBtn.addActionListener(_ -> dlg.dispose());

        dlg.add(new Label("Task Name:")); dlg.add(name);
        dlg.add(new Label("Worker:")); dlg.add(worker);
        dlg.add(saveBtn); dlg.add(cancelBtn);

        dlg.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dlg.dispose(); }
        });

        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }

    // ====== Persistence ======
    private static void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadTasks() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            List<AddTask.Task> loaded = (List<AddTask.Task>) ois.readObject();
            tasks.clear();
            tasks.addAll(loaded);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
