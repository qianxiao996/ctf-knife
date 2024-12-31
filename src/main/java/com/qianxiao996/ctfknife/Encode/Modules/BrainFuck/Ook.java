package com.qianxiao996.ctfknife.Encode.Modules.BrainFuck;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Ook {
    public static String encode(String pText) {
        StringBuilder vResult = new StringBuilder();
        pText = BFCodex.mTextToBF(pText);
        for (char ch : pText.toCharArray()) {
            switch (ch) {
                case '>':
                    vResult.append(" Ook. Ook? ");
                    continue;
                case '<':
                    vResult.append(" Ook? Ook. ");
                    continue;
                case '+':
                    vResult.append(" Ook. Ook. ");
                    continue;
                case '-':
                    vResult.append(" Ook! Ook! ");
                    continue;
                case '.':
                    vResult.append(" Ook! Ook. ");
                    continue;
                case ',':
                    vResult.append(" Ook. Ook! ");
                    continue;
                case '[':
                    vResult.append(" Ook! Ook? ");
                    continue;
                case ']':
                    vResult.append(" Ook? Ook! ");
                    continue;
                default:
                    vResult.append(" Ook? Ook? ");
                    continue;
            }
        }
        return vResult.toString().trim();
    }

    public static String encode_Short(String pText)
    {
       String output =  encode(pText);
        // 1. 移除所有出现的 "Ook"
        output = output.replace("Ook", "");
        // 2. 移除所有的空格
        output = output.replaceAll("\\s+", ""); // 使用正则表达式移除所有空白字符
        // 3. 每五个字符后插入一个空格
        output = insertSpaceEveryFiveChars(output);
        return output;
    }
    public static String decode_Short(String pText)
    {
        StringBuilder vResult = new StringBuilder();
        // 1. 移除每五个字符后的空格
        String noSpaceOutput = pText.replace(" ", "");
        for (char ch : noSpaceOutput.toCharArray()) {
            vResult.append("Ook"+ch+" ");
        }
        return decode(vResult.toString().trim());
    }
    private static String insertSpaceEveryFiveChars(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (i > 0 && i % 5 == 0) {
                sb.append(' ');
            }
            sb.append(str.charAt(i));
        }
        return sb.toString();
    }
    public static String decode(String text)
    {
        StringBuilder vResult= new StringBuilder();
        // 按空格分割字符串
//        String[] words = text.trim().split(" ");
        String[] words = text.trim().split("\\s+");

        // 遍历数组并创建成对的元素
        for (int i = 0; i < words.length - 1; i += 2) {

            String str= words[i] + " " + words[i + 1];
            switch (str){
                case "Ook. Ook?":
                    vResult.append(">");continue;
                case "Ook? Ook.":
                    vResult.append("<");continue;
                case "Ook. Ook.":
                    vResult.append("+");continue;
                case "Ook! Ook!":
                    vResult.append("-");continue;
                case "Ook! Ook.":
                    vResult.append(".");continue;
                case "Ook. Ook!":
                    vResult.append(",");continue;
                case "Ook! Ook?":
                    vResult.append("[");continue;
                case "Ook? Ook!":
                    vResult.append("]");continue;
                default:
                    vResult.append(" ");continue;
            }
        }
        return BFCodex.mBFToText(vResult.toString().trim());
    }
    /**
     * Generates brainfuck code from text.
     *
     * @param text The input string to be converted into brainfuck code.
     * @return A string containing the generated brainfuck code.
     */
    public static String fuckText(String text) {
        int value = 0;
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            int diff = c - value;
            value = c;

            if (diff == 0) {
                result.append(">.<");
                continue;
            }

            if (Math.abs(diff) < 10) {
                result.append('>');
                result.append(repeat(diff > 0 ? "+" : "-", Math.abs(diff)));
            } else {
                int loop = (int) Math.sqrt(Math.abs(diff));
                result.append(repeat("+", loop));

                if (diff > 0) {
                    result.append("[->").append(repeat("+", loop)).append("<]");
                    result.append(">").append(repeat("+", diff - loop * loop));
                } else if (diff < 0) {
                    result.append("[->").append(repeat("-", loop)).append("<]");
                    result.append(">").append(repeat("-", -diff - loop * loop));
                }
            }

            result.append(".<");
        }

        return result.toString().replace("<>", "");
    }

    private static String repeat(String str, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(str);
        }
        return builder.toString();
    }

}
