package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.model.KakaoSdkError;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AccessTokenInfo;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView btn_login;
    private SignInButton g_login_btn;
    private static final String TAG2 = "GoogleActivity";
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login = findViewById(R.id.btn_login);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

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

        // 토큰 유효성 체크하는 콜백함수
        Function2<AccessTokenInfo, Throwable, Unit> token_callback = new Function2<AccessTokenInfo, Throwable, Unit>() {
            @Override
            public Unit invoke(AccessTokenInfo accessTokenInfo, Throwable throwable) {
                if(throwable != null) {
                    if (throwable instanceof KakaoSdkError && ((KakaoSdkError) throwable).isInvalidTokenError() == true) {
                        // 로그인 필요하다
                        Log.d("need login : ","로그인이 필요합니다");
                    }
                    else {
                        //기타 에러
                        Log.d("error occured : ", "기타 에러 발생" + throwable.toString());
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신 됨)
                    Log.d("success token check : ", "토큰 유효성 체크 성공");
                    updateKakaoLoginUI();
                }
                return null;
            }
        };

        if (AuthApiClient.getInstance().hasToken()){
            UserApiClient.getInstance().accessTokenInfo(token_callback);
        } else {
            // 로그인 필요하다
            Log.d("need login : ","로그인이 필요합니다");
        }

        // 로그인 공통 callback 구성
        Function2<OAuthToken, Throwable, Unit> login_callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken != null){         // 로그인 성공 했다면
                    Log.d("success login : ", "로그인에 성공했습니다");
                }
                if (throwable != null){         // 로그인 실패 했다면
                    Log.d("fail login : ", "로그인에 실패했습니다" + throwable.toString());
                }
                updateKakaoLoginUI();
                return null;
            }
        };

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)){
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this,login_callback);
                }else{
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this,login_callback);
                    Log.d("login in KakaoAccount :", "카카오 계정으로 로그인");
                }

            }
        });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //로그인이 되어있는지를 확인하고 로그인 되어있는 경우, 아닌경우에 따라 다른 이벤트 설정
    private void updateKakaoLoginUI(){
        CalendarFragment calendarFragment = new CalendarFragment();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                // 로그인이 되어 있을때 이벤트
                if(user != null){
                    long uuid = user.getId();
                    String email = user.getKakaoAccount().getEmail();
                    String nickname = user.getKakaoAccount().getProfile().getNickname();


                    //카카오 API로부터 넘어오는 정보들 확인용 로그
                    Log.i(TAG, "invoke: id=" + uuid);
                    Log.i(TAG, "invoke: email=" + email);
                    Log.i(TAG, "invoke: nickname=" + nickname);
                    Log.i(TAG, "invoke: gender=" + user.getKakaoAccount().getGender());
                    Log.i(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());

                    myRef.child("Users").child(String.valueOf(uuid)).child("Nickname").setValue(nickname);
                    myRef.child("Users").child(String.valueOf(uuid)).child("email").setValue(email);

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("uuid",uuid);
                    intent.putExtra("profile",user.getKakaoAccount().getProfile().getThumbnailImageUrl());
                    intent.putExtra("nickname",user.getKakaoAccount().getProfile().getNickname());
                    intent.putExtra("email",user.getKakaoAccount().getEmail());
                    startActivity(intent);
                } else { //로그인이 되어 있지 않을 때, (user = null 일때를 의미)
                    Toast.makeText(MainActivity.this,"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        });
    }


    // google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
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
        if(user !=null){
            Intent homeMove_intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(homeMove_intent);
            finish();
        }
    }
}