package sg.edu.rp.c346.mybmicalculator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    EditText etWeight;
    EditText etHeight;
    Button btnCalculate;
    Button btnReset;
    TextView tvDate;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.editTextWeight);
        etHeight = findViewById(R.id.editTextHeight);
        btnCalculate = findViewById(R.id.buttonCalculate);
        btnReset = findViewById(R.id.buttonReset);
        tvDate = findViewById(R.id.textViewDate);
        tvResult = findViewById(R.id.textViewResult);

       btnReset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               etWeight.setText("");
               etHeight.setText("");
               tvDate.setText("Last Calculated Date:");
               tvResult.setText("Last Calculated BMI: ");
               SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
               SharedPreferences.Editor preEdit = prefs.edit();
               preEdit.clear();
               preEdit.commit();
           }
       });

       btnCalculate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String str1 = etWeight.getText().toString();
               String str2 = etHeight.getText().toString();

               if (TextUtils.isEmpty(str1)){
                   etWeight.setError("Please enter your weight");
                   etWeight.requestFocus();
                   return;
               }

               if (TextUtils.isEmpty(str2)){
                   etHeight.setError("Please enter your height");
                   etHeight.requestFocus();
                   return;
               }

               float weight = Float.parseFloat(str1);
               float height = Float.parseFloat(str2);

               float bmiValue = calculateBMI(weight, height);
               String bmiInterpretation = interpretBMI(bmiValue);

               Calendar now = Calendar.getInstance();
               String datetime = now.get(Calendar.DAY_OF_MONTH) + "/" + (now.get(Calendar.YEAR) + "" + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));

               tvDate.setText("Last calculated date " + datetime);
               tvResult.setText("Last calculated BMI " + String.valueOf(bmiValue + "\n" + bmiInterpretation));
           }
       });

    }

    public float calculateBMI(float weight, float height){
        return (float)(weight/(height * height));
    }

    @Override
    protected void onPause() {
        super.onPause();

        float weight = Float.parseFloat(etWeight.getText().toString());
        float height = Float.parseFloat(etHeight.getText().toString());

        String strdate = tvDate.getText().toString();
        String strbmi = tvResult.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor prefEdit = prefs.edit();

        prefEdit.putFloat("Weight", weight);
        prefEdit.putFloat("Height", height);
        prefEdit.putString("date", strdate);
        prefEdit.putString("bmi", strbmi);

        prefEdit.commit();



    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String date = prefs.getString("date", "");
        String bmi = prefs.getString("bmi", "");
        float theWeight = prefs.getFloat("myWeight", 0);
        float theHeight = prefs.getFloat("myHeight", 0);

        etWeight.setText(Float.toString(theWeight));
        etHeight.setText(Float.toString(theHeight));
        tvDate.setText("Last Calculated date:" + date);
        tvResult.setText("Last Calculated BMI:" + bmi);
    }

    String interpretBMI(float bmiValue){
        if (bmiValue < 18.5){
            return "You are underweight";
        } else if (bmiValue < 24.9){
            return "You are normal";
        } else if (bmiValue < 29.9){
            return "You are overweight";
        } else{
            return "Obese";
        }
    }


}
