package main.api.response;

import java.util.Date;

public interface CalendarPostProjection {
    Date getTime();
    int getCount();
}
