package nta.nguyenanh.code_application.fragment.otp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import nta.nguyenanh.code_application.R;


public class NumberPhoneFragment extends Fragment {

    private static final String ARG_NUMBERPHONE1 = "numberphone1";
    private EditText et_numberPhone;
    private Button btn_numberPhone;
    private Button btn_confrim_NumberPhone;
    private FirebaseAuth mAuth;
    private String codeSend="";
    private boolean visible = false;
    private boolean countdown_canel = false;
    private LinearLayout linear_confirmOTP;
    private ViewGroup tContainer;
    private LinearLayout linear_numberphone;
    OnBackPressedCallback onBackPressedCallback;
    private EditText et_confirmCode1;
    private EditText et_confirmCode2;
    private EditText et_confirmCode3;
    private EditText et_confirmCode4;
    private String phonenumber;
    private EditText et_confirmCode5;
    private EditText et_confirmCode6;
    private TextView tv_second_otp;
    private Handler handler = new Handler();
    private LinearLayout linear_guilaima_2;
    private LinearLayout linear_guilaima_1;
    private TextView reSendOTP;
    private Button btn_confrim_error_numberPhone;
    private String mVerificationID;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;

    public NumberPhoneFragment() {
    }

    public static NumberPhoneFragment newInstance(String numberphone) {
        NumberPhoneFragment fragment = new NumberPhoneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBERPHONE1, numberphone);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mPhoneNumber = getArguments().getString(ARG_NUMBERPHONE1);
            if (mPhoneNumber.length() == 9){
                phonenumber = "0"+mPhoneNumber;
            }else {
                phonenumber = mPhoneNumber;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_number_phone, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setOnBackPressedCallback();
        setOTP_Event();
        setKey();
    }

    private void initUI(View view){
        et_numberPhone = view.findViewById(R.id.et_NumberPhone);
        btn_numberPhone = view.findViewById(R.id.btn_NumberPhone);
        btn_confrim_NumberPhone = view.findViewById(R.id.btn_confrim_NumberPhone);
        btn_confrim_error_numberPhone = view.findViewById(R.id.btn_confrim_error_NumberPhone);
        linear_confirmOTP = view.findViewById(R.id.linear_confirmOTP);
        mAuth = FirebaseAuth.getInstance();
        tContainer = view.findViewById(R.id.tContainer);
        linear_numberphone = view.findViewById(R.id.linear_numberphone);
        et_confirmCode1 = view.findViewById(R.id.et_ConfirmCode1);
        et_confirmCode2 = view.findViewById(R.id.et_ConfirmCode2);
        et_confirmCode3 = view.findViewById(R.id.et_ConfirmCode3);
        et_confirmCode4 = view.findViewById(R.id.et_ConfirmCode4);
        et_confirmCode5 = view.findViewById(R.id.et_ConfirmCode5);
        et_confirmCode6 = view.findViewById(R.id.et_ConfirmCode6);
        linear_guilaima_2 = view.findViewById(R.id.linear_guilaima_2);
        linear_guilaima_1 = view.findViewById(R.id.linear_guilaima_1);
        tv_second_otp = view.findViewById(R.id.tv_second_otp);
        reSendOTP = view.findViewById(R.id.reSendOTP);
    }
    private void setKey(){
        setKeyChange(et_confirmCode1, et_confirmCode2);
        setKeyChange(et_confirmCode2, et_confirmCode3);
        setKeyChange(et_confirmCode3, et_confirmCode4);
        setKeyChange(et_confirmCode4, et_confirmCode5);
        setKeyChange(et_confirmCode5, et_confirmCode6);
        setKeyChange(et_confirmCode6, et_confirmCode6);
        setKeyChange2(et_confirmCode2, et_confirmCode1);
        setKeyChange2(et_confirmCode3, et_confirmCode2);
        setKeyChange2(et_confirmCode4, et_confirmCode3);
        setKeyChange2(et_confirmCode5, et_confirmCode4);
        setKeyChange2(et_confirmCode6, et_confirmCode5);
//        setKeyChange(et_confirmCode1, et_confirmCode1);
    }

    private CountDownTimer countDownStart() {
        CountDownTimer count = new CountDownTimer(60*1000+1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                tv_second_otp.setText(String.format("%02d", seconds));
                if (countdown_canel){
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                if (countdown_canel == true){
                    linear_guilaima_1.setVisibility(View.GONE);
                    linear_guilaima_2.setVisibility(View.GONE);
                }else {
                    linear_guilaima_2.setVisibility(View.GONE);
                    linear_guilaima_1.setVisibility(View.VISIBLE);
                }

            }
        };
        return count;
    }

