package belka.us.acirefund.brand;

import com.google.api.services.sheets.v4.Sheets;

import java.util.List;

import javax.inject.Inject;

import belka.us.acirefund.base.presenter.BaseRxLcePresenter;
import belka.us.acirefund.model.Brand;
import belka.us.acirefund.model.BrandManager;
import belka.us.acirefund.model.event.EvaluateResultEvent;
import belka.us.acirefund.model.event.HideResultEvent;
import belka.us.acirefund.rxbus.RxBus;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by fabriziorizzonelli on 28/09/2016.
 */

public class BrandPresenter extends BaseRxLcePresenter<BrandView, List<Brand>> {

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

    private Observable<List<Brand>> getDistinctBrand() {
        return brandManager.getDistinctBrands();
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
