package nta.nguyenanh.code_application.OTP;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import nta.nguyenanh.code_application.LoginActivity;
import nta.nguyenanh.code_application.R;

public class EnterOtpActivity extends AppCompatActivity {
    private EditText edtOTP;
    private Button btnVerifyOTP;
    private TextView tvSentOTPagain;
    FirebaseAuth mAuth;
    private String mPhoneNumber;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        getDataIntent();
        initUi();

        mAuth= FirebaseAuth.getInstance();
        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOTP = edtOTP.getText().toString().trim();
                onClickVerifyOTP(strOTP);
            }



            private void onClickVerifyOTP(String strOTP) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, strOTP);
            }
        });

        tvSentOTPagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSendOTPagain();
            }
        });
    }

    private void getDataIntent(){
        mPhoneNumber= getIntent().getStringExtra("Phone_number");
        mVerificationId=getIntent().getStringExtra("verification_id");
    }




    private void initUi(){
        edtOTP = findViewById(R.id.edtOTP);
        btnVerifyOTP = findViewById(R.id.btnVerifyOTP);
    }
    private void onClickSendOTPagain() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setForceResendingToken(mForceResendingToken)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(EnterOtpActivity.this,"Verification failed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                mVerificationId = verificationId;
                                mForceResendingToken=forceResendingToken;
                            }


                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Task<AuthResult> authResultTask = mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            goToLoginActivity(user.getPhoneNumber());
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                                Toast.makeText(EnterOtpActivity.this,"the verification code entered wa invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                });
    }
    private void goToLoginActivity(String phoneNumber) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("phone number", phoneNumber);
        startActivity(intent);
    }
}
