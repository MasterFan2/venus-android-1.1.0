package com.tool.widget.calendar.format;

import com.tool.widget.calendar.CalendarDay;

/**
 * Used to format a {@linkplain com.tool.widget.calendar.CalendarDay} to a string for the month/year title
*/
public interface TitleFormatter {
    /**
     * Converts the supplied day to a suitable month/year title
     * @param day the day containing relevant month and year information
     * @return a label to display for the given month/year
     */
    CharSequence format(CalendarDay day);
}
