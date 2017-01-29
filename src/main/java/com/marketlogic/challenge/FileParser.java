package com.marketlogic.challenge;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class FileParser {
    static final String OFFICE_HOUR_FORMAT = "HHmm";
    static final String REQUEST_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String MEETING_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    
    /**
     * Creates an {@link Optional<Schedule>} object by reading input from a text
     * file. If the input file is not present or incorrectly formatted, the
     * returned {@link Optional} is empty.
     * 
     * @param input
     *            The path to the input file
     * @return an {@link Optional<Schedule>} that contains the processed
     *         meetings from the file.
     */
    public Optional<Schedule> createScheduleFromFile(String input) {
        Schedule schedule;
        List<Booking> bookings = new ArrayList<>();
        
        try (Scanner fileScanner = new Scanner(new File(input))) {
            // read working hours
            if (fileScanner.hasNextLine()) {
                String workingHours = fileScanner.nextLine();
                String[] hours = workingHours.split("\\s+");
                LocalTime start = LocalTime.parse(hours[0], DateTimeFormatter.ofPattern(OFFICE_HOUR_FORMAT));
                LocalTime end = LocalTime.parse(hours[1], DateTimeFormatter.ofPattern(OFFICE_HOUR_FORMAT));
                schedule = new Schedule(start, end);
            } else {
                fileScanner.close();
                return Optional.empty();
            }
            while (fileScanner.hasNextLine()) {
                String firstLine = fileScanner.nextLine();
                if (fileScanner.hasNextLine()) {
                    String secondLine = fileScanner.nextLine();
                    bookings.add(parseBooking(firstLine, secondLine));
                }
            }
            schedule.addBookings(bookings);
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(schedule);
    }
    
    /**
     * Construct a booking from input lines.
     * 
     * @param firstLine
     *            The first input line describing the request submission time
     *            and the employee id.
     * @param secondLine
     *            The second input describing the meeting time and duration
     *
     * @return A {@link Booking} object based on the provided inputs.
     */
    Booking parseBooking(String firstLine, String secondLine) {
        String requestTime = firstLine.substring(0, firstLine.lastIndexOf(" "));
        String employeeId = firstLine.substring(1 + firstLine.lastIndexOf(" "));
        String meetingTime = secondLine.substring(0, secondLine.lastIndexOf(" "));
        String duration = secondLine.substring(1 + secondLine.lastIndexOf(" "));
        
        LocalDateTime parsedRequestTime = LocalDateTime.parse(requestTime,
                DateTimeFormatter.ofPattern(REQUEST_TIME_FORMAT));
        LocalDateTime parsedMeetingTime = LocalDateTime.parse(meetingTime,
                DateTimeFormatter.ofPattern(MEETING_TIME_FORMAT));
        double parsedDuration = Double.parseDouble(duration);
        return new Booking(employeeId, parsedRequestTime, parsedMeetingTime, parsedDuration);
        
    }
}
