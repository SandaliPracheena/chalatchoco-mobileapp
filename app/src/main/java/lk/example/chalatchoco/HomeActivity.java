package lk.example.chalatchoco;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import lk.example.chalatchoco.navigation.CartFragment;
import lk.example.chalatchoco.navigation.HomeFragment;
import lk.example.chalatchoco.navigation.InfoFragment;
import lk.example.chalatchoco.navigation.MapFragment;
import lk.example.chalatchoco.navigation.PaymentFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerlayout1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });








        DrawerLayout drawerLayout1 = findViewById(R.id.drawerlayout1);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        NavigationView navigationView1 = findViewById(R.id.navigationView);



        Intent i = getIntent();
        String firstName = i.getStringExtra("firstName");
        String email = i.getStringExtra("email");



        View headerView = navigationView1.getHeaderView(0);
        TextView textViewEmail = headerView.findViewById(R.id.userEmail);
        TextView textViewFirstName = headerView.findViewById(R.id.userName);


        textViewEmail.setText(email);
        textViewFirstName.setText(firstName);

        Bundle bundle = new Bundle();
        bundle.putString(email,"email"); // Add your data to the bundle

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);

        loadFragment(new HomeFragment());


        navigationView1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

               if (item.getItemId()==R.id.nav_menu_cart) {
                   loadFragment(new CartFragment());
                    toolbar.setSubtitle(item.getTitle());
                }else if (item.getItemId()==R.id.nav_menu_payment) {
                    loadFragment(new PaymentFragment());
                    toolbar.setSubtitle(item.getTitle());
                } else if (item.getItemId()==R.id.nav_menu_home) {
                   loadFragment(new HomeFragment());
                   toolbar.setSubtitle(item.getTitle());

               } else if (item.getItemId()==R.id.nav_menu_map) {
                   loadFragment(new MapFragment());
                   toolbar.setSubtitle(item.getTitle());

               }

               else if (item.getItemId()==R.id.nav_menu_info) {
                   loadFragment(new InfoFragment());
                   toolbar.setSubtitle(item.getTitle());

               }

                drawerLayout1.closeDrawers();

                return true;
            }
        });








    }


    private void loadFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_view,fragment,null)
                .setReorderingAllowed(true)
                .commit();

    }
}