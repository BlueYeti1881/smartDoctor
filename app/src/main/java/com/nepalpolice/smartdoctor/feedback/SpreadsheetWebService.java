package com.nepalpolice.smartdoctor.feedback;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SpreadsheetWebService {

    @POST("1FAIpQLSdatlKheW5G_H8g9DCD8_2K1Ltytb4Fn64erYleewN8PWB1vQ/formResponse")
    @FormUrlEncoded
    Call<Void> feedbackSend(
            @Field("entry.161281878") String name,
            @Field("entry.327087789") String email
    );

}
