package yuken.example.ntu_timetable;

import android.util.Patterns;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class Validations {
    Boolean isEmpty ;
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
    public boolean isEmptyGoogle(TextInputEditText textField){

        if(!textField.getText().toString().isEmpty()){
            valid = true;
        }else {
            textField.setError("Please fill all details!!");
            valid = false;
        }
        return valid;
    }
    public boolean isValidEmail(EditText textField){

        if(!textField.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(textField.getText()).matches()){
            valid = true;
        }else {
            textField.setError("Enter valid Email address !");
            valid = false;
        }

        return valid;
    }
    public boolean isEmptyAll(List<TextInputEditText> textField)
    {
        for (int i = 0; i <= textField.size(); i++)
        {
            if(!textField.get(i).getText().toString().isEmpty()){
                isEmpty = true;
            }else {
                textField.get(i).setError("Please fill all details!!");
                isEmpty = false;
            }
        }
        return isEmpty;
    }

}
