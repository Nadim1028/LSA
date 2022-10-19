package Tokenization;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenization {
    public static void main(String[] args) throws IOException {

      /* Tokenization tokenization = new Tokenization();
        Collection<String> tokens = tokenization.tokenGenerator("src/data/clean_data.txt");

        for(String str:tokens){
            System.out.println(str + ", size ="+str.length());
        }*/

      int s = '1';
        System.out.println("ASCII value = "+s);


    }

    public Collection<String> tokenGenerator(String filepath) throws IOException {
        File file=new File(filepath);
        FileReader fr=new FileReader(file);
        BufferedReader br=new BufferedReader(fr);
        String str = "";
        String line;

        while((line=br.readLine())!=null)
        {
            str+=line;
        }
        fr.close();

        // Pattern pattern = Pattern.compile("\\b(?:(?<=\")[^\"]*(?=\")|\\w+)\\b");
        Pattern pattern = Pattern.compile("[\\w']+|[^\\w\\s']\"gm");
        Matcher matcher = pattern.matcher(str);
        Collection<String> tokens = new ArrayList<String>();
        while (matcher.find()) {
            if(isValidTokens(matcher.group(0))){
                tokens.add(matcher.group(0));
            }
           // System.out.println(matcher.group(0));
        }

        return tokens;
    }

    public boolean isValidTokens(String str){

        String[] inValidTokens = new String[] { "include", "iostream", "public", "static","main","class","args", "using", "namespace", "std"
                ,"import","java","util","io","cout","cin","System","out","println","console","writeline"
        };

        boolean result = true;

        for(String token :inValidTokens){

            int firstCharAscii = str.charAt(0);
            if (token.equals(str) || str.length()<1 || isNumericValue(firstCharAscii)) {
                result = false;
                break;
            }

        }

        return  result;
    }

    public boolean isNumericValue(int value)
    {
        if(value>=48 && value<=57)
            return true;
        else
            return false;
    }
}




/* File file=new File("src/data/clean_data.txt");
        FileReader fr=new FileReader(file);
        BufferedReader br=new BufferedReader(fr);
        //StringBuffer sb=new StringBuffer();
        String str = "";
        String line;
        while((line=br.readLine())!=null)
        {
            //sb.append(line);
            str+=line;
        }
        fr.close();

       // Pattern pattern = Pattern.compile("\\b(?:(?<=\")[^\"]*(?=\")|\\w+)\\b");
        Pattern pattern = Pattern.compile("[\\w']+|[^\\w\\s']\"gm");
        Matcher matcher = pattern.matcher(str);
        Collection<String> tokens = new ArrayList<String>();
        while (matcher.find()) {
            tokens.add(matcher.group(0));
            System.out.println(matcher.group(0));
        }*/
