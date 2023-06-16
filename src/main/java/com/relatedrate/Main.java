package com.relatedrate;

import com.relatedrate.Equation.Equation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {

    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Math Problem Generator");
        stage.getIcons().add(new Image(new FileInputStream("src/res/icon.png")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        /*boolean gameActive = true;

        while(gameActive){
            Question randomQuestion = new Question(QuestionType.RELATED_RATE);

            System.out.println(randomQuestion.getQuestion());

            System.out.print("Continue Playing?: ");
            if(scanner.nextLine().equals("no")) gameActive = false;
        }*/

    }

    public static void main(String[] args) throws IOException {
        launch();

        System.out.println("Thanks for Playing");
    }
}