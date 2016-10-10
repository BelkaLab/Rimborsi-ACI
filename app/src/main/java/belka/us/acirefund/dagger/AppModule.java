package belka.us.acirefund.dagger;

import javax.inject.Singleton;

import belka.us.acirefund.rxbus.RxBus;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fabriziorizzonelli on 06/10/2016.
 */

@Module
public class AppModule {
    @Singleton @Provides public RxBus providesRxBus() { return RxBus.getDefault(); }
}
