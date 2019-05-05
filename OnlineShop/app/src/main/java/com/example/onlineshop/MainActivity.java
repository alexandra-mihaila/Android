package com.example.onlineshop;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    public static final String EXTRA_MESSAGE = "com.example.onlineshop.MESSAGE";
    public static final String USERNAME = "USERNAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        isStoragePermissionGranted();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String username = sharedPref.getString(USERNAME, "");
        File directory = getFilesDir();
        File file = new File(directory, "myfile_username");
        String contents = "";
        FileInputStream in = null;
        String contents_ext = "";

        if (file.exists()) {
            byte[] bytes = new byte[(int) file.length()];
            try {
                in = new FileInputStream(file);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                in.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            contents = new String(bytes);
        }
        in = null;
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/temp/whatever");
        Log.e(TAG, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/temp/whatever");
        if(path.exists()) {
            byte[] bytes = new byte[(int) path.length()];

            try {
                in = new FileInputStream(path);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                in.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            contents_ext = new String(bytes);
        }

        final List<String> your_array_list = new ArrayList<>();
        your_array_list.add("Ana");
        your_array_list.add("are");
        your_array_list.add("mere");
        your_array_list.add("shared_pref -> " + username);
//        your_array_list.add("internal_storage -> " + contents);
        your_array_list.add("ext_storage -> " + contents_ext);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);
        ListView list_view = (ListView) findViewById(R.id.list_view);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text_view = (TextView) findViewById(R.id.text_view);
                text_view.setText(((TextView) view).getText());
                String item = your_array_list.get(position).toString();
                sendMessage(text_view);

            }
        });
        list_view.setAdapter(arrayAdapter);


    }
    public void sendMessage(TextView text_view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        String message = text_view.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop called");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int CAMERA_REQUEST = 1888;
        final Context context = getApplicationContext();
        Intent intent;
        switch (item.getItemId()) {
            case R.id.camera:
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                Intent cameraIntent = new Intent(context, CameraActivity.class);
                startActivity(cameraIntent);
                return true;

            case R.id.show_sensors:
                Log.i(TAG, "Pressed sensors button.");
                intent = new Intent(context, SensorActivity.class);
                startActivity(intent);
                return true;

            case R.id.sign_in:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.dialog_singin, null))
                        .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Dialog dialog2 = (Dialog) dialog;
                                EditText mEdit = (EditText) dialog2.findViewById(R.id.username);
                                EditText mEdit_p = (EditText) dialog2.findViewById(R.id.password);
                                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(USERNAME, mEdit.getText().toString());
                                editor.apply();
                                String filename = "myfile_username";
                                String fileContents = mEdit.getText().toString();
                                String altFileContents = mEdit_p.getText().toString();
                                FileOutputStream outputStream;

                                try {
                                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                    outputStream.write(fileContents.getBytes());
                                    outputStream.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    File dir = new File(Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES), "temp");
                                    if (!dir.exists()) {
                                        Log.e(TAG,"!!!!!Trying to create dir.");
                                        if (!dir.mkdirs()) {
                                            Log.e(TAG,"!!!!FAILED TO CREATE DIR.");
                                        }
                                    }
                                    File file = new File(dir.getAbsolutePath() + "/whatever");
                                    FileOutputStream output_stream = new FileOutputStream(file, false);
                                    output_stream.write(altFileContents.getBytes());
                                    output_stream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(), ":)", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onResume();
                            }
                        });
                builder.create();
                builder.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
}
