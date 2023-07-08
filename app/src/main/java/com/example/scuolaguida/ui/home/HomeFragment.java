package com.example.scuolaguida.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.scuolaguida.R;
import com.example.scuolaguida.databinding.FragmentHomeBinding;
import com.example.scuolaguida.models.CalendarProvider;

import org.checkerframework.checker.units.qual.C;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2023);
        calendar.set(Calendar.MONTH, Calendar.JULY);
        calendar.set(Calendar.DAY_OF_MONTH,8);
        calendar.set(Calendar.HOUR_OF_DAY,13);
        calendar.set(Calendar.MINUTE,30);
        calendar.set(Calendar.SECOND,0);
        long starttime = calendar.getTimeInMillis();
        long endtime = starttime+(60*60*1000);

        Button bottonecalendario = view.findViewById(R.id.bottonecalendario);

        bottonecalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarProvider calendarProvider = new CalendarProvider(getActivity().getContentResolver());
                //calendarProvider.writeEventToCalendar(getContext(),"ciao","ciao",starttime,endtime);
                boolean isEventAdded = calendarProvider.writeEventToCalendar(getActivity(),"nuovo evento",
                        "ciao",starttime,endtime);
                if(isEventAdded){
                    Toast.makeText(getActivity(),"evento aggiunto",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(),"errore",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}