package com.example.scuolaguida.models;

import com.example.scuolaguida.adapter.HomeAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventPratica {


    private String giorno,mese,anno,orario,istruttore,veicolo,targa,tipo,patente;
    private long EventID;

    public EventPratica(){}

    public EventPratica(String giorno, String mese, String anno, String orario, String veicolo, String targa, String istruttore
                        , String tipo, String patente){
        this.giorno = giorno;
        this.mese=mese;
        this.anno=anno;
        this.orario=orario;
        this.istruttore=istruttore;
        this.targa=targa;
        this.veicolo=veicolo;
        this.tipo=tipo;
        this.patente=patente;
    }

    private HomeAdapter.ViewHolder viewHolder;
    public HomeAdapter.ViewHolder getViewHolder() {
        return viewHolder;
    }
    public void setViewHolder(HomeAdapter.ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    public String getGiorno() {return giorno;}
    public String getMese(){return mese;}
    public String getAnno(){return anno;}
    public String getOrario() {return orario;}
    public String getIstruttore() {return istruttore;}
    public String getVeicolo() {return veicolo;}
    public String getTipo() {return tipo;}
    public String getPatente() {return patente;}
    public String getTarga() {return targa;}
    public long getEventID(){return  EventID;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventPratica myPlaces = (EventPratica) o;
        return veicolo.equals(myPlaces.veicolo) && giorno.equals(myPlaces.giorno) && orario.equals(myPlaces.orario)
                && EventID == myPlaces.EventID && mese.equals(myPlaces.mese) && anno.equals(myPlaces.anno) && targa.equals(myPlaces.targa)
                && istruttore.equals(myPlaces.istruttore) && patente.equals(myPlaces.patente) && tipo.equals(myPlaces.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(giorno, mese, anno, orario, EventID, istruttore, veicolo, targa,patente,tipo);
    }

    public static class Collection<T extends EventPratica> {
        private static final String TAG = EventPratica.Collection.class.getCanonicalName();
        public final List<T> places;

        public Collection(List<T> places) {
            this.places = places;
        }

        public void firebaseDbCallback(Task<DataSnapshot> result) {
            // NOTE: This is a callback -- I do not have any guarantee when it is invoked!
            // --> Do not wait for these results on the main activity
            assert result != null;

            if (result.isSuccessful()) {
                List<EventPratica> MyPlacesArray = new ArrayList<EventPratica>();

                // Iterate over the db objects
                for (DataSnapshot child : result.getResult().getChildren()) {
                    EventPratica Place = child.getValue(EventPratica.class);
                    MyPlacesArray.add(Place);
                }

            }
        }
    }

}
