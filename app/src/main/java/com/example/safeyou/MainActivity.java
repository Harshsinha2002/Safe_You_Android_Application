package com.example.safeyou;

import static com.example.safeyou.R.color.pink;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.safeyou.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Dialog dialog;
    Button Dialog_Cancelbtn, Dialog_Logoutbtn;
    DatabaseReference db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            String email = user.getEmail();
            db = FirebaseDatabase.getInstance().getReference("User Info");
            db.child(email.replace(".", "")).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            Toast.makeText(MainActivity.this, "Welcome Back", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            startActivity(new Intent(MainActivity.this, Create_New_User_Info_Page.class));
                            finish();
                        }
                    }
                }
            });
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(pink)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add("Logout");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getTitle().equals("Logout"))
        {
            dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.custom_dialog_box);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_box_background));
            dialog.setCancelable(false);
            dialog.show();

            Dialog_Cancelbtn = dialog.findViewById(R.id.Dialog_Cancelbtn);
            Dialog_Logoutbtn = dialog.findViewById(R.id.Dialog_Logoutbtn);

            Dialog_Cancelbtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });

            Dialog_Logoutbtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, Login_Page.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}