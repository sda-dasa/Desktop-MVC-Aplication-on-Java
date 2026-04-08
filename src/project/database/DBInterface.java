package project.database;

import project.dto.Task;
import java.time.LocalDate;
import java.util.List;

public interface DBInterface {
    // Получить задачи за конкретную дату
    List<Task> getTasksByDate(LocalDate date);
    
    // Получить задачу по ID
    Task getTaskById(int id);
    
    // Добавить новую задачу
    void addTask(Task task);
    
    // Обновить задачу
    void updateTask(Task task);
    
    // Удалить задачу
    void deleteTask(int id);
    
    // Получить все задачи
    List<Task> getAllTasks();
    
    // Закрыть соединение с БД
    void close();
}