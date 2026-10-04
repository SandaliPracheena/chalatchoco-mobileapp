package lk.example.chalatchoco;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

import lk.example.chalatchoco.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ProgressBar progressBar1 = findViewById(R.id.progressBar1);
        progressBar1.setVisibility(View.GONE);


        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar1.setVisibility(View.VISIBLE);

                EditText editText1 = findViewById(R.id.editText1);
                EditText editText2 = findViewById(R.id.editText2);
                EditText editText3 = findViewById(R.id.editText3);
                EditText editText4 = findViewById(R.id.editText4);
                EditText editText5 = findViewById(R.id.passwordText5);


                String firstName = String.valueOf(editText1.getText());
                String lastName= String.valueOf(editText2.getText());
                String mobile = String.valueOf(editText3.getText());
                String  email= String.valueOf(editText4.getText());
                String password = String.valueOf(editText5.getText());

                View signUpView = findViewById(R.id.main);
                TextInputLayout textInputLayout1 = findViewById(R.id.textInputLayout1);
                TextInputLayout textInputLayout2 = findViewById(R.id.textInputLayout3);
                TextInputLayout textInputLayout3 = findViewById(R.id.textInputLayout2);
                TextInputLayout textInputLayout4 = findViewById(R.id.textInputLayout6);
                TextInputLayout textInputLayout5 = findViewById(R.id.textInputLayout5);

                if(firstName.isEmpty()){

                    Snackbar.make(signUpView,"Please enter the First Name",Snackbar.LENGTH_LONG).show();

                    SpringAnimation springAnimation = new SpringAnimation(textInputLayout1, DynamicAnimation.TRANSLATION_X);

                    SpringForce springForce = new SpringForce();
                    springForce.setStiffness(SpringForce.STIFFNESS_HIGH);
                    springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

                    springForce.setFinalPosition(20f);

                    springAnimation.setSpring(springForce);
                    springAnimation.start();

                    editText1.setError("Please fill");

                } else if (lastName.isEmpty()) {

                    Snackbar.make(signUpView,"Please enter the Last Name",Snackbar.LENGTH_LONG).show();


                    SpringAnimation springAnimation = new SpringAnimation(textInputLayout2, DynamicAnimation.TRANSLATION_X);

                    SpringForce springForce = new SpringForce();
                    springForce.setStiffness(SpringForce.STIFFNESS_HIGH);
                    springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

                    springForce.setFinalPosition(20f);

                    springAnimation.setSpring(springForce);
                    springAnimation.start();

                    editText2.setError("Please fill");

                }else if (mobile.isEmpty()) {
                    Snackbar.make(signUpView,"Please enter the Mobile Number",Snackbar.LENGTH_LONG).show();


                    SpringAnimation springAnimation = new SpringAnimation(textInputLayout3, DynamicAnimation.TRANSLATION_X);

                    SpringForce springForce = new SpringForce();
                    springForce.setStiffness(SpringForce.STIFFNESS_HIGH);
                    springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

                    springForce.setFinalPosition(20f);

                    springAnimation.setSpring(springForce);
                    springAnimation.start();

                    editText3.setError("Please fill");

                }else if (email.isEmpty()) {
                    Snackbar.make(signUpView,"Please enter the Email",Snackbar.LENGTH_LONG).show();


                    SpringAnimation springAnimation = new SpringAnimation(textInputLayout4, DynamicAnimation.TRANSLATION_X);

                    SpringForce springForce = new SpringForce();
                    springForce.setStiffness(SpringForce.STIFFNESS_HIGH);
                    springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

                    springForce.setFinalPosition(20f);

                    springAnimation.setSpring(springForce);
                    springAnimation.start();

                    editText4.setError("Please fill");

                }else if (password.isEmpty()) {
                    Snackbar.make(signUpView,"Please enter the Password",Snackbar.LENGTH_LONG).show();


                    SpringAnimation springAnimation = new SpringAnimation(textInputLayout5, DynamicAnimation.TRANSLATION_X);

                    SpringForce springForce = new SpringForce();
                    springForce.setStiffness(SpringForce.STIFFNESS_HIGH);
                    springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

                    springForce.setFinalPosition(20f);

                    springAnimation.setSpring(springForce);
                    springAnimation.start();

                    editText5.setError("Please fill");

                }else{

                    FirebaseFirestore firebase = FirebaseFirestore.getInstance();

                    User user = new User();

                    HashMap<String, Object> document = new HashMap<>();
                    document.put("firstName",firstName);
                    document.put("lastName",lastName);
                    document.put("mobile",mobile);
                    document.put("email",email);
                    document.put("password",password);

                    firebase.collection("user_signup").add(document)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Snackbar.make(signUpView,"Success",Snackbar.LENGTH_LONG).show();

                                            Intent  s = new Intent(MainActivity.this, SignInActivity.class);
                                            s.putExtra("firstName",firstName);
                                            startActivity(s);

                                        }
                                    })

                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Snackbar.make(signUpView,"Fail",Snackbar.LENGTH_LONG).show();

                                                }
                                            });








                    




                }


            }


        });

        TextView textView1 = findViewById(R.id.textView2);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  i = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(i);

            }
        });

        TextView textViewAdmin = findViewById(R.id.textView11);
        textViewAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m = new Intent(MainActivity.this,AdminActivity.class);
                startActivity(m);
            }
        });




    }
}