package belka.us.acirefund.brand;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import belka.us.acirefund.base.presenter.BaseRxGoogleServiceLcePresenter;
import belka.us.acirefund.model.Brand;
import belka.us.acirefund.model.BrandManager;
import belka.us.acirefund.model.event.EvaluateResultEvent;
import belka.us.acirefund.model.event.HideResultEvent;
import belka.us.acirefund.rxbus.RxBus;
import belka.us.acirefund.utils.Config;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

import static belka.us.acirefund.utils.ConversionUtils.*;
import static belka.us.acirefund.utils.ConversionUtils.removeDuplicate;

/**
 * Created by fabriziorizzonelli on 28/09/2016.
 */

public class BrandPresenter extends BaseRxGoogleServiceLcePresenter<BrandView, List<Brand>> {

    private BrandManager brandManager;
    private Sheets sheetsService;
    private RxBus rxBus;

    @Inject
    public BrandPresenter(BrandManager brandManager, RxBus rxBus) {
        this.brandManager = brandManager;
        this.rxBus = rxBus;

        this.rxBus.toObserverable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object event) {
                if (event instanceof EvaluateResultEvent) {
                    EvaluateResultEvent evaluateResultEvent = (EvaluateResultEvent) event;
                    showEvalutationCard(evaluateResultEvent.getEvaluation(), evaluateResultEvent.getKmCost());
                } else if (event instanceof HideResultEvent) {
                    hideEvaluationCard();
                }
            }
        });
    }

    public void setSheetsService(Sheets sheetsService) {
        this.sheetsService = sheetsService;
    }

    private Observable<List<Brand>> getDistinctBrand() {
        return Observable.defer(new Func0<Observable<List<Brand>>>() {
            @Override
            public Observable<List<Brand>> call() {
                try {
                    BatchGetValuesResponse response = sheetsService.spreadsheets().values().batchGet(Config.SHEET_ID).setRanges(Config.getBrandRanges()).execute();

                    List<Brand> brands = Ordering.natural().onResultOf(new Function<Brand, String>() {
                        @Nullable
                        @Override
                        public String apply(@Nullable Brand input) {
                            return input.getName();
                        }
                    }).immutableSortedCopy(removeDuplicate(Lists.newArrayList(Iterables.transform(convertBatchGetToList(response), new Function<List<Object>, Brand>() {
                        @Nullable
                        @Override
                        public Brand apply(@Nullable List<Object> input) {
                            return new Brand(input.get(0).toString());
                        }
                    }))));

                    return Observable.just(brands);
                } catch (IOException e) {
                    e.printStackTrace();
                    if (e instanceof UserRecoverableAuthIOException && isViewAttached()) {
                        getView().showRequestAuthorizationModal((UserRecoverableAuthIOException) e);
                    }
                    return Observable.error(e);
                }
            }
        });
    }

    void loadBrands() {
        subscribe(getDistinctBrand(), false);
    }

    void showRefunds(String brandName) {
        if (isViewAttached())
            getView().showRefunds(brandName);
    }

    void showRefunds(String brandName, String fuelType) {
        if (isViewAttached())
            getView().showRefunds(brandName, fuelType);
    }

    void showEvalutationCard(double evaluation, double kmCost) {
        if (isViewAttached())
            getView().showEvaluationCard(evaluation, kmCost);
    }

    void hideEvaluationCard() {
        if (isViewAttached())
            getView().hideEvaluationCard();
    }
}
