package lk.example.chalatchoco.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.example.chalatchoco.R;
import lk.example.chalatchoco.model.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<Order> orderArrayList;
    private Context mContext;



    public OrderAdapter(ArrayList<Order> orderArrayList, Context context) {
        this.orderArrayList = orderArrayList;
        this.mContext = context;

    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflatedView = layoutInflater.inflate(R.layout.payment_item, parent, false);
        return new OrderAdapter.OrderViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Order order = orderArrayList.get(position);
        holder.name.setText(order.getName());
        holder.type.setText(order.getType());
        holder.price.setText(order.getPrice());

        if (order.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(order.getImage(), 0, order.getImage().length);
            holder.imageView.setImageBitmap(bitmap);
        }

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(order.getImage(), 0, order.getImage().length);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] compressedImage = baos.toByteArray();


//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, Product_InfoActivity.class);
//                intent.putExtra("id", order.getId());
//                intent.putExtra("name", order.getName());
//                intent.putExtra("price", order.getPrice());
//                intent.putExtra("image", compressedImage);
//                mContext.startActivity(intent);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        TextView price;
        ImageView imageView;
        Button buttonBuy;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView7);
            type = itemView.findViewById(R.id.textView32);
            price = itemView.findViewById(R.id.textView33);
            imageView = itemView.findViewById(R.id.imageView4);
            buttonBuy = itemView.findViewById(R.id.button7);
        }
    }


}
