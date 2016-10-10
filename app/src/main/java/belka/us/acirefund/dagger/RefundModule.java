package belka.us.acirefund.dagger;

import javax.inject.Singleton;

import belka.us.acirefund.model.RefundManager;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fabriziorizzonelli on 28/09/2016.
 */

@Module
public class RefundModule {

    @Singleton @Provides public RefundManager refundManager() { return new RefundManager(); }
}
