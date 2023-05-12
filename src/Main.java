import javax.swing.*;

import Equation.Equation;

public class Main {
    public static void main(String[] args){
        setupGUI();
        Equation equation = new Equation("x^2+2*x+1");
    }

    public static void setupGUI(){
        JFrame frame = new JFrame("Related Rate Question Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280,720);
        frame.setIconImage(new ImageIcon("res\\icon.png", "Related Rate Icon").getImage());
        frame.setVisible(true);
    }
}