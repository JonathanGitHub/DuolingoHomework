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
    public static final String STUDENT_ANSWER_EMTPY_MSG = "Student answer cannot be null or empty";
           
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
        String input = ">>> grade(\"" + correct_answer + "\", " + "\"" + student_answer + 
                "\")" + "\n";
         //edge cases
        if (correct_answer == null || correct_answer.isEmpty()) {
            return CORRECT_ANSWER_EMPTY_MSG;
        }
        if(student_answer == null || student_answer.isEmpty()) {
            return STUDENT_ANSWER_EMTPY_MSG;
        }
        
        //convert both string input into lowercase
        correct_answer = correct_answer.toLowerCase();
        student_answer = student_answer.toLowerCase();
        //generate a highlight list
        ArrayList<String> highlights = new ArrayList<String>();
        //instantiate boolean values
        boolean hasTypo = false;
        boolean hasWrong = false;
        boolean hasMissing = false;
        
        //trim all white spaces and split the text into a String array      
        ArrayList<String> correctList = new ArrayList<String>();
        ArrayList<String> studentList = new ArrayList<String>();
        
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
        
        //Case 1: if two lists have same length, then result is any of the the three[typo, wrong, none]
        if (correctLength == studentLength) {
            int numberOfCorrect = 0;//denote number of correct words from student's answer
            //compare each element from correctList with its counterpart from studentList
            for(int k = 0; k < correctLength; k++){
                if (!studentList.isEmpty() && correctList.get(k).equals(studentList.get(k))){
                    numberOfCorrect++;
                } else{
                    //typo detection
                    if(!studentList.isEmpty() && StringUtils.minDistance(correctList.get(k), studentList.get(k)) == 1
                                && !StringUtils.isValidWord(studentList.get(k))){
                        hasTypo = true;
                        numberOfCorrect++;
                        prepareHighlights(correctList, studentList, k, correct_answer, student_answer, highlights);
                    } 
                    //wrong word detection
                    else if(!studentList.isEmpty() && StringUtils.minDistance(correctList.get(k), studentList.get(k)) == 1 
                                && StringUtils.isValidWord(studentList.get(k))){
                         hasWrong = true;
                         prepareHighlights(correctList, studentList, k, correct_answer, student_answer, highlights);
                    }
                    //wrong word detection
                    else if(!studentList.isEmpty() && StringUtils.minDistance(correctList.get(k), studentList.get(k)) > 1){
                        hasWrong = true;
                        prepareHighlights(correctList, studentList, k, correct_answer, student_answer, highlights);
                    }
                }
            }
            
            if(numberOfCorrect == correctLength ){
                if(hasTypo){
                    return input + "(True, \"typo\", " + highlights.toString() + ")" + "\n";
                }else{
                    return input + "(True, None, [])" + "\n";
                }
                
            } else if(correctLength - numberOfCorrect == 1 ){
               return input + "(False, \"wrong_word\", " + highlights.toString() + ")" + "\n";
            } else {//two wrong words
                return input + "(False, None, [])" + "\n";
            }
        } 
        
        //Case 2: missing word case
        else if (correctLength - studentLength == 1){ //missing word, assumes that only one word
            int correct_index = 0;
            for(int k = 0; k < studentLength; k++) {
                if(!correctList.get(correct_index).equals(studentList.get(k))) {             
                    prepareMissingWordHighlights(correctList, studentList, correct_index, correct_answer, student_answer, highlights);
                    correct_index++;                       
                }
                correct_index++;          
            }
            //Take care of missing word if the position is in the last
            //Of course this logic won't deal with "me me me" "me me" case but that kind of 
            //input rarely exist in real world 
            if(highlights.isEmpty()){//student's answer miss the last correct word
                prepareMissingWordHighlights(correctList, studentList, correct_index, correct_answer, student_answer, highlights);
            }
             return input + "(False, \"missing\", " + highlights.toString() + ")" + "\n";
        } else if(correctLength - studentLength > 1) {
            return input + "(False, None, [])" + "\n";
            
        }
        
        //Case 3: False case
        else if(correctLength < studentLength){
            return input + "(False, None, [])" + "\n";
        } 
        //Default return
        return "";
    }  
    
    //private helper method to prepare Highlights of blamed words
    private static void prepareHighlights(ArrayList<String> correctList, ArrayList<String> studentList, 
                int k, String correct_answer, String student_answer, ArrayList<String> highlights){
        //find the indexes of the correct words
        int startOfCorrect = correct_answer.indexOf(correctList.get(k));
        int endOfCorrect = startOfCorrect + correctList.get(k).length();
        //find the indexes of blamed words
        int startOfStu = student_answer.indexOf(studentList.get(k));
        int endOfStu = startOfStu + studentList.get(k).length();
        String highlight = StringUtils.generateHighlights(startOfCorrect, endOfCorrect, startOfStu, endOfStu);
        //check whether a highlight exits in hightlights list, if so, iterate till the next one
        String candidateCorrect = highlight.substring(0, highlight.indexOf("), "));
        String candidateStu = highlight.substring(highlight.indexOf("), "));
        while(isHighlightedBefore(highlights, candidateCorrect)){
            startOfCorrect = endOfCorrect + correct_answer.substring(endOfCorrect).indexOf(correctList.get(k));
            endOfCorrect = startOfCorrect + correctList.get(k).length();
            highlight = StringUtils.generateHighlights(startOfCorrect, endOfCorrect, startOfStu, endOfStu);
            candidateCorrect = highlight.substring(0, highlight.indexOf("), "));
        }
        while(isHighlightedBefore(highlights, candidateStu)){
            startOfStu = endOfStu + student_answer.substring(endOfStu).indexOf(studentList.get(k));
            endOfStu = startOfStu + studentList.get(k).length();
            highlight = StringUtils.generateHighlights(startOfCorrect, endOfCorrect, startOfStu, endOfStu);
            candidateStu = highlight.substring(0, highlight.indexOf("), "));
        }
        
        highlights.add(highlight);      
    }  
    
    //private helper method to prepare Highlights of missing word
    private static void prepareMissingWordHighlights(ArrayList<String> correctList, ArrayList<String> studentList, 
                int correct_index, String correct_answer, String student_answer, ArrayList<String> highlights){
        //find the indexes of the correct words
         int startOfCorrect = correct_answer.indexOf(" " + correctList.get(correct_index)) + 1;
         int endOfCorrect = startOfCorrect + correctList.get(correct_index).length();
         //find the indexes of blamed words
         int startOfStu = startOfCorrect;
         int endOfStu = startOfCorrect;
         String highlight = StringUtils.generateHighlights(startOfCorrect, endOfCorrect, startOfStu, endOfStu);
         //check whether a highlight exits in hightlights list, if so, iterate till the next one
         String candidateCorrect = highlight.substring(0, highlight.indexOf("), "));
         String candidateStu = highlight.substring(highlight.indexOf("), "));
         while(isHighlightedBefore(highlights, candidateCorrect)){
             startOfCorrect = endOfCorrect + correct_answer.substring(endOfCorrect).indexOf(" " + correctList.get(correct_index)) + 1;
             endOfCorrect = startOfCorrect + correctList.get(correct_index).length();
             highlight = StringUtils.generateHighlights(startOfCorrect, endOfCorrect, startOfStu, endOfStu);
             candidateCorrect = highlight.substring(0, highlight.indexOf("), "));
         }
         while(isHighlightedBefore(highlights, candidateStu)){
             startOfStu = startOfCorrect;
             endOfStu = endOfCorrect;
             highlight = StringUtils.generateHighlights(startOfCorrect, endOfCorrect, startOfStu, endOfStu);
             candidateStu = highlight.substring(0, highlight.indexOf("), "));
         }
        
         highlights.add(highlight);   
    }  
    
    //private helper to check whether a highlight has been used before
    private static boolean isHighlightedBefore(ArrayList<String> highlights, String candidate){
        for(String str:highlights){
            if(str.contains(candidate)){
                return true;
            }
        }
        return false;
    }
}


