package belka.us.acirefund.refund;

import com.google.api.services.sheets.v4.Sheets;

import java.util.List;

import javax.inject.Inject;

import belka.us.acirefund.base.presenter.BaseRxLcePresenter;
import belka.us.acirefund.model.Refund;
import belka.us.acirefund.model.RefundManager;
import belka.us.acirefund.model.event.EvaluateResultEvent;
import belka.us.acirefund.model.event.HideResultEvent;
import belka.us.acirefund.rxbus.RxBus;
import belka.us.acirefund.utils.Config;
import rx.Observable;

/**
 * Created by fabriziorizzonelli on 27/09/2016.
 */

public class RefundPresenter extends BaseRxLcePresenter<RefundView, List<Refund>> {

    private RefundManager refundManager;
    private Sheets sheetsService;
    private RxBus rxBus;

    @Inject
    public RefundPresenter(RefundManager refundManager, RxBus rxBus) {
        this.refundManager = refundManager;
        this.rxBus = rxBus;
    }

    private Observable<List<Refund>> getRefundsByBrand(final String brand) {
        return refundManager.getRefundsByBrand(brand, Config.getRefundByBrandRanges());
    }

    private Observable<List<Refund>> getRefundsByBrandAndFuelType(final String brand, final String fuelType) {
        return refundManager.getRefundsByBrandAndFuelType(brand, fuelType);
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
