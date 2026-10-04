package lk.example.chalatchoco.navigation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import lk.example.chalatchoco.R;
import lk.example.chalatchoco.adapter.ProductAdapter;
import lk.example.chalatchoco.model.Product;
import lk.example.chalatchoco.model.SQLiteHelper;

public class HomeFragment extends Fragment {

    private ArrayList<Product> productArrayList;
    private ProductAdapter productAdapter;

    private RecyclerView recyclerView;
    private SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext(), "chalatchoco.db", null, 2);

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
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                        String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                        byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                        productArrayList.add(new Product(id, name, price, image));
                    } while (cursor.moveToNext());

                    cursor.close();
                } else {
                    Log.e("HomeFragment", "Cursor is null or empty");
                }

                sqLiteDatabase.close();

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         productAdapter = new ProductAdapter(productArrayList, requireContext());
                        recyclerView1.setAdapter(productAdapter);
                    }
                });
            }
        }).start();




         searchView = view.findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.filter(newText);
                return false;
            }
        });


        return view;



    }




}


