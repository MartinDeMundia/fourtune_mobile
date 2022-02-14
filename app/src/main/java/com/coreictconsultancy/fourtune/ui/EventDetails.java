package com.coreictconsultancy.fourtune.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.Env;
import com.androidstudy.daraja.util.TransactionType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.coreictconsultancy.fourtune.MainActivity;
import com.coreictconsultancy.fourtune.R;
import com.coreictconsultancy.fourtune.data.DataCache;
import com.coreictconsultancy.fourtune.data.LoaderToUIListener;
import com.coreictconsultancy.fourtune.data.URLProvider;
import com.coreictconsultancy.fourtune.pojo.Res_Event_Details;
import com.coreictconsultancy.fourtune.pojo.Res_Event_Images;
import com.google.android.material.snackbar.Snackbar;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EventDetails extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String eventId;
    private String mParam2;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ArrayList<Res_Event_Details> EventsDetailsDAO;
    private ArrayList<Res_Event_Images> EventsImagesDAO;;
    private ViewPager viewPager;
    protected DataCache mDataCache;
    ImageView imgbuytickets;
    String userId,savedPhoneNo,gamerName = null;
    public static SharedPreferences sharedpreferences;
    TextView txtdate,title,txtlocation,txtprice,details;
    Dialog pick_Dialog;
    RelativeLayout relativeSend;
    EditText txtTotalPrice,txtNoTickets;
    String totalPrice = "0";
    Integer tikectnumber,totalamnt;
    private SaveTask mSaveTask = null;
    Integer event_id = 0;
    CoordinatorLayout mainLayout;
    ImageButton btnbuynow;
    String strTotalPrice , strTotalNumber , strPaytype = "";
    static Daraja daraja;
    //View mListContainer;
    View mEmptyContainer;
    View mProgressContainer;

    public EventDetails(){
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static EventDetails newInstance(String param1, String param2) {
        EventDetails fragment = new EventDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daraja = Daraja.with(getString(R.string.consumerKey), getString(R.string.consumerSecret), Env.PRODUCTION, new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
            }
            @Override
            public void onError(String error){
            }
        });
        if (getArguments() != null) {
        }
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            Integer position = arg0;
            //lblCount.setText((position + 1) + "-" + images.size());
            //Image image = images.get(position);
            //lblTitle.setText(image.getName());
            //lblDate.setText(image.getTimestamp());
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private void displayMetaInfo(int position) {
        if(position >0){
        }
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(0);
    }


    protected LoaderToUIListener loaderToUIListener = new LoaderToUIListener(){
        public void loadedData(String param1String) {
            myViewPagerAdapter = new MyViewPagerAdapter();
            EventsDetailsDAO.clear();
            JSONObject jObject = new JSONObject();
            try {
                if(param1String != null) {
                    jObject = new JSONObject(param1String);
                    JSONObject jSONObject = jObject.getJSONObject("data");
                    JSONObject jSONObject_CoinsEarned = jSONObject.getJSONObject("data_object");
                    JSONArray j = jSONObject_CoinsEarned.getJSONArray("dbrows");
                    if (j != null) {
                        for (int i = 0; i < j.length(); i++) {
                            JSONObject Obj;
                            Obj = j.getJSONObject(i);
                            Res_Event_Details temp = new Res_Event_Details();
                            temp.setId(Obj.getString("id"));
                            temp.setEventname(Obj.getString("event_name"));
                            temp.setEventfeatured_image(Obj.getString("event_featured_image"));
                            temp.setEventdate(Obj.getString("event_date"));
                            temp.setEventdescription(Obj.getString("event_description"));
                            temp.setEventlocation(Obj.getString("event_location"));
                            temp.setPrice(Obj.getString("price"));
                            EventsDetailsDAO.add(temp);
                            EventsImagesDAO.clear();
                            JSONArray jImgs = new JSONArray(Obj.getString("event_images"));
                            if (jImgs != null) {
                                for (int k = 0; k < jImgs.length(); k++) {
                                    JSONObject ObjImages;
                                    ObjImages = jImgs.getJSONObject(k);
                                    Res_Event_Images tempImg = new Res_Event_Images();
                                    tempImg.setId(ObjImages.getString("id"));
                                    tempImg.setImagepath(ObjImages.getString("image_path"));
                                    EventsImagesDAO.add(tempImg);
                                }
                            }
                        }
                        myViewPagerAdapter.notifyDataSetChanged();
                        viewPager.setAdapter(myViewPagerAdapter);
                        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
                        userInterface(EventsDetailsDAO);
                        setListShown(true,false);
                    }
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void userInterface(ArrayList<Res_Event_Details> eventsDetailsDAO) {
        txtdate.setText(eventsDetailsDAO.get(0).getEventdate());
        title.setText(eventsDetailsDAO.get(0).getEventname());
        txtlocation.setText(eventsDetailsDAO.get(0).getEventlocation());
        txtprice.setText("Charges "+eventsDetailsDAO.get(0).getPrice() +" /=");
        details.setText(eventsDetailsDAO.get(0).getEventdescription());
        totalPrice = eventsDetailsDAO.get(0).getPrice();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event_details, container, false);
        Bundle fragBundle = getArguments();
        event_id = fragBundle.getInt("event_id");
        viewPager = root.findViewById(R.id.viewpager);
        txtdate = root.findViewById(R.id.txtdate);
        title = root.findViewById(R.id.title);
        txtlocation = root.findViewById(R.id.txtlocation);
        txtprice = root.findViewById(R.id.txtprice);
        details = root.findViewById(R.id.details);
        imgbuytickets = root.findViewById(R.id.imgbuytickets);
        imgbuytickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                 buyTicket();
            }
        });
        mainLayout =  root.findViewById(R.id.mainLayout);

        //this.mListContainer = root.findViewById(R.id.mainLayout);
        this.mProgressContainer = root.findViewById(R.id.progressContainer);
        this.mEmptyContainer = root.findViewById(R.id.emptyContainer);

        EventsDetailsDAO = new ArrayList<Res_Event_Details>();
        EventsImagesDAO = new ArrayList<Res_Event_Images>();
        this.mDataCache = new DataCache(getContext());
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("event_id",""+event_id));
            setListShown(false,true);
            this.mDataCache.loadData(URLProvider.LiveURL + "/api/v1/fetch-event-details", URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)), this.loaderToUIListener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return root;
    }
    public  String GetPref(Context con, String Key) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        String Value = "";
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, "");
        }
        return Value;
    }

    private void buyTicket() {
        userId =  GetPref(getContext(), "Pref_User_ID");
        savedPhoneNo = GetPref(getContext(), "Pref_Phone");
        gamerName = GetPref(getContext(), "Pref_Username");
        if(savedPhoneNo.equals("")){
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
            Intent intent = new Intent(getContext(), Signin.class);
            startActivity(intent);
        }else {

            if (pick_Dialog == null) {
                pick_Dialog = new Dialog(this.getContext());
                pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                pick_Dialog.setCancelable(true);
                pick_Dialog.setCanceledOnTouchOutside(true);
                pick_Dialog.setContentView(R.layout.pop_purchase_tickets);
                pick_Dialog.show();
                //relativeSend = pick_Dialog.findViewById(R.id.relativeSend);
                txtTotalPrice = pick_Dialog.findViewById(R.id.txtTotalPrice);
                txtNoTickets = pick_Dialog.findViewById(R.id.txtNoTickets);
                txtTotalPrice.setText(totalPrice);

                txtNoTickets.addTextChangedListener(new TextWatcher() {
                    public void onTextChanged(CharSequence s, int start, int before,int count) {
                        if(!s.equals("") ) {
                            //do your work here
                            if (txtNoTickets.getText().toString().trim().equals("")) {
                                tikectnumber = 0;
                            }else{
                                tikectnumber = Integer.parseInt(txtNoTickets.getText().toString().trim());
                            }
                            totalamnt = (int) (Float.parseFloat(totalPrice) * tikectnumber);
                        }
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                    }
                    public void afterTextChanged(Editable s){
                        txtTotalPrice.setText(""+totalamnt);
                    }
                });

                btnbuynow = pick_Dialog.findViewById(R.id.btnbuynow);
                btnbuynow.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        strTotalPrice = txtTotalPrice.getText().toString().trim();
                        strTotalNumber = txtNoTickets.getText().toString().trim();
                        if (strTotalNumber.equals("")) {
                            Toast.makeText(getActivity(), "Please enter the valid no. of Ticket(s)", Toast.LENGTH_LONG).show();
                        } else {
                            mSaveTask = new SaveTask(""+event_id,strTotalPrice,strTotalNumber,userId,strPaytype,savedPhoneNo);
                            mSaveTask.execute((String) null);
                        }
                    }
                });

               /* btnmpesa = pick_Dialog.findViewById(R.id.btnmpesa);
                btnmpesa.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                         strTotalPrice = txtTotalPrice.getText().toString().trim();
                         strTotalNumber = txtNoTickets.getText().toString().trim();
                        if (strTotalNumber.equals("")) {
                            Toast.makeText(getActivity(), "Please enter the valid no. of Ticket(s)", Toast.LENGTH_LONG).show();
                        } else {
                            strPaytype = "mpesa";
                            mSaveTask = new SaveTask(""+event_id,strTotalPrice,strTotalNumber,userId,strPaytype,savedPhoneNo);
                            mSaveTask.execute((String) null);
                        }
                    }
                });

                btnvisa = pick_Dialog.findViewById(R.id.btnvisa);
                btnvisa.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                         strTotalPrice = txtTotalPrice.getText().toString().trim();
                         strTotalNumber = txtNoTickets.getText().toString().trim();
                        if (strTotalNumber.equals("")) {
                            Toast.makeText(getActivity(), "Please enter the valid no. of Ticket(s)", Toast.LENGTH_LONG).show();
                        } else {
                            strPaytype = "visa";
                            mSaveTask = new SaveTask(""+event_id,strTotalPrice,strTotalNumber,userId,strPaytype,savedPhoneNo);
                            mSaveTask.execute((String) null);
                        }
                    }
                });*/

             } else if (pick_Dialog.isShowing()) {
                pick_Dialog.dismiss();
            } else {
                pick_Dialog = null;
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        public MyViewPagerAdapter() {
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);
            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);
            Res_Event_Images imagesobject = EventsImagesDAO.get(position);
            Glide.with(getActivity()).load(imagesobject.getImagepath())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);
            imageViewPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return EventsDetailsDAO.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }




    public class SaveTask extends AsyncTask<String, Void, JSONObject> {
        private final String mEventId;
        private final String mTotalPrice;
        private final String mTotalTicketNumber;
        private final String mUserId;
        private final String mPaytype;
        private final String mPhone;
        SaveTask(String EventId,String TotalPrice, String TotalTicketNumbe,String UserId,String Paytype,String Phone) {
            this.mEventId = EventId;
            this.mTotalPrice = TotalPrice;
            this.mTotalTicketNumber = TotalTicketNumbe;
            this.mUserId = UserId;
            this.mPaytype = Paytype;
            this.mPhone = Phone;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            InputStream is = null;
            LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
                public void loadedData(String param1String) {
                    if(param1String != null) {
                        JSONObject jObjectDetails;
                        try {
                            jObjectDetails = new JSONObject(param1String);
                            if(jObjectDetails == null) {
                                Snackbar snackbar = Snackbar
                                        .make(mainLayout,"Please check your internet connection!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }else {
                                String success = jObjectDetails.getString("success");
                                if (success.equals("true")){
                                    pick_Dialog.dismiss();
                                    new AlertDialog.Builder(getContext()).setTitle("Event Booking")
                                            .setMessage(jObjectDetails.getString("message")).setIcon(R.drawable.indicator_error).
                                            setNeutralButton("Close", null).show();
                                    Snackbar snackbar = Snackbar
                                            .make(mainLayout,jObjectDetails.getString("message"), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }else{
                                    new AlertDialog.Builder(getContext()).setTitle("Event Booking")
                                            .setMessage(jObjectDetails.getString("message")).setIcon(R.drawable.indicator_error).
                                            setNeutralButton("Close", null).show();
                                    Snackbar snackbar = Snackbar
                                            .make(mainLayout,jObjectDetails.getString("message"), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
            };

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("event_id", this.mEventId));
            nameValuePairs.add(new BasicNameValuePair("total_price", this.mTotalPrice));
            nameValuePairs.add(new BasicNameValuePair("total_ticket_number", this.mTotalTicketNumber));
            nameValuePairs.add(new BasicNameValuePair("user_id", this.mUserId));
            nameValuePairs.add(new BasicNameValuePair("pay_type", this.mPaytype));
            mDataCache = new DataCache(getContext());
            try {
                mDataCache.loadData(URLProvider.LiveURL+"/api/v1/buy-ticket",URLProvider.purchaseToken(new UrlEncodedFormEntity(nameValuePairs)),loaderToUIListener);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return  null;
        }
        public SharedPreferences sharedpreferences;
        public void SetPref(Context con, String Key, String Value) {
            sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedpreferences.edit();
            edit.putString(Key, Value);
            edit.commit();
        }

        public  String GetPref(Context con, String Key) {
            sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
            String Value = "";
            if (sharedpreferences.contains(Key)) {
                Value = sharedpreferences.getString(Key, "");
            }
            return Value;
        }

        @Override
        protected void onPostExecute(final JSONObject jsonObjects) {
        }
        @Override
        protected void onCancelled() {
            //showProgress(false);
        }
    }


    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    new AlertDialog.Builder(getContext()).setTitle("Event Booking")
                            .setMessage("Successfully booked!").setIcon(R.drawable.indicator_ok).
                            setNeutralButton("Close", null).show();
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    public String cardPayments(String amount,String userid){
        PayPalConfiguration config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                .clientId(getString(R.string.payPalClientId));
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);

        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "USD", "Fourtune Token ("+userid+")", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intentPay = new Intent(getActivity(), com.paypal.android.sdk.payments.PaymentActivity.class);
        // send the same configuration for restart resiliency
        intentPay.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intentPay.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        intentPay.putExtra(PayPalPayment.PAYMENT_INTENT_SALE,userid);
        intentPay.putExtra(PayPalPayment.PAYMENT_INTENT_SALE,amount);
        startActivityForResult(intentPay, 0);
        return "";
    }



    private void paymentFinalize(String type, String userId, String totalPrice,String PhoneNumber){
        switch (type) {
            case "mpesa":
                mpesaPayments(totalPrice,userId,PhoneNumber);
            break;
            case "visa":
                cardPayments(totalPrice,userId);
              break;
        }

    }

    private void mpesaPayments(String totalPrice, String userId, String phoneNumber) {
        LNMExpress lnmExpress = new LNMExpress(
                getString(R.string.businessShortcode),
                getString(R.string.passKey),
                TransactionType.CustomerBuyGoodsOnline,
                totalPrice,
                getString(R.string.partyA),
                getString(R.string.businessTillnumber),
                phoneNumber,
                getString(R.string.callBackURL),
                "Fourtune Purchase for Client - "+userId,
                "Fourtune Purchase"
        );
        daraja.requestMPESAExpress(lnmExpress,new DarajaListener<LNMResult>() {
            @Override
            public void onResult(@NonNull LNMResult lnmResult) {
                if (lnmResult.ResponseCode.equals("0")) {

                } else {
                    Snackbar snackbar = Snackbar
                            .make(mainLayout, lnmResult.ResponseDescription, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
            @Override
            public void onError(String error){
                new AlertDialog.Builder(getContext()).setTitle("Fourtune Purchases")
                        .setMessage("Am error occured ("+error+"),pls try again").setIcon(R.drawable.indicator_error).
                        setNeutralButton("Close", null).show();
                Snackbar snackbar = Snackbar
                        .make(mainLayout, "An error occured,please try again!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }


    @SuppressLint("WrongConstant")
    public void setListShown(boolean paramBoolean1, boolean paramBoolean2) {
        if (paramBoolean1) {
            if (paramBoolean2) {
                this.mEmptyContainer.setVisibility(0);
                //this.mListContainer.setVisibility(8);
            } else {
                //this.mListContainer.setVisibility(0);
                this.mEmptyContainer.setVisibility(8);
            }
            this.mProgressContainer.setVisibility(8);
            return;
        }
        this.mEmptyContainer.setVisibility(8);
        this.mProgressContainer.setVisibility(0);
       // this.mListContainer.setVisibility(8);
    }


}