package belka.us.acirefund.dagger;

import javax.inject.Singleton;

import belka.us.acirefund.model.BrandManager;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fabriziorizzonelli on 28/09/2016.
 */

@Module
public class BrandModule {
    @Singleton @Provides public BrandManager brandManager() { return new BrandManager(); }
    @Singleton @Provides public GoogleFactory googleFactory() { return new GoogleFactory(); }
}
