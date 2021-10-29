package main.service;

import lombok.AllArgsConstructor;
import main.api.response.CalendarPostProjection;
import main.api.response.CalendarResponse;
import main.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class CalendarService {

    private final PostRepository postRepository;

    public CalendarResponse getPostsInCalendar(String year) {
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        CalendarResponse calendarResponse = new CalendarResponse();
        List<CalendarPostProjection> responses = postRepository.findPostsInCalendar();
        Set<String> years = new TreeSet<>();
        Map<String, Integer> map = new TreeMap<>(Comparator.reverseOrder());
        if (year == null) {
            year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        }

        for (CalendarPostProjection projection : responses) {
            years.add(formatYear.format(projection.getTime()));
            if (projection.getTime().toString().startsWith(year)) {
                map.put(df.format(projection.getTime()), projection.getCount());
            }
        }
        calendarResponse.setYears(years);
        calendarResponse.setPosts(map);
        return calendarResponse;
    }
}
