package belka.us.acirefund.model;

import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.util.List;

import javax.annotation.Nullable;

import belka.us.acirefund.model.rest.BrandApi;
import belka.us.acirefund.model.rest.GoogleSpreadsheetGsonConverter;
import belka.us.acirefund.utils.Config;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;

import static belka.us.acirefund.utils.ConversionUtils.convertBatchGetToList;
import static belka.us.acirefund.utils.ConversionUtils.removeDuplicate;

/**
 * Created by fabriziorizzonelli on 27/09/2016.
 */

public class BrandManager {

    private Retrofit mRetrofit;

    public BrandManager() {
        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GoogleSpreadsheetGsonConverter.buildGsonConverter())
                .baseUrl("https://sheets.googleapis.com/v4/spreadsheets/1Z9vA5TVyNYrl2GNgaz1j9rHhq--PD_qlvg4XQvnYWZ8/")
                .build();
    }

    public Observable<List<Brand>> getDistinctBrands() {
        return mRetrofit.create(BrandApi.class).getDistinctBrand("https://sheets.googleapis.com/v4/spreadsheets/1Z9vA5TVyNYrl2GNgaz1j9rHhq--PD_qlvg4XQvnYWZ8/values:batchGet", Config.getBrandRanges(), Config.API_KEY).flatMap(new Func1<BatchGetValuesResponse, Observable<List<Brand>>>() {
            @Override
            public Observable<List<Brand>> call(BatchGetValuesResponse response) {
                return Observable.just(convertValueRangeToList(convertBatchGetToList(response)));
            }
        });
    }

    private List<Brand> convertValueRangeToList(List<List<Object>> values) {
        return Ordering.natural().onResultOf(new Function<Brand, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Brand input) {
                return input.getName();
            }
        }).immutableSortedCopy(removeDuplicate(Lists.newArrayList(Lists.transform(values, new Function<List<Object>, Brand>() {
            @Nullable
            @Override
            public Brand apply(@Nullable List<Object> input) {
                return new Brand(input.get(0).toString());
            }
        }))));
    }
}
