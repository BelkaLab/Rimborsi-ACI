package belka.us.acirefund.refund;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import belka.us.acirefund.base.view.GoogleView;
import belka.us.acirefund.model.Refund;

/**
 * Created by fabriziorizzonelli on 27/09/2016.
 */

public interface RefundView extends MvpLceView<List<Refund>>, GoogleView {

}
