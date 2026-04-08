package project.dto;

import project.database.DBInterface;
import project.database.H2Database;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import project.TasksListView;

public class TasksList {
    private List<Task> tasks;
    private DBInterface db;
    private List<TasksListView> observers;
    
    public TasksList() {
        this.db = new H2Database(); 
        this.observers = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }
    
    public void add_observer(TasksListView obs) {
        observers.add(obs);
    }
    
    public void notify_observers(LocalDateTime date) {
        TaskShort[] shortInfo = get_short_info(date);
        for (TasksListView observer : observers) {
            observer.notify(shortInfo);
        }
    }
    
    public TaskShort[] get_short_info(LocalDateTime date) {

        // Конвертируем LocalDateTime в LocalDate для поиска в БД

        LocalDate localDate = date.toLocalDate();
        List<Task> tasksForDate = db.getTasksByDate(localDate);
        
        if (tasksForDate == null || tasksForDate.isEmpty()) {
            return new TaskShort[0];
        }
        
        TaskShort[] shortTasks = new TaskShort[tasksForDate.size()];
        for (int i = 0; i < tasksForDate.size(); i++) {
            shortTasks[i] = new TaskShort(tasksForDate.get(i));
        }
        return shortTasks;
    }
    

    public void addTask(Task task) {
        db.addTask(task);
        notify_observers(LocalDateTime.now());
    }

    public void updateTask(int id, String title, String content) {
        Task task = db.getTaskById(id);
        if (task != null) {
            task.setTitle(title);
            task.setContent(content);
            task.setLast_changes_date(LocalDateTime.now());
            db.updateTask(task);
            notify_observers(LocalDateTime.now());
        }
    }

    public void deleteTask(int id) {
        db.deleteTask(id);
        notify_observers(LocalDateTime.now());
    }

    public Task getTaskById(int id) {
        return db.getTaskById(id);
    }




}