package belka.us.acirefund.model;

import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Nullable;

import belka.us.acirefund.model.rest.GoogleSpreadsheetGsonConverter;
import belka.us.acirefund.model.rest.RefundApi;
import belka.us.acirefund.utils.Config;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;

import static belka.us.acirefund.utils.ConversionUtils.concactList;
import static belka.us.acirefund.utils.ConversionUtils.convertBatchGetToList;
import static belka.us.acirefund.utils.ConversionUtils.parseToDouble;

/**
 * Created by fabriziorizzonelli on 27/09/2016.
 */

public class RefundManager {
    private Retrofit mRetrofit;

    public RefundManager() {
        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GoogleSpreadsheetGsonConverter.buildGsonConverter())
                .baseUrl("https://sheets.googleapis.com/v4/spreadsheets/1Z9vA5TVyNYrl2GNgaz1j9rHhq--PD_qlvg4XQvnYWZ8/")
                .build();
    }

    public Observable<List<Refund>> getRefundsByBrand(final String brand, List<String> allFuelRanges) {
        return mRetrofit.create(RefundApi.class).getRefundsByBrand("https://sheets.googleapis.com/v4/spreadsheets/1Z9vA5TVyNYrl2GNgaz1j9rHhq--PD_qlvg4XQvnYWZ8/values:batchGet", allFuelRanges, Config.API_KEY).flatMap(new Func1<BatchGetValuesResponse, Observable<List<Refund>>>() {
            @Override
            public Observable<List<Refund>> call(BatchGetValuesResponse response) {
                return Observable.just(convertValueRangeToList(convertBatchGetToList(response), brand));
            }
        });
    }

    public Observable<List<Refund>> getRefundsByBrandAndFuelType(final String brand, final String fuelType) {
        return mRetrofit.create(RefundApi.class).getRefundsByBrandAndByFuelType(Config.getRefundByBrandAndByFuelRange(fuelType), Config.API_KEY).flatMap(new Func1<ValueRange, Observable<List<Refund>>>() {
            @Override
            public Observable<List<Refund>> call(ValueRange response) {
                return Observable.just(convertValueRangeToList(response.getValues(), brand));
            }
        });
    }

    private List<Refund> convertValueRangeToList(List<List<Object>> values, final String brand) {
        return Ordering.natural().onResultOf(new Function<Refund, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Refund input) {
                return input.getModel();
            }
        }).immutableSortedCopy(Lists.newArrayList(Iterables.filter(Lists.transform(values, new Function<List<Object>, Refund>() {
            @Nullable
            @Override
            public Refund apply(@Nullable List<Object> input) {
                try {
                    Refund refund = new Refund(input.get(0).toString(), concactList(input.get(1).toString(), input.get(2).toString()), parseToDouble(input.get(3).toString()));
                    if (refund.getBrand().equalsIgnoreCase(brand))
                        return refund;
                    else
                        return null;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }), new Predicate<Refund>() {
            @Override
            public boolean apply(@Nullable Refund input) {
                return input != null;
            }
        })));
    }
}


