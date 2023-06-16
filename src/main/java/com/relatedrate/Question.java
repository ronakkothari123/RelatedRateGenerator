package com.relatedrate;

import com.relatedrate.Equation.Equation;
import com.relatedrate.Equation.Equation.MultipleVariableException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Question {
    private String question;
    private final QuestionType questionType;
    private final HashMap<String, Double> variables = new HashMap<>();
    private final HashMap<String, String> preconditions = new HashMap<>();
    private String answer;

    private final int maxNum = 25;

    //creates question based on questionType
    public Question(QuestionType questionType) throws IOException, MultipleVariableException {
        this.questionType = questionType;
        generateQuestion();
    }

    //returns true if successful
    private boolean generateQuestion() throws IOException, MultipleVariableException {
        List<String> lines;

        if (questionType == QuestionType.RELATED_RATE)
            lines = Files.readAllLines(Paths.get("src", "main", "java", "com", "relatedrate", "RelatedRate.dat"));
        else {
            System.out.println("Question Type Not Supported");
            return false;
        }

        question = lines.get((int) (Math.random() * lines.size()));

        Equation equation = new Equation("X=1");

        for (int i = question.length() - 1; i >= 0; i--) {
            if (question.charAt(i) == ':') {
                int[] colonSequence = findCharacterSequence(question, i, ':', false);

                int randomValue = 0;

                //System.out.println(question.substring(colonSequence[0] + 1, colonSequence[1]));

                String precondition = preconditions.get(question.substring(colonSequence[0] + 1, colonSequence[1]));

                if (precondition == null) {
                    randomValue = (int) (Math.random() * maxNum);
                } else if (precondition.charAt(0) == '=') {
                    int[] numSequence = findNumSequence(precondition, 1, true);
                    randomValue = Integer.parseInt(precondition.substring(numSequence[0], numSequence[1]));
                } else if (precondition.charAt(0) == '>') {
                    int[] numSequence = findNumSequence(precondition, 1, true);
                    randomValue = (int) (Math.random() * (maxNum - Integer.parseInt(precondition.substring(numSequence[0], numSequence[1])) - 1) + Integer.parseInt(precondition.substring(numSequence[0], numSequence[1])) + 1);
                } else {
                    int[] numSequence = findNumSequence(precondition, 1, true);
                    randomValue = (int) (Math.random() * Integer.parseInt(precondition.substring(numSequence[0], numSequence[1])));
                }

                //checks to update HashMap
                if (variables.containsKey(question.substring(colonSequence[0] + 1, colonSequence[1])))
                    variables.replace(question.substring(colonSequence[0] + 1, colonSequence[1]), (double) randomValue);
                i -= question.substring(colonSequence[0], colonSequence[1] + 1).length() + 1;
                question = question.replace(question.substring(colonSequence[0], colonSequence[1] + 1), Integer.toString(randomValue));
            } else if (question.charAt(i) == '|') {
                int[] pipeSequence = findCharacterSequence(question, i, '|', false);

                char[] operators = new char[]{'+', '-', '/', '*', '^', '='};

                for (int j = pipeSequence[0] + 1; j < pipeSequence[1]; j++) {
                    if (!Character.isDigit(question.charAt(j)) && !arrayContainsChar(operators, question.charAt(j)) && question.charAt(j) != 'd') {
                        variables.put(Character.toString(question.charAt(j)), null);
                        variables.put("d" + question.charAt(j), null);
                    }
                }

                equation = new Equation(question.substring(pipeSequence[0] + 1, pipeSequence[1]));
                i -= question.substring(pipeSequence[0], pipeSequence[1]).length();
                question = question.replace(question.substring(pipeSequence[0], pipeSequence[1] + 1), "");
            } else if (question.charAt(i) == '}') {
                int[] curlyBraceSequence = findCharacterSequence(question, i, '{', false);
                char[] comparisonOperators = new char[]{'<', '>', '=', ';'};

                for (int j = curlyBraceSequence[0] + 1; j < curlyBraceSequence[1]; j++) {
                    if (!Character.isDigit(question.charAt(j)) && !arrayContainsChar(comparisonOperators, question.charAt(j))) {
                        if (question.charAt(j) == 'd') {
                            int[] numSequence = findNumSequence(question, j + 3, true);
                            preconditions.put(question.substring(j, j + 2), question.charAt(j + 2) + question.substring(numSequence[0], numSequence[1]));
                        } else {
                            int[] numSequence = findNumSequence(question, j + 2, true);
                            preconditions.put(question.substring(j, j + 1), question.charAt(j + 1) + question.substring(numSequence[0], numSequence[1]));
                        }
                    }
                }

                i -= question.substring(curlyBraceSequence[0], curlyBraceSequence[1]).length();
                question = question.replace(question.substring(curlyBraceSequence[0], curlyBraceSequence[1] + 1), "");
            } else if (question.charAt(i) == ']') {
                int[] pipeSequence = findCharacterSequence(question, i, '[', false);

                answer = question.substring(pipeSequence[0] + 1, pipeSequence[1]);

                i -= question.substring(pipeSequence[0], pipeSequence[1]).length();
                question = question.replace(question.substring(pipeSequence[0], pipeSequence[1] + 1), "");
            }
        }

        equation.substituteValues(variables);
        Map<String, Double> rootAnswer = equation.solveEquation(equation.getRoot());

        variables.putAll(rootAnswer);
        equation.substituteValues(variables);

        Map<String, Double> derivativeAnswer = equation.solveEquation(equation.getDerivative());
        variables.putAll(derivativeAnswer);

        System.out.println(getAnswer());

        /*for (Map.Entry<String, Double> entry : equation.solveEquation(equation.getRoot()).entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }*/

        return true;
    }

    private boolean arrayContainsChar(char[] array, char target) {
        boolean returnVal = false;

        for (char character : array) {
            if (character == target) {
                returnVal = true;
                break;
            }
        }
        return returnVal;
    }

    private int[] findCharacterSequence(String str, int index, char character, boolean directionRight) {
        if (directionRight) {
            for (int i = index + 1; i < str.length(); i++) {
                if (str.charAt(i) == character) return new int[]{index, i};
            }
            return new int[]{index, str.length()};
        } else {
            for (int i = index - 1; i >= 0; i--) {
                if (str.charAt(i) == character) return new int[]{i, index};
            }
            return new int[]{0, index};
        }
    }

    private int[] findNumSequence(String equation, int startingIndex, boolean directionRight) {
        if (directionRight) {
            for (int i = startingIndex; i < equation.length(); i++) {
                if (!Character.isDigit(equation.charAt(i))) return new int[]{startingIndex, i};
            }
            return new int[]{startingIndex, equation.length()};
        } else {
            for (int i = startingIndex; i >= 0; i--) {
                if (!Character.isDigit(equation.charAt(i))) return new int[]{i + 1, startingIndex + 1};
            }
            return new int[]{0, startingIndex + 1};
        }
    }

    public String getQuestion() {
        return question;
    }

    public double getAnswer() {
        return variables.get(answer);
    }
}
