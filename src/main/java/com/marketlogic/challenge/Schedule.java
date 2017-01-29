package com.marketlogic.challenge;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;

public class Schedule {
    static final String MEETING_OUTPUT_TIME_FORMAT = "HH:mm";
    static final String MEETING_OUTPUT_FULL_FORMAT = "yyyy-MM-dd HH:mm";
    
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Booking> calendar;
    
    public Schedule(LocalTime startTime, LocalTime endTime) {
        Validate.notNull(startTime, "startTime cannot be null.");
        Validate.notNull(endTime, "endTime cannot be null.");
        
        calendar = new ArrayList<>();
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    /**
     * Add a list of bookings to the calendar. Bookings are processed in order of their submission time.
     * 
     * @param bookings The list of bookings to be added.
     */
    public void addBookings(List<Booking> bookings) {
        Validate.notNull(bookings, "bookings cannot be null");
        Validate.noNullElements(bookings, "bookings cannot contain a null element");
        
        Collections.sort(bookings, new Comparator<Booking>() {
                @Override
                public int compare(Booking o1, Booking o2) {
                    return o1.getSubmissionTime().compareTo(o2.getSubmissionTime());
                }
            }
        );
        
        for (Booking booking : bookings) {
            if (!isBookingInWorkingHours(booking)) {
                continue;
            }
            
            if (isBookingFeasible(calendar, booking)) {
                calendar.add(booking);
            }
        }
    }
    
    /**
     * Checks if the meeting is within the working hours of the company.
     * 
     * @param booking the booking to be checked.
     * @return <code>True</code> if the meeting is between the startTime and endTime
     */
    boolean isBookingInWorkingHours(Booking booking) {
        LocalDateTime currentDayStart = booking.getStartTime().toLocalDate().atTime(startTime);
        LocalDateTime currentDayEnd = booking.getStartTime().toLocalDate().atTime(endTime);
        
        // If place is 24-hours open, then any booking is feasible
        if (currentDayEnd.equals(currentDayStart)) {
            return true;
        }
        if (currentDayEnd.isBefore(currentDayStart)) {
            currentDayEnd = currentDayEnd.plusDays(1);
        }
        
        return booking.getStartTime().compareTo(currentDayStart) >=0 && booking.getEndTime().compareTo(currentDayEnd) <= 0;
    }
    
    /**
     * Checks if a given booking does not overlap with a list of bookings.
     * 
     * @param bookings the list of existing booking
     * @param booking the new booking, for which the check is done 
     * @return <code>True</code> if the booking does not overlap with any of the existing booking. Otherwise <code>False</code>
     */
    boolean isBookingFeasible(List<Booking> bookings, Booking booking) {
        for (Booking b : bookings) {
            if (booking.doOverlap(b)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Gets the calendar containing the schedules bookings as a read-only {@link SortedMap} where keys represent the date of the meeting 
     * and the values are the list of bookings on that day. The values are guaranteed to be sorted by start time.
     * 
     * @return The calendar containing the scheduled bookings. 
     */
    public SortedMap<LocalDate, List<Booking>> getCalendar() {
        SortedMap<LocalDate, List<Booking>> calendarMap = new TreeMap<>();
        for (Booking booking : calendar) {
            LocalDate meetingDate = booking.getStartTime().toLocalDate();
            calendarMap.putIfAbsent(meetingDate, new ArrayList<>());
            calendarMap.get(meetingDate).add(booking);
        }
        
        for (Entry<LocalDate, List<Booking>> entry : calendarMap.entrySet()) {
            Collections.sort(entry.getValue(), new Comparator<Booking>() {
                @Override
                public int compare(Booking o1, Booking o2) {
                    return o1.getStartTime().compareTo(o2.getStartTime());
                }
            });
        }
        
        return Collections.unmodifiableSortedMap(calendarMap);
    }
    
    /**
     * Convert the schedule to a list of strings for printing purposes.
     * 
     * @return A {@link List<String>} containing the schedule in printable form.
     */
    public List<String> getScheduleAsStrings() {
        List<String> output = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Booking>> pair : getCalendar().entrySet()) {
            output.add(pair.getKey().format(DateTimeFormatter.ISO_LOCAL_DATE));
            for (Booking booking : pair.getValue()) {
                StringBuilder sb = new StringBuilder();
                sb.append(booking.getStartTime().format(DateTimeFormatter.ofPattern(MEETING_OUTPUT_TIME_FORMAT)));
                sb.append(" ");
                String endTimeFormat = MEETING_OUTPUT_TIME_FORMAT;
                
                // If the meeting ends on the next day, print the end time as full date
                if (booking.getEndTime().toLocalTime().compareTo(booking.getStartTime().toLocalTime()) <= 0) {
                    endTimeFormat = MEETING_OUTPUT_FULL_FORMAT;
                }
                
                sb.append(booking.getEndTime().format(DateTimeFormatter.ofPattern(endTimeFormat)));
                sb.append(" ");
                sb.append(booking.getEmployeeId());
                output.add(sb.toString());
            }
        }
        return output;
    }
}
