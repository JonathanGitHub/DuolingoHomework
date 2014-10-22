package utils;

import java.io.IOException;
import java.util.List;
import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;

/**
 * StringUtils is a Java class that provides various string methods which are frequently
 * used in DuolingoHomework.java
 * 
 * @author jianyang
 * Last Modified: 2014-10-21
 * Email: cmu dot jonathan at gmail dot com
 */
public class StringUtils {
    
    /**
    * Returns a string expression which highlights blamed words based on input indexes. 
    * ((c1, c2), ((s1, s2)) and c1/s1 is the index of the first character of a blamed 
    * word in  the correct/student's answer and c2/s2 is the index of the last character 
    * of that same blamed word
    * 
    * @return      the string expressionL
    */
    public static String generateHighlights(int c1, int c2, int s1, int s2){
        return "((" + c1 + "," + c2 + "), (" + s1 + "," + s2 + "))";
    }
    
   /**
    * Returns a boolean value indicates whether the input string is a valid word in 
    * The implementation of this method is based on the languagetool api
    * Source: http://wiki.languagetool.org/java-api
    * 
    * @return      a boolean value 
    */
    public static boolean isValidWord(String str) throws IOException{
        boolean isValid = true;
         JLanguageTool langTool = new JLanguageTool(Language.getLanguageForShortName("en-US"));
         for (Rule rule : langTool.getAllRules()) {
            if (!rule.isDictionaryBasedSpellingRule()) {
                langTool.disableRule(rule.getId());
            }
         }
        List<RuleMatch> matches = langTool.check(str);
        for (RuleMatch match : matches) {
            isValid = false;
        }
        return isValid;
    }
   
   /**
    * Returns the minium operations that needed in order to convert one word into another . 
    * The implementation of this method is based on http://www.programcreek.com/2013/12/edit-distance-in-java/
    * 
    * @return       minimum distance
    */
    public static int minDistance(String word1, String word2) {
	int len1 = word1.length();
	int len2 = word2.length();
 
	// len1+1, len2+1, because finally return dp[len1][len2]
	int[][] dp = new int[len1 + 1][len2 + 1];
 
	for (int i = 0; i <= len1; i++) {
		dp[i][0] = i;
	}
 
	for (int j = 0; j <= len2; j++) {
		dp[0][j] = j;
	}
 
	//iterate though, and check last char
	for (int i = 0; i < len1; i++) {
		char c1 = word1.charAt(i);
		for (int j = 0; j < len2; j++) {
			char c2 = word2.charAt(j);
			//if last two chars equal
			if (c1 == c2) {
				//update dp value for +1 length
				dp[i + 1][j + 1] = dp[i][j];
			} else {
				int replace = dp[i][j] + 1;
				int insert = dp[i][j + 1] + 1;
				int delete = dp[i + 1][j] + 1;
 
				int min = replace > insert ? insert : replace;
				min = delete > min ? min : delete;
				dp[i + 1][j + 1] = min;
			}
		}
	}
 	return dp[len1][len2];
    }
}
