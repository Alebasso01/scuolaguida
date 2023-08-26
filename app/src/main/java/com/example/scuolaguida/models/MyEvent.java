package com.example.scuolaguida.models;

import com.example.scuolaguida.adapter.HomeAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyEvent {

    private String giorno,mese,anno,capitolo,orario,tipo,patente,capienzaattuale,capienza;
    private long EventID;

    public MyEvent(){}

    public MyEvent(String giorno, String mese, String anno, String capitolo, String orario,String tipo
            , String patente, String capienzaattuale, String capienza){
        this.giorno = giorno;
        this.mese=mese;
        this.anno=anno;
        this.capitolo=capitolo;
        this.orario=orario;
        this.tipo=tipo;
        this.patente=patente;
        this.capienzaattuale=capienzaattuale;
        this.capienza=capienza;
    }

    private HomeAdapter.ViewHolder viewHolder;

    public HomeAdapter.ViewHolder getViewHolder() {
        return viewHolder;
    }
    public void setViewHolder(HomeAdapter.ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    public String getCapitolo() {return capitolo;}

    public String getGiorno() {return giorno;}
    public String getMese(){return mese;}
    public String getAnno(){return anno;}
    public String getTipo(){return tipo;}
    public String getPatente(){return patente;}

    public String getOrario() {return orario;}
    public long getEventID(){return  EventID;}
    public String getCapienza(){return capienza;}
    public String getCapienzaattuale(){return capienzaattuale;}

    public void setCapitolo(String capitolo) {this.capitolo = capitolo;}

    public void setGiorno(String giorno) {this.giorno = giorno;}

    public void setOrario(String orario) {this.orario = orario;}
    public void setEventID(long EventId){this.EventID = EventId;}
    public void setMese(String mese){this.mese = mese;}
    public void setAnno(String anno){this.anno = anno;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyEvent myPlaces = (MyEvent) o;
        return capitolo.equals(myPlaces.capitolo) && giorno.equals(myPlaces.giorno) && orario.equals(myPlaces.orario)
                 && EventID == myPlaces.EventID && mese.equals(myPlaces.mese) && anno.equals(myPlaces.anno)
                && tipo.equals(myPlaces.tipo) && patente.equals(myPlaces.patente)
                && capienza.equals(myPlaces.capienza) && capienzaattuale.equals(myPlaces.capienzaattuale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(giorno, mese, anno, orario, capitolo, EventID,tipo,patente,capienzaattuale,capienza);
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
