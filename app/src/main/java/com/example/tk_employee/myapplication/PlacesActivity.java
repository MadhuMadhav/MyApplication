package com.example.tk_employee.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlacesActivity extends AppCompatActivity {

    String[] places={"Movies","Shopping Mall","Restaurants","Banks","Schools","library","Park","Hospitals","Airport","ATM","Bakery","Book Store"
            ,"Bus Station","Gym","Temple","Police Station","Jewelry Shop","Railway Station","Travel Agency","Lodges"
            ,"Government Offices","Parking","Plumber","Foot Wear","Medical Shops","Painter"};

    String[] jsonPlacesNames={"movie_theater","shopping_mall","restaurant","bank","school","library","park","hospital","airport","atm","bakery","book_store"
            ,"bus_station","gym","hindu_temple","police","jewelry_store","train_station","travel_agency","lodging"
            ,"local_government_office","parking","plumber","shoe_store","pharmacy","painter"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        ListView listView=(ListView) findViewById(R.id.listView);

        PlacesActivity.this.setTitle("Places");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,places);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String value=adapter.getItem(position);
                Intent intent= new Intent(PlacesActivity.this, ResultsActivity.class);
                intent.putExtra("JSON_ID", jsonPlacesNames[position]);
                intent.putExtra("PLACE_ID",places[position]);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home: finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
