package edu.oregonstate.rsrobinett.sqliteandlocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import static edu.oregonstate.rsrobinett.sqliteandlocation.Constants.*;

public class MainActivity extends AppCompatActivity {

    //PermissionServices permissionServices = new PermissionServices();

    NavigationServices navigationServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED);
        */

        final Button button = (Button) findViewById(R.id.button_assignment);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AssignmentActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart(){
        startActivity(new Intent(MainActivity.this, NavigationServices.class));
        super.onStart();
    }
    /*
    @Override
    protected void onStart(){
        navigationServices = new NavigationServices();
        navigationServices.start();
        super.onStart();
    }
    */
}

