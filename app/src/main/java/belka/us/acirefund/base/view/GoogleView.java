package belka.us.acirefund.base.view;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

/**
 * Created by fabriziorizzonelli on 03/10/2016.
 */

public interface GoogleView {
    void showRequestAuthorizationModal(UserRecoverableAuthIOException e);
}
