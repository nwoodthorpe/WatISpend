package nathaniel.watispend;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * Created by Nathaniel on 12/30/2015.
 */
// A simple class used in formatting password field
public class ClickableSpan extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return source;
    }
}
