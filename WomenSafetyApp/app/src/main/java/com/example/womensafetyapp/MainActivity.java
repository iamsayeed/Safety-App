package com.example.womensafetyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.iceteck.silicompressorr.SiliCompressor;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private long pressedTime;


    String latitude, longitude, address_line;
    FusedLocationProviderClient fusedLocationProviderClient;

    private TextView tv;
    private String upload_URL = "http://192.168.43.223/video_upload/uploadfile.php?";
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;
    String url = "http://192.168.43.223";

    Button btSelect;
    VideoView videoView1, videoView2;
    TextView tv1, tv2, tv3;

    EditText name, email, frnd_email;
    String uname1, uemail1, umobile1, uname2;
    ConnectionDetector connectionDetector;

    CardView cardView;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionDetector = new ConnectionDetector(this);

        //Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        setdata();
        getCurrentLocation();

        //requestPermission();




        name = (EditText) findViewById(R.id.et_username);
        email = (EditText) findViewById(R.id.et_useremail);
        frnd_email = (EditText) findViewById(R.id.et_frndemail);
        uname1 = name.getText().toString();
        uemail1 = email.getText().toString();
        umobile1 = frnd_email.getText().toString();


        // update=(Button)findViewById(R.id.btn_update);

        uname2 = name.getText().toString();


        //Toast.makeText(getApplicationContext(), "User_name--" + my_id, Toast.LENGTH_LONG).show();




        tv = findViewById(R.id.tv);


        btSelect = (Button) findViewById(R.id.bt_select);
        videoView1 = (VideoView) findViewById(R.id.video_view1);
        videoView2 = (VideoView) findViewById(R.id.video_view2);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);

        btSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionDetector.isConnectingToInternet()){

                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {

                    if(name.getText().toString().length()>0 && email.getText().toString().length()>0) {
                        getCurrentLocation();
                        selectVideo();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Enter Name and Emails ", Toast.LENGTH_LONG).show();
                    }

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }else
                {
                    Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });


        cardView = findViewById(R.id.cv_siren);
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //To decrease media player volume

                    MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.police_siren2);
                    audioManager.setStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                            0);
                    mp.start();

                }
                catch (Exception e)
                {

                }

            }
        });






    } //oncreateclose

    public void status_alertbox(View view) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setTitle("Status Update");

        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.user_input_dialog_box,
                        null);
        builder.setView(customLayout);

        // add a button
        builder
                .setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which)
                            {

                                // send data from the
                                // AlertDialog to the Activity
                                EditText editText
                                        = customLayout
                                        .findViewById(
                                                R.id.userInputDialog);
                                sendDialogDataToActivity(
                                        editText
                                                .getText()
                                                .toString());
                            }


                        });

           builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

    // Do something with the data
    // coming from the AlertDialog
    private void sendDialogDataToActivity(String data)
    {
        Toast.makeText(this,
                data,
                Toast.LENGTH_SHORT)
                .show();

        TextView status=findViewById(R.id.tv_status);
        status.setText(data);
        TextView status1=findViewById(R.id.tv_status_orange);
        status1.setText(data);
        TextView status2=findViewById(R.id.tv_status_red);
        status2.setText(data);


    }








    private void getCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Toast.makeText(MainActivity.this, "Location:" + addresses.get(0).getAddressLine(0), Toast.LENGTH_LONG).show();
                        double latitude1 = addresses.get(0).getLatitude();
                        double longitude1 = addresses.get(0).getLongitude();
                        address_line = addresses.get(0).getAddressLine(0);

                        latitude = Double.toString(latitude1);
                        longitude = Double.toString(longitude1);

                       // Toast.makeText(MainActivity.this, "Location: " + latitude + "/" + longitude, Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Location Failed",Toast.LENGTH_LONG).show();
                }

            }
        });

    }



    private void selectVideo() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("video/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "select video"), 100);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        if (intent.resolveActivity(getPackageManager()) != null)
        startActivityForResult(intent, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//          // bitmap = (Bitmap)data.getExtras().get("data");
//            Uri uri = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//              imageView.setImageBitmap(bitmap);
//              uploadImage();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            videoView1.setVideoURI(uri);
            videoView1.start();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            new CompressVideo().execute("false", uri.toString(), file.getPath());
        }


    }




    private class CompressVideo extends AsyncTask<String, String, String> {
        // Dialog dialog;
        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Uploading Please a Wait...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    MainActivity.CompressVideo.this.cancel(true);
                }
            });
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Compression Started Wait....", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String videoPath = null;

            try {
                Uri uri = Uri.parse(strings[1]);
                videoPath = SiliCompressor.with(MainActivity.this).compressVideo(uri, strings[2]);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return videoPath;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            videoView1.setVisibility(View.VISIBLE);
            videoView2.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            tv3.setVisibility(View.VISIBLE);

            File file = new File(s);
            Uri uri = Uri.fromFile(file);

            videoView2.setVideoURI(uri);
            videoView1.start();
            videoView2.start();

            float size = file.length() / 1024f;
            tv3.setText(String.format("Size :%.2f KB ", size));
            Toast.makeText(getApplicationContext(), "Compression done....", Toast.LENGTH_LONG).show();


            String path = file.getAbsolutePath();
            String displayName = String.valueOf(Calendar.getInstance().getTimeInMillis() + ".mp4");
            Log.d("Video Name ooooooo", displayName);
            uploadPDF(displayName, uri);


            progressDialog.dismiss();

//            String my_id = sharedPrefHandler.getSharedPreferences("name");
//            Intent in = new Intent(getApplication(), Alert_Send_Email.class);
//            startActivity(in);
        }
    }


    private void uploadPDF(final String pdfname, Uri pdffile) {

        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo", new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                jsonObject.toString().replace("\\\\", "");

                                if (jsonObject.getString("status").equals("true")) {
                                    Log.d("come::: >>>  ", "yessssss");
                                    arraylist = new ArrayList<HashMap<String, String>>();
                                    JSONArray dataArray = jsonObject.getJSONArray("data");


                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataobj = dataArray.getJSONObject(i);
                                        url = dataobj.optString("pathToFile");

                                        tv.setText(url);
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("f1", latitude);
                    params.put("f2", longitude);
                    params.put("f3", name.getText().toString());
                    params.put("f4", email.getText().toString());
                    params.put("f5", frnd_email.getText().toString());

                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();

                    params.put("filename", new DataPart(pdfname, inputData));

                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(MainActivity.this);
            rQueue.add(volleyMultipartRequest);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024 * 100;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    //check camera permission

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                200);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    //emailcode
                    //main code
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow Camera permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }





    public void setdata() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    //stuff that updates ui
                    //t1.setText(status1);
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Accept App's Permissions!")
                            .setMessage("For the app to function properly Please Enable Device Location ,Camera ,Media File access and Insert Emails Properly")

                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();

                                }
                            })
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);

                                    getCurrentLocation();
                                    requestPermission();

                                }
                            })
                            .show();

                }
            });
        }



    }


    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}