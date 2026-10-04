package lk.example.chalatchoco.navigation;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import lk.example.chalatchoco.R;


public class InfoFragment extends Fragment {


    private static final int REQUEST_SEND_SMS_PERMISSION = 1;
    ArrayList<String> permissionList = new ArrayList<>();

    View view1;

    EditText editTextMessage ;

    String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_info, container, false);



        VideoView videoView1 = view.findViewById(R.id.videoView1);
        MediaController mediaController = new MediaController(getContext());
        mediaController.setMediaPlayer(videoView1);
        mediaController.setAnchorView(videoView1);

        videoView1.setMediaController(mediaController);
        Uri uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.video);
        videoView1.setVideoURI(uri);
        videoView1.start();

        videoView1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView1.start();
            }
        });





        Button buttonCall = view.findViewById(R.id.buttonCall);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = "tel:0771122333";
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse(phoneNumber));
                startActivity(call);
                videoView1.start();

              //  Toast.makeText(getContext(),"hello",Toast.LENGTH_SHORT).show();


            }
        });







        Button buttonMsg = view.findViewById(R.id.buttonMsg);
        buttonMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checkAndSendSMS();
                LayoutInflater inflater1 = LayoutInflater.from(getContext());
                 view1 = inflater1.inflate(R.layout.custome_msg_alert,null,false);

               AlertDialog alertDialog =  new AlertDialog.Builder(getContext()).setView(view1).show();

                Button buttonsend = view1.findViewById(R.id.buttonsend);
                buttonsend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editTextMessage = view1.findViewById(R.id.editTextMessage);
                        String message = editTextMessage.getText().toString();
                        if(message != null && !message.trim().isEmpty()){

                            checkAndSendSMS(message);

                            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel notificationChannel = new NotificationChannel(
                                        "C1",
                                        "Channel1",
                                        NotificationManager.IMPORTANCE_DEFAULT
                                );
                                notificationManager.createNotificationChannel(notificationChannel);
                            }

                            // API 26+
                            Intent intent = new Intent(getContext(), HomeFragment.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(
                                    getContext(),
                                    1,
                                    intent,
                                    PendingIntent.FLAG_IMMUTABLE
                            );

                            NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                                    R.drawable.info,
                                    "View",
                                    pendingIntent
                            ).build();

                            Notification notification = new NotificationCompat.Builder(getContext(), "C1")
                                    .setContentTitle("Chalat Choco")
                                    .setContentText("We will contact you as soon as posiable. Thank you.")
                                    .setSmallIcon(R.drawable.info)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.choco))
                                    .setStyle(new NotificationCompat.BigTextStyle())
                                    .setPriority(Notification.PRIORITY_DEFAULT)
                                    .addAction(action)
                                    .build();

                            notificationManager.notify(1, notification);
                            videoView1.start();

                        }else {

                            Toast.makeText(getContext(),"Message box is Empty",Toast.LENGTH_LONG).show();
                        }


                    }



                });


                Button buttoncancel = view1.findViewById(R.id.buttoncancel);
                buttoncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       alertDialog.dismiss();
                        videoView1.start();

                    }
                });

            }
        });


        return view;



    }


    private void checkAndSendSMS(String message) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Log.i("App", "SEND_SMS");


            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(
                    "0771667719",
                    null,
                    message,
                    null,
                    null);
            Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
            editTextMessage.setText("");
        } else {
            permissionList.add(Manifest.permission.SEND_SMS);
            ActivityCompat.requestPermissions(getActivity(), permissionList.toArray(new String[0]), REQUEST_SEND_SMS_PERMISSION);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SEND_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("App", "Permission granted");
                checkAndSendSMS(message);
            } else {
                // Permission was denied, handle accordingly
                Toast.makeText(getContext(), "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }



}