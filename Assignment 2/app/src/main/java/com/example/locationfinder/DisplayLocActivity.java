package com.example.locationfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DisplayLocActivity extends AppCompatActivity {

    private LocModel currLoc;
    private int locID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_loc);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener((v) -> onBackPressed());

        TextView addressText = findViewById(R.id.addressText);
        EditText inputLat = findViewById(R.id.inputLat);
        EditText inputLong = findViewById(R.id.inputLong);

//      delete button functionality
        ImageView imageDelete = findViewById(R.id.imageDelete);

        Bundle bundle = getIntent().getExtras();
        String action = bundle.getString("action");

//      show the info if location is from db
        if (action.equalsIgnoreCase("edit")){
            imageDelete.setVisibility(View.VISIBLE);
            currLoc = (LocModel) bundle.getSerializable("loc");
            addressText.setText(currLoc.getAddress());
            inputLat.setText(Double.toString(currLoc.getLatitude()));
            inputLong.setText(Double.toString(currLoc.getLongtitude()));
            locID = currLoc.getId();
        }

        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DisplayLocActivity.this);
                alertBuilder.setMessage("Are you sure you want to delete this location?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplication(), HomeActivity.class);
                                intent.putExtra("loc", currLoc);
                                intent.putExtra("action", "delete");
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });

//      calculate the latitude and longitude, and display when clicked
        Button calculate = findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double latitude = Double.parseDouble(inputLat.getText().toString());
                Double longitude = Double.parseDouble(inputLong.getText().toString());
                LocModel newLoc;
                if (latitude != null && longitude != null){
                    if (currLoc == null){
                        newLoc = calculate(-1, latitude, longitude);
                    } else {
                        newLoc = calculate(currLoc.getId(), latitude, longitude);
                    }

                    if (newLoc != null){
                        addressText.setText(newLoc.getAddress());
                    } else {
                        Toast.makeText(DisplayLocActivity.this, "No location with that address or name could be found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DisplayLocActivity.this, "Enter an address or location name", Toast.LENGTH_SHORT).show();
                }
            }
        });

//      done button functionality
        ImageView imageDone = findViewById(R.id.imageDone);
        imageDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action.equalsIgnoreCase("edit")){
                    String newAddr = addressText.getText().toString();
                    Double newLat = Double.parseDouble(inputLat.getText().toString());
                    Double newLong = Double.parseDouble(inputLong.getText().toString());

                    LocModel newLoc = new LocModel(currLoc.getId(), newAddr, newLat, newLong);

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("loc", newLoc);
                    intent.putExtra("action", "update");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else if (!addressText.getText().toString().isEmpty()){
                    String newAddr = addressText.getText().toString();
                    Double newLat = Double.parseDouble(inputLat.getText().toString());
                    Double newLong = Double.parseDouble(inputLong.getText().toString());

                    LocModel newLoc = new LocModel(-1, newAddr, newLat, newLong);

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("loc", newLoc);
                    intent.putExtra("action", "add");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(DisplayLocActivity.this, "Could not save location", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//  function to get the address from lat and long
    public LocModel calculate(int id, Double inputLat, Double inputLong){
        LocModel location = new LocModel(id, null, inputLat, inputLong);
        if (Geocoder.isPresent()){
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> ls = geocoder.getFromLocation(inputLat, inputLong, 1);
                if (ls.size() == 0) {
                    return null;
                }
                Address loc = ls.get(0);
                String address = loc.getAddressLine(0);

                location.setAddress(address);

                return location;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(DisplayLocActivity.this, "Error running geocoder", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        return null;
    }
}