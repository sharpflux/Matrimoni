package com.example.matrimonyapp.customViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.activity.SetPreferencesActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.adapter.PopupFetcher;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.MultipleSelectionDataFetcher;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteFarmDetails;
import com.example.matrimonyapp.sqlite.SQLiteMamaDetails;
import com.example.matrimonyapp.sqlite.SQLitePropertyDetails;
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;

import java.util.ArrayList;
import java.util.Map;

//import jp.wasabeef.blurry.internal.Blur;
//import jp.wasabeef.blurry.internal.BlurFactor;
import me.abhinay.input.CurrencyEditText;
import me.abhinay.input.CurrencySymbols;

public class CustomDialogAddFarm extends Dialog {



    public Context context;
    private EditText editText_crops;
    private TextView textView_title, textView_addFarm;

    private CurrencyEditText editText_totalLand, editText_irrigatedArea, editText_partiallyIrrigatedArea, editText_nonIrrigatedArea;

    private ImageView imageView_back;

    //private RadioGroup radioGroup_type, radioGroup_irrigationType;

    private Map<String, Integer> list;

    private SQLiteFarmDetails sqLiteFarmDetails;
    private DataFetcher dataFetcher;

    public static String id="0", farm_details_id;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    private UserModel userModel;

    private AddPersonAdapter addPersonAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;
    private ArrayList<String> arrayList_crops;
    private MultipleSelectionDataFetcher multipleSelectionDataFetcher;

    public CustomDialogAddFarm(Context context, String id, String farm_details_id,  AddPersonAdapter addPersonAdapter,
                                   ArrayList<AddPersonModel> addPersonModelArrayList, int position)
    {
        super(context);
        this.context = context;
        this.id = id;
        this.farm_details_id = farm_details_id;
        this.addPersonAdapter = addPersonAdapter;
        this.addPersonModelArrayList = addPersonModelArrayList;
        this.position = position;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);




        setContentView(R.layout.custom_dialog_add_farm);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        sqLiteFarmDetails = new SQLiteFarmDetails(context);
        multipleSelectionDataFetcher = new MultipleSelectionDataFetcher("", context);
        arrayList_crops = new ArrayList<>();

        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        imageView_back = findViewById(R.id.imageView_back);
        textView_title = findViewById(R.id.textView_title);
        editText_totalLand = findViewById(R.id.editText_totalLand);
        editText_irrigatedArea = findViewById(R.id.editText_irrigatedArea);
        editText_partiallyIrrigatedArea = findViewById(R.id.editText_partiallyIrrigatedArea);
        editText_nonIrrigatedArea = findViewById(R.id.editText_nonIrrigatedArea);
        /*radioGroup_type = findViewById(R.id.radioGroup_type);
        radioGroup_irrigationType = findViewById(R.id.radioGroup_irrigationType);*/
        editText_crops = findViewById(R.id.editText_crops);
        textView_addFarm = findViewById(R.id.textView_addFarm);

        dataFetcher = new DataFetcher("Address",context);


        textView_title.setText("Farm Details");
        imageView_back.setVisibility(View.GONE);

        editText_totalLand.setCurrency(CurrencySymbols.NONE);
        editText_irrigatedArea.setCurrency(CurrencySymbols.NONE);
        editText_partiallyIrrigatedArea.setCurrency(CurrencySymbols.NONE);
        editText_nonIrrigatedArea.setCurrency(CurrencySymbols.NONE);
        editText_totalLand.setDecimals(false);
        editText_irrigatedArea.setDecimals(false);
        editText_partiallyIrrigatedArea.setDecimals(false);
        editText_nonIrrigatedArea.setDecimals(false);

        textChangeListener();


        int irrigatedArea = 0;
        String irrigated = editText_irrigatedArea.getText().toString().replace(",","");
        if(!irrigated.equals(""))
        {
            irrigatedArea = Integer.parseInt(irrigated);
        }

        int partiallyIrrigatedArea = 0;
        String partiallyIrrigated = editText_partiallyIrrigatedArea.getText().toString().replace(",","");
        if(!partiallyIrrigated.equals(""))
        {
            partiallyIrrigatedArea = Integer.parseInt(partiallyIrrigated);
        }

        int nonIrrigatedArea = 0;
        String nonIrrigated = editText_nonIrrigatedArea.getText().toString().replace(",","");
        if(!nonIrrigated.equals(""))
        {
            nonIrrigatedArea = Integer.parseInt(nonIrrigated);
        }

        editText_totalLand.setText(String.valueOf(irrigatedArea+partiallyIrrigatedArea+nonIrrigatedArea));

