package Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Delivery_get_address_adapter;
import Config.BaseURL;
import Model.Delivery_address_model;
import cifato.foody.AppController;
import cifato.foody.CustomDialogClass;
import cifato.foody.MainActivity;
import cifato.foody.MyInterface;
import cifato.foody.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;


public class Delivery_fragment extends Fragment implements View.OnClickListener{

    private static String TAG = Delivery_fragment.class.getSimpleName();

    TextView tv_guestuser, tv_guestmobile;
    String Guestuser, Guestmobile;
    String TrainNo, PnrNo;
    private RadioButton radioTrain;
    private TextView tv_afternoon, tv_morning, tv_total, tv_item, tv_socity;
    private TextView tv_date, tv_time, tv_add_address;
    private EditText et_address;
    private Button btn_checkout;
    private RecyclerView rv_address;
    private TextView tv_train_no, tv_train_pnr;
    private Delivery_get_address_adapter adapter;
    private ArrayList<Delivery_address_model> delivery_address_modelList = new ArrayList<>();
    private Context context;
    private DatabaseHandler db_cart;
    private MyInterface myInterface;

    private Session_management sessionManagement;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private String gettime = "";
    private String getdate = "02/01/2020";

    private String deli_charges;
    private String getlocation_id;
    private String getpin;
    private String gethouse;
    private String getname;
    private String getphone;
    private RadioButton radioOptionButton;
    private String id;

