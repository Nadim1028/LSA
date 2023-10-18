package Parser;

public class LocalVariableCounter {
    public int getValue(String line){
        int variableCounter = 0;

        if(!(line.contains("(") && line.contains(")") && line.contains(";") && !(line.contains("="))
           &&  !line.contains("printf")   && !line.contains("cout") && !line.contains(" System.out.print")) &&
                !line.contains("=") && !line.contains("{") && !line.contains("cout"))
        {
            if(hasContainsValidDataType(line) && line.contains(";")  && !line.contains("for") && !line.contains("print") &&
                    !line.contains("printf") && !line.contains(" System.out.print") && !( line.contains("=") && line.contains(")")
                    && !( line.contains("=") && line.contains("}") ) && !(line.contains("nextInt()") || line.contains("nextLine()") ||
                    line.contains("nextDouble()") || line.contains("nextFloat()") ) ) )
            {

                if(line.contains(",")){
                    variableCounter += charCounter(line,',')+1;

                }
                else if(!line.contains(",")){
                    variableCounter += 1;
                }
            }
        }

        return variableCounter;
    }

    public  boolean hasContainsValidDataType(String str){
        return  (str.contains("char") || str.contains("int") || str.contains("void" ) || str.contains("float") ||
                str.contains("double") || str.contains("String") || str.contains("bool") || str.contains("Map") || str.contains("map") ||
                str.contains("vector") || str.contains("Vector") || str.contains("boolean") || str.contains("string"));
    }

    public  int charCounter(String string, char mychar){
        int count =0;
        for(int i = 0; i < string.length(); i++) {
            if(string.charAt(i) == mychar)
                count++;
        }

        return count;
    }
}
