package belka.us.acirefund.refund;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import belka.us.acirefund.base.presenter.BaseRxGoogleServiceLcePresenter;
import belka.us.acirefund.model.Refund;
import belka.us.acirefund.model.RefundManager;
import belka.us.acirefund.model.event.EvaluateResultEvent;
import belka.us.acirefund.model.event.HideResultEvent;
import belka.us.acirefund.rxbus.RxBus;
import belka.us.acirefund.utils.Config;
import rx.Observable;
import rx.functions.Func0;

import static belka.us.acirefund.utils.ConversionUtils.concactList;
import static belka.us.acirefund.utils.ConversionUtils.convertBatchGetToList;
import static belka.us.acirefund.utils.ConversionUtils.parseToDouble;
import static com.google.common.collect.Lists.transform;

/**
 * Created by fabriziorizzonelli on 27/09/2016.
 */

public class RefundPresenter extends BaseRxGoogleServiceLcePresenter<RefundView, List<Refund>> {

    private RefundManager refundManager;
    private Sheets sheetsService;
    private RxBus rxBus;

    @Inject
    public RefundPresenter(RefundManager refundManager, RxBus rxBus) {
        this.refundManager = refundManager;
        this.rxBus = rxBus;
    }

    @Override
    protected void setSheetsService(Sheets sheetsService) {
        this.sheetsService = sheetsService;
    }

    private Observable<List<Refund>> getRefundsByBrand(final String brand) {
        return Observable.defer(new Func0<Observable<List<Refund>>>() {
            @Override
            public Observable<List<Refund>> call() {
                try {
                    BatchGetValuesResponse response = sheetsService.spreadsheets().values().batchGet(Config.SHEET_ID).setRanges(Config.getRefundByBrandRanges()).execute();

                    return Observable.just(convertValueRangeToList(convertBatchGetToList(response), brand));
                } catch (IOException e) {
                    e.printStackTrace();
                    return Observable.error(e);
                }
            }
        });
    }

    private Observable<List<Refund>> getRefundsByBrandAndFuelType(final String brand, final String fuelType) {
        return Observable.defer(new Func0<Observable<List<Refund>>>() {
            @Override
            public Observable<List<Refund>> call() {
                try {
                    ValueRange response = sheetsService.spreadsheets().values().get(Config.SHEET_ID, Config.getRefundByBrandAndByFuelRange(fuelType)).execute();

                    return Observable.just(convertValueRangeToList(response.getValues(), brand));
                } catch (IOException e) {
                    e.printStackTrace();
                    return Observable.error(e);
                }
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
        }).immutableSortedCopy(Lists.newArrayList(Iterables.filter(transform(values, new Function<List<Object>, Refund>() {
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

    void loadRefunds(String brand) {
        subscribe(getRefundsByBrand(brand), false);
    }

    void loadRefunds(String brand, String fuelType) {
        subscribe(getRefundsByBrandAndFuelType(brand, fuelType), false);
    }

    void evaluateRefundValue(Refund refund, int km) {
        rxBus.send(new EvaluateResultEvent(refund.getKmCost() * km, refund.getKmCost()));
    }

    void hideEvaluationCard() {
        rxBus.send(new HideResultEvent());
    }
}
