package com.example.vacationmalangver1.Ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vacationmalangver1.Model.User;
import com.example.vacationmalangver1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import static android.text.TextUtils.isEmpty;
import static com.example.vacationmalangver1.util.Check.doStringsMatch;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private EditText mEmail, mPassword, mCOnfirmPassword;
    private ProgressBar mProgressBar;
    private Button btnRegister;

    private FirebaseFirestore mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail = (EditText)findViewById(R.id.input_email);
        mPassword =(EditText)findViewById(R.id.input_password);
        mCOnfirmPassword = (EditText)findViewById(R.id.input_confirm_password);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        btnRegister = findViewById(R.id.btn_register);


        findViewById(R.id.btn_register).setOnClickListener(this);

        mDb = FirebaseFirestore.getInstance();

        hideSoftKeyboard();
    }

    public void registerNewEmail(final String email, String password){
        showDialog();
        btnRegister.setVisibility(View.INVISIBLE);

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"createUserWithEmail : onComplete:"+ task.isSuccessful());

                        if (task.isSuccessful()){
                            Log.d(TAG, "oncomplete: AuthState:"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

                            User user = new User();
                            user.setEmail(email);
                            user.setUsername(email.substring(0,email.indexOf("@")));
                            user.setUser_id(FirebaseAuth.getInstance().getUid());

                            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                                    .setTimestampsInSnapshotsEnabled(true)
                                    .build();
                            mDb.setFirestoreSettings(settings);

                            DocumentReference newUserRef = mDb
                                    .collection("Users")
                                    .document(FirebaseAuth.getInstance().getUid());

                            newUserRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    hideDialog();
                                    btnRegister.setVisibility(View.VISIBLE);

                                    if (task.isSuccessful()){
                                        redirectLoginScreen();
                                    }else{
                                        View parentLayout = findViewById(android.R.id.content);
                                        Snackbar.make(parentLayout,"something went wrong",Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else{
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout,"something went wrongg.",Snackbar.LENGTH_SHORT).show();
                            hideDialog();
                        }
                    }
                });
    }
    private void redirectLoginScreen(){
        Log.d(TAG, "redirectLoginScreen : redirect to login screen ");

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register: {
                Log.d(TAG, "onclick: register");

                //cek null value in field
                if (!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())
                        && !isEmpty(mCOnfirmPassword.getText().toString())){

                    //cek if password same
                    if (doStringsMatch(mPassword.getText().toString(),mCOnfirmPassword.getText().toString())){
                        //initiate registration
                        registerNewEmail(mEmail.getText().toString(),mPassword.getText().toString());

                    }else{
                        Toast.makeText(RegisterActivity.this,"password do not match",Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(RegisterActivity.this, "u must fill all the field", Toast.LENGTH_SHORT).show();

                }
                break;

            }
        }

    }
}
