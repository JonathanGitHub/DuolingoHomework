/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package duolingohomework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.StringUtils;

/**
 * DuolingoGrader is a Java class that provide grading function for student's homework
 * 
 * @author jianyang
 * Last Modified: 2014-10-21
 * Email: cmu dot jonathan at gmail dot com
 */
public class DuolingoGrader {
    
    public static final String CORRECT_ANSWER_EMPTY_MSG = "Correct answer cannot be null or empty";
    public static final String STUDENT_ANSWER_EMTPY_MSG = "Student answer cannot be null";
           
    /**
        String grade(correct_answer, student_answer):
        - correct_answer: a unicode string, the correct answer
        - student_answer: a unicode string, what the student typed  

        returns a tuple (correct, blame, highlights)
            - correct:      a boolean, True if and only if the student_answer 
                        should be considered a correct answer
            - blame:        one out of {None, "typo", "missing", "wrong_word"} 
                        depending on the cause of the mistake, if it can 
                        be detected
            - highlights:   a list of tuples, where each tuple is of type 
                        ((c1, c2), ((s1, s2)) and c1/s1 is the index of the 
                        first character of a blamed word in 
                        the correct/student's answer and c2/s2 is the index
                        of the last character of that same blamed word 
    * 
    */
    public static String grade(String correct_answer, String student_answer) throws IOException{
        
         //edge cases
        if (correct_answer == null || correct_answer.isEmpty()) {
            return CORRECT_ANSWER_EMPTY_MSG;
        }
        if(student_answer == null) {
            return STUDENT_ANSWER_EMTPY_MSG;
        }
        
        //convert both string input into lowercase
        correct_answer = correct_answer.toLowerCase();
        student_answer = student_answer.toLowerCase();
        //generate a highlight list
        List<String> highlights = new ArrayList<String>();
        //instantiate boolean values
        boolean hasTypo = false;
        boolean hasWrong = false;
        boolean hasMissing = false;
       
        
        //trim all white spaces and split the text into a String array      
        List<String> correctList = new ArrayList<String>();
        List<String> studentList = new ArrayList<String>();
        
        String correct_trimmed = correct_answer.trim().toLowerCase();
        String student_trimmed = student_answer.trim().toLowerCase();
        
        String[] correct_split = correct_trimmed.split("[、，。；？！,.;?! \\s+]");
        String[] student_split = student_trimmed.split("[、，。；？！,.;?! \\s+]");
        
        
        int correctLength = correct_split.length;
        int studentLength = student_split.length;
        
        //add trimmed string tokens into arrayLists
        for (int i = 0; i < correctLength; i++){
            if(!correct_split[i].equals("")){
                correctList.add(correct_split[i]);
            }
            
        }
        for (int j = 0; j < studentLength; j++){
            if(!student_split[j].equals("")){
                studentList.add(student_split[j]);
            }
            
        }
              
//        System.out.println("correctList: " + correctList.toString());
//        System.out.println("studentList: " + studentList.toString());
//        
//        System.out.println("correctList size: " + correctList.size());
//        System.out.println("studentList size: " + studentList.size());
        
        
        //typo or wrong words
        if (correctLength == studentLength) {
            int numberOfCorrect = 0;
            //compare each element from correctList with its counterpart from studentList
            for(int k = 0; k < correctLength; k++){
                if (!studentList.isEmpty() && correctList.get(k).equals(studentList.get(k))){
                    numberOfCorrect++;
                } else{
                    if(!studentList.isEmpty() && StringUtils.minDistance(correctList.get(k), studentList.get(k)) == 1 && !StringUtils.isValidWord(studentList.get(k))){
                        hasTypo = true;
                        numberOfCorrect++;
                        //find the indexes of the correct words
                        int startOfCorrect = correct_answer.indexOf(correctList.get(k));
                        int endOfCorrect = startOfCorrect + correctList.get(k).length();
                        //find the indexes of blamed words
                        int startOfStu = student_answer.indexOf(studentList.get(k));
                        int endOfStu = startOfStu + studentList.get(k).length();
                        String hightlight = StringUtils.generateHighlights(startOfCorrect, endOfCorrect, startOfStu, endOfStu);
                        highlights.add(hightlight);
                    } else if(!studentList.isEmpty() && StringUtils.minDistance(correctList.get(k), studentList.get(k)) > 1){//wrong words
                        hasWrong = true;
                        //find the indexes of the correct words
                        int startOfCorrect = correct_answer.indexOf(correctList.get(k));
                        int endOfCorrect = startOfCorrect + correctList.get(k).length();
                        //find the indexes of blamed words
                        int startOfStu = student_answer.indexOf(studentList.get(k));
                        int endOfStu = startOfStu + studentList.get(k).length();
                        String hightlight = StringUtils.generateHighlights(startOfCorrect, endOfCorrect, startOfStu, endOfStu);
                        highlights.add(hightlight);
                    }
                }
            }
            if(numberOfCorrect == correctLength ){//how about edge case? correctLength = 1
                if(hasTypo){
                    return "True, \"typo\", " + highlights.toString();
                }else{
                    return "True, None, []";
                }
                
            } else if(correctLength - numberOfCorrect == 1 ){
//               System.out.println("numberOfCorrect: " + numberOfCorrect);
//               System.out.println("correctLength: " + correctLength);
               return "False, \"wrong_word\", " + highlights.toString();
            } else {//two wrong words
                return "False, None, []";
            }
        } else if (correctLength - studentLength == 1){ //missing word, assumes that only one word
            int correct_index = 0;
            for(int k = 0; k < studentLength; k++) {
                //this is my house    is my house
                //this is my house    this is house
                
                /*
                correct_answer: this is my house.
                correctList.get(1): is
                studentList.get(1): my
                startOfCorrect: 2
                computer thought Th"is" is the answer, not This "is"
                Solution: Add a space before "is"
                */

                if(!correctList.get(correct_index).equals(studentList.get(k))) {//be sure to use equals, not ==
                    //find the indexes of the correct words
                        int startOfCorrect = correct_answer.indexOf(" " + correctList.get(correct_index)) + 1;
                        int endOfCorrect = startOfCorrect + correctList.get(correct_index).length();
//                        System.out.println("correct_answer: " + correct_answer);
//                        System.out.println("correctList.get(" + correct_index + "): " + correctList.get(correct_index));
//                        System.out.println("studentList.get(" + k + "): " + studentList.get(k));
//                        System.out.println("startOfCorrect: " + startOfCorrect);
//                        System.out.println("endOfCorrect: " + endOfCorrect);
                        //find the indexes of blamed words
                        int startOfStu = startOfCorrect;
                        int endOfStu = startOfCorrect;
                        String hightlight = StringUtils.generateHighlights(startOfCorrect, endOfCorrect, startOfStu, endOfStu);
                        highlights.add(hightlight);
                        
                        correct_index++;                       
                }
                correct_index++;
                
            }
             return "False, \"missing\", " + highlights.toString();
        } else if(correctLength - studentLength > 1) {
            return "False, \"none\", []";
        }
        else if(correctLength < studentLength){//wrong words(student input has more words
            return "False, \"none\", []";
        }        
        return "";
    }  
    
}
