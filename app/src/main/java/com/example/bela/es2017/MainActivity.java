package com.example.bela.es2017;

import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends SideBarActivity {



    @Override
    protected SideBarInfo getInfo(){
        return new SideBarInfo("EasyFeed - Home",R.layout.activity_main);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
