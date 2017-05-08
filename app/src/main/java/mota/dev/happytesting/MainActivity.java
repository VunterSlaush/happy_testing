package mota.dev.happytesting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mota.dev.happytesting.Views.GalleryActivity;

public class MainActivity extends AppCompatActivity {

  private static final int PICK_IMAGE_MULTIPLE = 147;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this,GalleryActivity.class);
        startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_MULTIPLE)
    {
        ArrayList<String> imagesPathList = new ArrayList<String>();
        String[] imagesPath = data.getStringExtra("data").split("\\|");
        ((TextView)findViewById(R.id.textView)).setText(data.getStringExtra("data"));
    }

  }
}
