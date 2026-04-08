package project.dto;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.text.ParseException;

public class Task {

    private LocalDateTime creation_date;
    private LocalDateTime last_changes_date;
    
    private int id;


    private String title;

    private String content;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public Task(){
        this.creation_date = LocalDateTime.now();
        this.last_changes_date = this.creation_date;         
    }

    public Task(int id, String title, String content) { 
        this.id = id;
        this.title = title;
        this.content = content;
        this.creation_date = LocalDateTime.now();
        this.last_changes_date = this.creation_date;
    }

    

    public void setContent(String text) {
        if (text == null || text.isBlank()) {
            this.content = "";  
        } else {
            this.content = text;
        }
        this.last_changes_date = LocalDateTime.now();
    }


    public void setTitle(String text){
        this.title = text;
        this.last_changes_date = LocalDateTime.now();
    }


    public String get_date_info(){        

        return String.format("Создано: %s, Изменено: %s", creation_date.format(formatter), last_changes_date.format(formatter));

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
    }

    public void setLast_changes_date(LocalDateTime last_changes_date) {
        this.last_changes_date = last_changes_date;
    }


    public int getId(){
        return  this.id;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreation_date() {
        return this.creation_date;
    }

    public LocalDateTime getLast_changes_date() {
        return this.last_changes_date;
    }
        

}