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
import lk.example.chalatchoco.model.Cart;
import lk.example.chalatchoco.model.SQLiteHelper;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<Cart> cartArrayList;
    private Context mContext;

    public CartAdapter(ArrayList<Cart> cartArrayList, Context context) {
        this.cartArrayList = cartArrayList;
        this.mContext = context;
    }


    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflatedView = layoutInflater.inflate(R.layout.product_cart_item, parent, false);
        return new CartAdapter.CartViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Cart cart = cartArrayList.get(position);
        holder.name.setText(cart.getName());
        holder.price.setText(cart.getPrice());

        if (cart.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(cart.getImage(), 0, cart.getImage().length);
            holder.imageView.setImageBitmap(bitmap);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeByteArray(cart.getImage(), 0, cart.getImage().length);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] compressedImage = baos.toByteArray();

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, Product_InfoActivity.class);
//                intent.putExtra("id", product.getId());
//                intent.putExtra("name", product.getName());
//                intent.putExtra("price", product.getPrice());
//                intent.putExtra("image", compressedImage);
//                mContext.startActivity(intent);
//
//            }
//        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SQLiteHelper sqLiteHelper = new SQLiteHelper(
                        holder.itemView.getContext(),
                        "chalatchoco.db",
                        null,
                        2
                );

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //delete
                        SQLiteDatabase sqLiteDatabase =   sqLiteHelper.getWritableDatabase();
                        int row =    sqLiteDatabase.delete(
                                "cart",
                                "`product_id`=?",
                                new String[]{cart.getProduct_id()}

                        );



                        Log.i("mynotebook","row"+row+"Deleted");


                        // Remove the item from the data set
                        if (row > 0) {
                            cartArrayList.remove(cart);

                            // Notify the adapter of the item removal
                            holder.itemView.post(new Runnable() {
                                @Override
                                public void run() {
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemRangeChanged(holder.getAdapterPosition(), cartArrayList.size());
                                }
                            });
                        }



                    }
                }).start();


            }
        });



        holder.buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Product_InfoActivity.class);
                intent.putExtra("id", cart.getProduct_id());
                intent.putExtra("name", cart.getName());
                intent.putExtra("price", cart.getPrice());
                intent.putExtra("image", compressedImage);
                mContext.startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        ImageView imageView;

        Button button;

        Button buttonCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView16);
            price = itemView.findViewById(R.id.textView18);
            imageView = itemView.findViewById(R.id.imageView6);
            button = itemView.findViewById(R.id.button6);
            buttonCart = itemView.findViewById(R.id.buttoncart);
        }
    }
}
