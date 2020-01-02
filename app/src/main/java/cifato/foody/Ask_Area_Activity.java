package cifato.foody;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.City_Data_Datas;
import util.CustomVolleyJsonRequest;

public class Ask_Area_Activity extends AppCompatActivity {
    Spinner area_spin;
    EditText city_search_edt;
    Button selec_area_btn;
    ArrayList<City_Data_Datas>category_modelList  = new ArrayList<>();
    public static String selected_area_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask__area_);
        area_spin = findViewById(R.id.area_spin);
        city_search_edt = findViewById(R.id.city_search_edt);
        selec_area_btn = findViewById(R.id.selec_area_btn);
        getallmyCities();
        selec_area_btn.setVisibility(View.GONE);
//        selec_area_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private void getallmyCities() {

        // Tag used to cancel the request
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
               "http://cifato.com/cifato_store/api/get_area", params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Area Tag", response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<City_Data_Datas>>() {
                        }.getType();

                        category_modelList = gson.fromJson(response.getString("data"), listType);
                        ArrayList<String> spinnerarraylist = new ArrayList<>();
                        spinnerarraylist.add("Select Area");
                        for(int i=0;i<category_modelList.size();i++)
                        {
                            spinnerarraylist.add(i+1 , category_modelList.get(i).getArea());
                        }
                        //Creating the ArrayAdapter instance having the country list
                        ArrayAdapter aa = new ArrayAdapter(Ask_Area_Activity.this,android.R.layout.simple_spinner_item,spinnerarraylist);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        area_spin.setAdapter(aa);
                        area_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Log.e("Selected" , "location id"+selected_area_id);
                                if(area_spin.getSelectedItemPosition() !=0) {
                                    Intent intent = new Intent(Ask_Area_Activity.this, MainActivity.class);
                                    selected_area_id = category_modelList.get(area_spin.getSelectedItemPosition()-1).getId();
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(Ask_Area_Activity.this, "Please select Area", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Ask_Tag", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Ask_Area_Activity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    @Override
    public void onBackPressed() {
        Intent intent  =new Intent(this , MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
