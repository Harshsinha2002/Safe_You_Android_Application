package com.example.safeyou.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.safeyou.About_Us_Page;
import com.example.safeyou.Quick_Contacts_Page;
import com.example.safeyou.R;
import com.example.safeyou.Safety_Products_Page;
import com.example.safeyou.Self_Defence_Tips_Page;
import com.example.safeyou.User_Profile_Page;
import com.example.safeyou.databinding.FragmentHomeBinding;
import com.example.safeyou.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    Activity content;
    private FragmentProfileBinding binding;

    DatabaseReference db;
    String User_id, User_FullName, User_PhoneNo, User_Address_Line1, User_Address_Line2, User_PinCode;
    String Sos_Contact_1_FullName, Sos_Contact_1_PhoneNo, Sos_Contact_2_FullName, Sos_Contact_2_PhoneNo, Sos_Contact_3_FullName, Sos_Contact_3_PhoneNo;
    String Quick_Contact_1_FullName, Quick_Contact_1_PhoneNo,Quick_Contact_2_FullName, Quick_Contact_2_PhoneNo, Quick_Contact_3_FullName, Quick_Contact_3_PhoneNo, Quick_Contact_4_FullName, Quick_Contact_4_PhoneNo, Quick_Contact_5_FullName, Quick_Contact_5_PhoneNo;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        content = getActivity();
        return root;

    }

    public void onStart()
    {
        super.onStart();
        ImageView User_Profile = content.findViewById(R.id.User_Profile);
        ImageView Quick_Contacts = content.findViewById(R.id.Quick_Contacts);
        ImageView Safety_Products = content.findViewById(R.id.Safety_Products);
        ImageView About_Us = content.findViewById(R.id.About_Us);
        ImageView Self_Defence_Tips = content.findViewById(R.id.Self_Defence_Tips);

        User_Profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)
                {
                    String email = user.getEmail();
                    User_id = email;
                }

                db = FirebaseDatabase.getInstance().getReference("User Info");
                db.child(User_id.replace(".", "")).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().exists())
                            {
                                DataSnapshot dataSnapshot = task.getResult();

                                User_id = String.valueOf(dataSnapshot.child("User_id").getValue());
                                User_FullName = String.valueOf(dataSnapshot.child("User_FullName").getValue());
                                User_PhoneNo = String.valueOf(dataSnapshot.child("User_PhoneNo").getValue());
                                User_Address_Line1 = String.valueOf(dataSnapshot.child("User_Address_Line1").getValue());
                                User_Address_Line2 = String.valueOf(dataSnapshot.child("User_Address_Line2").getValue());
                                User_PinCode = String.valueOf(dataSnapshot.child("User_PinCode").getValue());


                                Sos_Contact_1_FullName = String.valueOf(dataSnapshot.child("Sos_Contact_1_FullName").getValue());
                                Sos_Contact_1_PhoneNo = String.valueOf(dataSnapshot.child("Sos_Contact_1_PhoneNo").getValue());
                                Sos_Contact_2_FullName = String.valueOf(dataSnapshot.child("Sos_Contact_2_FullName").getValue());
                                Sos_Contact_2_PhoneNo = String.valueOf(dataSnapshot.child("Sos_Contact_2_PhoneNo").getValue());
                                Sos_Contact_3_FullName = String.valueOf(dataSnapshot.child("Sos_Contact_3_FullName").getValue());
                                Sos_Contact_3_PhoneNo = String.valueOf(dataSnapshot.child("Sos_Contact_3_PhoneNo").getValue());


                                Quick_Contact_1_FullName = String.valueOf(dataSnapshot.child("Quick_Contact_1_FullName").getValue());
                                Quick_Contact_1_PhoneNo =  String.valueOf(dataSnapshot.child("Quick_Contact_1_PhoneNo").getValue());
                                Quick_Contact_2_FullName = String.valueOf(dataSnapshot.child("Quick_Contact_2_FullName").getValue());
                                Quick_Contact_2_PhoneNo =  String.valueOf(dataSnapshot.child("Quick_Contact_2_PhoneNo").getValue());
                                Quick_Contact_3_FullName = String.valueOf(dataSnapshot.child("Quick_Contact_3_FullName").getValue());
                                Quick_Contact_3_PhoneNo =  String.valueOf(dataSnapshot.child("Quick_Contact_3_PhoneNo").getValue());
                                Quick_Contact_4_FullName = String.valueOf(dataSnapshot.child("Quick_Contact_4_FullName").getValue());
                                Quick_Contact_4_PhoneNo =  String.valueOf(dataSnapshot.child("Quick_Contact_4_PhoneNo").getValue());
                                Quick_Contact_5_FullName = String.valueOf(dataSnapshot.child("Quick_Contact_5_FullName").getValue());
                                Quick_Contact_5_PhoneNo =  String.valueOf(dataSnapshot.child("Quick_Contact_5_PhoneNo").getValue());


                                Intent intent = new Intent(content, User_Profile_Page.class);

                                intent.putExtra("User_id", User_id);
                                intent.putExtra("User_FullName", User_FullName);
                                intent.putExtra("User_PhoneNo", User_PhoneNo);
                                intent.putExtra("User_Address_Line1", User_Address_Line1);
                                intent.putExtra("User_Address_Line2", User_Address_Line2);


                                intent.putExtra("Sos_Contact_1_FullName", Sos_Contact_1_FullName);
                                intent.putExtra("Sos_Contact_1_PhoneNo", Sos_Contact_1_PhoneNo);
                                intent.putExtra("Sos_Contact_2_FullName", Sos_Contact_2_FullName);
                                intent.putExtra("Sos_Contact_2_PhoneNo", Sos_Contact_2_PhoneNo);
                                intent.putExtra("Sos_Contact_3_FullName", Sos_Contact_3_FullName);
                                intent.putExtra("Sos_Contact_3_PhoneNo", Sos_Contact_3_PhoneNo);


                                intent.putExtra("Quick_Contact_1_FullName", Quick_Contact_1_FullName);
                                intent.putExtra("Quick_Contact_1_PhoneNo", Quick_Contact_1_PhoneNo);
                                intent.putExtra("Quick_Contact_2_FullName", Quick_Contact_2_FullName);
                                intent.putExtra("Quick_Contact_2_PhoneNo", Quick_Contact_2_PhoneNo);
                                intent.putExtra("Quick_Contact_3_FullName", Quick_Contact_3_FullName);
                                intent.putExtra("Quick_Contact_3_PhoneNo", Quick_Contact_3_PhoneNo);
                                intent.putExtra("Quick_Contact_4_FullName", Quick_Contact_4_FullName);
                                intent.putExtra("Quick_Contact_4_PhoneNo", Quick_Contact_4_PhoneNo);
                                intent.putExtra("Quick_Contact_5_FullName", Quick_Contact_5_FullName);
                                intent.putExtra("Quick_Contact_5_PhoneNo", Quick_Contact_5_PhoneNo);

                                startActivity(intent);
                            }
                        }
                    }
                });

            }
        });

        Quick_Contacts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)
                {
                    String email = user.getEmail();
                    User_id = email;
                }

                db = FirebaseDatabase.getInstance().getReference("User Info");
                db.child(User_id.replace(".", "")).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().exists())
                            {
                                DataSnapshot dataSnapshot = task.getResult();
                                Quick_Contact_1_FullName = String.valueOf(dataSnapshot.child("Quick_Contact_1_FullName").getValue());
                                Quick_Contact_1_PhoneNo =  String.valueOf(dataSnapshot.child("Quick_Contact_1_PhoneNo").getValue());
                                Quick_Contact_2_FullName = String.valueOf(dataSnapshot.child("Quick_Contact_2_FullName").getValue());
                                Quick_Contact_2_PhoneNo =  String.valueOf(dataSnapshot.child("Quick_Contact_2_PhoneNo").getValue());
                                Quick_Contact_3_FullName = String.valueOf(dataSnapshot.child("Quick_Contact_3_FullName").getValue());
                                Quick_Contact_3_PhoneNo =  String.valueOf(dataSnapshot.child("Quick_Contact_3_PhoneNo").getValue());
                                Quick_Contact_4_FullName = String.valueOf(dataSnapshot.child("Quick_Contact_4_FullName").getValue());
                                Quick_Contact_4_PhoneNo =  String.valueOf(dataSnapshot.child("Quick_Contact_4_PhoneNo").getValue());
                                Quick_Contact_5_FullName = String.valueOf(dataSnapshot.child("Quick_Contact_5_FullName").getValue());
                                Quick_Contact_5_PhoneNo =  String.valueOf(dataSnapshot.child("Quick_Contact_5_PhoneNo").getValue());


                                Intent intent = new Intent(content, Quick_Contacts_Page.class);

                                intent.putExtra("Quick_Contact_1_FullName", Quick_Contact_1_FullName);
                                intent.putExtra("Quick_Contact_1_PhoneNo", Quick_Contact_1_PhoneNo);
                                intent.putExtra("Quick_Contact_2_FullName", Quick_Contact_2_FullName);
                                intent.putExtra("Quick_Contact_2_PhoneNo", Quick_Contact_2_PhoneNo);
                                intent.putExtra("Quick_Contact_3_FullName", Quick_Contact_3_FullName);
                                intent.putExtra("Quick_Contact_3_PhoneNo", Quick_Contact_3_PhoneNo);
                                intent.putExtra("Quick_Contact_4_FullName", Quick_Contact_4_FullName);
                                intent.putExtra("Quick_Contact_4_PhoneNo", Quick_Contact_4_PhoneNo);
                                intent.putExtra("Quick_Contact_5_FullName", Quick_Contact_5_FullName);
                                intent.putExtra("Quick_Contact_5_PhoneNo", Quick_Contact_5_PhoneNo);

                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });

        Safety_Products.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(content, Safety_Products_Page.class);
                startActivity(intent);
            }
        });

        About_Us.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(content, About_Us_Page.class);
                startActivity(intent);
            }
        });

        Self_Defence_Tips.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(content, Self_Defence_Tips_Page.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}