/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author jianyang
 */
public class StringUtils {
    
    public static String generateHighlights(int startOfCorrect, int endOfCorrect, int startOfStu, int endOfStu){
        return "((" + startOfCorrect + "," + endOfCorrect + "), (" + startOfStu + "," + endOfStu + "))";
    }
    //Check whether a word is valid word
//    public static boolean isValidWord(String str){
//        return 
//    }
   
    //minDistance method return the minium operations that needed in order to 
    //convert word2 into word1
    //Referece: http://www.programcreek.com/2013/12/edit-distance-in-java/
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
