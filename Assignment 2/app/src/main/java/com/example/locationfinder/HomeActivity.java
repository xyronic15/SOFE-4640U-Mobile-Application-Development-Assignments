package com.example.locationfinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements LocAdapter.onLocListener {

    public static final int REQUEST_CODE_ADD = 1;
    public static final int REQUEST_CODE_EDIT_DELETE = 2;
    LocModel locData;
    LocDatabase locDatabase;
    ArrayList<LocModel> locations;
    LocAdapter locAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        set the database
        locDatabase = new LocDatabase(this);

//      set button for adding location to db
        ImageView imageAddLoc = findViewById(R.id.imageAddLoc);
        imageAddLoc.setOnClickListener((v) -> {
            Intent intent = new Intent(getApplicationContext(), DisplayLocActivity.class);
            intent.putExtra("action", "add");
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });

//      get all the locations and display them
        locations = locDatabase.getAllLocs();

        recyclerView = findViewById(R.id.locRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        locAdapter = new LocAdapter(HomeActivity.this, locations, this);
        recyclerView.setAdapter(locAdapter);

//      search bar function
        EditText searchBar = findViewById(R.id.inputSearch);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                searchExisting(searchBar.getText().toString());
            }
        });


    }

//  searchbar function
    private void searchExisting(String sub){

        ArrayList<LocModel> searchedLocs = new ArrayList<>();
        locations = locDatabase.getAllLocs();
        if (!sub.isEmpty()){
            for (LocModel l : locations){
                if (l.getAddress().toLowerCase().contains(sub.toLowerCase())){
                    searchedLocs.add(l);
                }
            }
            locations = searchedLocs;
            locAdapter.updateAdapter(locations);
        }
        else {
            locations = locDatabase.getAllLocs();
            locAdapter.updateAdapter(locations);
        }
    }

//  set onclick function
    @Override
    public void onLocClick(int position) {
        LocModel location = locations.get(position);
        Intent intent = new Intent(HomeActivity.this, DisplayLocActivity.class);
        intent.putExtra("loc", location);
        intent.putExtra("action", "edit");
        startActivityForResult(intent, REQUEST_CODE_EDIT_DELETE);
    }

//  get result of the activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_CODE_ADD){
                Toast.makeText(this, "Location added", Toast.LENGTH_SHORT).show();
                locData = (LocModel) data.getExtras().getSerializable("loc");
                locDatabase.addLoc(locData);
            } else if (requestCode == REQUEST_CODE_EDIT_DELETE){
                locData = (LocModel) data.getExtras().getSerializable("loc");
                String action = (String) data.getExtras().get("action");

                if (action.equalsIgnoreCase("update")){
                    locDatabase.updateLoc(locData);
                } else if (action.equalsIgnoreCase("delete")){
                    locDatabase.deleteLoc(locData);
                }
            }
        }

//      update locations arraylist and update locadapter
        locations = locDatabase.getAllLocs();
        locAdapter.updateAdapter(locations);
    }
}