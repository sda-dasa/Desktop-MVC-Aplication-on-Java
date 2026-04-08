package project;
import project.TasksListView;
import project.dto.TasksList;
import project.dto.Task;
import java.time.*;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;


public class TasksListController {

    private TasksList model;
    private TasksListView view;
    private Consumer<Task> onTaskSelected;

    public TasksListController(TasksListView view){

        this.model = new TasksList();        
        this.view = view;
        this.model.add_observer(this.view);

    }

    public void refresh_data(LocalDate date){
        
        LocalDateTime dateTime = date.atStartOfDay();
        model.notify_observers(dateTime);

    }


    public void addTask(String title, String content) {
        Task newTask = new Task();
        newTask.setTitle(title);
        newTask.setContent(content);
        model.addTask(newTask);
        
        refresh_data(LocalDate.now());
    }
    
    public void updateTask(int id, String title, String content) {
        model.updateTask(id, title, content);
        
        refresh_data(LocalDate.now());
    }
    
    public void deleteTask(int id) {
        model.deleteTask(id);
        
        refresh_data(LocalDate.now());
    }


    public void loadTaskForEdit(int id, Consumer<Task> callback) {
        Task task = model.getTaskById(id);
        if (task != null && callback != null) {
            callback.accept(task);
        }
    }


    public void setOnTaskSelected(Consumer<Task> callback) {
        this.onTaskSelected = callback;
    }

    public void selectTask(int id) {
        Task task = model.getTaskById(id);
        if (task != null && onTaskSelected != null) {
            onTaskSelected.accept(task);
        }
    }


}