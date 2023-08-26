package com.example.scuolaguida.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.models.EventPratica;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
import com.example.scuolaguida.models.MyWorker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.CookieHandler;
import java.util.Calendar;
import java.util.List;

public class PraticaAdapter extends RecyclerView.Adapter<PraticaAdapter.ViewHolder>{

    private List<EventPratica> lessons;
    private Context context;

    public PraticaAdapter(List<EventPratica> L, Context c) {
        this.lessons = L;
        this.context = c;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView giornoID;
        private final TextView meseID;
        private final TextView annoID;
        private final TextView orarioID;
        private final TextView istruttoreID;
        private final TextView veicoloID;
        private final TextView targaID;
        private final TextView patenteID;
        private final TextView tipoID;


        private FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();
        private MyWorker worker;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;  // Mese inizia da 0, quindi aggiungi 1
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        TextView bottoneprenotazioni;
        CardView cardView = itemView.findViewById(R.id.cardviewpratica);

        public ViewHolder(View view) {
            super(view);
            this.giornoID = (TextView) view.findViewById(R.id.giornoID);
            this.orarioID = (TextView) view.findViewById(R.id.orarioID);
            this.meseID = (TextView) view.findViewById(R.id.meseID);
            this.annoID = (TextView) view.findViewById(R.id.annoID);
            this.istruttoreID = (TextView) view.findViewById(R.id.istruttoreID);
            this.veicoloID = (TextView) view.findViewById(R.id.veicoloID);
            this.targaID = (TextView) view.findViewById(R.id.targaID);
            this.tipoID = (TextView) view.findViewById(R.id.tipoID);
            this.patenteID = (TextView) view.findViewById(R.id.patenteID);
            bottoneprenotazioni = view.findViewById(R.id.bottonenuovaprenotazione);

            bottoneprenotazioni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String giorno = getGiornoID().getText().toString();
                    String orario = getOrarioID().getText().toString();
                    String mese = getMeseID().getText().toString();
                    String anno = getAnnoID().getText().toString();
                    String veicolo = getVeicoloID().getText().toString();
                    String istruttore = getIstruttoreID().getText().toString();
                    String targa = getTargaID().getText().toString();
                    String tipo = getTipoID().getText().toString();
                    String patente = getPatenteID().getText().toString();
                    String userId = auth.getUid();

                    DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
                            .getReference();
                    String lezioneid = giorno+"-"+mese+"-"+anno+"-"+targa+"-"+orario;
                    DatabaseReference userRef_teoria = databaseRef.child("users").child(userId).child("teoria").child(lezioneid);
                    DatabaseReference userRef_pratica = databaseRef.child("users").child(userId).child("pratica").child(lezioneid);
                    DatabaseReference Ref = databaseRef.child("users").child(userId);

                    userRef_pratica.child("giorno").setValue(giorno);
                    userRef_pratica.child("mese").setValue(mese);
                    userRef_pratica.child("anno").setValue(anno);
                    userRef_pratica.child("targa").setValue(targa);
                    userRef_pratica.child("orario").setValue(orario);
                    userRef_pratica.child("istruttore").setValue(istruttore);
                    userRef_pratica.child("veicolo").setValue(veicolo);
                    userRef_pratica.child("tipo").setValue(tipo);
                    userRef_pratica.child("patente").setValue(patente);

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(view.getContext().getString(R.string.titolo_prenotazione));
                    builder.setMessage(view.getContext().getString(R.string.contenuto_prenotazione));
                    AlertDialog dialog = builder.create();
                    dialog.show();}
            });
        }

        public TextView getGiornoID(){return giornoID;}
        public TextView getOrarioID(){return orarioID;}
        public TextView getMeseID(){return meseID;}
        public TextView getAnnoID(){return annoID;}
        public TextView getIstruttoreID(){return istruttoreID;}
        public TextView getVeicoloID(){return veicoloID;}
        public TextView getTargaID(){return targaID;}
        public TextView getTipoID(){return tipoID;}
        public TextView getPatenteID(){return patenteID;}
        }

    @NonNull
    @Override
    public PraticaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lezioni_pratica, viewGroup, false);

        return new PraticaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.getGiornoID().setText(String.valueOf(this.lessons.get(position).getGiorno()));
        viewHolder.getOrarioID().setText(String.valueOf(this.lessons.get(position).getOrario()));
        viewHolder.getMeseID().setText(String.valueOf(this.lessons.get(position).getMese()));
        viewHolder.getAnnoID().setText(String.valueOf(this.lessons.get(position).getAnno()));
        viewHolder.getIstruttoreID().setText(String.valueOf(this.lessons.get(position).getIstruttore()));
        viewHolder.getTargaID().setText(String.valueOf(this.lessons.get(position).getTarga()));
        viewHolder.getVeicoloID().setText(String.valueOf(this.lessons.get(position).getVeicolo()));
        viewHolder.getTipoID().setText(String.valueOf(this.lessons.get(position).getTipo()));
        viewHolder.getPatenteID().setText(String.valueOf(this.lessons.get(position).getPatente()));
    }

    @Override
    public int getItemCount() {return this.lessons.size();}


    public void setLessons(List<EventPratica> lessonList) {
        this.lessons = lessonList;
    }
}
