package com.marketlogic.challenge;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import junit.framework.Assert;

/**
 * Unit test for the {@link Schedule} class.
 */
public class ScheduleTest {
    
    
    @Test(expected=NullPointerException.class)
    public void scheduleWithNullStartTimeShouldThrowException() {
        new Schedule(null, LocalTime.of(17, 30));
    }
    
    @Test(expected=NullPointerException.class)
    public void scheduleWithNullEndTimeShouldThrowException() {
        new Schedule(LocalTime.of(17, 30), null);
    }
    @Test
    public void testCreateValidSchedule() {
        Schedule s = new Schedule(LocalTime.of(7, 30), LocalTime.of(17, 30));
        Assert.assertNotNull(s);
    }
    
    @Test
    public void testScheduleWithEndTimeAfterStartTime() {
        Schedule s = new Schedule(LocalTime.of(17, 30), LocalTime.of(7, 30));
        Assert.assertNotNull(s);
    }
    
    @Test
    public void testBookingInWorkingHours() {
        LocalDateTime requestTime = LocalDateTime.of(2017, 1, 1, 1, 0);
        LocalDateTime startTime = LocalDateTime.of(2017, 1, 5, 10, 0);
        
        Schedule schedule = new Schedule(LocalTime.of(9, 0), LocalTime.of(17, 30));
        Booking b = new Booking("emp", requestTime, startTime, 1);
        boolean result = schedule.isBookingInWorkingHours(b);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testBookingStartsAfterWorkingHours() {
        LocalDateTime requestTime = LocalDateTime.of(2017, 1, 1, 1, 0);
        LocalDateTime startTime = LocalDateTime.of(2017, 1, 5, 18, 0);
        
        Schedule schedule = new Schedule(LocalTime.of(9, 0), LocalTime.of(17, 30));
        Booking b = new Booking("emp", requestTime, startTime, 1);
        boolean result = schedule.isBookingInWorkingHours(b);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testBookingStartsBeforeWorkingHours() {
        LocalDateTime requestTime = LocalDateTime.of(2017, 1, 1, 1, 0);
        LocalDateTime startTime = LocalDateTime.of(2017, 1, 5, 8, 0);
        
        Schedule schedule = new Schedule(LocalTime.of(9, 0), LocalTime.of(17, 30));
        Booking b = new Booking("emp", requestTime, startTime, 1);
        boolean result = schedule.isBookingInWorkingHours(b);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testBookingEndsAfterWorkingHours() {
        LocalDateTime requestTime = LocalDateTime.of(2017, 1, 1, 1, 0);
        LocalDateTime startTime = LocalDateTime.of(2017, 1, 5, 17, 0);
        
        Schedule schedule = new Schedule(LocalTime.of(9, 0), LocalTime.of(17, 30));
        Booking b = new Booking("emp", requestTime, startTime, 1);
        boolean result = schedule.isBookingInWorkingHours(b);
        Assert.assertFalse(result);
    }
    
    // TODO: Add more tests for boundary conditions
    
    // 
    // Tests for opening hours that are wrapped. Ex: (18:00, 3:00) 
    //
    
    @Test
    public void testBookingInWorkingHours2() {
        LocalDateTime requestTime = LocalDateTime.of(2017, 1, 1, 1, 0);
        LocalDateTime startTime = LocalDateTime.of(2017, 1, 5, 19, 0);
        
        Schedule schedule = new Schedule(LocalTime.of(18, 0), LocalTime.of(3,0 ));
        Booking b = new Booking("emp", requestTime, startTime, 1);
        boolean result = schedule.isBookingInWorkingHours(b);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testBookingStartsAfterWorkingHours2() {
        LocalDateTime requestTime = LocalDateTime.of(2017, 1, 1, 1, 0);
        LocalDateTime startTime = LocalDateTime.of(2017, 1, 5, 4, 0);
        
        Schedule schedule = new Schedule(LocalTime.of(18, 0), LocalTime.of(3, 0));
        Booking b = new Booking("emp", requestTime, startTime, 1);
        boolean result = schedule.isBookingInWorkingHours(b);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testBookingStartsBeforeWorkingHours2() {
        LocalDateTime requestTime = LocalDateTime.of(2017, 1, 1, 1, 0);
        LocalDateTime startTime = LocalDateTime.of(2017, 1, 5, 8, 0);
        
        Schedule schedule = new Schedule(LocalTime.of(18, 0), LocalTime.of(3, 0));
        Booking b = new Booking("emp", requestTime, startTime, 1);
        boolean result = schedule.isBookingInWorkingHours(b);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testBookingEndsAfterWorkingHours2() {
        LocalDateTime requestTime = LocalDateTime.of(2017, 1, 1, 1, 0);
        LocalDateTime startTime = LocalDateTime.of(2017, 1, 5, 4, 0);
        
        Schedule schedule = new Schedule(LocalTime.of(18, 0), LocalTime.of(3, 0));
        Booking booking = new Booking("emp", requestTime, startTime, 1);
        boolean result = schedule.isBookingInWorkingHours(booking);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testExampleSchedule() {
        Schedule schedule = new Schedule(LocalTime.of(9, 0), LocalTime.of(17, 30));
        
        Booking b1 = new Booking("EMP001", LocalDateTime.of(2015, 8, 17, 10, 17, 06), LocalDateTime.of(2015, 8, 21, 9, 0), 2);
        Booking b2 = new Booking("EMP002", LocalDateTime.of(2015, 8, 16, 12, 34, 56), LocalDateTime.of(2015, 8, 21, 9, 0), 2);
        Booking b3 = new Booking("EMP003", LocalDateTime.of(2015, 8, 16, 9, 28, 23), LocalDateTime.of(2015, 8, 22, 14, 0), 2);
        Booking b4 = new Booking("EMP004", LocalDateTime.of(2015, 8, 17, 11, 23, 45), LocalDateTime.of(2015, 8, 22, 16, 0), 1);
        Booking b5 = new Booking("EMP005", LocalDateTime.of(2015, 8, 15, 17, 29, 12), LocalDateTime.of(2015, 8, 21, 16, 0), 3);
        
        List<Booking> bookings = Arrays.asList(b1, b2, b3, b4, b5);
        schedule.addBookings(bookings);
        
        SortedMap<LocalDate, List<Booking>> result = schedule.getCalendar();
        SortedMap<LocalDate, List<Booking>> expected = new TreeMap<>();
        expected.put(LocalDate.of(2015, 8, 21), Arrays.asList(b2));
        expected.put(LocalDate.of(2015, 8, 22), Arrays.asList(b3, b4));
        Assert.assertEquals(expected, result);
    }
}
