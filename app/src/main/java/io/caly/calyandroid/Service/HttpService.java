package io.caly.calyandroid.Service;

import io.caly.calyandroid.Model.Category;
import io.caly.calyandroid.Model.Response.AccountResponse;
import io.caly.calyandroid.Model.Response.BasicResponse;
import io.caly.calyandroid.Model.Response.EventResponse;
import io.caly.calyandroid.Model.Response.NoticeResponse;
import io.caly.calyandroid.Model.Response.RecoResponse;
import io.caly.calyandroid.Model.Response.SessionResponse;
import io.caly.calyandroid.Model.Response.SyncResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Copyright 2017 JSpiner. All rights reserved.
 *
 * @author jspiner (jspiner@naver.com)
 * @project Caly
 * @since 17. 2. 11
 */

public interface HttpService {

    //TEST
    @FormUrlEncoded
    @POST("v1.0/")
    Call<BasicResponse> test(
            @Field("key") String key
    );

    /*
    =============================
                MEMBER
    =============================
     */

    //loginCheck
    @FormUrlEncoded
    @POST("v1.0/member/loginCheck")
    Call<SessionResponse> loginCheck(
            @Field("uId") String userId,
            @Field("uPw") String userPw,
            @Field("uuid") String uuid,
            @Field("apikey") String apiKey,
            @Field("loginPlatform") String loginPlatform,
            @Field("subject") String subject,
            @Field("appVersion") String appVersion/*,
            @Field("sdkLevel") String sdkLevel*/
    );

    //signUp
    @FormUrlEncoded
    @POST("v1.0/member/signUp")
    Call<SessionResponse> signUp(
            @Field("uId") String userId,
            @Field("uPw") String userPw,
            @Field("authCode") String authCode,
            @Field("gender") int gender,
            @Field("birth") int birth,
            @Field("loginPlatform") String loginPlatform,
            @Field("pushToken") String pushToken,
            @Field("deviceType") int deviceType,
            @Field("appVersion") String appVersion,
            @Field("deviceInfo") String deviceInfo,
            @Field("uuid") String uuid,
            @Field("sdkLevel") String sdkLevel
    );

    //registerDevice
    @FormUrlEncoded
    @POST("v1.0/member/registerDevice")
    Call<SessionResponse> registerDevice(
            @Field("apikey") String apiKey,
            @Field("pushToken") String pushToken,
            @Field("deviceType") int deviceType,
            @Field("appVersion") String appVersion,
            @Field("deviceInfo") String deviceInfo,
            @Field("uuid") String uuid,
            @Field("sdkLevel") String sdkLevel
    );

    //logout
    @FormUrlEncoded
    @POST("v1.0/member/logout")
    Call<BasicResponse> logout(
            @Field("apikey") String apiKey
    );

    //checkVersion
    @FormUrlEncoded
    @POST("v1.0/member/checkVersion")
    Call<BasicResponse> checkVersion(
            @Field("appVersion") String appVersion,
            @Field("apikey") String apiKey
    );

    //accountList
    @FormUrlEncoded
    @POST("v1.0/member/accountList")
    Call<AccountResponse> accountList(
            @Field("apikey") String apiKey
    );

    //addAccount
    @FormUrlEncoded
    @POST("v1.0/member/addAccount")
    Call<BasicResponse> addAccount(
            @Field("apikey") String apiKey,
            @Field("loginPlatform") String loginPlatform,
            @Field("uId") String userId,
            @Field("uPw") String userPw,
            @Field("authCode") String authCode
    );

    //withdrawal
    @FormUrlEncoded
    @POST("v1.0/member/withdrawal")
    Call<BasicResponse> withdrawal(
            @Field("apikey") String apiKey,
            @Field("contents") String contents
    );

    //updateAccount
    @FormUrlEncoded
    @POST("v1.0/member/updateAccount")
    Call<BasicResponse> updateAccount(
            @Field("uId") String userId,
            @Field("uPw") String userPw,
            @Field("loginPlatform") String loginPlatform
    );

    //removeAccount
    @FormUrlEncoded
    @POST("v1.0/member/removeAccount")
    Call<BasicResponse> removeAccount(
            @Field("apikey") String apiKey,
            @Field("loginPlatform") String loginPlatform,
            @Field("uId") String userId
    );


    /*
    =============================
                EVENT
    =============================
     */

    @FormUrlEncoded
    @POST("v1.0/events/getList")
    Call<EventResponse> getEventList(
            @Field("apikey") String apiKey,
            @Field("pageNum") int pageNum
    );



    /*
    =============================
                SYNC
    =============================
     */

    //sync
    @FormUrlEncoded
    @POST("v1.0/sync")
    Call<BasicResponse> sync(
            @Field("apikey") String apiKey
    );

    //checkSync
    @FormUrlEncoded
    @POST("v1.0/checkSync")
    Call<BasicResponse> checkSync(
            @Field("apikey") String apiKey
    );

    //caldavManualSync
    @FormUrlEncoded
    @POST("v1.0/sync/caldavManualSync")
    Call<SyncResponse> caldavManualSync(
            @Field("apikey") String apiKey,
            @Field("userId") String user_id,
            @Field("loginPlatform") String loginPlatform
    );


    /*
    =============================
                RECO
    =============================
     */

    //getList
    @FormUrlEncoded
    @POST("v1.0/reco/getList")
    Call<RecoResponse> getRecoList(
            @Field("apikey") String apiKey,
            @Field("eventHashkey") String eventHashKey,
            @Field("category") String category
    );

    //tracking
    @FormUrlEncoded
    @POST("v1.0/reco/tracking")
    Call<BasicResponse> tracking(
            @Field("apikey") String apiKey,
            @Field("eventHashkey") String eventHashkey,
            @Field("recoHashkey") String recoHashkey,
            @Field("type") String type,
            @Field("residenseTime") long residenseTime
    );

    //checkRepoState
    @Deprecated
    @FormUrlEncoded
    @POST("v1.0/reco/checkRecoState")
    Call<BasicResponse> checkRepoState(
            @Field("apikey") String apiKey
    );

    /*
    =============================
              SETTING
    =============================
     */

    //updatePushToken
    @FormUrlEncoded
    @POST("v1.0/setting/updatePushToken")
    Call<BasicResponse> updatePushToken(
            @Field("pushToken") String pushToken,
            @Field("apikey") String apiKey
    );

    //setReceivePush
    @FormUrlEncoded
    @POST("v1.0/setting/setReceivePush")
    Call<BasicResponse> setReceivePush(
            @Field("apikey") String apiKey,
            @Field("receive") int receive
    );



    /*
    =============================
              SUPPORT
    =============================
     */

    //notices
    @FormUrlEncoded
    @POST("v1.0/support/notices")
    Call<NoticeResponse> notices(
            @Field("apikey") String apiKey
    );

    //requests
    @FormUrlEncoded
    @POST("v1.0/support/requests")
    Call<BasicResponse> requests(
            @Field("apikey") String apiKey,
            @Field("contents") String contents
    );



}
