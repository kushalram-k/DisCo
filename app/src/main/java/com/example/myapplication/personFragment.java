package com.example.myapplication;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link personFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class personFragment extends Fragment {


    public static final String GROUP_NAME="Food_DisCo";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<String> deviceList;
    private ArrayAdapter<String> adapter;
    Button discoverButton;


    public personFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment personFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static personFragment newInstance(String param1, String param2) {
        personFragment fragment = new personFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceList = new ArrayList<>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        // Find the ListView in the inflated view
        ListView listView = view.findViewById(R.id.listViewPersons);

        // Create the data for the ListView
//        String[] persons = {"person1", "person2", "person3"};
        ArrayList<String> deviceNames = getArguments() != null ?
                getArguments().getStringArrayList("deviceNames") : new ArrayList<>();
        // Create an ArrayAdapter and set it on the ListView
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, deviceList);
        listView.setAdapter(adapter);

        // Set an OnItemClickListener on the ListView
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            mainPageholder.mainPage1.connect(position);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(getActivity(), chatPage.class);
                intent.putExtra(GROUP_NAME, deviceList.get(position));
                startActivity(intent);
            }, 5000);

        });




        // Now use view.findViewById to get references to your views
        discoverButton = view.findViewById(R.id.button13);
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof mainPage) {
                    ((mainPage) getActivity()).discover();
                }
            }
        });


        // Return the inflated view
        return view;
    }

    public void updateDeviceList(String[] newDevices) {
        if(!deviceList.isEmpty())  deviceList.clear();
        deviceList.addAll(Arrays.asList(newDevices));
        adapter.notifyDataSetChanged();
    }
}