package ru.andtalk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Стартовый класс приложения.
 * В нем происходит создание начального окна приложения с выбором метода.
 * За данное окно отвечает класс Controller, в котором и описан весь оставшийся функционал.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("choiceWindow.fxml"));
        primaryStage.setTitle("Контрольная работа");
        primaryStage.setScene(new Scene(root, 325, 150));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}