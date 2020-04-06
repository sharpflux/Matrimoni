package com.example.matrimonyapp.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.adapter.ChatAdapter;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.adapter.TimelineAdapter;
import com.example.matrimonyapp.customViews.CustomDialogAddSibling;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.google.android.material.textfield.TextInputLayout;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyDetailsFragment extends Fragment {

    View view;
    private TextView textView_saveAndContinue, textView_fatherOccupationId, textView_motherOccupationId,
            textView_siblingOccupationId, textView_mamaOccupationId, textView_noOfSiblings;

    private EditText editText_fatherName, editText_fatherMobileNo, editText_fatherOccupation,
            editText_fatherAnnualIncome, editText_fatherProperty, editText_fatherAddress,
            editText_motherName, editText_motherMobileNo, editText_motherOccupation,
            editText_motherAnnualIncome, editText_siblingsName, editText_siblingEducation,
            editText_siblingOccupation, editText_relative1, editText_relative2, editText_relative3,
            editText_relative4, editText_mamaName, editText_mamaMobileNo, editText_mamaOccupation;


    ImageView imageView_back, imageView_add, imageView_subtract, imageView_addSibling;

    private String fatherName, fatherMobileNo, fatherOccupation, fatherAnnualIncome, fatherProperty,
            fatherAddress, motherName, motherMobileNo, motherOccupation, motherAnnualIncome,
            siblingsName, siblingEducation, siblingOccupation, relative1, relative2, relative3,
            relative4, mamaName, mamaMobileNo, mamaOccupation;


    private TextInputLayout textField_mamaName, textField_mamaMobileNo, textField_mamaOccupation;

    SwitchButton switchButton_haveFather, switchButton_haveMother, switchButton_haveMama;

    Bundle bundle;

    DataFetcher dataFetcher;

    UserModel userModel;

    ArrayList<AddPersonModel> addPersonModelArrayList_sibling;
    RecyclerView recyclerView_addSibling;
    AddPersonAdapter addPersonAdapter_sibling;

    SQLiteSiblingDetails sqLiteSiblingDetails;

    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    CustomDialogAddSibling customDialogAddSibling;

    public FamilyDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_family_details, container, false);

        bundle = getArguments();


        if (!CustomSharedPreference.getInstance(getContext()).isLoggedIn()) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        editText_fatherName = view.findViewById(R.id.editText_fatherName);
        editText_fatherMobileNo = view.findViewById(R.id.editText_fatherMobileNo);
        editText_fatherOccupation = view.findViewById(R.id.editText_fatherOccupation);
        editText_fatherAnnualIncome = view.findViewById(R.id.editText_fatherAnnualIncome);
        editText_fatherProperty = view.findViewById(R.id.editText_fatherProperty);
        editText_fatherAddress = view.findViewById(R.id.editText_fatherAddress);
        editText_motherName= view.findViewById(R.id.editText_motherName);
        editText_motherMobileNo = view.findViewById(R.id.editText_motherMobileNo);
        editText_motherOccupation = view.findViewById(R.id.editText_motherOccupation);
        editText_motherAnnualIncome = view.findViewById(R.id.editText_motherAnnualIncome);
        editText_siblingsName = view.findViewById(R.id.editText_siblingsName);
        editText_siblingEducation = view.findViewById(R.id.editText_siblingEducation);
        editText_siblingOccupation = view.findViewById(R.id.editText_siblingOccupation);
        editText_relative1 = view.findViewById(R.id.editText_relative1);
        editText_relative2 = view.findViewById(R.id.editText_relative2);
        editText_relative3 = view.findViewById(R.id.editText_relative3);
        editText_relative4 = view.findViewById(R.id.editText_relative4);
        editText_mamaName= view.findViewById(R.id.editText_mamaName);
        editText_mamaMobileNo = view.findViewById(R.id.editText_mamaMobileNo);
        editText_mamaOccupation = view.findViewById(R.id.editText_mamaOccupation);
        textView_noOfSiblings = view.findViewById(R.id.textView_noOfSiblings);
        //imageView_add = view.findViewById(R.id.imageView_add);
        imageView_addSibling = view.findViewById(R.id.imageView_addSibling);

        recyclerView_addSibling = view.findViewById(R.id.recyclerView_addSibling);



        textView_fatherOccupationId = view.findViewById(R.id.textView_fatherOccupationId);
        textView_motherOccupationId = view.findViewById(R.id.textView_motherOccupationId);
        textView_siblingOccupationId = view.findViewById(R.id.textView_siblingId);
        textView_mamaOccupationId = view.findViewById(R.id.textView_mamaOccupationId);

        textField_mamaName = view.findViewById(R.id.textField_mamaName);
        textField_mamaMobileNo = view.findViewById(R.id.textField_mamaMobileNo );
        textField_mamaOccupation = view.findViewById(R.id.textField_mamaOccupation);

        switchButton_haveMama = view.findViewById(R.id.switchButton_haveMama);

        textView_saveAndContinue=((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);
        imageView_back =((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv =((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText("Family Details");


        dataFetcher = new DataFetcher("State",getContext());





        addPersonModelArrayList_sibling = new ArrayList<>();

        sqLiteSiblingDetails = new SQLiteSiblingDetails(getContext());

        Cursor cursor_sibling = sqLiteSiblingDetails.getAllData();

        if(cursor_sibling!=null) {
            for (boolean hasItem = cursor_sibling.moveToFirst(); hasItem; hasItem = cursor_sibling.moveToNext()) {
                AddPersonModel addPersonModel = new AddPersonModel(
                        cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.ID)),
                        cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.NAME)),
                        cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.MOBILE_NO))
                );

                addPersonModelArrayList_sibling.add(addPersonModel);

            }

            addPersonAdapter_sibling = new AddPersonAdapter(getContext(), addPersonModelArrayList_sibling);
            recyclerView_addSibling.setAdapter(addPersonAdapter_sibling);
            recyclerView_addSibling.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView_addSibling.setLayoutManager(mLayoutManager);

        }


        textView_noOfSiblings.setText(String.valueOf(sqLiteSiblingDetails.numberOfRows()));

        onClickListener();



        return view;
    }



    public void onClickListener()
    {

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager.getBackStackEntryCount()>0)
                {
                    fragmentManager.popBackStack();
                }
            }
        });


        imageView_addSibling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customDialogAddSibling = new CustomDialogAddSibling(getContext(), "0",
                        addPersonAdapter_sibling, addPersonModelArrayList_sibling, 0);
                customDialogAddSibling.show();

            }
        });

        switchButton_haveMama.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener()
                                                         {
                                                             @Override
                                                             public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                                                                 if(isChecked)
                                                                 {

                                                                     textField_mamaName.setVisibility(View.VISIBLE);
                                                                     textField_mamaMobileNo.setVisibility(View.VISIBLE);
                                                                     textField_mamaOccupation.setVisibility(View.VISIBLE);
                                                                 }
                                                                 else
                                                                 {
                                                                     textField_mamaName.setVisibility(View.GONE);
                                                                     textField_mamaMobileNo.setVisibility(View.GONE);
                                                                     textField_mamaOccupation.setVisibility(View.GONE);

                                                                 }
                                                             }
                                                         }
        );



        editText_fatherOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                String control = "Father";
                runner.execute(control);

            }
        });
        editText_motherOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                String control = "Mother";
                runner.execute(control);

            }
        });
        editText_siblingOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                String control = "Mother";
                runner.execute(control);

            }
        });
        editText_mamaOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                String control = "Mother";
                runner.execute(control);

            }
        });



        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fatherName = editText_fatherName.getText().toString();
                fatherMobileNo= editText_fatherMobileNo.getText().toString();
                fatherOccupation= editText_fatherOccupation.getText().toString();
                fatherAnnualIncome= editText_fatherAnnualIncome.getText().toString();
                fatherProperty= editText_fatherProperty.getText().toString();
                fatherAddress= editText_fatherAddress.getText().toString();
                motherName= editText_motherName.getText().toString();
                motherMobileNo= editText_motherMobileNo.getText().toString();
                motherOccupation= editText_motherOccupation.getText().toString();
                motherAnnualIncome= editText_motherAnnualIncome.getText().toString();
                siblingsName= editText_siblingsName.getText().toString();
                siblingEducation= editText_siblingEducation.getText().toString();
                siblingOccupation= editText_siblingOccupation.getText().toString();
                relative1= editText_relative1.getText().toString();
                relative2= editText_relative2.getText().toString();
                relative3= editText_relative3.getText().toString();
                relative4= editText_relative4.getText().toString();
                mamaName= editText_mamaName.getText().toString();
                mamaMobileNo= editText_mamaMobileNo.getText().toString();
                mamaOccupation= editText_mamaOccupation.getText().toString();

                /*= view.findViewById(R.id.);
                = view.findViewById(R.id.);*/

                if(bundle!=null)
                {
                    bundle.putString("fatherName",fatherName);
                    bundle.putString("fatherMobileNo",fatherMobileNo);
                    bundle.putString("fatherOccupation",fatherOccupation);
                    bundle.putString("fatherAnnualIncome",fatherAnnualIncome);
                    bundle.putString("fatherProperty",fatherProperty);
                    bundle.putString("fatherAddress",fatherAddress);
                    bundle.putString("motherName",motherName);
                    bundle.putString("motherMobileNo",motherMobileNo);
                    bundle.putString("motherOccupation",motherOccupation);
                    bundle.putString("motherAnnualIncome",motherAnnualIncome);
                    bundle.putString("siblingsName",siblingsName);
                    bundle.putString("siblingEducation",siblingEducation);
                    bundle.putString("siblingOccupation",siblingOccupation);
                    bundle.putString("relative1",relative1);
                    bundle.putString("relative2",relative2);
                    bundle.putString("relative3",relative3);
                    bundle.putString("relative4",relative4);
                    bundle.putString("mamaName",mamaName);
                    bundle.putString("mamaMobileNo",mamaMobileNo);
                    bundle.putString("mamaOccupation",mamaOccupation);
                }


