package project.gui;

import project.TasksListView;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import project.gui.MinimalCalendar;
import project.TasksListController;
import project.dto.Task;

public class Box1 extends JFrame {
    private MinimalCalendar calendar;
    private TasksListView tasksListView;
    private JPanel taskDetailsPanel;
    private TasksListController controller;
    
    public Box1() {
        super("Менеджер задач");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(350, 0));
        

        calendar = new MinimalCalendar();
        tasksListView = new TasksListView();
        tasksListView.setCalendar(calendar);
        
        
        controller = new TasksListController(tasksListView); 
        controller.setOnTaskSelected(this::updateTaskDetails);
        
        calendar.setController(controller);
        tasksListView.setController(controller);


        leftPanel.add(calendar, BorderLayout.NORTH);        
        
        taskDetailsPanel = new JPanel();
        taskDetailsPanel.setLayout(new BorderLayout());
        taskDetailsPanel.setBorder(BorderFactory.createTitledBorder("Детали задачи"));
        taskDetailsPanel.setPreferredSize(new Dimension(350, 200));
        
        JLabel emptyLabel = new JLabel("Выберите задачу для просмотра деталей", SwingConstants.CENTER);
        emptyLabel.setForeground(Color.GRAY);
        taskDetailsPanel.add(emptyLabel, BorderLayout.CENTER);
        
        leftPanel.add(taskDetailsPanel, BorderLayout.CENTER);
        
        c.add(leftPanel, BorderLayout.WEST);
        c.add(tasksListView, BorderLayout.CENTER);
        
        setVisible(true);
    }



    public void updateTaskDetails(Task task) {
        taskDetailsPanel.removeAll();
        
        if (task != null) {
            JPanel details = new JPanel();
            details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
            details.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JLabel titleLabel = new JLabel("Заголовок: " + task.getTitle());
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel dateLabel = new JLabel("Создано: " + task.get_date_info());
            dateLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            
            JTextArea contentArea = new JTextArea(task.getContent());
            contentArea.setEditable(false);
            contentArea.setLineWrap(true);
            contentArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(contentArea);
            scrollPane.setPreferredSize(new Dimension(320, 150));
            
            details.add(titleLabel);
            details.add(Box.createVerticalStrut(10));
            details.add(dateLabel);
            details.add(Box.createVerticalStrut(10));
            details.add(new JLabel("Содержимое:"));
            details.add(scrollPane);
            
            taskDetailsPanel.add(details, BorderLayout.CENTER);
        } else {
            JLabel emptyLabel = new JLabel("Выберите задачу для просмотра деталей", SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            taskDetailsPanel.add(emptyLabel, BorderLayout.CENTER);
        }
        
        taskDetailsPanel.revalidate();
        taskDetailsPanel.repaint();
    }



}