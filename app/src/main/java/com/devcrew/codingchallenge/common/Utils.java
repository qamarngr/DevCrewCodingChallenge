package com.devcrew.codingchallenge.common;

/**
 * Created by Qamar on 9/16/2016.
 */
public class Utils {

    /**
     * Converts the comma separated first-name,second-name
     * into title case, input should in format first-name,second-name
     * **/
    public static String toTitleCase(String inputString) {
        StringBuilder outputString = new StringBuilder();
        //separate the first and last name
        String[] tokens = inputString.toLowerCase().split(",");
        //remove trailing spaces in names
        tokens[0] = tokens[0].trim();
        tokens[1] = tokens[1].trim();
        // capitalize first character
        String firstName = tokens[0].replaceFirst(tokens[0].substring(0,1),tokens[0].substring(0,1).toUpperCase());
        String secondName = tokens[1].replaceFirst(tokens[1].substring(0,1),tokens[1].substring(0,1).toUpperCase());
        // format accordingly
        outputString.append(firstName);
        outputString.append(", ");
        outputString.append(secondName);
        return outputString.toString();
    }

    /**
     * Checks for validity of entered name
     * */
    public static boolean isValidCommaSepratedName(String inputName ){
        boolean isValidCommaSeparatedName = false;

        if(! inputName.trim().isEmpty()){
            String[] tokens = inputName.split(",");
            if(tokens.length == 2){
                if(!( tokens[0].trim().isEmpty() || tokens[1].trim().isEmpty())){
                    isValidCommaSeparatedName = true;
                }
            }
        }
        return isValidCommaSeparatedName;
    }

}
