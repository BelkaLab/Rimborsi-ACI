package belka.us.acirefund.model;

import android.util.Log;

import com.google.api.client.util.IOUtils;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import belka.us.acirefund.model.rest.RefundApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by fabriziorizzonelli on 27/09/2016.
 */

public class RefundManager {
    private Retrofit mRetrofit;

    public Observable<List<Refund>> getRefunds(String brand) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("google", Context.MODE_PRIVATE);
//        final String access_token = sharedPreferences.getString("access_token", null);

        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
//                .client(new OkHttpClient.Builder().authenticator(new Authenticator() {
//                    @Override
//                    public Request authenticate(Route route, Response response) throws IOException {
//                        return response.request().newBuilder().header(AUTHORIZATION, access_token).build();
//                    }
//                }).build())
                .baseUrl("https://sheets.googleapis.com/v4/spreadsheets/1HP7xSG-1Rgk6-8CjjKimnOp3IPrvLo0WJqUDylnjU8Y/")
                .build();

        RefundApi refundApi = mRetrofit.create(RefundApi.class);
//        "plated-dryad-144708@appspot.gserviceaccount.com"
        return refundApi.getRefunds().flatMap(new Func1<ValueRange, Observable<List<Refund>>>() {
            @Override
            public Observable<List<Refund>> call(ValueRange valueRange) {
                List<List<Object>> values = valueRange.getValues();
                List<Refund> refunds = new ArrayList<>();
                return Observable.just(refunds);
            }
        }).doOnError(new Action1<Throwable>() {
                         @Override
                         public void call(Throwable throwable) {
                             ByteArrayOutputStream writer = new ByteArrayOutputStream();
                             try {
                                 IOUtils.copy(((HttpException) throwable).response().errorBody().byteStream(), writer);
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }

                             Log.e("RETRO-ERROR", writer.toString());
                         }
                     }
        );
    }
}