/*                bundle.putString("",);
                bundle.putString("",);*/


                QualificationDetailsFragment qualificationDetailsFragment = new QualificationDetailsFragment();
                qualificationDetailsFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, qualificationDetailsFragment);
                fragmentTransaction.commit() ;
            }
        });




    }


    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;



        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {


                if(params[0].toString()=="Father")
                {
                    dataFetcher.loadList(URLs.URL_GET_OCCUPATION+"Language="+userModel.getLanguage(),
                            "OccupationId", "OccupationName", editText_fatherOccupation,
                            textView_fatherOccupationId,getContext(), customDialogLoadingProgressBar);


                }
                if(params[0].toString()=="Mother")
                {
                    dataFetcher.loadList(URLs.URL_GET_OCCUPATION+"Language="+userModel.getLanguage(),
                            "OccupationId", "OccupationName", editText_motherOccupation,
                            textView_motherOccupationId,getContext(), customDialogLoadingProgressBar);


                }
                if(params[0].toString()=="Sibling")
                {
                    dataFetcher.loadList(URLs.URL_GET_RELIGION+"Language="+userModel.getLanguage(),
                            "OccupationId", "OccupationName", editText_fatherOccupation,
                            textView_siblingOccupationId,getContext(), customDialogLoadingProgressBar);


                }
                if(params[0].toString()=="Mama")
                {
                    dataFetcher.loadList(URLs.URL_GET_RELIGION+"Language="+userModel.getLanguage(),
                            "OccupationId", "OccupationName", editText_fatherOccupation,
                            textView_mamaOccupationId,getContext(), customDialogLoadingProgressBar);


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

            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
            customDialogLoadingProgressBar.show();

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

    }



}
