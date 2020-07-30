package ru.andtalk;

/**
 *  Стартовый класс приложения для shade плагина.
 *  Для создания fat jar файла shade плагину необходим стартовый класс не расширяющийся классом Application.
 *  На работу самого приложения класс никак не влияет.
 */
public class SuperApp {
    public static void main(String[] args) {
        App.main(args);
    }
}