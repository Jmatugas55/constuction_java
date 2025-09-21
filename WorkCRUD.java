
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkCRUD {

    // ====== Work Model ======
    public static class Work implements Serializable {
        private String id, workName, ownerName, contactNumber, workerWanted,
                location, dateCreated, endDate, status;

        public Work(String id, String workName, String ownerName, String contactNumber,
                    String workerWanted, String location, String dateCreated, String endDate) {
            this.id = id;
            this.workName = workName;
            this.ownerName = ownerName;
            this.contactNumber = contactNumber;
            this.workerWanted = workerWanted;
            this.location = location;
            this.dateCreated = dateCreated;
            this.endDate = endDate;
            this.status = "Ongoing"; // default
        }

        // Getters
        public String getId() { return id; }
        public String getWorkName() { return workName; }
        public String getOwnerName() { return ownerName; }
        public String getContactNumber() { return contactNumber; }
        public String getWorkerWanted() { return workerWanted; }
        public String getLocation() { return location; }
        public String getDateCreated() { return dateCreated; }
        public String getEndDate() { return endDate; }
        public String getStatus() { return status; }

        // Setters
        public void setWorkName(String v) { workName = v; }
        public void setOwnerName(String v) { ownerName = v; }
        public void setContactNumber(String v) { contactNumber = v; }
        public void setWorkerWanted(String v) { workerWanted = v; }
        public void setLocation(String v) { location = v; }
        public void setDateCreated(String v) { dateCreated = v; }
        public void setEndDate(String v) { endDate = v; }
        public void setStatus(String v) { status = v; }
    }

    // ====== Storage ======
    private static final String FILE_NAME = "works.dat";
    private static final List<Work> works = new ArrayList<>();
    private static Panel cardsPanel;
    private static Frame parentRef;

    public static Panel getWorkPanel(Frame parentFrame) {
        parentRef = parentFrame;
        loadWorks();

        Panel mainPanel = new Panel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        Label header = new Label(" Works Management", Label.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setBackground(new Color(25, 118, 210));
        header.setForeground(Color.white);
        mainPanel.add(header, BorderLayout.NORTH);

        // Top bar with button
        Panel topBar = new Panel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        Button addBtn = createButton("＋ Add New Work", new Color(25, 118, 210));
        topBar.setBackground(new Color(245, 245, 245));
        topBar.add(addBtn);
        mainPanel.add(topBar, BorderLayout.SOUTH);

        // Cards area
        cardsPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        refreshCards();

        ScrollPane scroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);

        cardsPanel.setPreferredSize(new Dimension(1000, 600));

        scroll.add(cardsPanel);

        mainPanel.add(scroll, BorderLayout.CENTER);


        // Event for adding work
        addBtn.addActionListener(_ -> showAddDialog(parentRef));

        return mainPanel;
    }

    // ====== Refresh Card Layout ======
    private static void refreshCards() {
        cardsPanel.removeAll();

        for (Work w : works) {
            Panel card = new Panel(new BorderLayout());
            card.setBackground(Color.white);
            card.setPreferredSize(new Dimension(220, 200));
            card.setLayout(new BorderLayout());
            card.setFont(new Font("Segoe UI", Font.PLAIN, 13));

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
            details.add(new Label("End: " + w.getEndDate()));
            details.add(new Label("Status: " + w.getStatus()));
            card.add(details, BorderLayout.CENTER);

            // Action buttons
            Panel actions = new Panel(new FlowLayout(FlowLayout.CENTER, 5, 5));
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
            card.add(actions, BorderLayout.SOUTH);

            // Events
            view.addActionListener(_ -> showDetails(parentRef, w));
            edit.addActionListener(_ -> showEditDialog(parentRef, w));
            process.addActionListener(_ -> { w.setStatus("Processing"); saveWorks(); refreshCards(); });
            complete.addActionListener(_ -> { w.setStatus("Completed"); saveWorks(); refreshCards(); });
            delete.addActionListener(_ -> { works.remove(w); saveWorks(); refreshCards(); });

            cardsPanel.add(card);
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    // ====== Add Work ======
    private static void showAddDialog(Frame frame) {
        Dialog dlg = new Dialog(frame, "Add Work", true);
        dlg.setSize(400, 450);
        dlg.setLayout(new GridLayout(9, 2, 10, 10));
        dlg.setBackground(Color.white);

        TextField workName = new TextField();
        TextField owner = new TextField();
        TextField contact = new TextField();
        TextField workers = new TextField();
        TextField location = new TextField();
        TextField dateCreated = new TextField();
        TextField endDate = new TextField();

        dlg.add(new Label("Work Name:")); dlg.add(workName);
        dlg.add(new Label("Owner Name:")); dlg.add(owner);
        dlg.add(new Label("Contact No:")); dlg.add(contact);
        dlg.add(new Label("Worker Wanted:")); dlg.add(workers);
        dlg.add(new Label("Location:")); dlg.add(location);
        dlg.add(new Label("Date Created:")); dlg.add(dateCreated);
        dlg.add(new Label("End Date:")); dlg.add(endDate);

        Button save = createButton("Save", new Color(25, 118, 210));
        Button cancel = createButton("Cancel", new Color(158, 158, 158));
        dlg.add(save); dlg.add(cancel);

        save.addActionListener(_ -> {
            String id = String.valueOf(works.size() + 1);
            Work w = new Work(id, workName.getText(), owner.getText(), contact.getText(),
                    workers.getText(), location.getText(), dateCreated.getText(), endDate.getText());
            works.add(w);
            saveWorks();
            dlg.dispose();
            refreshCards();
        });

        cancel.addActionListener(_ -> dlg.dispose());
        dlg.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dlg.dispose(); }
        });

        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }

    // ====== Edit Work ======
    private static void showEditDialog(Frame frame, Work w) {
        Dialog dlg = new Dialog(frame, "Edit Work", true);
        dlg.setSize(400, 450);
        dlg.setLayout(new GridLayout(9, 2, 10, 10));
        dlg.setBackground(Color.white);

        TextField workName = new TextField(w.getWorkName());
        TextField owner = new TextField(w.getOwnerName());
        TextField contact = new TextField(w.getContactNumber());
        TextField workers = new TextField(w.getWorkerWanted());
        TextField location = new TextField(w.getLocation());
        TextField dateCreated = new TextField(w.getDateCreated());
        TextField endDate = new TextField(w.getEndDate());

        dlg.add(new Label("Work Name:")); dlg.add(workName);
        dlg.add(new Label("Owner Name:")); dlg.add(owner);
        dlg.add(new Label("Contact No:")); dlg.add(contact);
        dlg.add(new Label("Worker Wanted:")); dlg.add(workers);
        dlg.add(new Label("Location:")); dlg.add(location);
        dlg.add(new Label("Date Created:")); dlg.add(dateCreated);
        dlg.add(new Label("End Date:")); dlg.add(endDate);

        Button save = createButton("Update", new Color(25, 118, 210));
        Button cancel = createButton("Cancel", new Color(158, 158, 158));
        dlg.add(save); dlg.add(cancel);

        save.addActionListener(_ -> {
            w.setWorkName(workName.getText());
            w.setOwnerName(owner.getText());
            w.setContactNumber(contact.getText());
            w.setWorkerWanted(workers.getText());
            w.setLocation(location.getText());
            w.setDateCreated(dateCreated.getText());
            w.setEndDate(endDate.getText());
            saveWorks();
            dlg.dispose();
            refreshCards();
        });

        cancel.addActionListener(_ -> dlg.dispose());
        dlg.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dlg.dispose(); }
        });

        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }

    public static List<Work> getAllWorks() {
        loadWorks(); // always reload from file to keep in sync
        return new ArrayList<>(works); // return a copy to avoid modification outside
    }


    // ====== View Details ======
    private static void showDetails(Frame frame, Work w) {
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
        

        dlg.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dlg.dispose(); }
        });

        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }

    // ====== Persistence ======
    private static void saveWorks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(works);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private static void loadWorks() {
        File f = new File(FILE_NAME);
        if (!f.exists()) {
            // 🔑 Add sample work if no file exists
            works.clear();
            works.add(new Work("1", "Construction Helper", "John Doe", "09123456789",
                    "5", "Cebu City", "2025-09-21", "2025-09-30"));
            works.add(new Work("2", "Farm Assistant", "Maria Lopez", "09998887777",
                    "3", "Davao", "2025-09-21", "2025-09-25"));
            saveWorks(); // save sample works
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            works.clear();
            works.addAll((List<Work>) ois.readObject());
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static int getWorksCount() {
    loadWorks(); // ensure latest
    return works.size();
}

    // ====== Button factory ======
    private static Button createButton(String text, Color bg) {
        Button btn = new Button(text);
        btn.setBackground(bg);
        btn.setForeground(Color.white);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return btn;
    }
}
