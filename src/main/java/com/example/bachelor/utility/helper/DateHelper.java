package com.example.bachelor.utility.helper;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DateHelper {

    public static TimePeriod getTimePeriod(Date startDate, Date endDate) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long totalSeconds = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return new TimePeriod(hours, minutes, seconds);
    }
}
