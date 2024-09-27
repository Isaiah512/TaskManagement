import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTaskDialog extends JDialog {
    private DatabaseManager dbManager;

    public AddTaskDialog(Frame owner, DefaultListModel<String> taskListModel, DatabaseManager dbManager) {
        super(owner, "Add New Task", true);
        this.dbManager = dbManager;

        setSize(400, 100);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel dialogPanel = new JPanel(new BorderLayout());

        JLabel taskLabel = new JLabel("Enter new task:");
        JTextField taskField = new JTextField(24);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(taskLabel);
        inputPanel.add(taskField);

        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newTask = taskField.getText();
                if (!newTask.isEmpty()) {
                    taskListModel.addElement(newTask);
                    dbManager.addTask(newTask);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AddTaskDialog.this, "Task cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialogPanel.add(inputPanel, BorderLayout.NORTH);
        dialogPanel.add(addTaskButton, BorderLayout.SOUTH);

        add(dialogPanel);
    }
}

