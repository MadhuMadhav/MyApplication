package com.example.tk_employee.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    String placeId,actionBarName;
    ListView listView;
    TextView tvError;

    Boolean isConnectionExist = false;
    MobileInternetConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvError=findViewById(R.id.tvError);

        cd = new MobileInternetConnectionDetector(getApplicationContext());
        isConnectionExist = cd.checkMobileInternetConn();

        expandableListView=findViewById(R.id.resultsListView);
        Intent intent=getIntent();
        if(intent != null){
            getDataFromIntent(intent);
        }
        ResultsActivity.this.setTitle(actionBarName);

        if(isConnectionExist) {
            jsonParse();
        }

        else
        {
            expandableListView.setVisibility(View.GONE);
            tvError.setText("Oops...No Internet Connection");
        }
    }


    public void getDataFromIntent(Intent intent)
    {
        placeId=intent.getStringExtra("JSON_ID");
        actionBarName=intent.getStringExtra("PLACE_ID");
        Log.e("places",placeId);
    }

    public void jsonParse()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        //String lat="17.4258206",lang="78.42021569999997";
        String lat,lang;
        lat=MapsActivity.lat;
        lang=MapsActivity.lang;
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lang+"&radius=1500&type="+placeId+"&key=AIzaSyAZpgQR5TSoDuWPSMHQGLiZ31Lm1Koe58M";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest sr=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseString) {
                try
                {
                    JSONObject responseObj = new JSONObject(responseString);
                    Log.i("inElse","Results "+responseString);
                    //JSONArray resArray = responseObj.getJSONArray("status");
                    if(!(responseObj.optString("status").equals("ZERO_RESULTS"))) {
                        ArrayList<ParentClass> TotalListDos = new ArrayList<ParentClass>();

                        //JSONObject responseObj = new JSONObject(responseString);
                        JSONArray resultArray = responseObj.getJSONArray("results");

                        for (int i = 0; i < resultArray.length(); i++) {
                            ArrayList<ChildClass> childClassDo = new ArrayList<ChildClass>();
                            ParentClass parentClassDos = new ParentClass();
                            ChildClass childClass = new ChildClass();

                            JSONObject dataObject = resultArray.optJSONObject(i);
                            parentClassDos.pName = dataObject.optString("name");
                            parentClassDos.pRating = dataObject.optString("rating");

                            childClass.cName = parentClassDos.pName;
                            childClass.cRating = parentClassDos.pRating;
                            childClass.cLocation = dataObject.optString("vicinity");

                            childClassDo.add(childClass);
                            parentClassDos.setChildItems(childClassDo);
                            TotalListDos.add(parentClassDos);
                        }
                        ExpGroupAdapter jsonProfileAdapter = new ExpGroupAdapter(ResultsActivity.this, TotalListDos);
                        expandableListView.setAdapter(jsonProfileAdapter);
                    }

                    else
                    {
                        Log.i("inElse","Results Not Found");
                        expandableListView.setVisibility(View.GONE);

                    }
                }
                catch (Exception e)
                {
                    Log.e("error","Problem In Parsing...");
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error","Volley Error...Response Not Received");
                progressDialog.dismiss();
            }
        });
        queue.add(sr);
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
