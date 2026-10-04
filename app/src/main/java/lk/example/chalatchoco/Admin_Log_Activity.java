package lk.example.chalatchoco;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import lk.example.chalatchoco.adapter.AdminProductAdapter;
import lk.example.chalatchoco.adapter.OrderAdapter;
import lk.example.chalatchoco.adapter.ProductAdapter;
import lk.example.chalatchoco.model.Order;
import lk.example.chalatchoco.model.Product;
import lk.example.chalatchoco.model.SQLiteHelper;

public class Admin_Log_Activity extends AppCompatActivity {

    ImageView imageView;

    SQLiteHelper sqLiteHelper;

    private ArrayList<Product> productArrayList;

    private AdminProductAdapter productAdapter;

    private ArrayList<Order> orderArrayList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_log);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//pie chart



        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            PieChart pieChart1 = findViewById(R.id.pieChart1);

                            ArrayList<PieEntry> pieChartArrayList = new ArrayList<>();

                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                                String name = String.valueOf(documentSnapshot.get("name"));
                                String value = String.valueOf(documentSnapshot.get("quantity"));

                                pieChartArrayList.add(new PieEntry(Float.parseFloat(value),name));



                            }


                            PieDataSet pieDataSet = new PieDataSet(pieChartArrayList,"orders");

                            ArrayList<Integer> colors = new ArrayList<>();

                            for(int i = 0;i <pieChartArrayList.size();i++){

                                Random random = new Random();
                                int color = Color.rgb(
                                        random.nextInt(256),
                                        random.nextInt(256),
                                        random.nextInt(256)


                                );

                                colors.add(color);


                            }

                            pieDataSet.setColors(colors);

                            PieData pieData = new PieData();
                            pieData.setDataSet(pieDataSet);
                            pieData.setValueTextSize(15);

                            pieChart1.setDescription(null);
                            pieChart1.setCenterText("Choco");
                            pieChart1.setCenterTextSize(20);
                            pieChart1.setCenterTextColor(getColor(R.color.white));
                            pieChart1.animateY(2000, Easing.EaseInCirc);
                            pieChart1.setHoleColor(getColor(R.color.toolbar));
                            pieChart1.setData(pieData);
                            pieChart1.invalidate();



                        }else{

                            Log.e("App24","Task not complete");

                        }


                    }
                });


        Button buttonRefresh = findViewById(R.id.button3);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore.collection("orders")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){
                                    PieChart pieChart1 = findViewById(R.id.pieChart1);

                                    ArrayList<PieEntry> pieChartArrayList = new ArrayList<>();

                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                                        String name = String.valueOf(documentSnapshot.get("name"));
                                        String value = String.valueOf(documentSnapshot.get("quantity"));

                                        pieChartArrayList.add(new PieEntry(Float.parseFloat(value),name));



                                    }


                                    PieDataSet pieDataSet = new PieDataSet(pieChartArrayList,"orders");

                                    ArrayList<Integer> colors = new ArrayList<>();

                                    for(int i = 0;i <pieChartArrayList.size();i++){

                                        Random random = new Random();
                                        int color = Color.rgb(
                                                random.nextInt(256),
                                                random.nextInt(256),
                                                random.nextInt(256)


                                        );

                                        colors.add(color);


                                    }

                                    pieDataSet.setColors(colors);

                                    PieData pieData = new PieData();
                                    pieData.setDataSet(pieDataSet);
                                    pieData.setValueTextSize(15);

                                    pieChart1.setDescription(null);
                                    pieChart1.setCenterText("Choco");
                                    pieChart1.setCenterTextSize(20);
                                    pieChart1.setCenterTextColor(getColor(R.color.white));
                                    pieChart1.animateY(2000, Easing.EaseInCirc);
                                    pieChart1.setHoleColor(getColor(R.color.toolbar));
                                    pieChart1.setData(pieData);
                                    pieChart1.invalidate();



                                }else{

                                    Log.e("App24","Task not complete");

                                }


                            }
                        });



            }
        });


         sqLiteHelper = new SQLiteHelper(
                Admin_Log_Activity.this,
                "chalatchoco.db",
                null,
                2
        );

        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);

                        // Convert the selected image to Bitmap
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                            // Set the ImageView with the selected image
                            imageView.setImageBitmap(bitmap);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });


        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                LayoutInflater inflater = LayoutInflater.from(Admin_Log_Activity.this);
                View view1 = inflater.inflate(R.layout.add_product_alert,null,false);

             AlertDialog alertDialog =    new AlertDialog.Builder(Admin_Log_Activity.this).setView(view1).show();

                imageView = view1.findViewById(R.id.productImage);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       pickMedia.launch(new PickVisualMediaRequest.Builder()
                               .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                               .build());



                        // Include only one of the following calls to launch(), depending on the types
