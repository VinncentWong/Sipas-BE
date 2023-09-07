package bcc.sipas.constant;

public class OpenApiConstant {
    public static final String MODEL = "gpt-3.5-turbo";
    public static final String URL = "https://api.openai.com/v1/chat/completions";
    public static final String ROLE = "user";

    public static final String NOTES = """   
    \n
    Notes: make sure the questions above are related to stunting, nutrition, breastfeeding, breast milk, child nutrition, child height, and child weight! 
    IF NOT, then if the questions are in ENGLISH or ANY LANGUAGE other than INDONESIA
    YOU SHOULD return "Question out of context". If it's in INDONESIA LANGUAGE please return "Pertanyaan di luar konteks"
    
    PRIORITY THIS NOTES BEFORE ANSWERING THE QUESTION!
    
    IF THE QUESTION IS RELATED TO STUNTING, NUTRITION, BREASTFEEDING, BREASTMILK, 
    CHILD NUTRITION, CHILD HEIGHT, AND CHILD WEIGHT PLEASE PROVIDE YOUR ANSWER!
    """;
}
