package project.database;

import project.dto.Task;
import java.time.LocalDate;
import java.util.List;

public interface DBInterface {

    List<Task> getTasksByDate(LocalDate date);
    
    Task getTaskById(int id);
    
    void addTask(Task task);
    
    void updateTask(Task task);
    
    void deleteTask(int id);
    
    List<Task> getAllTasks();
    
    void close();
}