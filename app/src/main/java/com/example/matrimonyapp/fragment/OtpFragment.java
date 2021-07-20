package com.example.matrimonyapp.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    EditText et1, et2, et3, et4;
    private EditText[] editTexts;
    TextView txt_saveAndContinue;
    TextView btn;
View view;

    public OtpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtpFragment newInstance(String param1, String param2) {
        OtpFragment fragment = new OtpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_otp, container, false);



        btn = ((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);
        et1 = view.findViewById(R.id.et1);
        et2 = view.findViewById(R.id.et2);
        et3 = view.findViewById(R.id.et3);
        et4 = view.findViewById(R.id.et4);
        txt_saveAndContinue=((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);

        editTexts = new EditText[]{et1, et2, et3, et4};

        et1.addTextChangedListener(new PinTextWatcher(0));
        et2.addTextChangedListener(new PinTextWatcher(1));
        et3.addTextChangedListener(new PinTextWatcher(2));
        et4.addTextChangedListener(new PinTextWatcher(3));


        et1.setOnKeyListener(new PinOnKeyListener(0));
        et2.setOnKeyListener(new PinOnKeyListener(1));
        et3.setOnKeyListener(new PinOnKeyListener(2));
        et4.setOnKeyListener(new PinOnKeyListener(3));

        txt_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new BasicDetailsFragment());

            }
        });

        TextView tv=((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText(getContext().getResources().getString(R.string.verify_otp));
        return  view;
    }

    public void replaceFragment(Fragment destFragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, destFragment);
        fragmentTransaction.commit();
    }


    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            newTypedString = s.subSequence(start, start + count).toString().trim();

        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            if (!s.toString().isEmpty()) {
                checkABC();
            } else {
                //grey
                btn.setBackgroundColor(getResources().getColor(R.color.gray));
                btn.setText("ENTER OTP");
            }

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1) {
                moveToNext();
            } else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            btn.setBackgroundResource(R.drawable.gradient_place_order);
            btn.setText("VERIFY");

            return true;
        }

        private void hideKeyboard() {
            if (getActivity(). getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity(). getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    private void checkABC() {
        if (et1.getText().toString().isEmpty()) {
            //grey
            btn.setBackgroundColor(Color.parseColor("#C7CACF"));
        } else {
            if (et2.getText().toString().isEmpty()) {
                //grey
                btn.setBackgroundColor(Color.parseColor("#C7CACF"));
            } else {
                if (et3.getText().toString().isEmpty()) {
                    //grey
                    btn.setBackgroundColor(Color.parseColor("#C7CACF"));
                } else {
                    if (et4.getText().toString().isEmpty()) {
                        //grey
                        btn.setBackgroundColor(Color.parseColor("#C7CACF"));
                    } else {
                        //green
                        btn.setBackgroundColor(Color.parseColor("#349d13"));
                        et1.setCursorVisible(false);
                    }
                }
            }
        }
    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }
    }
}