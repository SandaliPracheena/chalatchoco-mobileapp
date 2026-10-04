package lk.example.chalatchoco;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lk.example.chalatchoco.model.SQLiteHelper;
import lk.example.chalatchoco.model.User;
import lk.example.chalatchoco.navigation.CartFragment;
import lk.example.chalatchoco.navigation.PaymentFragment;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class Product_InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        byte[] image = intent.getByteArrayExtra("image");

        // Log the retrieved data
        Log.d("Product_InfoActivity", "Received ID: " + id);
        Log.d("Product_InfoActivity", "Received Name: " + title);
        Log.d("Product_InfoActivity", "Received Price: " + price);

        // Verify the product ID
        if (isValidProductId(id)) {
            // Update UI elements
            TextView textViewTitle = findViewById(R.id.textViewTitle1);
            TextView textViewsubTitle = findViewById(R.id.textView28);
            TextView textViewPrice = findViewById(R.id.textView29);
            ImageView imageViewProduct = findViewById(R.id.imageView13);

            textViewTitle.setText(title);
            textViewsubTitle.setText(title);
            textViewPrice.setText(price);

            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewProduct.setImageBitmap(bitmap);
            }
        } else {
            Log.e("Product_InfoActivity", "Invalid product ID: " + id);
            // Handle the invalid product ID case (e.g., show an error message)
            Toast.makeText(Product_InfoActivity.this, "Invalid product ID: " + id, Toast.LENGTH_SHORT).show();
        }

        Spinner spinner1 = findViewById(R.id.spinner1);
        ArrayList<String> list = new ArrayList<>();
        list.add("Select....");
        list.add("10 Pack");
        list.add("20 Pack");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                Product_InfoActivity.this,
                R.layout.spinner_item,
                list
        );




        spinner1.setAdapter(arrayAdapter);

        TextView textViewPrice = findViewById(R.id.textView29);
        int basePrice =  Integer.parseInt(price); // Example base price

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinner1.getSelectedItem().toString();
                if (selectedItem.equals("10 Pack")) {
                    textViewPrice.setText("Price: " + (basePrice * 10));
                } else if (selectedItem.equals("20 Pack")) {
                    textViewPrice.setText("Price: " + (basePrice * 20));
                } else {
                    textViewPrice.setText("Price: " + basePrice);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle case when nothing is selected, if necessary
            }
        });





        Button buttonCart = findViewById(R.id.button9);
        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("lk.example.chalatchoco.user", Context.MODE_PRIVATE);
                String userJson = sp.getString("user", null);
                Gson gson = new Gson();
                User user = gson.fromJson(userJson, User.class);
                String email = String.valueOf(user.getEmail());

                SQLiteHelper sqLiteHelper = new SQLiteHelper(
                        Product_InfoActivity.this,
                        "chalatchoco.db",
                        null,
                        2);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("product_id", id);
                        contentValues.put("name", title);
                        contentValues.put("user", email);
                        contentValues.put("price", price);
                        contentValues.put("image", image);

                        long result = sqLiteDatabase.insert("cart", null, contentValues);
                        if (result != -1) {
                            Log.i("chalatchoco.db", "Insert success");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Product_InfoActivity.this, "Added to cart successfully", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Log.e("chalatchoco.db", "Insert failed");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Product_InfoActivity.this, "Failed to add to cart", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

//buy button


        Button buttonBuy = findViewById(R.id.button7);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textname = findViewById(R.id.textView28);
                TextView textprice = findViewById(R.id.textView29);
                Spinner spinner1 = findViewById(R.id.spinner1);
                ImageView imageView = findViewById(R.id.imageView13);

                TextView textViewPrice = findViewById(R.id.textView29);
                String basePrice = "100"; // Example base price



                if (textname == null || textprice == null || spinner1 == null) {
                    Toast.makeText(Product_InfoActivity.this, "Error: Missing UI components", Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferences sp = getSharedPreferences("lk.example.chalatchoco.user", Context.MODE_PRIVATE);
                String userJson = sp.getString("user", null);
                if (userJson == null) {
                    Toast.makeText(Product_InfoActivity.this, "Error: User not found", Toast.LENGTH_LONG).show();
                    return;
                }

                Gson gson = new Gson();
                User user = gson.fromJson(userJson, User.class);
                if (user == null) {
                    Toast.makeText(Product_InfoActivity.this, "Error: Invalid user data", Toast.LENGTH_LONG).show();
                    return;
                }

                String email = user.getEmail();
                String mobile = user.getMobile();
                String selectedspinner = spinner1.getSelectedItem().toString();
                String titlename = textname.getText().toString();
                String price = textprice.getText().toString();



                if (email == null || mobile == null || selectedspinner.equals("Select....")) {
                    Toast.makeText(Product_InfoActivity.this, "Error: Incomplete data", Toast.LENGTH_LONG).show();
                    return;
                }


                //pay here


                InitRequest req = new InitRequest();
                req.setMerchantId("1221160");       // Merchant ID
                req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
                req.setAmount(1000.00);             // Final Amount to be charged
                req.setOrderId("230000123");        // Unique Reference ID
                req.setItemsDescription("Door bell wireless");  // Item description title
                req.setCustom1("This is the custom message 1");
                req.setCustom2("This is the custom message 2");
                req.getCustomer().setFirstName("Saman");
                req.getCustomer().setLastName("Perera");
                req.getCustomer().setEmail("samanp@gmail.com");
                req.getCustomer().setPhone("+94771234567");
                req.getCustomer().getAddress().setAddress("No.1, Galle Road");
                req.getCustomer().getAddress().setCity("Colombo");
                req.getCustomer().getAddress().setCountry("Sri Lanka");
                req.setMerchantSecret("Mjg4ODA3MTY4NTEzMTE0NTA4MjczMjg1MDUwNzAxOTE3OTg3OTY1");

////Optional Params
//                req.setNotifyUrl(“xxxx”);           // Notifiy Url
//                req.getCustomer().getDelivdress().setAddress("No.2, Kandy Road");
//                req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
//                req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
//                req.getItems().add(new Item(null, "Door bell wireless", 1, 1000.0));eryAd

                Intent intent = new Intent(Product_InfoActivity.this, PHMainActivity.class);
                intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
                PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
                startActivityForResult(intent, 1001); //unique request ID e.g. "11001"






                //payhere




                addOrderToDatabase(id, titlename, email, mobile, selectedspinner, price, image);
            }
        private void addOrderToDatabase(String id, String name, String email, String mobile, String type, String price, byte[] image) {
            SQLiteHelper sqLiteHelper = new SQLiteHelper(
                    Product_InfoActivity.this,
                    "chalatchoco.db",
                    null,
                    2);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("product_id", id);
                    contentValues.put("name", name);
                    contentValues.put("user", email);
                    contentValues.put("mobile", mobile);
                    contentValues.put("type", type);
                    contentValues.put("price", price);
                    contentValues.put("image", image);

                    try {
                        long resultBuy = sqLiteDatabase.insert("orders", null, contentValues);

                        Log.d("ContentValues", contentValues.toString());


                        runOnUiThread(() -> {
                            if (resultBuy != -1) {
                                Log.i("chalatchoco.db", "Insert success");
                                Toast.makeText(Product_InfoActivity.this, "Added to order successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("chalatchoco.db", "Insert failed: Table orders not found or constraints issue");
                                Toast.makeText(Product_InfoActivity.this, "Failed to add to orders", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e) {
                        Log.e("chalatchoco.db", "Insert error: " + e.getMessage());
                        runOnUiThread(() -> Toast.makeText(Product_InfoActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    }
                }
            }).start();
        }








        });


    }



    // Method to check if the product ID is valid
    private boolean isValidProductId(String id) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this, "chalatchoco.db", null, 1);
        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                "product",
                new String[]{"id"},
                "id=?",
                new String[]{id},
                null,
                null,
                null
        );

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        sqLiteDatabase.close();

        return isValid;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
  //      if (requestCode == 1001 && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
//            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
//            if (resultCode == Activity.RESULT_OK) {
//                Intent i = new Intent(Product_InfoActivity.this, PaymentFragment.class);
//                startActivity(i);
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Log.i("chalatapp", "Payment was canceled");
//            }
  //      } else {
       //     Log.e("onActivityResult", "Request code or data is invalid");
  //      }
    }
}