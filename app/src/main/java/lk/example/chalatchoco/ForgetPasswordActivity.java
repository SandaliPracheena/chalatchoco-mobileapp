package lk.example.chalatchoco;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.List;

import lk.example.chalatchoco.model.User;

public class ForgetPasswordActivity extends AppCompatActivity {

    private int otp = (int) (Math.random() * 900000) + 100000;

    private String otpMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         otpMessage = String.valueOf(otp);
        Button buttonsendPin = findViewById(R.id.buttonsendpin);
        buttonsendPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                NotificationManager notificationManager = getSystemService(NotificationManager.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(
                            "C1",
                            "Channel1",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                // API 26+
                Intent intent = new Intent(ForgetPasswordActivity.this, ForgetPasswordActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        ForgetPasswordActivity.this,
                        1,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                );

                NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                        R.drawable.info,
                        "View",
                        pendingIntent
                ).build();

                Notification notification = new NotificationCompat.Builder(ForgetPasswordActivity.this, "C1")
                        .setContentTitle("Chalat Choco")
                        .setContentText(otpMessage)
                        .setSmallIcon(R.drawable.info)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.choco))
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setPriority(Notification.PRIORITY_DEFAULT)
                        .addAction(action)
                        .build();

                notificationManager.notify(1, notification);



            }
        });



        Button buttonSend = findViewById(R.id.buttonotpSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextPin = findViewById(R.id.editPin);
                String pin = String.valueOf(editTextPin.getText());

                if (pin.equals(otpMessage)) {
                    fetchUserDetails();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                    Log.i("forgetpassword","Worngdetails");
                }
            }
        });





    }


    private void fetchUserDetails() {
        Intent fi = getIntent();
        String email = fi.getStringExtra("email");

        Log.d("Debug", "Retrieved Email: " + email);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("user_signup")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                            for (DocumentSnapshot document : documentSnapshotList) {
                                Log.i("Choco", "user" + document.getString("firstName"));

                                String fname = document.getString("firstName");
                                String lname = document.getString("lastName");
                                String mobile = document.getString("mobile");

                                User user = new User();
                                user.setFirstName(fname);
                                user.setLastName(lname);
                                user.setMobile(mobile);
                                user.setEmail(email);

                                Gson gson = new Gson();
                                String userJson = gson.toJson(user);

                                SharedPreferences sp = getSharedPreferences("lk.example.chalatchoco.user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("user", userJson);
                                editor.apply();


                                    Intent i = new Intent(ForgetPasswordActivity.this, HomeActivity.class);
                                    i.putExtra("firstName", fname);
                                    i.putExtra("lastName", lname);
                                    i.putExtra("mobile", mobile);
                                    i.putExtra("email", email);
                                    startActivity(i);

                                      Log.i("forgetpassword","success");


                            }
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, "Failed to fetch user details", Toast.LENGTH_LONG).show();
                            Log.i("forgetpassword","Failed to fetch user details");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgetPasswordActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i("forgetpassword","Error: " + e.getMessage());
                    }
                });
    }


}