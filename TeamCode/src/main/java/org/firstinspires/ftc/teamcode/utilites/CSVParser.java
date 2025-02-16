// CSVParser.java
package org.firstinspires.ftc.teamcode.utilites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CSVParser {

    // CSV format:
    // id,action,value,requirement,distanceAccuracy,rotationAccuracy,shoulderAccuracy,liftAccuracy
    // String,String,Number,ListOfIds,Number/NONE,Number/NONE,Number/NONE,Number/NONE
    // Will turn into:
    // id, action, value, requirement, distanceAccuracy, rotationAccuracy, shoulderAccuracy, liftAccuracy
    // String,String,Double,List<String>,Double/null,Double/null,Double/null,Double/null

    public static List<Action> parseCsvString(String csvString) {
        List<Action> actions = new ArrayList<>();
        Scanner scanner = new Scanner(csvString);

//        if (scanner.hasNextLine()) {
//            scanner.nextLine(); // Skip header line if present
//        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");

            if (parts.length >= 4) { // Ensure at least id, action, value, requirement are present
                try {
                    String id = parts[0].trim();
                    String action = parts[1].trim();
                    Double value = parseDoubleOrNull(parts[2].trim());
                    String requirementsString = parts[3].trim();
                    List<String> requirementsList = new ArrayList<>();
                    if (!requirementsString.isEmpty()) {
                        requirementsList = Arrays.asList(requirementsString.split("\\s*,\\s*")); // Split by comma and trim whitespace

                    }


                    Double distanceAccuracy = parts.length > 4 ? parseDoubleOrNull(parts[4].trim()) : null;
                    Double rotationAccuracy = parts.length > 5 ? parseDoubleOrNull(parts[5].trim()) : null;
                    Double shoulderAccuracy = parts.length > 6 ? parseDoubleOrNull(parts[6].trim()) : null;
                    Double liftAccuracy = parts.length > 7 ? parseDoubleOrNull(parts[7].trim()) : null;

                    Action parsedAction = new Action(id, action, value, requirementsList, distanceAccuracy, rotationAccuracy, shoulderAccuracy, liftAccuracy); // Pass requirementsList
                    actions.add(parsedAction);

                } catch (NumberFormatException e) {
                    System.err.println("Error parsing a number: " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Error accessing CSV element: " + e.getMessage() + " in line: " + line);
                }
            } else {
                System.err.println("Invalid CSV line (too few columns): " + line);
            }
        }

        scanner.close();
        return actions;
    }

    private static Double parseDoubleOrNull(String s) {
        if (s == null || s.trim().equalsIgnoreCase("NONE") || s.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}