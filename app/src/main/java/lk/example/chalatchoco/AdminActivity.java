package lk.example.chalatchoco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View admin = findViewById(R.id.main);

        Button buttonAdmin = findViewById(R.id.button5);
        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editTextEmail = findViewById(R.id.editTextAdmin);
                String adminEmail = String.valueOf(editTextEmail.getText());

                if(adminEmail.isEmpty()){

                    Snackbar.make(admin,"Please enter the Email",Snackbar.LENGTH_LONG).show();


                } else if (adminEmail.equals("s@gmail.com")) {
                    Snackbar.make(admin,"success",Snackbar.LENGTH_LONG).show();

                    Intent adminLog = new Intent(AdminActivity.this, Admin_Log_Activity.class);
                    startActivity(adminLog);



                }else{
                    Snackbar.make(admin,"Unknown Email",Snackbar.LENGTH_LONG).show();
                    editTextEmail.setText("");


                }

            }
        });
    }
}