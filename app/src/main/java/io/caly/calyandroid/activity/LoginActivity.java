package io.caly.calyandroid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.caly.calyandroid.exception.HttpResponseParsingException;
import io.caly.calyandroid.exception.UnExpectedHttpStatusException;
import io.caly.calyandroid.util.Logger;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.caly.calyandroid.activity.base.BaseAppCompatActivity;
import io.caly.calyandroid.CalyApplication;
import io.caly.calyandroid.model.DeviceType;
import io.caly.calyandroid.model.LoginPlatform;
import io.caly.calyandroid.model.orm.TokenRecord;
import io.caly.calyandroid.model.response.BasicResponse;
import io.caly.calyandroid.model.response.SessionResponse;
import io.caly.calyandroid.R;
import io.caly.calyandroid.util.ApiClient;
import io.caly.calyandroid.util.StringFormmater;
import io.caly.calyandroid.util.Util;
import io.caly.calyandroid.view.LoginDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Copyright 2017 JSpiner. All rights reserved.
 *
 * @author jspiner (jspiner@naver.com)
 * @project Caly
 * @since 17. 2. 11
 */

public class LoginActivity extends BaseAppCompatActivity {

    @Bind(R.id.btn_login_google)
    Button btnLoginGoogle;

    @Bind(R.id.linear_loading_parent)
    LinearLayout linearLoading;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    private void init(){

        ButterKnife.bind(this);


        //set toolbar
        Util.setStatusBarColor(this, Color.BLACK);

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestServerAuthCode(getString(R.string.google_client_id), true)
                        .requestIdToken(getString(R.string.google_client_id))
                        .requestScopes(
                                new Scope("https://www.googleapis.com/auth/calendar"),
                                new Scope("https://www.googleapis.com/auth/userinfo.email"),
                                new Scope("https://www.googleapis.com/auth/calendar.readonly")
                        ).build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , onGoogleConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    OnConnectionFailedListener onGoogleConnectionFailedListener = new OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Logger.d(TAG, "onConnectionFailed : " + connectionResult.getErrorMessage());
        }
    };

    @OnClick(R.id.btn_login_google)
    void onGoogleLoginClick(){
        Logger.d(TAG,"onclick");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Util.RC_INTENT_GOOGLE_SIGNIN);

        Tracker t = ((CalyApplication)getApplication()).getDefaultTracker();
        t.setScreenName(this.getClass().getName());
        t.send(
                new HitBuilders.EventBuilder()
                        .setCategory(getString(R.string.ga_action_button_click))
                        .setAction(Util.getCurrentMethodName())
                        .build()
        );
    }

    @OnClick(R.id.btn_login_naver)
    void onNaverLoginClick(){
        LoginDialog dialog = new LoginDialog(this, "Naver로 로그인", new LoginDialog.LoginDialogCallback() {
            @Override
            public void onPositive(LoginDialog dialog, String userId, String userPw) {
                dialog.dismiss();

                procLoginCaldav(StringFormmater.hostnameAuthGenerator(userId, "naver.com"), userPw, LoginPlatform.CALDAV_NAVER.value);
            }

            @Override
            public void onNegative(LoginDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();

        Tracker t = ((CalyApplication)getApplication()).getDefaultTracker();
        t.setScreenName(this.getClass().getName());
        t.send(
                new HitBuilders.EventBuilder()
                        .setCategory(getString(R.string.ga_action_button_click))
                        .setAction(Util.getCurrentMethodName())
                        .build()
        );

    }

    @OnClick(R.id.btn_login_apple)
    void onAppleLoginClick(){
        LoginDialog dialog = new LoginDialog(this, "Apple로 로그인", new LoginDialog.LoginDialogCallback() {
            @Override
            public void onPositive(LoginDialog dialog, String userId, String userPw) {
                dialog.dismiss();

                procLoginCaldav(userId, userPw, LoginPlatform.CALDAV_ICAL.value);
            }

            @Override
            public void onNegative(LoginDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();

        Tracker t = ((CalyApplication)getApplication()).getDefaultTracker();
        t.setScreenName(this.getClass().getName());
        t.send(
                new HitBuilders.EventBuilder()
                        .setCategory(getString(R.string.ga_action_button_click))
                        .setAction(Util.getCurrentMethodName())
                        .build()
        );
    }

    void startEventActivity(){
        Intent intent = new Intent(LoginActivity.this, EventListActivity.class);
        startActivity(intent);
        finish();
    }

    void startSignupActivity(String userId, String userPw, String loginPlatform, String authCode){
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userPw", userPw);
        intent.putExtra("loginPlatform", loginPlatform);
        intent.putExtra("authCode", authCode);
        startActivity(intent);
        finish();
    }

    void signOutGoogle(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //Toast.makeText(getBaseContext(),"logout status : " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    void registerDeviceInfo(String sessionKey){
        Logger.i(TAG, "registerDeviceInfo");
        ApiClient.getService().registerDevice(
                sessionKey,
                FirebaseInstanceId.getInstance().getToken(),
                DeviceType.ANDROID.value,
                Util.getAppVersion(),
                Util.getDeviceInfo(),
                Util.getUUID(),
                Util.getSdkLevel()
        ).enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
                Logger.d(TAG,"onResponse code : " + response.code());

                SessionResponse body = response.body();

                switch (response.code()){
                    case 200:
                        startEventActivity();
                        break;
                    case 400:
                        Toast.makeText(
                                getBaseContext(),
                                getString(R.string.toast_msg_login_fail),
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                    default:
                        Crashlytics.logException(new UnExpectedHttpStatusException(call, response));
                        Toast.makeText(
                                getBaseContext(),
                                getString(R.string.toast_msg_server_internal_error),
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                }

            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {


                if(t instanceof MalformedJsonException || t instanceof JsonSyntaxException){
                    Crashlytics.logException(new HttpResponseParsingException(call, t));
                }
                Logger.e(TAG,"onfail : " + t.getMessage());
                Logger.e(TAG, "fail " + t.getClass().getName());

                Toast.makeText(
                        getBaseContext(),
                        getString(R.string.toast_msg_network_error),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    void procLogin(final String userId, final String userPw, final String loginPlatform, final String subject, final String authCode){
        Logger.i(TAG, "procLogin");

        linearLoading.setVisibility(View.VISIBLE);
        ApiClient.getService().loginCheck(
                userId,
                userPw,
                Util.getUUID(),
                "null", //session
                loginPlatform,
                subject,
                Util.getAppVersion()
        ).enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
                Logger.d(TAG,"onResponse code : " + response.code());
                Logger.d(TAG,"req url : " + call.request().url().toString());

                SessionResponse body = response.body();

                TokenRecord tokenRecord = TokenRecord.getTokenRecord();

                signOutGoogle();
                linearLoading.setVisibility(View.GONE);
                switch (response.code()){
                    case 200:
//                    case 205:
                        tokenRecord.setApiKey(body.payload.apiKey);
                        tokenRecord.setLoginPlatform(loginPlatform);
                        tokenRecord.setUserId(userId);
                        tokenRecord.save();
                        requestUpdatePushToken();
                        startEventActivity();
                        break;
                    case 202:
                        requestUpdatePushToken();
                        startSignupActivity(userId, userPw, loginPlatform, authCode);
                        break;
                    case 201:
                        tokenRecord.setApiKey(body.payload.apiKey);
                        tokenRecord.setLoginPlatform(loginPlatform);
                        tokenRecord.setUserId(userId);
                        tokenRecord.save();
                        registerDeviceInfo(body.payload.apiKey);
                        requestUpdatePushToken();
                        break;
                    case 400:
                    case 401:
                        Toast.makeText(
                                getBaseContext(),
                                getString(R.string.toast_msg_login_fail),
                                Toast.LENGTH_LONG
                        ).show();
                        signOutGoogle();
                        break;
                    default:
                        Crashlytics.logException(new UnExpectedHttpStatusException(call, response));
                        Toast.makeText(
                                getBaseContext(),
                                getString(R.string.toast_msg_server_internal_error),
                                Toast.LENGTH_LONG
                        ).show();
                        signOutGoogle();
                        break;
                }


            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                if(t instanceof MalformedJsonException || t instanceof JsonSyntaxException){
                    Crashlytics.logException(new HttpResponseParsingException(call, t));
                }

                Logger.e(TAG,"onfail : " + t.getMessage());
                Logger.e(TAG, "fail " + t.getClass().getName());

                linearLoading.setVisibility(View.GONE);

                Toast.makeText(
                        getBaseContext(),
                        getString(R.string.toast_msg_network_error),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }


    private void requestUpdatePushToken() {
        Logger.i(TAG, "sendRegistrationToServer");

        if(TokenRecord.getTokenRecord().getApiKey()!=null) {
            ApiClient.getService().updatePushToken(
                    FirebaseInstanceId.getInstance().getToken(),
                    TokenRecord.getTokenRecord().getApiKey()
            ).enqueue(new Callback<BasicResponse>() {
                @Override
                public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                    Logger.d(TAG, "onResponse code : " + response.code());

                    if (response.code() == 200) {
                        BasicResponse body = response.body();
                        Logger.d(TAG, "push token update success");

                    } else {
                        Crashlytics.logException(new UnExpectedHttpStatusException(call, response));
                        Logger.d(TAG, "push token update fail");

                    }
                }

                @Override
                public void onFailure(Call<BasicResponse> call, Throwable t) {
                    if(t instanceof MalformedJsonException || t instanceof JsonSyntaxException){
                        Crashlytics.logException(new HttpResponseParsingException(call, t));
                    }

                    Logger.d(TAG, "onfail : " + t.getMessage());
                    Logger.d(TAG, "fail " + t.getClass().getName());

                }
            });
        }
    }

    void procLoginCaldav(String userId, String userPw, String loginPlatform){
        Logger.i(TAG, "procLoginCaldav");
        procLogin(userId, userPw, loginPlatform, "null", "null");
    }

    void procLoginGoogle(String subject, String authCode){
        Logger.i(TAG, "procLoginGoogle");
        procLogin("null", "null", LoginPlatform.GOOGLE.value, subject, authCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Util.RC_INTENT_GOOGLE_SIGNIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            Logger.d(TAG, "handleSignInResult:" + result.isSuccess());
            Logger.d(TAG, "handleSignInResult:" + result.getStatus().getStatus());


            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                Logger.d(TAG, acct.getDisplayName());
                Logger.i(TAG, "id token : " + acct.getIdToken());
                Logger.i(TAG, "serverauthcode : " + acct.getServerAuthCode());
                Logger.i(TAG, "id : " + acct.getId());
                Logger.d(TAG, "email : " + acct.getEmail());

                procLoginGoogle(acct.getId(), acct.getServerAuthCode());

            } else {
                switch (result.getStatus().getStatusCode()){
                    case GoogleSignInStatusCodes.SIGN_IN_CANCELLED:
                        Toast.makeText(
                                getBaseContext(),
                                getString(R.string.toast_msg_login_canceled),
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                    case GoogleSignInStatusCodes.SIGN_IN_FAILED:
                        Toast.makeText(
                                getBaseContext(),
                                getString(R.string.toast_msg_login_fail),
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                    default:
                        Toast.makeText(
                                getBaseContext(),
                                getString(R.string.toast_msg_unknown_error),
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                }

            }
        }
    }


}
