package Equation;

public class Equation {
    private EquationNode root;

    public Equation(String equation){
        createEquation(equation);
    }

    private void createEquation(String equation){
        boolean[] checked = new boolean[equation.length()];
        boolean treeComplete = false;

        while(!treeComplete){
            treeComplete = true;

            for(int i = 0; i < checked.length; i++){
                if(!checked[i]) treeComplete = false;
            }
        }
    }

    public EquationNode getRoot(){
        return root;
    }
}