// of media that you want to let the user choose from.

// Launch the photo picker and let the user choose images and videos.
                        pickMedia.launch(new PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                                .build());

// Launch the photo picker and let the user choose only images.
                        pickMedia.launch(new PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                                .build());

// Launch the photo picker and let the user choose only videos.
                        pickMedia.launch(new PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
                                .build());

// Launch the photo picker and let the user choose only images/videos of a
// specific MIME type, such as GIFs.
                        String mimeType = "image/gif";
                        pickMedia.launch(new PickVisualMediaRequest.Builder()
                                .setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType))
                                .setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType))
                                .build());


                    }
                });








                Button buttonAddProduct = view1.findViewById(R.id.buttonAddProduct);
                buttonAddProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {





                        EditText editTextName = view1.findViewById(R.id.editTextName);
                        EditText editTextPrice = view1.findViewById(R.id.editTextPrice);

                        String name = String.valueOf(editTextName.getText());
                        String price = String.valueOf(editTextPrice.getText());

                        // Assume imageBitmap is the Bitmap of the selected image

                        Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        byte[] imageBytes = imageToByteArray(imageBitmap);

                        if(name.isEmpty()){
                            Toast.makeText(Admin_Log_Activity.this,"Please enter the product name",Toast.LENGTH_LONG).show();
                        }else if(price.isEmpty()){
                            Toast.makeText(Admin_Log_Activity.this,"Please enter the product price",Toast.LENGTH_LONG).show();

                        //}else if(imageBitmap ==null){
                          //  Toast.makeText(Admin_Log_Activity.this,"Please Select a Image",Toast.LENGTH_LONG).show();

                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("name", name);
                                    contentValues.put("price", price);
                                    contentValues.put("image", imageBytes);

//                                    sqLiteDatabase.insert("product", null, contentValues);
//                                    Log.i("chalatchoco.db","success");

                                    long newRowId = sqLiteDatabase.insert("product", null, contentValues);
                                    Log.i("chalatchoco.db", "success, new row id: " + newRowId);

                                    // Create new product
                                    Product newProduct = new Product(String.valueOf(newRowId), name, price, imageBytes);

                                    // Add new product to the data set
                                    productArrayList.add(newProduct);




                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            editTextName.setText("");
                                            editTextPrice.setText("");
                                            imageView.setImageResource(R.drawable.image);

                                            productAdapter.notifyItemInserted(productArrayList.size() - 1);

                                        }
                                    });

                                }
                            }).start();


                        }


                    }

                });

                Button buttonCancel = view1.findViewById(R.id.buttonCancelAlert);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();

                    }
                });









            }
        });




        RecyclerView recyclerView1 = findViewById(R.id.recycalView1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(Admin_Log_Activity.this, "chalatchoco.db", null, 2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.query(
                        "product",
                        null,
                        null,
                        null,
                        null,
                        null,
                        "`id` DESC"
                );

                productArrayList = new ArrayList<>();
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                            productArrayList.add(new Product(id, name, price, image));


                        } while (cursor.moveToNext());


                    }
                    cursor.close();
                } else {
                    Log.e("Admin", "Cursor is null or empty");
                }

                sqLiteDatabase.close();




                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         productAdapter = new AdminProductAdapter(productArrayList, Admin_Log_Activity.this);
                        recyclerView1.setAdapter(productAdapter);
                    }
                });
            }
        }).start();




    }


    // Method to convert image to byte array
    public byte[] imageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    // Method to convert byte array to image
    public Bitmap byteArrayToImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}