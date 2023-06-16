package com.relatedrate.Equation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Equation {
    private EquationNode root;
    private EquationNode derivative;

    public Equation(String equation) throws MultipleVariableException {
        createEquation(equation);
        derivative = getDerivative(root);
    }

    private void createEquation(String equation) {
        char[] targetOperator = new char[]{'^'};

        ArrayList<EquationNode> nodes = new ArrayList<>();

        for (int i = 0; i < equation.length(); i++) {
            //no checks for parenthesis atm
            //System.out.println(equation + " - " + Arrays.toString(targetOperator) + " - " + equation.charAt(i) + " - " + i);

            if (isPresent(targetOperator, equation.charAt(i))) {
                int leftBound;
                int rightBound;

                EquationNode newNode = new EquationNode(NodeTypes.OPERATOR_NODE, Character.toString(equation.charAt(i)));

                //create nodes (directional)
                if (equation.charAt(i - 1) == ']') {
                    int[] numSequence = findNumSequence(equation, i - 2, false);

                    newNode.setLeft(nodes.get(Integer.parseInt(equation.substring(numSequence[0], numSequence[1]))));
                    leftBound = numSequence[0] - 1;
                } else if (Character.isDigit(equation.charAt(i - 1))) {
                    int[] numSequence = findNumSequence(equation, i - 1, false);

                    newNode.setLeft(new EquationNode(NodeTypes.CONSTANT_NODE, equation.substring(numSequence[0], numSequence[1])));
                    leftBound = numSequence[0];
                } else {
                    newNode.setLeft(new EquationNode(NodeTypes.VARIABLE_NODE, equation.substring(i - 1, i)));
                    leftBound = i - 1;
                }

                if (equation.charAt(i + 1) == '[') {
                    int[] numSequence = findNumSequence(equation, i + 2, true);

                    newNode.setRight(nodes.get(Integer.parseInt(equation.substring(numSequence[0], numSequence[1]))));

                    rightBound = numSequence[1] + 1;
                } else if (Character.isDigit(equation.charAt(i + 1))) {
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

                i -= 2;

            } else if (i == equation.length() - 1) {
                if (Arrays.equals(targetOperator, new char[]{'^'})) {
                    targetOperator = new char[]{'/', '*'};
                    i = -1;
                } else if (Arrays.equals(targetOperator, new char[]{'/', '*'})) {
                    targetOperator = new char[]{'-', '+'};
                    i = -1;
                } else if (Arrays.equals(targetOperator, new char[]{'-', '+'})) {
                    targetOperator = new char[]{'='};
                    i = -1;
                }
            }
        }

        root = nodes.get(nodes.size() - 1);
    }

    public Map<String, Double> solveEquation(EquationNode root) {
        if (!hasOnlyOneVariable(root)) {
            throw new IllegalArgumentException("Equation does not have exactly one variable");
        }

        EquationNode isolated = isolateVariable(root, hasOnlyOneVariable(root.getLeft()));

        HashMap<String, Double> answer = new HashMap<>();

        if (hasOnlyOneVariable(isolated.getLeft())) {
            answer.put(isolated.getLeft().getData(), simplifyEquation(isolated.getRight()));
            return answer;
        } else {
            answer.put(isolated.getRight().getData(), simplifyEquation(isolated.getLeft()));
            return answer;
        }
    }


    public EquationNode isolateVariable(EquationNode node, boolean isVariableOnLeft) {
        if (isVariableOnLeft) {
            if (node.getLeft().getType() == NodeTypes.VARIABLE_NODE) {
                return node;
            }

            if (hasOnlyOneVariable(node.getLeft().getLeft())) {
                //Expression on right hand side
                if (node.getLeft().getData().equals("+")) {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "-", node.getRight(), node.getLeft().getRight()));
                } else if (node.getLeft().getData().equals("-")) {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "+", node.getRight(), node.getLeft().getRight()));
                } else if (node.getLeft().getData().equals("*")) {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "/", node.getRight(), node.getLeft().getRight()));
                } else if (node.getLeft().getData().equals("/")) {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "*", node.getRight(), node.getLeft().getRight()));
                } else if (node.getLeft().getData().equals("^")) {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "root", node.getRight(), node.getLeft().getRight()));
                } else if (node.getLeft().getData().equals("root")) {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "^", node.getRight(), node.getLeft().getRight()));
                }
                node.setLeft(node.getLeft().getLeft());
            } else {
                //Expression on left hand side
                if (node.getLeft().getData() == "+") {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "-", node.getRight(), node.getLeft().getLeft()));
                } else if (node.getLeft().getData().equals("-")) {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "+", node.getRight(), node.getLeft().getRight()));
                } else if (node.getLeft().getData().equals("*")) {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "/", node.getRight(), node.getLeft().getLeft()));
                } else if (node.getLeft().getData().equals("/")) {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "*", node.getRight(), node.getLeft().getRight()));
                } else if (node.getLeft().getData().equals("^")) {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "root", node.getRight(), node.getLeft().getRight()));
                } else if (node.getLeft().getData().equals("root")) {
                    node.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "^", node.getRight(), node.getLeft().getRight()));
                }
                node.setLeft(node.getLeft().getRight());
            }
        } else {
            if (node.getRight().getType() == NodeTypes.VARIABLE_NODE) {
                return node;
            }

            if (hasOnlyOneVariable(node.getRight().getLeft())) {
                //Expression on right hand side
                if (node.getRight().getData() == "+") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "-", node.getRight(), node.getRight().getRight()));
                } else if (node.getRight().getData() == "-") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "+", node.getRight(), node.getRight().getRight()));
                } else if (node.getRight().getData() == "*") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "/", node.getRight(), node.getRight().getRight()));
                } else if (node.getRight().getData() == "/") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "*", node.getRight(), node.getRight().getRight()));
                } else if (node.getRight().getData() == "^") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "root", node.getRight(), node.getRight().getRight()));
                } else if (node.getRight().getData() == "root") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "^", node.getRight(), node.getRight().getRight()));
                }
                node.setRight(node.getRight().getLeft());
            } else {
                //Expression on left hand side
                if (node.getRight().getData() == "+") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "-", node.getRight(), node.getRight().getLeft()));
                } else if (node.getRight().getData() == "-") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "+", node.getRight(), node.getRight().getRight()));
                } else if (node.getRight().getData() == "*") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "/", node.getRight(), node.getRight().getLeft()));
                } else if (node.getRight().getData() == "/") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "*", node.getRight(), node.getRight().getRight()));
                } else if (node.getRight().getData() == "^") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "root", node.getRight(), node.getRight().getRight()));
                } else if (node.getRight().getData() == "root") {
                    node.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "^", node.getRight(), node.getRight().getRight()));
                }
                node.setRight(node.getRight().getRight());
            }
        }

        return isolateVariable(node, hasOnlyOneVariable(node.getLeft()));
    }

    public double simplifyEquation(EquationNode node) {
        if (node.getType() == NodeTypes.CONSTANT_NODE) {
            return Double.parseDouble(node.getData());
        } else if (node.getType() == NodeTypes.OPERATOR_NODE) {
            double left = simplifyEquation(node.getLeft());
            double right = simplifyEquation(node.getRight());
            String operator = node.getData();

            switch (operator) {
                case "+":
                    return left + right;
                case "-":
                    return left - right;
                case "*":
                    return left * right;
                case "/":
                    return left / right;
                case "^":
                    return Math.pow(left, right);
                case "root":
                    return Math.pow(left, 1 / right);
                default:
                    throw new IllegalArgumentException("Invalid operator: " + operator);
            }
        } else {
            throw new IllegalArgumentException("Invalid node type: " + node.getType());
        }
    }

    public boolean hasOnlyOneVariable(EquationNode node) {
        if (node == null) {
            return false;
        }
        int variableCount = 0;
        if (node.getType() == NodeTypes.VARIABLE_NODE) {
            variableCount++;
        }
        if (node.getLeft() != null) {
            variableCount += hasOnlyOneVariable(node.getLeft()) ? 1 : 0;
        }
        if (node.getRight() != null) {
            variableCount += hasOnlyOneVariable(node.getRight()) ? 1 : 0;
        }
        return variableCount == 1;
    }

    public void substituteValues(HashMap<String, Double> variables) {
        substituteValues(variables, root);
        substituteValues(variables, derivative);
    }

    public void substituteValues(HashMap<String, Double> variables, EquationNode node) {
        if (node == null) return;
        if (node.getType() == NodeTypes.VARIABLE_NODE) {
            Double value = variables.get(node.getData());
            if (value != null) {
                node.setData(NodeTypes.CONSTANT_NODE, Double.toString(value));
            }
        }
        substituteValues(variables, node.getLeft());
        substituteValues(variables, node.getRight());
    }

    private EquationNode getDerivative(EquationNode root) //returns the derived equation in the form of a tree
    {
        if (root == null)
            return null;

        EquationNode newTree = new EquationNode(null, null);

        if (root.getType() == NodeTypes.VARIABLE_NODE)
            newTree.setData(NodeTypes.VARIABLE_NODE, "d" + root.getData());
        else if (root.getType() == NodeTypes.CONSTANT_NODE)
            newTree.setData(NodeTypes.CONSTANT_NODE, "0");
        else //if it's an operation, use recursion to get derivative of left and right
        {
            newTree.setData(root.getType(), root.getData());
            getDerivative(root.getLeft());
            getDerivative(root.getRight());
        }

        String operation = root.getData();

        switch (operation) //depending on operation calls a specific method to derive
        {
            case "+", "-", "=":
                newTree.setLeft(getDerivative(root.getLeft()));
                newTree.setRight(getDerivative(root.getRight()));
                break;
            case "*":
                newTree = productRule(root);
                break;
            case "/":
                newTree = quotientRule(root);
                break;
            case "^":
                newTree = chainRule(root);
        }

        return newTree;
    }

    private EquationNode chainRule(EquationNode root) {
        EquationNode newTree = new EquationNode(NodeTypes.OPERATOR_NODE, "*");

        newTree.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "*"));
        newTree.getLeft().setLeft(root.getRight());
        newTree.getLeft().setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "^"));
        newTree.getLeft().getRight().setLeft(root.getLeft());
        newTree.getLeft().getRight().setRight(new EquationNode(NodeTypes.CONSTANT_NODE, Integer.toString(Integer.parseInt(root.getRight().getData()) - 1))); //power rule, exponent-1
        newTree.setRight(getDerivative(root.getLeft()));

        return newTree;
    }

    private EquationNode productRule(EquationNode root) {
        EquationNode newTree = new EquationNode(NodeTypes.VARIABLE_NODE, "+");

        newTree.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "*"));
        newTree.getLeft().setLeft(getDerivative(root.getLeft()));
        newTree.getLeft().setRight(root.getRight());
        newTree.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "*"));
        newTree.getRight().setLeft(root.getLeft());
        newTree.getRight().setRight(getDerivative(root.getRight()));

        return newTree;
    }

    private EquationNode quotientRule(EquationNode root) {
        EquationNode newTree = new EquationNode(NodeTypes.VARIABLE_NODE, "/");

        newTree.setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "-")); //left of the fraction is the same as product rule but with -
        newTree.getLeft().setLeft(new EquationNode(NodeTypes.OPERATOR_NODE, "*"));
        newTree.getLeft().getLeft().setLeft(getDerivative(root.getLeft()));
        newTree.getLeft().getLeft().setRight(root.getRight());
        newTree.getLeft().setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "*"));
        newTree.getLeft().getRight().setLeft(root.getLeft());
        newTree.getLeft().getRight().setRight(getDerivative(root.getRight()));
        newTree.setRight(new EquationNode(NodeTypes.OPERATOR_NODE, "^")); //right of the fraction is the right node squared
        newTree.getRight().setLeft(root.getRight());
        newTree.getRight().setRight(new EquationNode(NodeTypes.CONSTANT_NODE, "2"));

        return newTree;
    }

    public void inOrderTraversal(EquationNode node) {
        //InOrder Traversal = Left Root Right
        if (node != null) {
            inOrderTraversal(node.getLeft());
            System.out.print(node.getData());
            inOrderTraversal(node.getRight());
        }
    }

    /*private void printNodeList(ArrayList<EquationNode> nodes){
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

    private boolean isPresent(char[] array, char target) {
        for (char elem : array) {
            if (elem == target) return true;
        }
        return false;
    }

    private int[] findNumSequence(String equation, int startingIndex, boolean directionRight) {
        if (directionRight) {
            for (int i = startingIndex; i < equation.length(); i++) {
                if (!Character.isDigit(equation.charAt(i)) && equation.charAt(i) != '.')
                    return new int[]{startingIndex, i};
            }
            return new int[]{startingIndex, equation.length()};
        } else {
            for (int i = startingIndex; i >= 0; i--) {
                if (!Character.isDigit(equation.charAt(i)) && equation.charAt(i) != '.')
                    return new int[]{i + 1, startingIndex + 1};
            }
            return new int[]{0, startingIndex + 1};
        }
    }

    public EquationNode getRoot() {
        return root;
    }

    public EquationNode getDerivative() {
        return derivative;
    }
}
