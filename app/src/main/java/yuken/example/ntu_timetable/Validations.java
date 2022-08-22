package yuken.example.ntu_timetable;

import android.util.Patterns;
import android.widget.EditText;

public class Validations {
    Boolean valid;

    public boolean isValidEmail(EditText textField){

        if(!textField.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(textField.getText()).matches()){
            valid = true;
        }else {
            textField.setError("Enter valid Email address !");
            valid = false;
        }

        return valid;
    }

}
