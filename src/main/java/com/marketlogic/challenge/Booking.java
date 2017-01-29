package com.marketlogic.challenge;

import java.time.LocalDateTime;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A class the holds the properties of a booking request.
 */
public class Booking {
    private String employeeId;
    private LocalDateTime submissionTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double duration;
    
    /**
     * Create a new {@link Booking}.
     *
     * @param employeeId
     *            the id of the employee creating the booking
     * @param submissionTime
     *            the time at which the booking request was submitted
     * @param startTime
     *            the start time of the meeting
     * @param duration
     *            the duration of the meeting
     */
    public Booking(String employeeId, LocalDateTime submissionTime, LocalDateTime startTime, double duration) {
        Validate.notBlank(employeeId, "Employee Id cannot be null");
        Validate.notNull(submissionTime, "Submission Time cannot be null");
        Validate.notNull(startTime, "Start time cannot be null");
        Validate.exclusiveBetween(0, Double.MAX_VALUE, duration, "Duration must be bigger than zero");
        Validate.isTrue(submissionTime.isBefore(startTime), "Submission time must be before start time");
        
        this.employeeId = employeeId;
        this.submissionTime = submissionTime;
        this.startTime = startTime;
        this.duration = duration;
    }
    
    /**
     * Gets the identifier of the employee who created the booking.
     * 
     * @return the employee identifier
     */
    public String getEmployeeId() {
        return employeeId;
    }
    
    /**
     * Gets the time at which the booking request was submitted.
     * 
     * @return the submission time
     */
    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }
    
    /**
     * Gets the start time of the meeting.
     * 
     * @return the start time of the meeting
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    /**
     * Gets the end time of the meeting.
     * 
     * @return the end time of the meeting
     */
    public LocalDateTime getEndTime() {
        if (endTime == null) {
            setEndTime();
        }
        return endTime;
    }
    
    /**
     * Sets the end time of the meeting.
     */
    private void setEndTime() {
        int meetingHours = (int) (duration % 1);
        int meetingMinutes = (int) Math.floor((duration - meetingHours) * 60);
        endTime = startTime.plusHours(meetingHours).plusMinutes(meetingMinutes);
    }
    
    /**
     * Checks if two bookings overlap with each other. Two bookings are
     * considered overlapping if either: e1.start < e2.end OR e2.start < e1.end
     * 
     * @param other
     *            The second booking that the intersection should be checked
     *            with
     * @return <code>True</code> if the two events intersect. Otherwise return
     *         <code>False</code>
     */
    public boolean doOverlap(Booking other) {
        return startTime.isBefore(other.endTime) && other.startTime.isBefore(endTime);
    }
    
    /** {@inheritDoc} **/
    @Override
    public String toString() {
        return "Booking [employeeId=" + employeeId + ", submissionTime=" + submissionTime + ", startTime=" + startTime
                + ", endTime=" + endTime + ", duration=" + duration + "]";
    }
    
    /** {@inheritDoc} **/
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) obj;
        return new EqualsBuilder()
                .append(employeeId, other.employeeId)
                .append(submissionTime, other.submissionTime)
                .append(startTime, other.startTime)
                .append(duration, other.duration)
                .build();
    }
    
    /** {@inheritDoc} **/
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(employeeId)
                .append(submissionTime)
                .append(startTime)
                .append(duration)
                .build();
    }
}
