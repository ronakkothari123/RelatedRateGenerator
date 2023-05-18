package com.relatedrate.Equation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Equation {
    private EquationNode root;

    public Equation(String equation) {
        createEquation(equation);
    }

    public Equation(){
        System.out.println("Initialized");
    }

    private void createEquation(String equation){
        char[] targetOperator = new char[]{'^'};

        ArrayList<EquationNode> nodes = new ArrayList<>();

        for(int i = 0; i < equation.length(); i++){
            //no checks for parenthesis atm
            //System.out.println(equation + " - " + Arrays.toString(targetOperator) + " - " + equation.charAt(i) + " - " + i);

            if(isPresent(targetOperator, equation.charAt(i))){
                int leftBound;
                int rightBound;

                EquationNode newNode = new EquationNode(NodeTypes.OPERATOR_NODE, Character.toString(equation.charAt(i)));

                //create nodes (directional)
                if(equation.charAt(i - 1) == ']'){
                    int[] numSequence = findNumSequence(equation, i - 2, false);

                    newNode.setLeft(nodes.get(Integer.parseInt(equation.substring(numSequence[0], numSequence[1]))));
                    leftBound = numSequence[0] - 1;
                } else if(Character.isDigit(equation.charAt(i - 1))){
                    int[] numSequence = findNumSequence(equation, i - 1, false);

                    newNode.setLeft(new EquationNode(NodeTypes.CONSTANT_NODE, equation.substring(numSequence[0], numSequence[1])));
                    leftBound = numSequence[0];
                } else {
                    newNode.setLeft(new EquationNode(NodeTypes.VARIABLE_NODE, equation.substring(i - 1, i)));
                    leftBound = i - 1;
                }

                if(equation.charAt(i + 1) == '['){
                    int[] numSequence = findNumSequence(equation, i + 2, true);

                    newNode.setRight(nodes.get(Integer.parseInt(equation.substring(numSequence[0], numSequence[1]))));

                    rightBound = numSequence[1] + 1;
                } else if(Character.isDigit(equation.charAt(i + 1))){
                    int[] numSequence = findNumSequence(equation, i + 1, true);

                    newNode.setRight(new EquationNode(NodeTypes.CONSTANT_NODE, equation.substring(numSequence[0], numSequence[1])));
                    rightBound = numSequence[1];
                } else {
                    newNode.setRight(new EquationNode(NodeTypes.VARIABLE_NODE, equation.substring(i + 1, i + 2)));
                    rightBound = i + 2;
                }

                //replace part in equation with [#]
                equation = equation.replace(equation.substring(leftBound, rightBound), "[" + nodes.size() + "]");

                //add node to ArrayList nodes
                nodes.add(newNode);

                i-=2;

            } else if(i == equation.length() - 1){
                if(Arrays.equals(targetOperator, new char[]{'^'})) {
                    targetOperator = new char[]{'/', '*'};
                    i = -1;
                } else if(Arrays.equals(targetOperator, new char[]{'/', '*'})) {
                    targetOperator = new char[]{'-', '+'};
                    i = -1;
                } else if(Arrays.equals(targetOperator, new char[]{'-', '+'})) {
                    targetOperator = new char[]{'='};
                    i = -1;
                }
            }
        }

        root = nodes.get(nodes.size() - 1);
    }

    public void substituteValues(EquationNode targetNode, HashMap<String, Double> variables){
        if(targetNode != null){
            if(variables.containsKey(targetNode.getData()) && variables.get(targetNode.getData()) != null){
                targetNode.setData(NodeTypes.CONSTANT_NODE, String.valueOf(variables.get(targetNode.getData())));
            }
            substituteValues(targetNode.getLeft(), variables);
            substituteValues(targetNode.getRight(), variables);
        }
    }

    public void printEquation(EquationNode node){
        if(node != null){
            printEquation(node.getLeft());
            System.out.print(node.getData());
            printEquation(node.getRight());
        }
    }

    /*private double solveEquation() throws MultipleVariableException {
        char[] targetOperator = new char[]{'-', '+'};

        if(variableCount(root) > 1) throw new MultipleVariableException("Multiple Variables");

        return 20.3;
    }

    private int variableCount(EquationNode node){
        //InOrder Traversal = Left Root Right
        int i = 0;

        if(node!=null){
            i += variableCount(node.getLeft());
            if(node.getType() == NodeTypes.VARIABLE_NODE) i++;
            i += variableCount(node.getRight());
        }

        return i;
    }

    private void printNodeList(ArrayList<EquationNode> nodes){
        for(int i = 0; i < nodes.size(); i++){
            inOrderTraversal(nodes.get(i));
        } System.out.println();
    }

    public static class ContainsExponentException extends Exception {
        public ContainsExponentException(String errorMessage) {
            super(errorMessage);
        }
    }*/

    public static class MultipleVariableException extends Exception {
        public MultipleVariableException(String errorMessage) {
            super(errorMessage);
        }
    }

    private boolean isPresent(char[] array, char target){
        for(char elem : array) {
            if(elem == target) return true;
        } return false;
    }

    private int[] findNumSequence(String equation, int startingIndex, boolean directionRight){
        if(directionRight) {
            for(int i = startingIndex; i < equation.length(); i++){
                if(!Character.isDigit(equation.charAt(i))) return new int[]{startingIndex, i};
            } return new int[]{startingIndex, equation.length()};
        } else {
            for(int i = startingIndex; i >= 0; i--){
                if(!Character.isDigit(equation.charAt(i))) return new int[]{i + 1, startingIndex + 1};
            } return new int[]{0, startingIndex + 1};
        }
    }

    public EquationNode getRoot(){
        return root;
    }
}
