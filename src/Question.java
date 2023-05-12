public class Question {
    private String question;
    private QuestionType questionType;
    private double answer;

    //creates question based on questionType
    public Question(QuestionType questionType){
        this.questionType = questionType;
        generateQuestion();
    }

    //returns true if successful
    private boolean generateQuestion(){
        return true;
    }

    public String getQuestion(){
        return question;
    }

    public double getAnswer(){
        return answer;
    }
}
