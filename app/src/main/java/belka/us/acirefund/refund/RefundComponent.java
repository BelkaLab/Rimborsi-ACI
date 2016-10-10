package belka.us.acirefund.refund;

import javax.inject.Singleton;

import belka.us.acirefund.dagger.AppComponent;
import belka.us.acirefund.dagger.AppModule;
import belka.us.acirefund.dagger.RefundModule;
import belka.us.acirefund.rxbus.RxBus;
import dagger.Component;

/**
 * Created by fabriziorizzonelli on 28/09/2016.
 */

@Singleton
@Component(
        modules = {AppModule.class, RefundModule.class },
        dependencies = AppComponent.class
)
public interface RefundComponent {
    RefundPresenter presenter();
}
