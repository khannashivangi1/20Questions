// Shivangi Khanna

// Implements QuestionGame class which is used to play the game of 20 questions.
// The user answers a series of questions which helps in building the question tree and predicting 
// what the user is guessing based on the previous answers. It can also save the question tree for 
// further use. 

import java.util.*;
import java.io.*;

public class QuestionsGame {
    private Scanner console;
    private QuestionNode questionTree;
    
    // Constructor to initialize QuestionGame object to a leafnode. It contains only "computer"
    public QuestionsGame() {
       console = new Scanner(System.in);
       questionTree = new QuestionNode("computer");
    }
    
    // Saves and stores the question tree to an output file. It writes the questions and the answers
    // in a pre-order syntax.
    public void write(PrintStream output) {
       writeHelper(output, questionTree);
    }
    
    // Runs through the tree and writes the contents in a file. 
    private void writeHelper(PrintStream output, QuestionNode questionTree) {
       if(questionTree != null) {
          if(questionTree.right != null && questionTree.left != null) {
             output.println("Q:");
             output.println(questionTree.data);
             writeHelper(output, questionTree.left);
             writeHelper(output, questionTree.right);
          } else {
             output.println("A:");
             output.println(questionTree.data);
          }
       }
    }
    
    // Replaces the current tree with the contents of the file passed. 
    // Reads the files in a pre-order syntax.
    public void read(Scanner input) {
       questionTree = readHelper(input);
    }
    
    // Forms the question tree by reading the file passed.
    private QuestionNode readHelper(Scanner input) {
       QuestionNode root = new QuestionNode(input.nextLine());
       if(input.nextLine().equals("Q:")) {
          root.left = readHelper(input);
          root.right = readHelper(input);
       }
       return root;
    }
    
    // Plays one game of 20 questions with the current question tree.
    // Asks the user for the correct answer and a question, that can be asnwered with a yes 
    // or a no, that can differentiate between the two answers, and adds the questions and 
    // answers to the question tree, if the prediction is wrong. 
    // It prints an appreciation message if the prediction is right. 
    
    public void askQuestions() {
       questionTree = askQuestionsHelper(questionTree);
    }
    
    // Asks questions to the user. 
    private QuestionNode askQuestionsHelper(QuestionNode root) {
       if(root != null) {
          boolean input;
          if(root.right != null && root.left != null) {
             input = yesTo(root.data);
             if(input) {
                root.left = askQuestionsHelper(root.left);
             } else {
                root.right = askQuestionsHelper(root.right);
             }
          } else {
             input = yesTo("Would your object happen to be " + root.data + "?");
             if(input) {
                System.out.println("Great, I got it right!");
             } else {
                return update(root);
             }
          }
       }
       return root;
    }
    
    // Updates the question tree with the question and answer. 
    private QuestionNode update(QuestionNode root) {
       System.out.print("What is the name of your object? ");
       QuestionNode answer = new QuestionNode(console.nextLine());
       System.out.println("Please give me a yes/no question that");
       System.out.println("distinguishes between your object");
       System.out.print("and mine--> ");
       QuestionNode question = new QuestionNode(console.nextLine());
       boolean input = yesTo("And what is the answer for your object?");
       QuestionNode temp = root;
       root = question;
       if(input) {
          root.left = answer;
          root.right = temp;
       } else {
          root.left = temp;
          root.right = answer;
       }
       return root;
    }      

    // post: asks the user a question, forcing an answer of "y" or "n";
    //       returns true if the answer was yes, returns false otherwise
    public boolean yesTo(String prompt) {
        System.out.print(prompt + " (y/n)? ");
        String response = console.nextLine().trim().toLowerCase();
        while (!response.equals("y") && !response.equals("n")) {
            System.out.println("Please answer y or n.");
            System.out.print(prompt + " (y/n)? ");
            response = console.nextLine().trim().toLowerCase();
        }
        return response.equals("y");
    }

    // Creates a question node used by the QuestionGame class. 
    private static class QuestionNode {
    
       public String data;
       public QuestionNode left; 
       public QuestionNode right;
       
       // Constructs QuestionNode object with the entered data and left and right as null. 
       public QuestionNode(String data) {
          this(data, null, null);
       }
       
       // Constructs QuestionNode object from the entered data.
       // Left leaf and Right leaf point to some other QuestionNode object.
       public QuestionNode(String data, QuestionNode left, QuestionNode right) {
          this.data = data;
          this.left = left;
          this.right = right;
       }
    }
}