    private void setOTP_Event(){
        btn_confrim_NumberPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visible == false){
                    et_numberPhone.setFocusable(false);
                    TransitionManager.beginDelayedTransition(tContainer);
                    visible = true;
                }
                else if (visible == true){
                    TransitionManager.beginDelayedTransition(tContainer);
                    if (getConfirmCode()!=null){
                        if (!codeSend.equals("")){
                            if (codeSend.equals(getConfirmCode())){
                                //xac nhan thanh cong
                                //tra ve
                                //String numberPhone = "?";
                                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.from_right, R.animator.to_left,R.animator.from_left, R.animator.to_right).replace(R.id.framelayout_container_register, RegisterFragment.newInstance(et_numberPhone.getText().toString()), "RegisterFragment").commit();
                            }else {
                                btn_confrim_error_numberPhone.setVisibility(View.VISIBLE);
                                btn_confrim_NumberPhone.setVisibility(View.GONE);
                                CountDownTimer count = new CountDownTimer(1+1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        Log.d(">>>>TAG:", "anima");

                                    }

                                    @Override
                                    public void onFinish() {
                                        TransitionManager.beginDelayedTransition(tContainer);
                                        btn_confrim_error_numberPhone.setVisibility(View.GONE);
                                        btn_confrim_NumberPhone.setVisibility(View.VISIBLE);
                                    }
                                };
                                count.start();

                            }
                        }else{
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, getConfirmCode());
                            Log.d(">>>>TAG:", ""+credential);
                            signInWithPhoneAuthCredential(credential);
                        }

                    }
                }
            }
        });
        if (phonenumber!= null && !phonenumber.equals("")) {
            et_numberPhone.setText(phonenumber);
        }


        reSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(">>>>TAG:", "SDT: "+et_numberPhone.getText().toString());
                linear_guilaima_1.setVisibility(visible ? View.GONE: View.VISIBLE);
                linear_guilaima_2.setVisibility(visible ? View.VISIBLE: View.GONE);
                String numberPhone = et_numberPhone.getText().toString();
                sendOTPCode(numberPhone);
                countdown_canel = false;
                countDownStart().start();
            }
        });

        btn_numberPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_numberPhone.getText().toString())){
                    Log.d(">>>>TAG:", "SDT: "+et_numberPhone.getText().toString());
                }else {
                    String numberPhone = et_numberPhone.getText().toString();
                    if (visible == false){
                        sendOTPCode(numberPhone);
                        et_numberPhone.setFocusable(false);
                        TransitionManager.beginDelayedTransition(tContainer);
                        visible = true;
                        linear_numberphone.setVisibility(visible ? View.GONE: View.VISIBLE);
                        linear_confirmOTP.setVisibility(visible ? View.VISIBLE: View.GONE);
                        linear_guilaima_2.setVisibility(visible ? View.VISIBLE: View.GONE);
                        btn_numberPhone.setVisibility(visible ? View.GONE: View.VISIBLE);
                        btn_confrim_NumberPhone.setVisibility(visible ? View.VISIBLE: View.GONE);
                        countDownStart().start();
                        countdown_canel = false;
                    }
                }
            }
        });

        et_numberPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(">>>>TAG:", visible+" et");
                if (visible==false){
                    et_numberPhone.setFocusableInTouchMode(true);
                }else if(visible== true){
                    TransitionManager.beginDelayedTransition(tContainer);
                    visible = !visible;
                    linear_confirmOTP.setVisibility(visible ? View.VISIBLE: View.GONE);
                    linear_numberphone.setVisibility(visible ? View.GONE: View.VISIBLE);
                    et_numberPhone.setFocusableInTouchMode(true);
                    et_numberPhone.setTextColor(Color.BLACK);
                    Log.d(">>>>TAG:", visible+" et");
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(">>>>TAG:", "signInWithCredential:success");
                            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.from_right, R.animator.to_left,R.animator.from_left, R.animator.to_right).replace(R.id.framelayout_container_register, RegisterFragment.newInstance(et_numberPhone.getText().toString()), "RegisterFragment").commit();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.d(">>>>TAG:", "signInWithCredential:failure"+task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void setOnBackPressedCallback(){
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (visible == true){
                    TransitionManager.beginDelayedTransition(tContainer);
                    visible = false;
                    linear_numberphone.setVisibility(visible ? View.GONE: View.VISIBLE);
                    linear_confirmOTP.setVisibility(visible ? View.VISIBLE: View.GONE);
                    btn_numberPhone.setVisibility(visible ? View.GONE: View.VISIBLE);
                    btn_confrim_NumberPhone.setVisibility(visible ? View.VISIBLE: View.GONE);
                    linear_confirmOTP.setVisibility(visible ? View.VISIBLE: View.GONE);
                    linear_guilaima_2.setVisibility(visible ? View.VISIBLE: View.GONE);
                    linear_guilaima_1.setVisibility(View.GONE);
                    et_numberPhone.setFocusableInTouchMode(true);
                    countdown_canel = true;
                    Log.d(">>>>TAG:", visible+" back");
                } else {
                    Log.d(">>>>TAG:", visible+" et");
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }

        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), onBackPressedCallback);
    }

    private void setKeyChange2(EditText editText, EditText et2){
        editText.removeTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(">>>>TAG:", "DDDDDDD");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(">>>>TAG:", "DDDDDDD");
            }
        });
    }


    private void setKeyChange(EditText editText, EditText et2){
        
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editText.getText().toString().trim().isEmpty()){
                    et2.requestFocus();
                }
                if (editText == et2){
                    if (!et2.getText().toString().trim().isEmpty()){
                        Log.d(">>>>TAG:", " et");
                        hideKeyBoard();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_numberPhone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    et_numberPhone.requestFocus();
                    hideKeyBoard();
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }



    private void hideKeyBoard(){
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    getActivity().getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    private String getConfirmCode(){
        String conFirmCode = et_confirmCode1.getText().toString()
                +et_confirmCode2.getText().toString()
                +et_confirmCode3.getText().toString()
                +et_confirmCode4.getText().toString()
                +et_confirmCode5.getText().toString()
                +et_confirmCode6.getText().toString();
        if (conFirmCode.isEmpty()){
            return null;
        }else {
            return conFirmCode;
        }
    }

    private void sendOTPCode(String phone){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84"+phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(getActivity())
                .setCallbacks(mCallback)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            codeSend = code;
            if(code!= null){
                Log.d(">>>>TAG:", "ngon: " + code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d(">>>>TAG:", "failed: " + e);
        }

        @Override
        public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(verificationID, forceResendingToken);
            mVerificationID = verificationID;
            mForceResendingToken = forceResendingToken;
        }
    };

}