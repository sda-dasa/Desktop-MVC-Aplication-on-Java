package project.gui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import project.TasksListController;

public class MinimalCalendar extends JPanel {
    private LocalDate currentDate;
    private LocalDate selectedDate;
    private JLabel monthYearLabel;
    private JPanel daysPanel;
    private Consumer<LocalDate> onDateSelected; //реакция (реальные действия с данными/запросами к контроллеру/модели) на выбор каждой новой даты
    private TasksListController controller;
    
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");
    private static final String[] DAYS_OF_WEEK = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};
    
    public MinimalCalendar() {
        this.currentDate = LocalDate.now();
        this.selectedDate = LocalDate.now();
        initUI();
    }
    
    public void setOnDateSelected(Consumer<LocalDate> callback) { //????????????????
        this.onDateSelected = callback; // вызов действия-реакции на событие (надо понять, где его прописать + придумать логику)
    }


    public void setController(TasksListController controller) {
        this.controller = controller;
    }

    
    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Верхняя панель с навигацией
        JPanel navigationPanel = createNavigationPanel();
        add(navigationPanel, BorderLayout.NORTH);
        
        // Панель с днями недели
        JPanel weekDaysPanel = createWeekDaysPanel();
        add(weekDaysPanel, BorderLayout.CENTER);
        
        // Панель с числами месяца
        daysPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        add(daysPanel, BorderLayout.SOUTH);
        
        updateCalendar();
    }
    
    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        monthYearLabel = new JLabel();
        monthYearLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthYearLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        JButton todayButton = new JButton("Сегодня");
        
        prevButton.addActionListener(e -> changeMonth(-1));
        nextButton.addActionListener(e -> changeMonth(1));
        todayButton.addActionListener(e -> {
            selectedDate = LocalDate.now();
            currentDate = LocalDate.now();
            updateCalendar();
            if (onDateSelected != null) {
                onDateSelected.accept(selectedDate);
            }
        });
        
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        navButtons.add(prevButton);
        navButtons.add(todayButton);
        navButtons.add(nextButton);
        
        panel.add(monthYearLabel, BorderLayout.CENTER);
        panel.add(navButtons, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createWeekDaysPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 7, 5, 5));
        for (String day : DAYS_OF_WEEK) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 11));
            dayLabel.setForeground(Color.GRAY);
            panel.add(dayLabel);
        }
        return panel;
    }
    
    private void updateCalendar() {
        YearMonth yearMonth = YearMonth.from(currentDate);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        
        // Получаем день недели первого числа месяца (понедельник = 1, воскресенье = 7)
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        // Корректируем, чтобы понедельник был первым
        int offset = firstDayOfWeek - 1;
        
        int daysInMonth = yearMonth.lengthOfMonth();
        
        monthYearLabel.setText(MONTH_FORMATTER.format(currentDate));
        
        daysPanel.removeAll();
        
        // Пустые ячейки для дней предыдущего месяца
        for (int i = 0; i < offset; i++) {
            daysPanel.add(createEmptyDayCell());
        }
        
        // Ячейки для дней текущего месяца
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentDate.withDayOfMonth(day);
            daysPanel.add(createDayCell(date));
        }
        
        daysPanel.revalidate();
        daysPanel.repaint();
    }
    
    private JPanel createDayCell(LocalDate date) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
        JLabel dayLabel = new JLabel(String.valueOf(date.getDayOfMonth()), SwingConstants.CENTER);
        dayLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        cell.add(dayLabel, BorderLayout.CENTER);
        
        // Подсветка выбранной даты
        if (date.equals(selectedDate)) {
            cell.setBackground(new Color(200, 220, 255));
            dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
        } else if (date.equals(LocalDate.now())) {
            cell.setBackground(new Color(255, 255, 200));
            dayLabel.setForeground(Color.RED);
        } else {
            cell.setBackground(Color.WHITE);
        }
        
        cell.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectedDate = date;
                updateCalendar();
                refresh_data(); // вызываем общий метод уведомления
            }
        });
        
        return cell;
    }
    
    private JPanel createEmptyDayCell() {
        JPanel cell = new JPanel();
        cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        cell.setBackground(Color.WHITE);
        return cell;
    }
    
    private void changeMonth(int delta) {
        currentDate = currentDate.plusMonths(delta);
        updateCalendar();
    }

    private void refresh_data() {
        if (controller != null) {
            controller.refresh_data(selectedDate);
        }
    }
    
    public LocalDate getSelectedDate() {
        return selectedDate;
    }
    
    public void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
        this.currentDate = date;
        updateCalendar();
    }
}