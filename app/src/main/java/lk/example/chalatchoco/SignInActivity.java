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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;


import java.util.List;

import lk.example.chalatchoco.model.User;

public class SignInActivity extends AppCompatActivity {

    private int otp = (int) (Math.random() * 900000) + 100000;
    String otpMessage = String.valueOf(otp);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View signInView = findViewById(R.id.main);


                TextInputLayout textInputLayout4 = findViewById(R.id.textInputLayout4);
                TextInputLayout textInputLayout5 = findViewById(R.id.textInputLayout5);

                TextView textViewEmail = findViewById(R.id.textEmail);
                TextView textViewPassword = findViewById(R.id.textPassword);

                String emailText = String.valueOf(textViewEmail.getText());
                String passwordText = String.valueOf(textViewPassword.getText());

                Intent s = getIntent();
                String firstName = s.getStringExtra("firstName");


                if (emailText.isEmpty()) {

                    Snackbar.make(signInView, "Please enter the Email", Snackbar.LENGTH_LONG).show();

                    SpringAnimation springAnimation = new SpringAnimation(textInputLayout4, DynamicAnimation.TRANSLATION_X);

                    SpringForce springForce = new SpringForce();
                    springForce.setStiffness(SpringForce.STIFFNESS_HIGH);
                    springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

                    springForce.setFinalPosition(20f);

                    springAnimation.setSpring(springForce);
                    springAnimation.start();

                    textViewEmail.setError("Please fill");

                } else if (passwordText.isEmpty()) {

                    Snackbar.make(signInView, "Please enter the Password", Snackbar.LENGTH_LONG).show();

                    SpringAnimation springAnimation = new SpringAnimation(textInputLayout5, DynamicAnimation.TRANSLATION_X);

                    SpringForce springForce = new SpringForce();
                    springForce.setStiffness(SpringForce.STIFFNESS_HIGH);
                    springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

                    springForce.setFinalPosition(20f);

                    springAnimation.setSpring(springForce);
                    springAnimation.start();

                    textViewPassword.setError("Please fill");

                } else {

//                    Intent i = new Intent(SignInActivity.this, HomeActivity.class);
//                    i.putExtra("email",email);
//                    i.putExtra("firstName",firstName);
//                    startActivity(i);


                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


                    firestore.collection("user_signup")

                            .whereEqualTo("email", emailText)
                            .whereEqualTo("password", passwordText)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                  //  progressBar2.setVisibility(View.VISIBLE);

                                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();

                                    if (documentSnapshotList.isEmpty()) {
                                        Toast.makeText(SignInActivity.this,"Invalid Email or Password",Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    for (DocumentSnapshot document : documentSnapshotList) {
                                        Log.i("Choco", "user" + String.valueOf(document.get("firstName")));

                                        String fname = String.valueOf(document.get("firstName"));
                                        String lname = String.valueOf(document.get("lastName"));
                                        String mobile = String.valueOf(document.get("mobile"));
                                        String password = String.valueOf(document.get("password"));
                                        String email = String.valueOf(document.get("email"));




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

                                            Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                                            i.putExtra("firstName", fname);
                                            i.putExtra("lastName", lname);
                                            i.putExtra("mobile", mobile);
                                            i.putExtra("email", email);
                                            startActivity(i);







                                    }


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Snackbar.make(signInView, "Invalid ", Snackbar.LENGTH_LONG).show();

                                }
                            });


                }


            }


        });


        TextView textView2 = findViewById(R.id.textView4);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        //forget password

        TextView forgotPassword = findViewById(R.id.textView5);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View signInView = findViewById(R.id.main);


                TextInputLayout textInputLayout4 = findViewById(R.id.textInputLayout4);

                TextView textViewEmail = findViewById(R.id.textEmail);
                String email = String.valueOf(textViewEmail.getText());

                if (email.isEmpty()) {
                    Snackbar.make(signInView, "Please enter the Email", Snackbar.LENGTH_LONG).show();

                    SpringAnimation springAnimation = new SpringAnimation(textInputLayout4, DynamicAnimation.TRANSLATION_X);

                    SpringForce springForce = new SpringForce();
                    springForce.setStiffness(SpringForce.STIFFNESS_HIGH);
                    springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

                    springForce.setFinalPosition(20f);

                    springAnimation.setSpring(springForce);
                    springAnimation.start();

                    textViewEmail.setError("Please fill");




                } else {


//                    notification



//                    Intent fi = new Intent(SignInActivity.this,ForgetPasswordActivity.class);
//
//                    fi.putExtra("email",email);
//                    startActivity(fi);

                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                    // Check if the email exists in the database
                    firestore.collection("user_signup")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                                        if (documents.isEmpty()) {
                                            // Email does not exist
                                            Snackbar.make(signInView, "Email does not exist", Snackbar.LENGTH_LONG).show();
                                        } else {
                                            // Email exists, proceed to ForgetPasswordActivity
                                            Intent fi = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
                                            fi.putExtra("email", email);
                                            startActivity(fi);
                                        }
                                    } else {

                                        Snackbar.make(signInView, "Error checking email", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Snackbar.make(signInView, "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });




                }


            }


        });

    }

}




