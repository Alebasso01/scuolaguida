package com.example.scuolaguida.models;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Calendar Content Provider doc : https://developer.android.com/guide/topics/providers/calendar-provider
public class CalendarProvider {
    private static final String TAG = CalendarProvider.class.getCanonicalName();

    private static final String[] EVENT_PROJECTION = {
            CalendarContract.Events._ID,
            CalendarContract.Events.CALENDAR_ID,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.DURATION,
            CalendarContract.Events.AVAILABILITY
    };

    // NOTE: This is the index of the column in the SELECT query
    // In our case the SELECT query retrieve the column in EVENT_PROJECTION!!
    private static final Map<String, Integer> EVENT_COLUMN = new HashMap<String, Integer>() {{
        put(CalendarContract.Events._ID, 0);
        put(CalendarContract.Events.CALENDAR_ID, 1);
        put(CalendarContract.Events.DESCRIPTION, 2);
        put(CalendarContract.Events.TITLE, 3);
        put(CalendarContract.Events.DTSTART, 4);
        put(CalendarContract.Events.DTEND, 5);
        put(CalendarContract.Events.DURATION, 6);
        put(CalendarContract.Events.ALL_DAY, 7);
        put(CalendarContract.Events.AVAILABILITY, 8);
    }};

    private final ContentResolver contentResolver;
    public CalendarProvider(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    /*public List<MyEvent> getAllEvents () {
        Uri uri = CalendarContract.Events.CONTENT_URI;

        /*
        Example of query selection:
        val selection: String = "((${CalendarContract.Events.DTSTART} >= ?) AND (" +
                "${CalendarContract.Events.DTSTART} < ?)) OR ((${CalendarContract.Events.DTSTART} < ?) AND (" +
                "${CalendarContract.Events.DTEND} >= ?))"
        val selectionArgs: Array<String> = arrayOf(startMillis.toString(), endMillis.toString(), startMillis.toString(), startMillis.toString())


        // I am interested only in events in 2023!
        String whereSelection = CalendarContract.Events.DTSTART + " >= ? and " + CalendarContract.Events.DTEND + " < ?";

        Calendar cal = Calendar.getInstance();
        cal.set(2023,1,1);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2024, 1, 1);
        String[] whereValues = {String.valueOf(cal.getTimeInMillis()), String.valueOf(cal2.getTimeInMillis())};

        Cursor cur =
                this.contentResolver
                        .query(uri, EVENT_PROJECTION, whereSelection, whereValues, null);

        List<MyEvent> result = new ArrayList<>();
        while (cur.moveToNext()) {
            // Get the field values
            String giorno = cur.getString(EVENT_COLUMN.get(CalendarProvider.))
            String capitolo = cur.getLong(EVENT_COLUMN.get(CalendarContract.Events.CALENDAR_ID));
            String orario = cur.getString(EVENT_COLUMN.get(CalendarContract.Events.TITLE));

            Log.d(TAG, "event id: " + eventId + " - title : " + title + " - start date : " + new Date(dtStart));

            result.add(new MyEvent(giorno, capitolo, orario));
        }
        Log.d(TAG, "Retrieved " + result.size() + " event(s) from the content provider of the google calendar");
        return result;
    }*/

    // TODO: Example for adding a new event
    //  https://developer.android.com/guide/topics/providers/calendar-provider#add-event

}
