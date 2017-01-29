package com.marketlogic.challenge;

import java.util.List;
import java.util.Optional;

/**
 * Console Application for the Market Logic Challenge.
 */
public class App  {
    private static final String USAGE_MESSAGE = "Usage: prog <input_path>";
    private static final String ERROR_MESSAGE = "File either not present or not in corret format. See example.txt for input format";
    
    public static void main( String[] args ) {
        if (args.length == 0) {
            System.out.println(USAGE_MESSAGE);
            return;
        }
        FileParser parser = new FileParser();
        Optional<Schedule> schedule = parser.createScheduleFromFile(args[0]);
        if (schedule.isPresent()) {
            List<String> lines = schedule.get().getScheduleAsStrings();
            for (String line : lines) {
                System.out.println(line);
            }
        } else {
            System.out.println(ERROR_MESSAGE);
        }
    }
}
