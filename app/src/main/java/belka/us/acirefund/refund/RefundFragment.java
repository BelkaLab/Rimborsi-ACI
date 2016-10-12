package belka.us.acirefund.refund;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import java.util.List;

import belka.us.acirefund.R;
import belka.us.acirefund.RefundApplication;
import belka.us.acirefund.base.exception.PermissionNotGrantedException;
import belka.us.acirefund.base.view.BaseLceFragment;
import belka.us.acirefund.model.Refund;
import butterknife.BindView;

import static belka.us.acirefund.utils.KeyboardUtils.hideKeyboard;
import static belka.us.acirefund.utils.KeyboardUtils.showKeyboard;

/**
 * Created by fabriziorizzonelli on 27/09/2016.
 */

@FragmentWithArgs
public class RefundFragment extends BaseLceFragment<RelativeLayout, List<Refund>, RefundView, RefundPresenter> implements RefundView {

    @Arg
    String brand;
    @Arg(required = false)
    String fuelType;
    private RefundComponent mRefundComponent;
    private Refund mCurrentRefund;
    @BindView(R.id.model_spinner)
    Spinner modelSpinner;
    @BindView(R.id.evaluate_km_refund_button)
    Button evaluateButton;
    @BindView(R.id.kmcount_edit_text)
    EditText kmCountEditText;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentRefund = position == -1 ? null : (Refund) parent.getItemAtPosition(position);

                //Hide result
                presenter.hideEvaluationCard();

                if (mCurrentRefund != null) {
                    evaluateButton.setEnabled(kmCountEditText.getText().length() > 0);
                    kmCountEditText.setText("");
                    moveFocusAndShowKeyboard();
                } else {
                    evaluateButton.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        evaluateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.evaluateRefundValue(mCurrentRefund, Integer.parseInt(kmCountEditText.getText().toString()));
                removeFocusAndHideKeyboard(v);
            }
        });
        kmCountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                evaluateButton.setEnabled(mCurrentRefund != null && s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        kmCountEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) removeFocusAndHideKeyboard(v);
                return false;
            }
        });

        loadData(false);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_refund;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e instanceof PermissionNotGrantedException ? e.getMessage() : getString(R.string.messages_error_has_occured);
    }

    @NonNull
    @Override
    public RefundPresenter createPresenter() {
        return mRefundComponent.presenter();
    }

    @Override
    public void setData(List<Refund> data) {
        modelSpinner.setAdapter(new RefundAdapter(getContext(), data));
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        //Hide result
        presenter.hideEvaluationCard();

        if (fuelType != null) presenter.loadRefunds(brand, fuelType);
        else presenter.loadRefunds(brand);
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mRefundComponent = DaggerRefundComponent.builder().appComponent(RefundApplication.getAppComponents()).build();
    }

    private void moveFocusAndShowKeyboard() {
        kmCountEditText.requestFocus();
        showKeyboard(kmCountEditText);
    }

    private void removeFocusAndHideKeyboard(View view) {
        kmCountEditText.clearFocus();
        hideKeyboard(view);
    }
}
