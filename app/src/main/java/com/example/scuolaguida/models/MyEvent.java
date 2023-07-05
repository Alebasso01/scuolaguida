package com.example.scuolaguida.models;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyEvent {

    private String giorno,capitolo,orario;
    private long EventID;

    public MyEvent(){}

    public String getCapitolo() {return capitolo;}

    public String getGiorno() {return giorno;}

    public String getOrario() {return orario;}
    public long getEventID(){return  EventID;}

    public void setCapitolo(String capitolo) {this.capitolo = capitolo;}

    public void setGiorno(String giorno) {this.giorno = giorno;}

    public void setOrario(String orario) {this.orario = orario;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyEvent myPlaces = (MyEvent) o;
        return capitolo.equals(myPlaces.capitolo) && giorno.equals(myPlaces.giorno)
                && orario.equals(myPlaces.orario) && EventID == myPlaces.EventID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(giorno, orario, capitolo, EventID);
    }

    public static class Collection<T extends MyEvent> {
        private static final String TAG = Collection.class.getCanonicalName();
        public final List<T> places;

        public Collection(List<T> places) {
            this.places = places;
        }

        public void firebaseDbCallback(Task<DataSnapshot> result) {
            // NOTE: This is a callback -- I do not have any guarantee when it is invoked!
            // --> Do not wait for these results on the main activity
            assert result != null;

            if (result.isSuccessful()) {
                List<MyEvent> MyPlacesArray = new ArrayList<MyEvent>();

                // Iterate over the db objects
                for (DataSnapshot child : result.getResult().getChildren()) {
                    MyEvent Place = child.getValue(MyEvent.class);
                    MyPlacesArray.add(Place);
                }

            }
        }
    }
}
