package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link groupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class groupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static final String GROUP_NAME="Food_DisCo";
    public groupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment groupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static groupFragment newInstance(String param1, String param2) {
        groupFragment fragment = new groupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        // Find the ListView in the inflated view
        ListView listView = view.findViewById(R.id.listViewGroups);

        // Create the data for the ListView
        String[] groups = {"Food", "Rescue", "Other"};

        // Create an ArrayAdapter and set it on the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, groups);
        listView.setAdapter(adapter);

        // Set an OnItemClickListener on the ListView
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Handle the click event
            String selectedGroup = groups[position]; // Get the clicked item
            if (selectedGroup.equals("Food")) {
                // Start the ChatPage activity
                Intent intent = new Intent(getActivity(), chatPage.class);
                intent.putExtra(GROUP_NAME,"Food");
                startActivity(intent);
            } else if (selectedGroup.equals("Rescue")) {
                // Start the ChatPage activity
                Intent intent = new Intent(getActivity(), chatPage.class);
                intent.putExtra(GROUP_NAME,"Rescue");
                startActivity(intent);
            }else if (selectedGroup.equals("Other")) {
                // Start the ChatPage activity
                Intent intent = new Intent(getActivity(), chatPage.class);
                intent.putExtra(GROUP_NAME,"Other");
                startActivity(intent);
            }
            // You can handle other group selections here if needed
        });
        // Return the inflated view
        return view;
    }
}
