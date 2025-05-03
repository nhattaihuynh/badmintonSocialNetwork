package com.social.profile_service.mapper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for custom mapping operations used by MapStruct mappers
 */
@Component
public class CustomMapperUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Formats a LocalDateTime to a string
     */
    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_FORMATTER) : null;
    }
    
    /**
     * Parses a string to a LocalDateTime
     */
    public LocalDateTime parseDateTime(String dateTimeStr) {
        return dateTimeStr != null ? LocalDateTime.parse(dateTimeStr, DATE_FORMATTER) : null;
    }
    
    /**
     * Converts years of experience to a descriptive string
     */
    public String yearsOfExperienceToDescription(Integer years) {
        if (years == null) {
            return "No experience";
        }
        
        if (years < 1) {
            return "Beginner (less than 1 year)";
        } else if (years < 3) {
            return "Novice (1-2 years)";
        } else if (years < 5) {
            return "Intermediate (3-4 years)";
        } else if (years < 10) {
            return "Advanced (5-9 years)";
        } else {
            return "Expert (10+ years)";
        }
    }
}
