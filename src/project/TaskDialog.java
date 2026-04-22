package project;

import project.dto.Task;
import javax.swing.*;
import java.awt.*;

public class TaskDialog extends JDialog {
    private JTextField titleField;
    private JTextArea contentArea;
    private JButton okButton;
    private JButton cancelButton;
    private boolean confirmed = false;
    private String title;
    private String content;
    
    public TaskDialog(Window owner, String dialogTitle, Task task) {
        super(owner, dialogTitle, ModalityType.APPLICATION_MODAL);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Центральная панель с полями
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        // Поле для заголовка
        JPanel titlePanel = new JPanel(new BorderLayout(5, 0));
        titlePanel.add(new JLabel("Заголовок:"), BorderLayout.WEST);
        titleField = new JTextField();
        titlePanel.add(titleField, BorderLayout.CENTER);
        
        // Поле для содержимого
        JPanel contentPanel = new JPanel(new BorderLayout(5, 0));
        contentPanel.add(new JLabel("Содержимое:"), BorderLayout.NORTH);
        contentArea = new JTextArea(5, 20);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        fieldsPanel.add(titlePanel);
        fieldsPanel.add(contentPanel);
        
        panel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        okButton = new JButton("OK");
        cancelButton = new JButton("Отмена");
        
        okButton.addActionListener(e -> {
            if (validateInput()) {
                this.title = titleField.getText().trim();
                this.content = contentArea.getText().trim();
                confirmed = true;
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(panel);
        
        // Заполняем поля, если редактируем задачу
        if (task != null) {
            titleField.setText(task.getTitle());
            contentArea.setText(task.getContent());
        }
    }
    
    private boolean validateInput() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Заголовок не может быть пустым", 
                "Ошибка ввода", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (title.isEmpty() && content.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Содержимое не может быть пустым", 
                "Ошибка ввода", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getContent() {
        return content;
    }
}