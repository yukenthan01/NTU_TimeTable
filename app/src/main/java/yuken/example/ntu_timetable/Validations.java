package yuken.example.ntu_timetable;

import android.util.Patterns;
import android.widget.EditText;

public class Validations {
    Boolean valid;

    public boolean isEmpty(EditText textField){

        if(!textField.getText().toString().isEmpty()){
            valid = true;
        }else {
            textField.setError("Please fill all details!!");
            valid = false;
        }
        return valid;
    }

}
