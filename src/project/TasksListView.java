package project;
import project.TasksListController;
import project.dto.TaskShort;
import java.time.*;
import javax.swing.*;
import java.awt.*;
import project.TaskDialog;
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent; 


public class TasksListView extends JPanel {
    private TasksListController controller;
    private JButton edit_btn;
    private JButton add_btn;
    private JButton delete_btn;
    
    private JPanel tasksContainer;
    private TaskShort[] currentTasks;
    private TaskShort selectedTask; 


    public TasksListView() {
        
        setLayout(new BorderLayout());

        
        
        btns_initialize();
        add_btns_on_frame();
        
        this.tasksContainer = new JPanel();
        this.tasksContainer.setLayout(new BoxLayout(tasksContainer, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(tasksContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);        

        setVisible(true);

    }

    public void setController(TasksListController controller) {
        this.controller = controller;
    }



    public void refresh_data(LocalDate date){       
        controller.refresh_data(date);
    }

    public void create_list (TaskShort[] tasks) {
        this.currentTasks = tasks;
        this.selectedTask = null;
        tasksContainer.removeAll();
        
        if (tasks == null || tasks.length == 0) {
            JLabel emptyLabel = new JLabel("Нет задач на выбранную дату");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            tasksContainer.add(emptyLabel);
        } else {
            for (TaskShort task : tasks) {
                tasksContainer.add(createTaskPanel(task));
                tasksContainer.add(Box.createVerticalStrut(5)); 
            }
        }
        
        tasksContainer.revalidate();
        tasksContainer.repaint();
    }


    private JPanel createTaskPanel(TaskShort task) {
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BorderLayout());
        taskPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        taskPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        taskPanel.setBackground(Color.WHITE);

        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel(task.getTitle());
        leftPanel.add(titleLabel);
        
        
        JLabel dateLabel = new JLabel(task.getDate_info());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        dateLabel.setForeground(Color.DARK_GRAY);
        
        taskPanel.add(leftPanel, BorderLayout.CENTER);
        taskPanel.add(dateLabel, BorderLayout.EAST);

        // add_listener_to_task_panel(taskPanel, task);

        taskPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectTask(task, taskPanel);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedTask != task) {
                    taskPanel.setBackground(new Color(240, 240, 255));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (selectedTask != task) {
                    taskPanel.setBackground(Color.WHITE);
                }
            }
        });
        
        return taskPanel;
    }


    // private void add_listener_to_task_panel(JPanel taskPanel, TaskShort task){
    //     taskPanel.addMouseListener(new MouseAdapter() {
    //         @Override
    //         public void mouseClicked(MouseEvent e) {
    //             selectTask(task, taskPanel);
    //         }
            
    //         @Override
    //         public void mouseEntered(MouseEvent e) {
    //             if (selectedTask != task) {
    //                 taskPanel.setBackground(new Color(240, 240, 255));
    //             }
    //         }
            
    //         @Override
    //         public void mouseExited(MouseEvent e) {
    //             if (selectedTask != task) {
    //                 taskPanel.setBackground(Color.WHITE);
    //             }
    //         }
    //     });
    // }


    private void selectTask(TaskShort task, JPanel panel) {
        
        if (selectedTask != null) {
            for (Component comp : tasksContainer.getComponents()) {
                if (comp instanceof JPanel) {
                    comp.setBackground(Color.WHITE);
                }
            }
        }
        
        selectedTask = task;
        panel.setBackground(new Color(200, 220, 255));
        
        controller.selectTask(task.getId());
    }


    private void btns_initialize() {
        this.edit_btn = new JButton("Редактировать");
        this.add_btn = new JButton("Добавить");
        this.delete_btn = new JButton("Удалить");        
        
        this.add_btn.addActionListener(e -> showAddTaskDialog());
        this.edit_btn.addActionListener(e -> showEditTaskDialog());
        this.delete_btn.addActionListener(e -> deleteSelectedTask());
    }


    private void add_btns_on_frame(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(this.add_btn);
        buttonPanel.add(this.edit_btn);
        buttonPanel.add(this.delete_btn);
        add(buttonPanel, BorderLayout.NORTH);
    }


    public void notify(TaskShort[] tasks){
        create_list(tasks);
    }





    private void showAddTaskDialog() {
        TaskDialog dialog = new TaskDialog(SwingUtilities.getWindowAncestor(this), "Добавить задачу", null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            String title = dialog.getTitle();
            String content = dialog.getContent();
            controller.addTask(title, content);
        }
    }
    
    private void showEditTaskDialog() {
        if (selectedTask == null) {
            JOptionPane.showMessageDialog(this, 
                "Пожалуйста, выберите задачу для редактирования", 
                "Нет выбранной задачи", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }        
        
        controller.loadTaskForEdit(selectedTask.getId(), (task) -> {
            TaskDialog dialog = new TaskDialog(
                SwingUtilities.getWindowAncestor(this), 
                "Редактировать задачу", 
                task
            );
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                String newTitle = dialog.getTitle();
                String newContent = dialog.getContent();
                controller.updateTask(selectedTask.getId(), newTitle, newContent);
            }
        });
    }


    
    private void deleteSelectedTask() {
        if (selectedTask == null) {
            JOptionPane.showMessageDialog(this, 
                "Пожалуйста, выберите задачу для удаления", 
                "Нет выбранной задачи", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Вы уверены, что хотите удалить задачу \"" + selectedTask.getTitle() + "\"?",
            "Подтверждение удаления",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteTask(selectedTask.getId());
        }
    }





}