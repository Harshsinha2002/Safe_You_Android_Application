package com.example.safeyou.ui.home;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.safeyou.R;
import com.example.safeyou.SafetyPlaces_Page;
import com.example.safeyou.User_Profile_Page;
import com.example.safeyou.databinding.FragmentHomeBinding;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    TextView User_Address;
    TextView SOS_Contacts_txt;
    private static final int REQUEST_CHECK_SETTING = 1001;
    private  static  final  int Call_Permission_Code = 1000;
    private LocationRequest locationRequest;
    double Latitude;
    double Longitude;

    DatabaseReference db;

    Activity content;

    String email;

    String Sos_Contact_1;
    String Sos_Contact_2;
    String Sos_Contact_3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        content = getActivity();
        return root;
    }

    public void onStart()
    {
        super.onStart();
        User_Address = content.findViewById(R.id.User_location);
        Button  SOS_Button = content.findViewById(R.id.SOS_btn);
        locationRequest = LocationRequest.create(); // create a location request
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // set how accurately the location will be provided
        locationRequest.setInterval(5000); // interval at with the location is set
        locationRequest.setFastestInterval(2000);
        ImageView Call_Police = content.findViewById(R.id.Call_Police);
        ImageView Call_FireSafety = content.findViewById(R.id.Call_FireSafety);
        ImageView Call_Women_Helpline = content.findViewById(R.id.Call_Women_Helpline);
        ImageView Call_Ambulance = content.findViewById(R.id.Call_Ambulance);
        ImageView SafePlaces = content.findViewById(R.id.SafePlaces);
        SOS_Contacts_txt = content.findViewById(R.id.SOS_Contacts_txt);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            email = user.getEmail();
        }
        db = FirebaseDatabase.getInstance().getReference("User Info");
        db.child(email.replace(".","")).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        DataSnapshot dataSnapshot = task.getResult();
                        String Sos_Contact_1_PhoneNo = String.valueOf(dataSnapshot.child("Sos_Contact_1_PhoneNo").getValue());
                        String Sos_Contact_2_PhoneNo = String.valueOf(dataSnapshot.child("Sos_Contact_2_PhoneNo").getValue());
                        String Sos_Contact_3_PhoneNo = String.valueOf(dataSnapshot.child("Sos_Contact_3_PhoneNo").getValue());

                        Sos_Contact_1 = Sos_Contact_1_PhoneNo;
                        Sos_Contact_2 = Sos_Contact_2_PhoneNo;
                        Sos_Contact_3 = Sos_Contact_3_PhoneNo;

                        SOS_Contacts_txt.setText(Sos_Contact_1_PhoneNo + ", " + Sos_Contact_2_PhoneNo + ", " + Sos_Contact_3_PhoneNo);
                    }

                    else
                    {
                        Toast.makeText(content, "Set User Information!!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


        SOS_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(ActivityCompat.checkSelfPermission(content, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) // checks if the gps permission is on or not
                    {
                        if(isGPSEnable()) // checks if the gps is on or not
                        {
                            LocationServices.getFusedLocationProviderClient(content).requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(content).removeLocationUpdates(this);

                                    if(locationResult != null && locationResult.getLocations().size() > 0)
                                    {
                                        int index =  locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        Latitude = (double)latitude;
                                        Longitude = (double) longitude;

                                        User_Address.setText("Latitude : " + Latitude  + "\nLongitude : " + Longitude );
                                        sendSMS(Longitude,Latitude);

                                        Toast.makeText(content, "SOS Message sent!!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, Looper.getMainLooper());
                        }

                        else // when gps is off
                        {
                            Toast.makeText(content, "Turn On Your GPS", Toast.LENGTH_LONG).show();
                            turnOnGPS();
                        }
                    }

                    else
                    {
                        requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 44);
                    }
                }

                if(ContextCompat.checkSelfPermission(content, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)// checks if the sms permission is on or not
                {
                    //
                }

                else
                {
                    ActivityCompat.requestPermissions(content, new String[]{Manifest.permission.SEND_SMS}, 100);

                }
            }
        });

        Call_Police.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callPolice();
            }
        });


        Call_FireSafety.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callFireSafety();
            }
        });


        Call_Women_Helpline.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callWomenHelpLine();
            }
        });


        Call_Ambulance.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callAmbulance();
            }
        });

        SafePlaces.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(content, SafetyPlaces_Page.class);
                startActivity(intent);
            }
        });
    }

    private void callPolice()
    {
        String number = "100";
        if(ContextCompat.checkSelfPermission(content,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(content, new String[]{Manifest.permission.CALL_PHONE}, Call_Permission_Code);
        }

        else
        {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }


    private void callFireSafety()
    {
        String number = "101";
        if(ContextCompat.checkSelfPermission(content,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(content, new String[]{Manifest.permission.CALL_PHONE}, Call_Permission_Code);
        }

        else
        {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }


    private void callWomenHelpLine()
    {
        String number = "181";
        if(ContextCompat.checkSelfPermission(content,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(content, new String[]{Manifest.permission.CALL_PHONE}, Call_Permission_Code);
        }

        else
        {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }


    private void callAmbulance()
    {
        String number = "102";
        if(ContextCompat.checkSelfPermission(content,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(content, new String[]{Manifest.permission.CALL_PHONE}, Call_Permission_Code);
        }

        else
        {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(requestCode == 44 && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                sendSMS(Longitude, Latitude);
            }
        }

        else if(requestCode == 1001 && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(requestCode == 44 && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPolice();
                callFireSafety();
                callWomenHelpLine();
                callAmbulance();
            }
        }

        else
        {
            Toast.makeText(content, "App Permission Denied!! Check Settings", Toast.LENGTH_LONG);
        }
    }

    private void sendSMS(Double Longitude, Double Latitude)
    {

        String message = "SOS NEED HELP!!!" + "\nCHECK  MY LOCATION " + "\nLatitude : " + Latitude + "\nLongitude : " + Longitude;

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(Sos_Contact_1, null, message,null,null);
        //smsManager.sendTextMessage(Sos_Contact_2, null, message, null, null);
        //smsManager.sendTextMessage(Sos_Contact_3, null, message, null, null);
    }

    private void turnOnGPS()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addAllLocationRequests(Collections.singleton(locationRequest)); //used to get the response as gps location of client
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(content, "GPS is On!!", Toast.LENGTH_LONG).show();

                }
                catch (ApiException e) // e here is the api exception error
                {
                    switch (e.getStatusCode())
                    {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED : //location setting status code used to get the location setting status(wether its turn on, or the device have location access or not etc)
                            //Resolution Required is used to check the location being turned on

                            try
                            {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e; // used to get the api exception error
                                resolvableApiException.startResolutionForResult(content, REQUEST_CHECK_SETTING);
                            }
                            catch (IntentSender.SendIntentException ex)
                            {
                                ex.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE :
                            break;
                    }
                }
            }
        });
    }

    private boolean isGPSEnable() // this method returns true is gps is turned on and returns false if gps is turned off
    {
        LocationManager locationManager = null;
        boolean isEnable = false;

        if(locationManager == null)
        {
            locationManager = (LocationManager)content.getSystemService(Context.LOCATION_SERVICE);
        }

        isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnable;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}