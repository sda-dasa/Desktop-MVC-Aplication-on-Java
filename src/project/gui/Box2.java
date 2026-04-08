package project.gui;
import javax.swing.*;
import java.awt.*;

public class Box2 extends JFrame {
    public Box2() {
        super("Box2 - X");
        // устанавливаем размер и позицию окна
        setSize(400, 200);
        setLocation(100, 100);
        setDefaultCloseOperation( EXIT_ON_CLOSE);
        // получаем панель содержимого
        Container c = getContentPane();
        // устанавливаем блочное расположение по оси X (полоской)
        BoxLayout boxx = new BoxLayout(c, BoxLayout.X_AXIS);
        c.setLayout(boxx);
        // добавляем компоненты
        c.add( new JButton("Один"));
        c.add( new JButton("Два"));
        c.add( new JButton("Три"));
        // выводим окно на экран
        setVisible(true);
    }
}