    public Delivery_fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            getlocation_id = getArguments().getString("user_id");
            getpin = getArguments().getString("coach_no");
            gethouse = getArguments().getString("berth_no");
            getname = getArguments().getString("pnr_no");
            getphone = getArguments().getString("train_no");
//            delivery_address_modelList.add(new Delivery_address_model(getlocation_id, getpin, gethouse, getname, getphone));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_time, container, false);
        sessionManagement = new Session_management(getActivity());
        Guestuser = sessionManagement.getUsername(getActivity());
        Guestmobile = sessionManagement.getMobileNo(getActivity());
        TrainNo = sessionManagement.getTrainNo(getActivity());
        PnrNo = sessionManagement.getPnrNo(getActivity());

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.delivery));

        tv_train_pnr = (TextView) view.findViewById(R.id.tv_train_pnr);
        tv_train_no = (TextView) view.findViewById(R.id.tv_train_no);
        tv_guestuser = (TextView) view.findViewById(R.id.tv_guestuser);
        tv_guestmobile = (TextView) view.findViewById(R.id.tv_guestmobile);
        tv_date = (TextView) view.findViewById(R.id.tv_deli_date);
        tv_time = (TextView) view.findViewById(R.id.tv_deli_fromtime);
        tv_add_address = (TextView) view.findViewById(R.id.tv_deli_add_address);
        tv_total = (TextView) view.findViewById(R.id.tv_deli_total);
        tv_item = (TextView) view.findViewById(R.id.tv_deli_item);
        btn_checkout = (Button) view.findViewById(R.id.btn_deli_checkout);
        rv_address = (RecyclerView) view.findViewById(R.id.rv_deli_address);
        rv_address.setLayoutManager(new LinearLayoutManager(getActivity()));
        //tv_socity = (TextView) view.findViewById(R.id.tv_deli_socity);
        //et_address = (EditText) view.findViewById(R.id.et_deli_address);

        tv_guestuser.setText(Guestuser);
        tv_guestmobile.setText(Guestmobile);
        tv_train_no.setText(TrainNo);
        tv_train_pnr.setText(PnrNo);

        db_cart = new DatabaseHandler(getActivity());
        tv_total.setText("" + db_cart.getTotalAmount());
        tv_item.setText("" + db_cart.getCartCount());

        // get session user data

        String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);
        String getaddress = sessionManagement.getUserDetails().get(BaseURL.KEY_HOUSE);

        //tv_socity.setText("Socity Name: " + getsocity);
        //et_address.setText(getaddress);

        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_add_address.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);

        String date = sessionManagement.getdatetime().get(BaseURL.KEY_DATE);
        String time = sessionManagement.getdatetime().get(BaseURL.KEY_TIME);

        if (date != null && time != null) {

            getdate = date;
            gettime = time;

            try {
                String inputPattern = "yyyy-MM-dd";
                String outputPattern = "dd-MM-yyyy";
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                Date date1 = inputFormat.parse(getdate);
                String str = outputFormat.format(date1);

                tv_date.setText(getResources().getString(R.string.delivery_date) + str);

            } catch (ParseException e) {
                e.printStackTrace();

                tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);
            }

            tv_time.setText(time);
        }

        if (ConnectivityReceiver.isConnected()) {
            String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
            makeGetAddressRequest(user_id);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_deli_checkout) {
            attemptOrder();
        } else if (id == R.id.tv_deli_date) {
            getdate();
        } else if (id == R.id.tv_deli_fromtime) {

            if (TextUtils.isEmpty(getdate)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
            } else {
                Bundle args = new Bundle();
                Fragment fm = new View_time_fragment();
                args.putString("date", getdate);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        } else if (id == R.id.tv_deli_add_address) {


           /* // custom dialog
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.address_option_layout);
            dialog.setTitle("Select Delivery Option");*/


            final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Select Delivery Option")
                    .setView(R.layout.address_option_layout)
                    .create();

            dialog.show();

            // set the custom dialog components - text, image and button
            final RadioGroup radioOption = (RadioGroup) dialog.findViewById(R.id.radioOption);
            radioTrain = (RadioButton) dialog.findViewById(R.id.radioTrain);
            RadioButton radioAddress = (RadioButton) dialog.findViewById(R.id.radioAddress);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_option);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //dialog.dismiss();

                    int selectedId = radioOption.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioTrain = v.findViewById(selectedId);

                    if (selectedId == R.id.radioTrain) {
                        dialog.dismiss();
                        Fragment fm = new Add_delivery_address_fragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().
                                replace(R.id.contentPanel, fm).addToBackStack(null).
                                commit();

                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("ListModel", delivery_address_modelList);
                        fm.setArguments(bundle);
                    } else if (selectedId == R.id.radioAddress) {
                        dialog.dismiss();

                        Fragment fm = new CustomDialogClass();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().
                                replace(R.id.contentPanel, fm).
                                addToBackStack(null).commit();

                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("ListModel", delivery_address_modelList);

                       /* Intent intent=new Intent(getActivity(),CustomDialogClass.class);
                        startActivity(intent);*/
                    }
                }
            });

            dialog.show();






          /*  CustomDialogClass cdd = new CustomDialogClass(getActivity());
            cdd.show();*/

            // sessionManagement.updateSocity("", "");


        }

    }

    private void getdate() {
// Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        getdate = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);

                        try {
                            String inputPattern = "yyyy-MM-dd";
                            String outputPattern = "dd-MM-yyyy";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                            Date date = inputFormat.parse(getdate);
                            String str = outputFormat.format(date);

                            tv_date.setText(getResources().getString(R.string.delivery_date) + str);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private void attemptOrder() {

        //String getaddress = et_address.getText().toString();

        String location_id = "";
        String address = "";
        String train = "";
        String addrtype = "";

        boolean cancel = false;

        if (TextUtils.isEmpty(gettime)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date_time), Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        if (!delivery_address_modelList.isEmpty()) {
            if (adapter.ischeckd()) {

                location_id = adapter.getlocation_id();
                address = adapter.getaddress();
                train = adapter.gettrain();
                addrtype = adapter.gettype();
                id = sessionManagement.getOtp();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT).show();
                cancel = true;
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_add_address), Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        /*if (TextUtils.isEmpty(getaddress)) {
            Toast.makeText(getActivity(), "Please add your address", Toast.LENGTH_SHORT).show();
            cancel = true;
        }*/

        if (!cancel) {
            //Toast.makeText(getActivity(), "date:"+getdate+"Fromtime:"+getfrom_time+"Todate:"+getto_time, Toast.LENGTH_SHORT).show();

            sessionManagement.cleardatetime();

            Bundle args = new Bundle();
            Fragment fm = new Delivery_payment_detail_fragment();
            args.putString("getdate", getdate);
            args.putString("time", gettime);
            args.putString("location_id", location_id);
            args.putString("address", address);
            args.putString("trainDetail", train);
            args.putString("addrtype", addrtype);
            args.putString("id", id);
            //args.putString("deli_charges", deli_charges);
            fm.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();

        }
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetAddressRequest(String user_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ADDRESS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    delivery_address_modelList.clear();
                        /*Gson gson = new Gson();
                        Type listType = new TypeToken<List<Delivery_address_model>>() {
                        }.getType();
                        delivery_address_modelList = gson.fromJson(response.getString("data"), listType);*/


                    boolean responce = response.getBoolean("responce");
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String id = obj.getString("id");
                        String user_id = obj.getString("user_id");
                        String train_no = obj.getString("train_no");
                        String berth_no = obj.getString("berth_no");
                        String coach_no = obj.getString("coach_no");
                        String pnr_no = obj.getString("pnr_no");
                        String type = obj.getString("type");
                        String area = obj.getString("area");
                        String adress = obj.getString("adress");
                        delivery_address_modelList.add(new Delivery_address_model(id, user_id, train_no, berth_no, coach_no, pnr_no, area, adress, type));
                    }
                    Collections.reverse(delivery_address_modelList);
                    //RecyclerView.Adapter adapter1 = new Delivery_get_address_adapter(delivery_address_modelList);
                    adapter = new Delivery_get_address_adapter(getActivity(),delivery_address_modelList);
                    ((Delivery_get_address_adapter) adapter).setMode(Attributes.Mode.Single);
                    rv_address.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    if (delivery_address_modelList.isEmpty()) {
                        if (getActivity() != null) {
                            //Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_delivery_charge"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                //updateData();
                deli_charges = intent.getStringExtra("charge");
                //Toast.makeText(getActivity(), deli_charges, Toast.LENGTH_SHORT).show();
                if (deli_charges != null) {
                    Double total = Double.parseDouble(db_cart.getTotalAmount()) + Integer.parseInt(deli_charges);
                    tv_total.setText("" + db_cart.getTotalAmount() + " + " + deli_charges + " = " + total);
                }
            }
        }
    };

}
