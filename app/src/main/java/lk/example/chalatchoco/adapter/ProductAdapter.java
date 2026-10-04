package lk.example.chalatchoco.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import lk.example.chalatchoco.Product_InfoActivity;
import lk.example.chalatchoco.R;
import lk.example.chalatchoco.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> productArrayList;
    private Context mContext;

    private ArrayList<Product> productArrayListFull;


    public ProductAdapter(ArrayList<Product> productArrayList, Context context) {
        this.productArrayList = productArrayList;
        this.productArrayListFull = new ArrayList<>(productArrayList);
        this.mContext = context;

    }



    public void filter(String text) {
        Log.d("ProductAdapter", "Filter text: " + text);
        productArrayList.clear();

        if (text.isEmpty()) {
            productArrayList.addAll(productArrayListFull);
        } else {
            text = text.toLowerCase();
            for (Product product : productArrayListFull) {
                if (product.getName().toLowerCase().contains(text)) {
                    productArrayList.add(product);
                }
            }
        }

        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflatedView = layoutInflater.inflate(R.layout.activity_chocolate_item, parent, false);
        return new ProductViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice());

        if (product.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
            holder.imageView.setImageBitmap(bitmap);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] compressedImage = baos.toByteArray();

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Product_InfoActivity.class);
                intent.putExtra("id", product.getId());
                intent.putExtra("name", product.getName());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("image", compressedImage);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        ImageView imageView;

        Button button;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textTitleView);
            price = itemView.findViewById(R.id.textPriceView);
            imageView = itemView.findViewById(R.id.imageView9);
            button = itemView.findViewById(R.id.button10);
        }
    }
}
