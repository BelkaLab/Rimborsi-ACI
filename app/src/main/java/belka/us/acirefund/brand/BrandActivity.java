package belka.us.acirefund.brand;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import belka.us.acirefund.R;
import belka.us.acirefund.RefundApplication;
import belka.us.acirefund.base.view.BaseGooglePermissionLceActivity;
import belka.us.acirefund.dagger.GoogleFactory;
import belka.us.acirefund.model.Brand;
import belka.us.acirefund.refund.RefundFragmentBuilder;
import belka.us.acirefund.terms.InfoActivity;
import belka.us.acirefund.utils.Config;
import butterknife.BindView;
import io.fabric.sdk.android.Fabric;

/**
 * Created by fabriziorizzonelli on 28/09/2016.
 */

public class BrandActivity extends BaseGooglePermissionLceActivity<RelativeLayout, List<Brand>, BrandView, BrandPresenter> implements BrandView {

    private BrandComponent brandComponent;
    private Brand mCurrentBrand;
    private String mFuelType;
    @BindView(R.id.brand_spinner)
    Spinner brandSpinner;
    @BindView(R.id.fuel_type_spinner)
    Spinner fuelTypeSpinner;
    @BindView(R.id.ad_view)
    AdView adView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refund_value_card_view)
    CardView refundCardView;
    @BindView(R.id.km_refund_value_text_view)
    TextView kmRefundValueTextView;
    @BindView(R.id.total_refund_value_text_view)
    TextView totalRefundValueTextView;

    @Override
    protected void validateBeforeLoad(boolean runLoad) {
        presenter.setSheetsService(GoogleFactory.createSheetsService(credential));
        if (runLoad)
            loadData(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_brand);

        setSupportActionBar(toolbar);

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_app_id));
        adView.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("D67D7412F147837CCA85B02A64BA209E").addTestDevice("AC58A582683C68755657FFD5BDEBE28A").build());

        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentBrand = position == -1 ? null : (Brand) parent.getItemAtPosition(position);
                checkAndShowRefunds();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> fuelAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getResources().getStringArray(R.array.fuelTypeArray));
        fuelAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        fuelTypeSpinner.setAdapter(fuelAdapter);
        fuelTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFuelType = position == -1 ?  null :  parent.getItemAtPosition(position).toString();
                checkAndShowRefunds();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (isGoogleAccountValid())
            loadData(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.brand, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.belka_credit_menu_item:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.BELKA_URL));
                startActivity(browserIntent);
                return true;
            case R.id.feedback_menu_item:
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("text/plain");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"hello@belka.us" });
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Rimborsi ACI - Feedback");
                startActivity(mailIntent);
                return true;
            case R.id.terms_of_use_menu_item:
                Intent termsOfUseIntent = new Intent(this, InfoActivity.class);
                startActivity(termsOfUseIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showRefunds(String brandName) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.refund_fragment_container, new RefundFragmentBuilder(brandName).build())
                .commit();
    }

    @Override
    public void showRefunds(String brandName, String fuelType) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.refund_fragment_container, new RefundFragmentBuilder(brandName).fuelType(fuelType).build())
                .commit();
    }

    @Override
    public void showEvaluationCard(double evaluation, double kmCost) {
        kmRefundValueTextView.setText(String.format(getResources().getString(R.string.labels_float_value_4d), kmCost));
        totalRefundValueTextView.setText(String.format(getResources().getString(R.string.labels_float_value_2d), evaluation));
        refundCardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEvaluationCard() {
        refundCardView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return getString(R.string.messages_error_has_occured);
    }

    @NonNull
    @Override
    public BrandPresenter createPresenter() {
        return brandComponent.presenter();
    }

    @Override
    public void setData(List<Brand> data) {
        brandSpinner.setAdapter(new BrandAdapter(this, data));
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        hideEvaluationCard();
        presenter.loadBrands();
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        brandComponent = DaggerBrandComponent.builder().appComponent(RefundApplication.getAppComponents()).build();
    }

    private void checkAndShowRefunds() {
        if (mCurrentBrand != null) {
            if (mFuelType != null)
                presenter.showRefunds(mCurrentBrand.getName(), mFuelType);
            else
                presenter.showRefunds(mCurrentBrand.getName());
        }
    }
}
