package belka.us.acirefund;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import belka.us.acirefund.dagger.AppComponent;
import belka.us.acirefund.dagger.DaggerAppComponent;

/**
 * Created by fabriziorizzonelli on 06/10/2016.
 */

public class RefundApplication extends MultiDexApplication {

    private static AppComponent mainComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainComponent = DaggerAppComponent.create();
    }

    public static AppComponent getAppComponents() {
        return mainComponent;
    }
}
