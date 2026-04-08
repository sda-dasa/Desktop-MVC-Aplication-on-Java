package project.dto;
import java.time.*;
import java.time.format.DateTimeFormatter;
//import lombok.Getter;

public class TaskShort {

    //@Getter
    private String date_info;
    
    //@Getter
    private int id;

    //@Getter
    private String title;


    public TaskShort(int id, String date_info, String title){
        this.id = id; this.date_info = date_info; this.title = title;
    }

    public TaskShort(Task task){
        this.id = task.getId(); 
        this.date_info = task.get_date_info();
        if (task.getTitle() != null && !task.getTitle().isBlank()) {
            this.title = task.getTitle();
        }
        else {
            this.title = task.getContent().substring(0, Math.min(task.getContent().length(), 20));
        }
    }


    public int getId(){
        return  this.id;
    }

    public String getDate_info() {
        return date_info;
    }

    public String getTitle() {
        return title;
    }

        

}