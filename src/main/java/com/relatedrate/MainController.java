package com.relatedrate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainController {

    @FXML
    private Text txtQuestion;

    @FXML
    private Button generateBtn;

    public void generateQuestion(ActionEvent event) throws IOException {
        Question randomQuestion = new Question(QuestionType.RELATED_RATE);

        txtQuestion.setText(randomQuestion.getQuestion().substring(0, randomQuestion.getQuestion().length() - 5));
    }
}
