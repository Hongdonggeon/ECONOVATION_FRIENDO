package org.techtown.myapplication;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;



//카카오 API 초기화 클래스
public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this,"cde9db3e2d1250e758b5d5c82b2a0767");
    }
}
