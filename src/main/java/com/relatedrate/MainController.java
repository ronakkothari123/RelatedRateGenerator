package com.relatedrate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class MainController {

    @FXML
    private Text txtQuestion;

    @FXML
    private Button generateBtn;

    @FXML
    private Button relatedRateBtn;

    private QuestionType selectedQuestionType;

    public void generateQuestion(ActionEvent event) throws IOException {
        if(selectedQuestionType == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please be sure to select the question that you want the application to generate.");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(new FileInputStream("src/res/warning.png")));
            alert.showAndWait();
            return;
        }

        Question randomQuestion = new Question(selectedQuestionType);

        txtQuestion.setText(randomQuestion.getQuestion());
    }

    public void setRelatedRateQuestion(){
        if(selectedQuestionType == QuestionType.RELATED_RATE){
            selectedQuestionType = null;
            relatedRateBtn.setStyle("-fx-text-fill: gray; -fx-background-color: black; -fx-border-width: 1px; -fx-border-color: #21262d; -fx-background-radius: 12px; -fx-border-radius: 12px;");
        } else {
            selectedQuestionType = QuestionType.RELATED_RATE;
            relatedRateBtn.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color: white; -fx-border-width: 1px; -fx-background-radius: 12px; -fx-border-radius: 12px;");
        }
    }
}
