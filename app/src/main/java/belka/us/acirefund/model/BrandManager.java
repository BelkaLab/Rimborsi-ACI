package belka.us.acirefund.model;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by fabriziorizzonelli on 27/09/2016.
 */

public class BrandManager {

    public Observable<List<Brand>> getBrands() {
        //TODO: Call retrofit
        List<Brand> brandList = new ArrayList<>();
        brandList.add(new Brand("FIAT"));
        brandList.add(new Brand("CITROEN"));
        brandList.add(new Brand("OPEL"));
        brandList.add(new Brand("SUBARU"));
        return Observable.just(brandList);
    }
}
