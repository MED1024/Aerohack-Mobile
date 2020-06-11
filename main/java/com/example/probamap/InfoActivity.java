package com.example.probamap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends Activity {
    public static String fioDB,emailDB,phoneDB,zoneDB,profDB;
    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal_info);
        dbHelper = new DbHelper(this);
    }

    public void saveInfo(View view) {
        View parent =(View)view.getParent();
        TextView fio=(TextView)findViewById(R.id.EditTextName);
        fioDB = String.valueOf(fio.getText());

        TextView email=(TextView)findViewById(R.id.EditTextEmail);
        emailDB = String.valueOf(email.getText());

        TextView phone=(TextView)findViewById(R.id.EditTextPhone);
        phoneDB = String.valueOf(phone.getText());

        TextView zone=(TextView)findViewById(R.id.InputZone);
        zoneDB = String.valueOf(zone.getText());

        Spinner prof = findViewById(R.id.SpinnerFeedbackType);
        profDB = prof.getSelectedItem().toString();

        Toast toast = Toast.makeText(getApplicationContext(),
                "Ваши данные успешно сохранены!", Toast.LENGTH_LONG);
        toast.show();

//        dbHelper.insertPersonal(fioDB,emailDB,phoneDB,zoneDB,phoneDB);
    }

}
