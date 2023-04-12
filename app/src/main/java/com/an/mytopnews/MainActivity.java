package com.an.mytopnews;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.an.mytopnews.ui.main.SectionsPagerAdapter;
import com.an.mytopnews.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final int[] tabIcons = {
            R.drawable.business_icon_feeds,
            R.drawable.entertainment,
            R.drawable.health,
            R.drawable.science
    };

    private final int[] tabLabels = {
            R.string.tab_business,
            R.string.tab_entertainment,
            R.string.tab_health,
            R.string.tab_science
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);


//        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.nav_tab, null);
//        tabOne.setText(tabLabels[0]);
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[0], 0, 0);

        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setIcon(tabIcons[i]);
            tabs.getTabAt(i).setText(tabLabels[i]);
        }


//
//            // finally publish this custom view to navigation tab
//            tabs.getTabAt(i).setCustomView(tab);
//        }
//        FloatingActionButton fab = binding.fab;
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
}