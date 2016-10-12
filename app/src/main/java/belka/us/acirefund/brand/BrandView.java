package belka.us.acirefund.brand;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import belka.us.acirefund.base.view.GoogleView;
import belka.us.acirefund.model.Brand;

/**
 * Created by fabriziorizzonelli on 28/09/2016.
 */

public interface BrandView extends MvpLceView<List<Brand>> {

    void showRefunds(String brandName);

    void showRefunds(String brandName, String fuelType);

    void showEvaluationCard(double evaluation, double kmCost);

    void hideEvaluationCard();
}
