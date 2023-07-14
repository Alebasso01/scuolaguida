package com.example.scuolaguida.models;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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

    private long getCalendarId(ContentResolver contentResolver) {
        long calendarId = -1;
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection = CalendarContract.Calendars.VISIBLE + " = 1";
        Cursor cursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI, projection, selection, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            calendarId = cursor.getLong(0);
            cursor.close();
        }
        return calendarId;
    }

    public boolean writeEventToCalendar(Context context, String title, String description, long startTime, long endTime) {
        // Verifica se l'app ha l'autorizzazione per scrivere sul calendario
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // Richiedi l'autorizzazione per scrivere sul calendario se non è stata ancora concessa
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, 0);
            return false;
        }

        ContentValues eventValues = new ContentValues();

        // Imposta i valori per il nuovo evento
        eventValues.put(CalendarContract.Events.CALENDAR_ID, getCalendarId(contentResolver));
        eventValues.put(CalendarContract.Events.TITLE, title);
        eventValues.put(CalendarContract.Events.DESCRIPTION, description);
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        eventValues.put(CalendarContract.Events.DTSTART, startTime);
        eventValues.put(CalendarContract.Events.DTEND, endTime);

        // Inserisci l'evento nel calendario
        Uri eventUri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, eventValues);

        return eventUri != null;
    }

    public boolean removeEventFromCalendar(Context context, long eventId) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
        int rowsDeleted = contentResolver.delete(deleteUri, null, null);
        if (rowsDeleted > 0) {
            // L'evento è stato eliminato con successo
            return  true;
        } else {
            // Non è stato possibile eliminare l'evento
            return false;
        }
    }


}