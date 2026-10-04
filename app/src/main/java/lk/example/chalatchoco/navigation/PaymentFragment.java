package lk.example.chalatchoco.navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import lk.example.chalatchoco.R;
import lk.example.chalatchoco.adapter.CartAdapter;
import lk.example.chalatchoco.adapter.OrderAdapter;
import lk.example.chalatchoco.model.Cart;
import lk.example.chalatchoco.model.Order;
import lk.example.chalatchoco.model.SQLiteHelper;
import lk.example.chalatchoco.model.User;


public class PaymentFragment extends Fragment {

    private RecyclerView recyclerView1;


    private ArrayList<Order> orderArrayList;
    private OrderAdapter orderAdapter;
    private SQLiteHelper sqLiteHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);


        recyclerView1 = view.findViewById(R.id.recyclePayment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager);

        SharedPreferences sp = getContext().getSharedPreferences("lk.example.chalatchoco.user", Context.MODE_PRIVATE);
        String userJson = sp.getString("user", null);

        if (userJson != null) {
            Gson gson = new Gson();
            User user = gson.fromJson(userJson, User.class);
            String email = user.getEmail();

            sqLiteHelper = new SQLiteHelper(getContext(), "chalatchoco.db", null, 2);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();
                    Cursor cursor = sqLiteDatabase.query(
                            "orders",
                            null,
                            "user = ?", // where clause
                            new String[]{email}, // where args
                            null,
                            null,
                            "`id` DESC"
                    );

                    orderArrayList = new ArrayList<>();
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            String product_id = cursor.getString(cursor.getColumnIndexOrThrow("product_id"));
                           // String user = cursor.getString(cursor.getColumnIndexOrThrow("user"));
                            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                            orderArrayList.add(new Order(product_id, name,type, price, image));
                        } while (cursor.moveToNext());
                        Log.d("ContentValues", cursor.toString());

                        cursor.close();
                    } else {
                        Log.e("OrderFragment", "Cursor is null or empty");
                    }

                    sqLiteDatabase.close();

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            orderAdapter = new OrderAdapter(orderArrayList, requireContext());
                            recyclerView1.setAdapter(orderAdapter);
                        }
                    });
                }
            }).start();
        } else {
            Log.e("orderFragment", "No user data found in SharedPreferences");
        }

        return view;
        }
    }