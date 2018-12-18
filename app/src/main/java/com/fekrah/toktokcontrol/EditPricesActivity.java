package com.fekrah.toktokcontrol;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPricesActivity extends AppCompatActivity {

    TextInputEditText minDistance, minDistancePrice, extraKmPrice;

    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prices);
        minDistance = findViewById(R.id.min_distance);
        minDistancePrice = findViewById(R.id.min_distance_price);
        extraKmPrice = findViewById(R.id.extra);
        save = findViewById(R.id.save);

        FirebaseDatabase.getInstance().getReference().child("defaults").child("prices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Prices prices = dataSnapshot.getValue(Prices.class);
                    if (prices != null) {
                        extraKmPrice.setText( prices.getExtra_km_price());
                        minDistance.setText( prices.getMin_distance());
                        minDistancePrice.setText( prices.getMin_distance_price() );
                        Log.d("nnnnnn", "onDataChange: " + prices.getExtra_km_price());
                    }
                    Log.d("nnnnnn", "onDataChange: null1");
                } else Log.d("nnnnnn", "onDataChange: null2");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(EditPricesActivity.this);
                dialog.show();

                if (minDistance.getText().toString().equals("") ||
                        minDistancePrice.getText().toString().equals("") ||
                        extraKmPrice.getText().toString().equals("")) {
                    Toast.makeText(EditPricesActivity.this, "برجاء ادخال جميع البيانات", Toast.LENGTH_SHORT).show();
                }

                Prices prices = new Prices(
                        extraKmPrice.getText().toString(),
                        minDistance.getText().toString(),
                        minDistancePrice.getText().toString()
                );
                FirebaseDatabase.getInstance().getReference().child("defaults").child("prices").setValue(prices).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditPricesActivity.this, "تم الحفظ", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }
        });


    }
}
