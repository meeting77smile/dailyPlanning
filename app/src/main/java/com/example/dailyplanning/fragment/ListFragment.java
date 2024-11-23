package com.example.dailyplanning.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.dailyplanning.R;
import com.example.dailyplanning.adapter.BillListAdapter;
import com.example.dailyplanning.adapter.ListListAdapter;
import com.example.dailyplanning.database.BillDBHelper;
import com.example.dailyplanning.database.ListDBHelper;
import com.example.dailyplanning.entity.BillInfo;
import com.example.dailyplanning.entity.ListInfo;

import java.util.List;


public class ListFragment extends Fragment {

    public ListView lv_list;

    public static ListFragment newInstance(String day) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString("day",day);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,container,false);
        lv_list = view.findViewById(R.id.lv_list);
        ListDBHelper mDBHelper = ListDBHelper.getInstance(getContext());
        Bundle arguments = getArguments();
        String Day = arguments.getString("day");
        List<ListInfo> listInfoList = mDBHelper.queryByDay(Day);
        ListListAdapter adapter = new ListListAdapter(getContext(),listInfoList);
        lv_list.setAdapter(adapter);
        return view;
    }
}