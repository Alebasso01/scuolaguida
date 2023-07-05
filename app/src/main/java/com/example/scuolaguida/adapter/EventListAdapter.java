package com.example.scuolaguida.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.models.MyEvent;

import org.w3c.dom.Text;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private MyEvent[] lessons;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView giornoID;
        private final TextView orarioID;
        private final TextView capitoloID;

        public ViewHolder(View view){
            super(view);
            this.giornoID = (TextView) view.findViewById(R.id.giornoID);
            this.capitoloID = (TextView) view.findViewById(R.id.capitoloID);
            this.orarioID = (TextView) view.findViewById(R.id.orarioID);
        }
        public TextView getGiornoID(){return giornoID;}
        public TextView getCapitoloID(){return capitoloID;}
        public TextView getOrarioID(){return orarioID;}


    }
    public EventListAdapter(MyEvent[] L){
        this.lessons = L;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lezioni_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.getGiornoID().setText(String.valueOf(this.lessons[position].getGiorno()));
        viewHolder.getOrarioID().setText(String.valueOf(this.lessons[position].getOrario()));
        viewHolder.getCapitoloID().setText(String.valueOf(this.lessons[position].getCapitolo()));
    }

    @Override
    public int getItemCount() {return this.lessons.length;}

}

