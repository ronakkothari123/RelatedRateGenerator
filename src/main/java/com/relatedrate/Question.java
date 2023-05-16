package com.relatedrate;

import com.relatedrate.Equation.Equation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Question {
    private String question;
    private QuestionType questionType;
    private double answer;

    //creates question based on questionType
    public Question(QuestionType questionType) throws IOException{
        this.questionType = questionType;
        generateQuestion();
    }

    //returns true if successful
    private boolean generateQuestion() throws IOException{
        List<String> lines;

        if(questionType == QuestionType.RELATED_RATE) lines = Files.readAllLines(Paths.get("src", "main", "java", "com", "relatedrate", "RelatedRate.dat"));
        else{
            System.out.println("Question Type Not Supported");
            return false;
        }

        question = lines.get((int) (Math.random() * lines.size()));

        Equation equation;

        for(int i = 0; i < question.length(); i++){
            if(question.charAt(i) == '<'){
                int[] chevronSequence = findCharacterSequence(question, i, '>');

                question = question.replace(question.substring(chevronSequence[0], chevronSequence[1] + 1), Integer.toString((int) (Math.random() * 20)));
            } else if(question.charAt(i) == '|'){
                int[] pipeSequence = findCharacterSequence(question, i, '|');

                equation = new Equation(question.substring(pipeSequence[0] + 1, pipeSequence[1]));
                question = question.replace(question.substring(pipeSequence[0], pipeSequence[1] + 1), "");
            }
        }

        return true;
    }

    private int[] findCharacterSequence(String str, int startingIndex, char character){
        for(int i = startingIndex + 1; i < str.length(); i++){
            if(str.charAt(i) == character) return new int[]{startingIndex, i};
        } return new int[]{startingIndex, str.length()};
    }

    public String getQuestion(){
        return question;
    }

    public double getAnswer(){
        return answer;
    }
}
