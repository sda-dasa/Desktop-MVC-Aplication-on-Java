package project.database;

import project.dto.Task;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class H2Database implements DBInterface {
    private Connection connection;
    private static final String DB_URL = "jdbc:h2:~/tasks_db;DB_CLOSE_DELAY=-1"; // ~/tasks_db - файл в домашней папке
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    
    public H2Database() {
        try {
            // Загружаем драйвер H2
            Class.forName("org.h2.Driver");
            
            // Подключаемся к БД (создаст файл автоматически)
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            
            // Создаём таблицу, если её нет
            createTableIfNotExists();
            
        } catch (ClassNotFoundException e) {
            System.err.println("H2 Driver not found! Add h2.jar to classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                content TEXT NOT NULL,
                creation_date TIMESTAMP NOT NULL,
                last_changes_date TIMESTAMP NOT NULL
            )
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            
            // Добавим несколько тестовых задач, если таблица пустая
            insertTestDataIfEmpty();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void insertTestDataIfEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tasks");
            rs.next();
            int count = rs.getInt(1);
            
            if (count == 0) {
                // Тестовые данные
                String insertSql = """
                    INSERT INTO tasks (title, content, creation_date, last_changes_date) VALUES
                    ('Купить продукты', 'Молоко, хлеб, яйца, масло', NOW(), NOW()),
                    ('Сделать домашнее задание', 'Закончить проект по Java', NOW(), NOW()),
                    ('Позвонить маме', 'Не забыть поздравить с днём рождения', NOW(), NOW()),
                    ('Записаться к врачу', 'Предварительная запись на пятницу', NOW(), NOW()),
                    ('Прочитать книгу', '"Чистый код" Роберт Мартин', NOW(), NOW())
                    """;
                stmt.execute(insertSql);
                System.out.println("Test data inserted successfully!");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Task> getTasksByDate(LocalDate date) {
        List<Task> tasks = new ArrayList<>();
        // Исправленный SQL - сравниваем дату без времени
        String sql = "SELECT * FROM tasks WHERE CAST(creation_date AS DATE) = ? ORDER BY id";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setContent(rs.getString("content"));
                task.setCreation_date(rs.getTimestamp("creation_date").toLocalDateTime());
                task.setLast_changes_date(rs.getTimestamp("last_changes_date").toLocalDateTime());
                tasks.add(task);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tasks;
    }
    
    @Override
    public Task getTaskById(int id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                
                task.setTitle(rs.getString("title"));
                task.setContent(rs.getString("content"));
                task.setCreation_date(rs.getTimestamp("creation_date").toLocalDateTime());
                task.setLast_changes_date(rs.getTimestamp("last_changes_date").toLocalDateTime());
                return task;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public void addTask(Task task) {
        String sql = "INSERT INTO tasks (title, content, creation_date, last_changes_date) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getContent());
            pstmt.setTimestamp(3, Timestamp.valueOf(task.getCreation_date()));
            pstmt.setTimestamp(4, Timestamp.valueOf(task.getLast_changes_date()));
            pstmt.executeUpdate();
            
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                task.setId(generatedKeys.getInt(1));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void updateTask(Task task) {
        String sql = "UPDATE tasks SET title = ?, content = ?, last_changes_date = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getContent());
            pstmt.setTimestamp(3, Timestamp.valueOf(task.getLast_changes_date()));
            pstmt.setInt(4, task.getId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY creation_date DESC";
        
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setContent(rs.getString("content"));
                task.setCreation_date(rs.getTimestamp("creation_date").toLocalDateTime());
                task.setLast_changes_date(rs.getTimestamp("last_changes_date").toLocalDateTime());
                tasks.add(task);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tasks;
    }
    
    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}