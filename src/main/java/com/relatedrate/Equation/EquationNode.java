package com.relatedrate.Equation;

public class EquationNode {
    private EquationNode left;
    private EquationNode right;
    private NodeTypes type;
    private String data;

    public EquationNode(NodeTypes type, String data){
        this.type = type;
        this.data = data;
    }

    public void setLeft(EquationNode left){
        this.left = left;
    }

    public void setRight(EquationNode right){
        this.right = right;
    }

    public void setData(NodeTypes type, String data){
        this.type = type;
        this.data = data;
    }

    public EquationNode getLeft() {
        return left;
    }

    public EquationNode getRight() {
        return right;
    }

    public String getData(){
        return data;
    }
}