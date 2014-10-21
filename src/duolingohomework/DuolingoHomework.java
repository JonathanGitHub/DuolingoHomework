/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package duolingohomework;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.languagetool.JLanguageTool;
import org.languagetool.rules.RuleMatch;
import utils.StringUtils;

/**
 *
 * @author jianyang
 */
public class DuolingoHomework {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Two test cases 1) this is      your house
        //2) This is funk    This is fuck(wrong word)|| This is fukk(typo)
        System.out.println(grade("This is funk.", "This is fuck!"));
    }
    public static String grade(String correct_answer, String student_answer){
        
        correct_answer = correct_answer.toLowerCase();
        student_answer = student_answer.toLowerCase();
        
        List<String> highlights = new ArrayList<String>();
        boolean hasTypo = false;
        boolean hasWrong = false;
        boolean hasMissing = false;
        if (correct_answer == null || correct_answer.isEmpty()) {
            return "Correct answer cannot be null or empty";
        }
        if(student_answer == null) {//if correct answer has one word, student answer is empty, this case would  be considered as "missing word"
            return "Student answer cannot be null";
        }
        //trim all white spaces and split the text into an String array
        
        List<String> correctList = new ArrayList<String>();
        List<String> studentList = new ArrayList<String>();
        
        String correct_trimmed = correct_answer.trim().toLowerCase();
        String student_trimmed = student_answer.trim().toLowerCase();
        
        String[] correct_split = correct_trimmed.split("[、，。；？！,.;?! \\s+]");//\?? multiple spaces
        String[] student_split = student_trimmed.split("[、，。；？！,.;?! \\s+]");///?？test case: [this is     house
        
        
        int correctLength = correct_split.length;
        int studentLength = student_split.length;
        
        for (int i = 0; i < correctLength; i++){
            correctList.add(correct_split[i]);
        }
        for (int j = 0; j < studentLength; j++){
            studentList.add(student_split[j]);
        }
        
        System.out.println("correctLength: " + correctLength);
        System.out.println("studentLength: " + studentLength);
        
        System.out.println("correctList: " + correctList.toString());
        System.out.println("studentList: " + studentList.toString());
        
        System.out.println("correctList size: " + correctList.size());
        System.out.println("studentList size: " + studentList.size());
        
        
        //typo or wrong words
        if (correctLength == studentLength) {
            int numberOfCorrect = 0;
            //compare each element from correctList with its counterpart from studentList
            for(int k = 0; k < correctLength; k++){
                if (correctList.get(k).equals(studentList.get(k))){
                    numberOfCorrect++;
                } else{
                    if(StringUtils.minDistance(correctList.get(k), studentList.get(k)) == 1){
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
                    } else if(StringUtils.minDistance(correctList.get(k), studentList.get(k)) > 1){//wrong words
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
                    return "True, \"typo\"" + highlights.toString();
                }else{
                    return "True, None, []";
                }
                
            } else if(correctLength - numberOfCorrect == 1 ){
               System.out.println("numberOfCorrect: " + numberOfCorrect);
               System.out.println("correctLength: " + correctLength);
               return "False, \"wrong_word\"" + highlights.toString();
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
                        System.out.println("correct_answer: " + correct_answer);
                        System.out.println("correctList.get(" + correct_index + "): " + correctList.get(correct_index));
                        System.out.println("studentList.get(" + k + "): " + studentList.get(k));
                        System.out.println("startOfCorrect: " + startOfCorrect);
                        System.out.println("endOfCorrect: " + endOfCorrect);
                        //find the indexes of blamed words
                        int startOfStu = startOfCorrect;
                        int endOfStu = startOfCorrect;
                        String hightlight = StringUtils.generateHighlights(startOfCorrect, endOfCorrect, startOfStu, endOfStu);
                        highlights.add(hightlight);
                        
                        correct_index++;
                        
                        
                }
                correct_index++;
                
            }
             return "False, \"missing\"" + highlights.toString();
        } else if(correctLength - studentLength > 1) {
            return "False, \"none\", []";
        }
        else if(correctLength < studentLength){//wrong words(student input has more words
            return "False, \"none\", []";
        }
        
        return "";
    }
    
}
    

