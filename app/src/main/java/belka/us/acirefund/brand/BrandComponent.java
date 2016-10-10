package belka.us.acirefund.brand;

import javax.inject.Singleton;

import belka.us.acirefund.dagger.AppComponent;
import belka.us.acirefund.dagger.AppModule;
import belka.us.acirefund.dagger.BrandModule;
import dagger.Component;

/**
 * Created by fabriziorizzonelli on 28/09/2016.
 */

@Singleton
@Component(
        modules = {AppModule.class,
                BrandModule.class},
        dependencies = AppComponent.class
)
public interface BrandComponent {

    BrandPresenter presenter();
}
