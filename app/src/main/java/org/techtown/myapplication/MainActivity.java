package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.annotations.NotNull;
import com.kakao.sdk.common.util.Utility;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView btn_login;
    private SignInButton g_login_btn;
    private static final String TAG2 = "GoogleActivity";
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(this);

//        if(gsa != null){
//            // 재로그인 방지 메소드
//            Log.d(TAG,"로그인 정보 확인");
//            Intent homeMove_intent = new Intent(getApplicationContext(), HomeActivity.class);
//            startActivity(homeMove_intent);
//        }

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user !=null) {
            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        Log.d(TAG, "아이디 토큰 = " + idToken);
                        Intent homeMove_intent = new Intent(getApplicationContext(), HomeActivity.class);
                        homeMove_intent.putExtra("emailGoogle",user.getEmail());
                        homeMove_intent.putExtra("nameGoogle",user.getDisplayName());
                        homeMove_intent.putExtra("uidGoogle",user.getUid());
                        Log.d(TAG,"구글이름1" + user.getDisplayName());
                        startActivity(homeMove_intent);
                    }
                }
            });
        }
        SignInButton g_login_btn = findViewById(R.id.google_login_btn);

        g_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("클릭테스트");
                signIn();
            }
        });

        String keyHash = Utility.INSTANCE.getKeyHash(MainActivity.this);
        Log.d(TAG, "해쉬키 값 : " + keyHash);


    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    // google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
                Intent homeMove_intent = new Intent(getApplicationContext(), HomeActivity.class);
                homeMove_intent.putExtra("emailGoogle",account.getEmail());
                homeMove_intent.putExtra("nameGoogle",account.getDisplayName());
                Log.d(TAG,"account.getId():"+account.getIdToken());
                startActivity(homeMove_intent);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }

        }

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null){
                                Intent homeMove_intent = new Intent(getApplicationContext(), HomeActivity.class);
                                homeMove_intent.putExtra("nameGoogle", user.getDisplayName());
                                Log.d(TAG, "구글이름2" + user.getDisplayName());
                                startActivity(homeMove_intent);
                            }
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user !=null) {
        }
    }
}