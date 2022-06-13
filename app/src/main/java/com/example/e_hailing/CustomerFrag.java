package com.example.e_hailing;

import static android.content.ContentValues.TAG;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TableLayout tableLayout;
    customerDataBase Db;
    private LayoutInflater inflater;
    private TextView txt;


    public CustomerFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerFrag newInstance(String param1, String param2) {
        CustomerFrag fragment = new CustomerFrag();
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
        Db = new customerDataBase(getActivity());
        tableLayout=getActivity().findViewById(R.id.tableLayout);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer, container, false);
    }

    public void displayInTable(){
        Cursor data=Db.getCustomerData();
        data.moveToFirst();
        do {
            View tableRow = inflater.from(getActivity()).inflate(R.layout.table_item,null,false);
            TextView name  = tableRow.findViewById(R.id.name);
            TextView status  = tableRow.findViewById(R.id.status);
            TextView expectedArrivalTime = tableRow.findViewById(R.id.expectedArrivalTime);
            TextView capacity  = tableRow.findViewById(R.id.capacity);
            TextView startLongLa  = tableRow.findViewById(R.id.startLongLa);
            TextView destination  = tableRow.findViewById(R.id.destinationLongLa);


            name.setText(data.getString(0));
            String email = data.getString(1);
            String password= data.getString(2);
            status.setText(data.getString(3));
            expectedArrivalTime.setText(data.getString(4));
            capacity.setText(data.getString(5));
            startLongLa.setText(data.getString(6));
            destination.setText(data.getString(7));

            tableLayout.addView(tableRow);

        } while (data.moveToNext());
        data.close();


    }
}