package mota.dev.happytesting;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import mota.dev.happytesting.managers.PermissionManager;
import mota.dev.happytesting.Views.GalleryActivity;


public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_MULTIPLE = 147;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 7645;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.getInstance().requestPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        REQUEST_READ_EXTERNAL_STORAGE,
                        new PermissionManager.PermisionResult() {
                            @Override
                            public void onDenied() {
                                System.exit(0);
                            }

                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                                startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
                            }
                        });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_MULTIPLE) {
            ArrayList<String> imagesPathList = new ArrayList<String>();
            String[] imagesPath = data.getStringExtra("data").split("\\|");
            ((TextView) findViewById(R.id.textView)).setText(data.getStringExtra("data"));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        PermissionManager.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
