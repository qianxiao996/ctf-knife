/**
 *
 */
package com.qianxiao996.ctfknife.Encrypt.Modeles;

import java.util.Arrays;
import java.util.List;

/**
 * @author Matthias
 *
 */
public class Adfgx{
    private List<Integer> length = Arrays.asList(25);

    private String algorithmEncrypt(String text, String key){
        text = text.toLowerCase();
        String adfgx = "ADFGX";
        char [] cArray = key.toCharArray();
        char[][] table = new char[5][5];
        for(int i = 0; i < 25; i++){
            table[(int) Math.floor(i/5)][i%5]=cArray[i];
        }
        String newString = "";
        for (int n = 0; n < text.length(); n++){
            if (key.contains(String.valueOf(text.charAt(n)))){
                for (int r = 0; r < 5; r++){
                    for (int c = 0; c < 5; c++){
                        if (table[r][c] == text.charAt(n)){
                            newString += adfgx.charAt(r);
                            newString += adfgx.charAt(c);

                        }
                    }
                }
            }
            else{
                newString += text.charAt(n);
            }
            //newString += " ";
        }
        return newString;
    }
    private String algorithmDecrypt(String text, String key){
        String adfgx = "ADFGX";
        char [] cArray = key.toCharArray();
        char[][] table = new char[5][5];
        for(int i = 0; i < 25; i++){
            table[(int) Math.floor(i/5)][i%5]=cArray[i];
        }
        String newString = "";
        for (int n = 0; n < text.length(); n= n+2){
            try{
                int pos1 = adfgx.indexOf(text.charAt(n));
                int pos2 = adfgx.indexOf(text.charAt(n+1));
                newString += table[pos1][pos2];
            }
            catch(Exception e){
                newString += text.charAt(n);
                n = n-1;
            }

			/*if (status == 0){
				if (adfgx.contains(String.valueOf(text.charAt(n)))){
					status = 1;
				}
				else{
					newString += text.charAt(n);
					status = 2;
				}
			}
			else if (status == 1){
				if (adfgx.contains(String.valueOf(text.charAt(n)))){
					int pos1 = adfgx.indexOf(text.charAt(n-1));
					int pos2 = adfgx.indexOf(text.charAt(n));
					newString += table[pos1][pos2];
					status = 2;
				}
				else{
					status = 2;
					newString += text.charAt(n-1);
					newString += text.charAt(n);
				}
			}
			else{
				if (!(text.charAt(n) == ' ')){
					newString += text.charAt(n);
				}
				status = 0;
			}*/
        }
        return newString;
    }


    public String encrypt(String text, String key) {
        key = key.toLowerCase();
        String verified = verify(key, "");

        if (verified != null){
            return verified;
        }
        return algorithmEncrypt(text, key);

    }

    public String decrypt(String text, String key) {
        key = key.toLowerCase();
        String verified = verify(key, "");
        if (verified != null){
            return verified;
        }
        return algorithmDecrypt(text, key);

    }

    protected String verify(String key, String alphabet) {
        if (!checkLength(key, length)){
            return "编码表长度需为25！当前长度："+key.length();
        }
        else{
            return null;
        }
    }
    public boolean checkLength(String text, List<Integer> length){
        return (length.contains(text.length()));
    }
}