package project.dto;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.text.ParseException;

public class Task {

    private LocalDateTime creation_date;
    private LocalDate appointment_date;
    
    private int id;


    private String title;

    private String content;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");

    public Task(LocalDate appointment_date){
        this.creation_date = LocalDateTime.now();   
        this.appointment_date = appointment_date;    
    }

    public Task(int id, String title, String content, LocalDate appointment_date) { 
        this.id = id;
        this.title = title;
        this.content = content;
        this.creation_date = LocalDateTime.now();
        this.appointment_date = appointment_date;
    }

    

    public void setContent(String text) {
        if (text == null || text.isBlank()) {
            this.content = "";  
        } else {
            this.content = text;
        }
    }


    public void setTitle(String text){
        this.title = text;
    }


    public String get_date_info(){  

         return String.format("Создано: %s, Назначено на: %s ", this.creation_date.format(formatter), this.appointment_date.format(formatter));

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
    }


    public void setAppointment_date(LocalDate appointment_date) {
        this.appointment_date = appointment_date;
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

    public LocalDate getAppointment_date() {
        return this.appointment_date;
    }
        

}