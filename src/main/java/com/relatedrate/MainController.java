package com.relatedrate;

import com.relatedrate.Equation.Equation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainController {

    @FXML
    private Text txtQuestion;

    @FXML
    private Button generateBtn;

    @FXML
    private Button relatedRateBtn;

    @FXML
    private TextField input;

    private QuestionType selectedQuestionType;

    private Question randomQuestion;

    public void generateQuestion(ActionEvent event) throws IOException, Equation.MultipleVariableException {
        if (selectedQuestionType == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please be sure to select the question that you want the application to generate.");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(new FileInputStream("src/res/warning.png")));
            alert.showAndWait();
            return;
        }

        randomQuestion = new Question(selectedQuestionType);

        txtQuestion.setText(randomQuestion.getQuestion());
    }

    public void setRelatedRateQuestion() {
        if (selectedQuestionType == QuestionType.RELATED_RATE) {
            selectedQuestionType = null;
            relatedRateBtn.setStyle("-fx-text-fill: gray; -fx-background-color: black; -fx-border-width: 1px; -fx-border-color: #21262d; -fx-background-radius: 12px; -fx-border-radius: 12px;");
        } else {
            selectedQuestionType = QuestionType.RELATED_RATE;
            relatedRateBtn.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color: white; -fx-border-width: 1px; -fx-background-radius: 12px; -fx-border-radius: 12px;");
        }
    }

    public void submitAnswer() throws IOException, Equation.MultipleVariableException {
        if (randomQuestion == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("No question has been generated");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(new FileInputStream("src/res/warning.png")));
            alert.showAndWait();
        } else if (!isDouble(input.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please make sure what you wrote is a number.");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(new FileInputStream("src/res/warning.png")));
            alert.showAndWait();
        } else if (Math.floor(randomQuestion.getAnswer()) <= Double.parseDouble(input.getText()) && Math.ceil(randomQuestion.getAnswer()) >= Double.parseDouble(input.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You got the answer correct!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(new FileInputStream("src/res/warning.png")));
            alert.showAndWait();

            randomQuestion = new Question(selectedQuestionType);

            txtQuestion.setText(randomQuestion.getQuestion());
            input.setText("");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Not Quite. Try Again");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(new FileInputStream("src/res/warning.png")));
            alert.showAndWait();
        }
    }

    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
