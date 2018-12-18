package com.fekrah.toktokcontrol;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class CodeFragment extends Fragment {


    View mainView;

    TextInputEditText price;
    Button generate;
    TextView code;
    private Dialog dialog;

    public CodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_code, container, false);
        return mainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        price = mainView.findViewById(R.id.code_price);
        generate = mainView.findViewById(R.id.generate);
        code = mainView.findViewById(R.id.generated_code);
        code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createMenu(v, code.getText().toString());
                return true;
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryGenerate();
            }
        });
    }

    List<String> generatedCodsInDataBase = new ArrayList<>();
    final List<Code> codesInDB = new ArrayList<>();

    int min = 10000000;
    int max = 99999999;
    int rundom;

    String generateCode;

    private void tryGenerate() {
        code.setText("");
        dialog = new Dialog(getActivity());
        dialog.show();
        if (price.getText().toString().equals("")){
            dialog.dismiss();
            Toast.makeText(getActivity(), "برجاء ادخال السعر اولا", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabase.getInstance().getReference().child("codes")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.getValue() != null) {
                            Code code = dataSnapshot.getValue(Code.class);
                            if (code != null) {
                                codesInDB.add(code);
                                generatedCodsInDataBase.add(String.valueOf(code.getGenerated_code()));
                            }

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        checkCode();

    }

    void checkCode() {
        rundom = new Random().nextInt((max - min) + 1) + min;

        generateCode = String.valueOf(rundom);
        if (generatedCodsInDataBase.contains(generateCode)) {
            again();
        } else {
            FirebaseDatabase.getInstance().getReference().child("codes").child(generateCode)
                    .setValue(new Code(generateCode, price.getText().toString(), Long.parseLong(generateCode))).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        dialog.dismiss();
                        code.setText(generateCode);

                    }
                }
            });
        }
    }

    void again() {
        rundom = new Random().nextInt((max - min) + 1) + min;

        generateCode = String.valueOf(rundom);
        if (generatedCodsInDataBase.contains(generateCode)) {
            checkCode();
        } else {
            FirebaseDatabase.getInstance().getReference().child("codes").child(generateCode)
                    .setValue(new Code(generateCode, price.getText().toString(), Long.parseLong(generateCode))).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        code.setText(generateCode);
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    private void setClipboard(Activity context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }


    private void createMenu(View view, final String messageText) {
        //creating a popup menu
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //inflating menu from xml resource
        popup.inflate(R.menu.menu_code);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.copy:
                        setClipboard(getActivity(), messageText);
                        Toast.makeText(getActivity(), "تم النسخ", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        //displaying the popup
        popup.show();
    }


}
