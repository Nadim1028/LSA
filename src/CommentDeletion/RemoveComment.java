package CommentDeletion;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveComment {

    public static void main(String[] args) throws IOException {
        RemoveComment removeComment= new RemoveComment();
        removeComment.createUncommentedSourceCode("src/Input/code.txt",1);
        removeComment.createUncommentedSourceCode("src/Input/code.txt",2);
    }

    public void createUncommentedSourceCode(String sourceCodePath,int number) throws IOException {
        Scanner scanner = new Scanner(System.in);

        Path path = Path.of(sourceCodePath);//"src/code.txt");

        String content = Files.readString(path, StandardCharsets.US_ASCII);

        String str = content;//scanner.nextLine();

        String outputFilePath = "src/Output/clean_data"+number+".txt";
        String result = deleteComments(str);
        //System.out.println(result);

        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter( new FileWriter( outputFilePath));
            writer.write( result);

        }
        catch ( IOException e)
        {
        }
        finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
            }
        }
    }



    public static String deleteComments(String myString) {
        String newString = "";

        if (myString.contains("\"")) {
            if (myString.indexOf("\"") != 0) {

                String[] stringParts = myString.split("\"");

                for (int i = 0; i < stringParts.length; i++) {
                  // System.out.println("string"+i+"="+stringParts[i]);

                    if ((i & 1) == 0) {
                        Pattern commentaryPattern = Pattern.compile("(/\\*((.|\n)*?)\\*/)|//.*");

                        Matcher m = commentaryPattern.matcher(stringParts[i]);

                        newString += m.replaceAll("");
                    } else {
                        newString += "\"" + stringParts[i] + "\"";
                    }
                }
            }
        } else {
            Pattern commentaryPattern = Pattern.compile("(/\\*((.|\n)*?)\\*/)|//.*");

            Matcher m = commentaryPattern.matcher(myString);

            newString += m.replaceAll("");
        }

        return newString;
    }
}

/*public static String deleteComments2(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        boolean inText = false;
        char previousCharacter = '\u0000';

        for (int i = 0; i < str.length(); ++i) {
            char currentCharacter = str.charAt(i);

            if (inText) {
                if (currentCharacter == '\"' && previousCharacter != '\\') {
                    inText = false;
                }
            } else {
                if (currentCharacter == '\"') {
                    if (previousCharacter == '\\') {
                        throw new RuntimeException("bad syntax");
                    }

                    inText = true;
                }

                if (currentCharacter == '/' && previousCharacter == '/') {
                    sb.deleteCharAt(sb.length() - 1);
                    break;
                }
            }

            sb.append(currentCharacter);
            previousCharacter = currentCharacter;
        }

        return sb.toString();
    }
*/