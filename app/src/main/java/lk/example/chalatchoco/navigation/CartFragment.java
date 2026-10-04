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
import lk.example.chalatchoco.adapter.ProductAdapter;
import lk.example.chalatchoco.model.Cart;
import lk.example.chalatchoco.model.Product;
import lk.example.chalatchoco.model.SQLiteHelper;
import lk.example.chalatchoco.model.User;


public class CartFragment extends Fragment {

    private RecyclerView recyclerView1;
    private ArrayList<Cart> cartArrayList;
    private CartAdapter cartAdapter;
    private SQLiteHelper sqLiteHelper;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cart, container, false);


        recyclerView1 = view.findViewById(R.id.recycleCart);
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
                            "cart",
                            null,
                            "user = ?", // where clause
                            new String[]{email}, // where args
                            null,
                            null,
                            "`id` DESC"
                    );

                    cartArrayList = new ArrayList<>();
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            String product_id = cursor.getString(cursor.getColumnIndexOrThrow("product_id"));
                            String user = cursor.getString(cursor.getColumnIndexOrThrow("user"));
                            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                            cartArrayList.add(new Cart(product_id, user, name, price, image));
                        } while (cursor.moveToNext());

                        cursor.close();
                    } else {
                        Log.e("CartFragment", "Cursor is null or empty");
                    }

                    sqLiteDatabase.close();

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cartAdapter = new CartAdapter(cartArrayList, requireContext());
                            recyclerView1.setAdapter(cartAdapter);
                        }
                    });
                }
            }).start();
        } else {
            Log.e("CartFragment", "No user data found in SharedPreferences");
        }


        return view;
    }
}