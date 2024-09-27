import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.TitledBorder;
import java.sql.*;

public class MainFrame extends JFrame {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private DatabaseManager dbManager;

    public MainFrame() {
        setTitle("TaskManager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        dbManager = new DatabaseManager();
        initializeComponents();
        loadTasksFromDatabase();
    }

    private void initializeComponents() {
        // Title Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("TaskManager");
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        topPanel.add(label);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Task Panel
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JScrollPane taskScrollPane = new JScrollPane(taskList);
        taskScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), 
                "Tasks", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("SansSerif", Font.BOLD, 16)
        ));
        taskScrollPane.setPreferredSize(new Dimension(400, 300));

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(taskScrollPane, BorderLayout.CENTER);

        add(mainPanel);

        addButton.addActionListener(new AddTask());
        editButton.addActionListener(new EditTask());
        deleteButton.addActionListener(new DeleteTask());
    }

    private void loadTasksFromDatabase() {
        try {
            Connection conn = dbManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT description FROM tasks");
            while (rs.next()) {
                taskListModel.addElement(rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class AddTask implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddTaskDialog dialog = new AddTaskDialog(MainFrame.this, taskListModel, dbManager);
            dialog.setVisible(true);
        }
    }

    private class EditTask implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedTask = taskListModel.getElementAt(selectedIndex);
                String newTask = JOptionPane.showInputDialog(MainFrame.this, "Edit Task", selectedTask);
                if (newTask != null && !newTask.trim().isEmpty()) {
                    taskListModel.setElementAt(newTask, selectedIndex);
                    dbManager.updateTask(selectedTask, newTask);
                }
            } else {
                JOptionPane.showMessageDialog(MainFrame.this, "Please click on a task to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteTask implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                String taskToDelete = taskListModel.getElementAt(selectedIndex);
                taskListModel.removeElementAt(selectedIndex);
                dbManager.deleteTask(taskToDelete);
            } else {
                JOptionPane.showMessageDialog(MainFrame.this, "Please click a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        dbManager.close();
    }
}

