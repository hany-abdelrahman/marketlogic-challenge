package com.marketlogic.challenge;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for the {@link FileParser} class.
 */
public class FileParserTest {
    @Test
    public void scheduleWithNullStartTimeShouldThrowException() {
        LocalTime result = LocalTime.parse("0930", DateTimeFormatter.ofPattern(FileParser.OFFICE_HOUR_FORMAT));
        LocalTime expected = LocalTime.of(9, 30);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testParseRequestTime() {
        LocalDateTime result = LocalDateTime.parse("2015-08-17 10:17:06", DateTimeFormatter.ofPattern(FileParser.REQUEST_TIME_FORMAT));
        LocalDateTime expected = LocalDateTime.of(2015, 8, 17, 10, 17, 6);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testParseMeetingTime() {
        LocalDateTime result = LocalDateTime.parse("2015-08-17 10:17", DateTimeFormatter.ofPattern(FileParser.MEETING_TIME_FORMAT));
        LocalDateTime expected = LocalDateTime.of(2015, 8, 17, 10, 17);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testParseBooking() {
        String firstLine = "2015-08-17 10:17:06 EMP001";
        String secondLine = "2015-08-21 09:00 2";
        FileParser parser = new FileParser();
        Booking actual = parser.parseBooking(firstLine, secondLine);
        Booking expected = new Booking("EMP001", LocalDateTime.of(2015, 8, 17, 10, 17, 06), LocalDateTime.of(2015, 8, 21, 9, 0), 2);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testParseFile() throws IOException {
        int nFiles = 4;
        FileParser parser = new FileParser();
        
        for (int i = 1; i <= nFiles; ++i) {
            String input = getClass().getClassLoader().getResource("input-" + i + ".txt").getFile();
            String output = getClass().getClassLoader().getResource("output-" + i + ".txt").getFile();
            
            Optional<Schedule> schedule = parser.createScheduleFromFile(input);
            if (!schedule.isPresent()) {
                Assert.fail("Failed to parse input file: " + input);
            }
            List<String> actual = schedule.get().getScheduleAsStrings();
            List<String> expected = FileUtils.readLines(FileUtils.getFile(output), Charset.defaultCharset());
            Assert.assertEquals(expected, actual);
        }
    }
}
