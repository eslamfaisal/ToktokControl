package com.fekrah.toktokcontrol;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class PricesFragment extends Fragment {


    TextView minDistance, minDistancePrice, extraKmPrice;

    public PricesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        minDistance = view.findViewById(R.id.min_distance);
        minDistancePrice = view.findViewById(R.id.min_distance_price);
        extraKmPrice = view.findViewById(R.id.extra);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),EditPricesActivity.class));

            }
        });

        FirebaseDatabase.getInstance().getReference().child("defaults").child("prices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Prices prices = dataSnapshot.getValue(Prices.class);
                    if (prices != null) {
                        extraKmPrice.setText(": "+prices.getExtra_km_price()+getString(R.string.SR));
                        minDistance.setText(": "+prices.getMin_distance()+getString(R.string.km));
                        minDistancePrice.setText(": "+prices.getMin_distance_price()+getString(R.string.SR));
                        Log.d("nnnnnn", "onDataChange: "+prices.getExtra_km_price());
                    }
                    Log.d("nnnnnn", "onDataChange: null1");
                }
                else Log.d("nnnnnn", "onDataChange: null2");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
