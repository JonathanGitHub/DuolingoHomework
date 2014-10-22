package duolingohomework;

import static duolingohomework.DuolingoGrader.grade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;
import utils.StringUtils;

/**
 * The goal of this task is to write a function grade() that grades a student's 
 * answer and finds certain common mistakes. In particular, the program should 
 * figure out (1) if there is a typo in the student's answer, (2) if a word is 
 * missing or (3) if a word is wrong. If the program finds a common mistake it
 * should highlight it.
 * 
 * @author jianyang
 * Last Modified: 2014-10-21
 * Email: cmu dot jonathan at gmail dot com
 */
public class DuolingoHomework {

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        //Two test cases 1) this is      your house
        //2) This is funk    This is fuck(wrong word)|| This is fukk(typo)
//        System.out.println(grade("This is funk.", "This is fuck!"));
        
//        System.out.println(StringUtils.isValidWord("American"));
//        System.out.println(StringUtils.isValidWord("mi"));
//        System.out.println(StringUtils.isValidWord("hhouse"));
//        System.out.println(StringUtils.isValidWord("über"));
        System.out.println("DuolingoGrader is grading....Just a few seconds...");
        System.out.println("##################################################################");
        System.out.println("Required test cases....");
        System.out.println(DuolingoGrader.grade("house", "house"));
        System.out.println(DuolingoGrader.grade("house", "house."));
        System.out.println(DuolingoGrader.grade("A house", "a house."));
        System.out.println(DuolingoGrader.grade("house", "hhouse"));
        System.out.println(DuolingoGrader.grade("This is my house.", "This is mi hhouse"));
        System.out.println(DuolingoGrader.grade("This is my house.", "This my house!"));
        System.out.println(DuolingoGrader.grade("This is my house.", "This is your house!"));
        
        System.out.println("");
        System.out.println("Extra test cases....");
        System.out.println(DuolingoGrader.grade("über is not an English word", "über is an English word"));
        System.out.println(DuolingoGrader.grade("", "a house."));
        System.out.println(DuolingoGrader.grade("", ""));
        System.out.println(DuolingoGrader.grade("A", ""));
        System.out.println(DuolingoGrader.grade(null, null));
        System.out.println(DuolingoGrader.grade("", null));
        System.out.println(DuolingoGrader.grade(null, ""));
        System.out.println(DuolingoGrader.grade("a small dog", "a small dog is what?>"));
        System.out.println(DuolingoGrader.grade("This is funk", "This is flunk"));
        System.out.println(DuolingoGrader.grade("This is funk", "This is fukk"));
        System.out.println("##################################################################");
        System.out.println("DuolingoGrader is finished its task, did you brew your coffee yet?");
        
//        System.out.println(DuolingoGrader.grade("你是 我 吗？", "你是 吗？"));
        
    }
}
    

