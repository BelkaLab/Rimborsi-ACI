package belka.us.acirefund.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by fabriziorizzonelli on 30/09/2016.
 */

public class KeyboardUtils {

    public static boolean showKeyboard(View view) {
        if (view == null) {
            throw new NullPointerException("View is null!");
        }

        try {
            InputMethodManager imm =
                    (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm == null) {
                return false;
            }

            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean hideKeyboard(View view) {
        if (view == null) {
            throw new NullPointerException("View is null!");
        }

        try {
            InputMethodManager imm =
                    (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm == null) {
                return false;
            }

            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
