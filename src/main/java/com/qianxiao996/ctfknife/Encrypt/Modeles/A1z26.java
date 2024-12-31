package com.qianxiao996.ctfknife.Encrypt.Modeles;



public class A1z26 {

    public static void main(String[] args) {
    }

    public static String a1z26(String inputText, String mode) {
        try {

            inputText = inputText.toLowerCase();
            StringBuilder output = new StringBuilder();
            char[] alpha = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

            if (mode == "encode") {
                inputText = inputText.replace("-", "").replace(" ", "");
                inputText = inputText.toUpperCase();
                for (int i = 0; i < inputText.length(); i++) {
                    for (int a = 0; a < alpha.length; a++) {
                        if (alpha[a] == inputText.charAt(i)) {
                            output.append((a + 1));
                            if (i < inputText.length() - 1) {
                                output.append("-");
                            }
                        }
                    }
                }
            }

            if (mode == "decode") {
                try {
                    String[] a1z26Numbers = inputText.split("-");
                    for (String a1z26Number : a1z26Numbers) {
                        output.append(alpha[Integer.valueOf(a1z26Number) - 1]);
                    }
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    return "Error, input format is wrong.";
                }
            }

            return output.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

}