        if(!id.equals("0"))
        {

            Cursor cursor = sqLiteFarmDetails.getDataById(Integer.parseInt(id));

            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {

                editText_totalLand.setText(cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.TOTAL_AREA)));
                editText_irrigatedArea.setText(cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.IRRIGATED_AREA)));
                editText_partiallyIrrigatedArea.setText(cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.PARTIALLY_IRRIGATED_AREA)));
                editText_nonIrrigatedArea.setText(cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.NON_IRRIGATED_AREA)));
                editText_crops.setText(cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.CROPS_NAME)));
                /*FieldValidation.setRadioButtonAccToValue(radioGroup_type,
                        cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.TYPE)));
                FieldValidation.setRadioButtonAccToValue(radioGroup_irrigationType,
                        cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.IRRIGATION_TYPE)));*/
                //Toast.makeText(context, cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.TYPE))+"---",Toast.LENGTH_SHORT).show();


            }


            cursor.close();
        }

        textView_addFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String totalLand = editText_totalLand.getText().toString().trim();
                String irrigatedArea = editText_irrigatedArea.getText().toString().trim();
                String partiallyIrrigatedArea = editText_partiallyIrrigatedArea.getText().toString().trim();
                String nonIrrigatedArea = editText_nonIrrigatedArea.getText().toString().trim();
                String cropsName = editText_crops.getText().toString().trim();
                /*String type = ((RadioButton)findViewById(radioGroup_type.getCheckedRadioButtonId())).getText().toString();
                String irrigationType = ((RadioButton)findViewById(radioGroup_irrigationType.getCheckedRadioButtonId())).getText().toString();*/
                String cropsId = arrayList_crops.toString().substring(1,arrayList_crops.toString().length()-1).replaceAll(" ","");


                if(id.equals("0")) {
                    long res = sqLiteFarmDetails.insertFarmDetails("0", totalLand,
                            irrigatedArea, partiallyIrrigatedArea, nonIrrigatedArea, cropsId, cropsName);

                    if (res != -1) {

                        id = String.valueOf(res);
                        Toast.makeText(context, "Value added & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.add(new AddPersonModel(String.valueOf(res),"0",
                                totalLand+" "+context.getResources().getString(R.string.acre), cropsName));
                        addPersonAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Error in sqlite insertion", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    int res = sqLiteFarmDetails.updateFarmDetails(id, farm_details_id, totalLand,
                            irrigatedArea, partiallyIrrigatedArea, nonIrrigatedArea, cropsId, cropsName);
                    if (res != -1) {
                        Toast.makeText(context, "Value Updated & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.set(position, new AddPersonModel(String.valueOf(id), farm_details_id,
                                totalLand+" "+context.getResources().getString(R.string.acre), cropsName));
                        addPersonAdapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(context, "Error in sqlite updation", Toast.LENGTH_SHORT).show();

                    }


                }
                dismiss();

            }
        });


        onClickListener();


    }

    private void textChangeListener() {


        calculateTotalLand(editText_irrigatedArea);
        calculateTotalLand(editText_partiallyIrrigatedArea);
        calculateTotalLand(editText_nonIrrigatedArea);


    }

    private void calculateTotalLand(CurrencyEditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int irrigatedArea = 0;
                String irrigated = editText_irrigatedArea.getText().toString().replace(",","");
                if(!irrigated.equals(""))
                {
                    irrigatedArea = Integer.parseInt(irrigated);
                }

                int partiallyIrrigatedArea = 0;
                String partiallyIrrigated = editText_partiallyIrrigatedArea.getText().toString().replace(",","");
                if(!partiallyIrrigated.equals(""))
                {
                    partiallyIrrigatedArea = Integer.parseInt(partiallyIrrigated);
                }

                int nonIrrigatedArea = 0;
                String nonIrrigated = editText_nonIrrigatedArea.getText().toString().replace(",","");
                if(!nonIrrigated.equals(""))
                {
                    nonIrrigatedArea = Integer.parseInt(nonIrrigated);
                }

                editText_totalLand.setText(String.valueOf(irrigatedArea+partiallyIrrigatedArea+nonIrrigatedArea));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void blurBackground()
    {
/*
        //take Screenshot
        Bitmap bitmap = Screenshot.getInstance().takeScreenshotForScreen((Activity)context);

        //set blurring factor and heighth width of screenshot
        BlurFactor blurFactor = new BlurFactor();
        blurFactor.height = bitmap.getHeight();
        blurFactor.width = bitmap.getWidth();
        blurFactor.color = context.getResources().getColor(R.color.transparent_bg);

        //blurred image
        Bitmap blurBitmap = Blur.of(context, bitmap, blurFactor);
        //convert blurred image into drawable
        Drawable drawable = new BitmapDrawable(context.getResources(), blurBitmap);

        //set blurred screenshot to background
        getWindow().setBackgroundDrawable(drawable);*/

    }


    private void onClickListener()
    {

        editText_crops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("Crops");
            }
        });


    }




    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {


                if (params[0].equals("Crops"))
                {


                        multipleSelectionDataFetcher.loadList(URLs.URL_GET_COUNTRY+"Language="+userModel.getLanguage(),"Id",
                                "Name", editText_crops, arrayList_crops, context,
                                customDialogLoadingProgressBar);


                }


            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {

        }


        @Override
        protected void onPreExecute() {

            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(context);
            customDialogLoadingProgressBar.show();

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

    }


}
