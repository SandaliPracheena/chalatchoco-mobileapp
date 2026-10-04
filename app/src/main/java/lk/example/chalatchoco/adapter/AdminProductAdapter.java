package lk.example.chalatchoco.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import lk.example.chalatchoco.Product_InfoActivity;
import lk.example.chalatchoco.R;
import lk.example.chalatchoco.model.Product;
import lk.example.chalatchoco.model.SQLiteHelper;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ProductViewHolder> {

    private ArrayList<Product> productArrayList;
    private Context mContext;

    public AdminProductAdapter(ArrayList<Product> productArrayList, Context context) {
        this.productArrayList = productArrayList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflatedView = layoutInflater.inflate(R.layout.admin_product_delete_item, parent, false);
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

        // The following part is optional based on your use case
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
         byte[] compressedImage = baos.toByteArray();

         //Uncomment and use this part if needed
         holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, Product_InfoActivity.class);
//                intent.putExtra("id", product.getId());
//                intent.putExtra("name", product.getName());
//                intent.putExtra("price", product.getPrice());
//                intent.putExtra("image", compressedImage);
//                mContext.startActivity(intent);


                SQLiteHelper sqLiteHelper = new SQLiteHelper(
                        holder.itemView.getContext(),
                        "mynotebook.db",
                        null,
                        1
                );

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //delete
                        SQLiteDatabase sqLiteDatabase =   sqLiteHelper.getWritableDatabase();
                        int row =    sqLiteDatabase.delete(
                                "product",
                                "`id`=?",
                                new String[]{product.getId()}

                        );



                        Log.i("mynotebook","row"+row+"Deleted");


                        // Remove the item from the data set
                        if (row > 0) {
                            productArrayList.remove(product);

                            // Notify the adapter of the item removal
                            holder.itemView.post(new Runnable() {
                                @Override
                                public void run() {
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemRangeChanged(holder.getAdapterPosition(), productArrayList.size());
                                }
                            });
                        }



                    }
                }).start();




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
            name = itemView.findViewById(R.id.textView6);
            price = itemView.findViewById(R.id.textView15);
            imageView = itemView.findViewById(R.id.imageView5);
            button = itemView.findViewById(R.id.button4);
        }
    }
}
