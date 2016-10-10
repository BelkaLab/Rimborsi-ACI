package belka.us.acirefund.base.presenter;

import com.google.api.services.sheets.v4.Sheets;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import belka.us.acirefund.base.view.GoogleView;

/**
 * Created by fabriziorizzonelli on 03/10/2016.
 */

public abstract class BaseRxGoogleServiceLcePresenter<V extends MvpLceView<M> & GoogleView, M> extends BaseRxLcePresenter<V, M> {
    protected abstract void setSheetsService(Sheets sheetsService);
}
