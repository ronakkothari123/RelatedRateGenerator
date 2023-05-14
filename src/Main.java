import javax.swing.*;

import Equation.Equation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        setupGUI();

        boolean gameActive = true;
        List<String> lines = Files.readAllLines(Paths.get("src", "RelatedRate.dat"));

        while(gameActive){
            String randomQuestion = lines.get((int) (Math.random() * lines.size()));

            Equation equation;

            for(int i = 0; i < randomQuestion.length(); i++){
                if(randomQuestion.charAt(i) == '<'){
                    int[] chevronSequence = findCharacterSequence(randomQuestion, i, '>');

                    randomQuestion = randomQuestion.replace(randomQuestion.substring(chevronSequence[0], chevronSequence[1] + 1), Integer.toString((int) (Math.random() * 20)));
                } else if(randomQuestion.charAt(i) == '|'){
                    int[] pipeSequence = findCharacterSequence(randomQuestion, i, '|');

                    equation = new Equation(randomQuestion.substring(pipeSequence[0] + 1, pipeSequence[1]));
                    randomQuestion = randomQuestion.replace(randomQuestion.substring(pipeSequence[0], pipeSequence[1] + 1), "");
                }
            }

            System.out.println(randomQuestion);

            System.out.print("Continue Playing?: ");
            if(scanner.nextLine().equals("no")) gameActive = false;
        }

        System.out.println("Thanks for Playing");
    }

    private static void setupGUI(){
        JFrame frame = new JFrame("Related Rate Question Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280,720);
        frame.setIconImage(new ImageIcon("res\\icon.png", "Related Rate Icon").getImage());
        frame.setVisible(true);
    }

    private static int[] findCharacterSequence(String str, int startingIndex, char character){
        for(int i = startingIndex + 1; i < str.length(); i++){
            if(str.charAt(i) == character) return new int[]{startingIndex, i};
        } return new int[]{startingIndex, str.length()};
    }
}