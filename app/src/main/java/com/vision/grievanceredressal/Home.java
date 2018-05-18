package com.vision.grievanceredressal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewFlipper;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import javax.security.auth.login.LoginException;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewFlipper view = (ViewFlipper) findViewById(R.id.viewer);
        view.setDisplayedChild(0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SliderLayout slideshow = (SliderLayout) findViewById(R.id.slider);
        DefaultSliderView slide1 = new DefaultSliderView(this);
        DefaultSliderView slide2 = new DefaultSliderView(this);
        DefaultSliderView slide3 = new DefaultSliderView(this);
        DefaultSliderView slide4 = new DefaultSliderView(this);
        DefaultSliderView slide5 = new DefaultSliderView(this);
        slide1.image(R.drawable.icds);
        slide2.image(R.drawable.icds_child);
        slide3.image(R.drawable.child);
        slide4.image(R.drawable.kids);
        slide5.image(R.drawable.edu_child);
        slideshow.addSlider(slide1);
        slideshow.addSlider(slide2);
        slideshow.addSlider(slide3);
        slideshow.addSlider(slide4);
        slideshow.addSlider(slide5);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_regcomp) {
            Intent myin = new Intent(getApplicationContext(), registerComplaint.class);
            startActivity(myin);
        } else if (id == R.id.nav_home) {
            Intent myin = new Intent(getApplicationContext(), Home.class);
            startActivity(myin);
        } else if (id == R.id.nav_login) {
            Intent myin = new Intent(getApplicationContext(), LoginWeb.class);
            startActivity(myin);
        } else if (id == R.id.nav_checkstatus) {
            Intent myin = new Intent(getApplicationContext(), checkStatus.class);
            startActivity(myin);
        }
        return true;
    }
}
