package Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CodeParser
{
    public static void main(String[] args) throws FileNotFoundException
    {
        File myObj = new File("src/CPP2.txt");
    }

    public  String getTemplateConversion(String method){

        ArrayList<String> variableList = new ArrayList<>();
        ArrayList<String> variableConversion = new ArrayList<>();
        ArrayList<String> userInputConversion = new ArrayList<>();
        ArrayList<String> conditionConversion = new ArrayList<>();
        ArrayList<String> loopConversion = new ArrayList<>();

        String stringTemplateOfMethod = "";
        String[] variables=null;
        int variableCounter=0;
        int numOfLOC, numOfArgumentsPassed,numOfLocalVariable,numOfGlobalVariable,
                numOfFunctionCalls, numOfConditions, numOfLoops, numOfReturnStatements;
        Scanner myReader = new Scanner(method);
        while (myReader.hasNextLine())
        {
            String line = myReader.nextLine();

            if( line.contains("(")  && !line.contains(";")&& checkReturnType(line)){
                stringTemplateOfMethod += "DataType FunctionName(";
                int numOfArguments = identifyNumOfArguments(line);
                for (int i=0;i<numOfArguments;i++){
                    stringTemplateOfMethod += i==numOfArguments-1? "DataType X" : "DataType X,";
                }
                stringTemplateOfMethod += ")\n";
            }

            //variable conversion
            if(!(line.contains("(") && line.contains(")") && line.contains(";") && !(line.contains("="))
                     &&  !line.contains("printf") && !line.contains(" System.out.print")) && !line.contains("=")
                    && !line.contains("{") && !line.contains("cout"))
            {
                if(hasContainsValidDataType(line) && line.contains(";")  && !line.contains("for") && !line.contains("print") &&
                        !line.contains("printf") && !line.contains(" System.out.print") && !( line.contains("=") && line.contains(")")
                        && !( line.contains("=") && line.contains("}") ) && !(line.contains("nextInt()") || line.contains("nextLine()") ||
                        line.contains("nextDouble()") || line.contains("nextFloat()") ) ) )
                {
                    //System.out.println(line);

                    if(line.contains(",")){
                        variableCounter += charCounter(line,',')+1;
                        //System.out.println(line);w

                        for(int i = 0; i<= charCounter(line,','); i++){
                            stringTemplateOfMethod += addTemplateConversion(line);
                        }
                    }
                    else if(!line.contains(",")){
                        variableCounter += 1;
                        stringTemplateOfMethod += addTemplateConversion(line);
                    }
                }
            }
            //return statement
            if(line.contains("return")){
                // System.out.println(line);
                stringTemplateOfMethod += "Return;\n";
            }



            //assignment statements

            if(line.contains("=") && !(line.contains("=") && line.contains("for")) && !(line.contains("=")  && line.contains("if"))
                    && line.contains(";") && !line.contains("print") && !line.contains("cout") && !line.contains("System.out.print")
                    ||  (charCounter(line,'=')>2 && line.contains("for")))
            {
                //System.out.println(line);
                String splitLine="";
                if(!(charCounter(line,'=')>2 && line.contains("for")))
                    splitLine = line.split("=",2)[1];

                if(charCounter(line,'=')>1 && !line.contains("for")){
                    for(int i=0;i<charCounter(line,'=');i++){
                        stringTemplateOfMethod += "Assignment\n";
                    }
                }
                else
                    stringTemplateOfMethod += "Assignment\n";

                if(splitLine.contains("(") && splitLine.contains(")") && splitLine.contains(";") && !splitLine.contains("=")
                        &&  !splitLine.contains("printf") && !splitLine.contains(" System.out.print")){
                    //System.out.println(splitLine);
                    stringTemplateOfMethod += "FunctionCall;\n";
                }

                if(new LocalVariableCounter().hasContainsValidDataType(line)){
                    //System.out.println("********** "+line);
                    //addTemplateConversion(line,stringTemplateOfMethod);
                    if(charCounter(line,'=')>1 && line.contains(",")){
                        for(int i=0;i<charCounter(line,'=');i++){
                            stringTemplateOfMethod += addTemplateConversion(line);
                        }
                    }
                    else
                        stringTemplateOfMethod += addTemplateConversion(line);
                }

            }

            //user input conversion
            if(line.contains("scanf") || line.contains("cin") || line.contains("nextInt()") || line.contains("nextLine()") || line.contains("nextDouble()")
                    || line.contains("nextFloat()") || line.contains("nextLong()") || line.contains("nextBoolean()"))
            {
                if(line.contains("%d") || line.contains("%f") || line.contains("%lf") || line.contains("%d") || line.contains("%s") || line.contains("%s")){
                    if(charCounter(line,',')>1){
                        for (int i = 0; i< charCounter(line,','); i++){
                            userInputConversion.add("INPUT");
                            stringTemplateOfMethod += "Input\n";
                        }
                    }

                    else{
                        stringTemplateOfMethod += "Input\n";
                        userInputConversion.add("INPUT");
                    }
                }

                else if(line.contains(">>")){
                    if(charCounter(line,'>')>2){
                        for (int i = 0; i< charCounter(line,'>')/2; i++){
                            userInputConversion.add("INPUT");
                            stringTemplateOfMethod += "Input\n";
                        }
                    }

                    else{
                        stringTemplateOfMethod += "Input\n";
                        userInputConversion.add("INPUT");
                    }
                }

                else{
                    stringTemplateOfMethod += "Input\n";
                    userInputConversion.add("INPUT");
                }
            }

            //condition conversion
            if((!line.contains(";") && (line.contains("if") || line.contains("else if"))) || line.contains("else")){
                conditionConversion.add("CONDITION");
                stringTemplateOfMethod += "Condition\n";
            }

            //loop conversion
            if((!line.contains(";") && (line.contains("while") || line.contains("do"))) || line.contains("for")){
                //conditionConversion.add("LOOP");
                loopConversion.add("LOOP");
                stringTemplateOfMethod += "Loop\n";
            }

            //function call
            if(line.contains("(") && line.contains(")") && line.contains(";") && !line.contains("=")
                    && !line.contains("scanf") &&  !line.contains("cout") &&  !line.contains("printf") && !line.contains(" System.out.print")){
                //System.out.println(line);
                stringTemplateOfMethod += "FunctionCall;\n";
            }
        }


           String templateValue = "";
           templateValue += arrayListToString(variableConversion);
           templateValue += arrayListToString(userInputConversion);
           templateValue += arrayListToString(conditionConversion);
           templateValue += arrayListToString(loopConversion);

            return stringTemplateOfMethod;
    }

    public String arrayListToString(ArrayList<String> arrayList)
    {

        String str = "";// new String[arrayList.size()];

        for (int i = 0; i < arrayList.size(); i++)
        {
            //tr[i] = arrayList.get(i);
            str += arrayList.get(i)+"\n";
        }

        return str;
    }

    public boolean hasContainsValidDataType(String str){
        return  (str.contains("char") || str.contains("int") || str.contains("void" ) || str.contains("float") ||
                str.contains("double") || str.contains("String") || str.contains("bool") || str.contains("boolean") || str.contains("string"));
    }

    public  int charCounter(String string, char mychar){
        int count =0;
        for(int i = 0; i < string.length(); i++) {
            if(string.charAt(i) == mychar)
                count++;
        }

        return count;
    }

    public  String addTemplateConversion(String line){
        String stringTemplateOfMethod ="";
        if(line.contains("int") || line.contains("float") || line.contains("double") ){
            stringTemplateOfMethod += "DataType1 X;\n";
        }

        if(line.contains("String") || line.contains("string") || line.contains("char")){
            stringTemplateOfMethod += "DataType2 X;\n";
        }

        if(line.contains("bool") || line.contains("boolean")){
            stringTemplateOfMethod += "DataType3 X;\n";
        }

        return stringTemplateOfMethod;

    }

    public boolean checkReturnType(String data){
        return  (data.contains("int") || data.contains("void" ) || data.contains("float") ||
                data.contains("double") || data.contains("String") || data.contains("bool") || data.contains("boolean") || data.contains("string"));
    }


    public  int identifyNumOfArguments(String str){
        String arguments = "";
        boolean checker=false;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)=='('){
                checker=true;
            }
            if(str.charAt(i)==')'){
                checker=false;
            }
            if(checker){
                //arguments.concat(String.valueOf(str.charAt(i)));
                if(str.charAt(i)!='(')
                    arguments += str.charAt(i);
            }
        }

        int numOfArguments = arguments.trim().isEmpty()  ? 0 :
                charCounter(arguments,',')>0 ? charCounter(arguments,',')+1  : 1;
        return numOfArguments;
    }
}


