package com.google.whatsappclone.Main;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.whatsappclone.Main.Contacts.ContactsActivity;
import com.google.whatsappclone.R;
import com.google.whatsappclone.databinding.ActivityMainBinding;
import com.google.whatsappclone.menu.CallsFragment;
import com.google.whatsappclone.menu.ChatFragment;
import com.google.whatsappclone.menu.StatusFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setUpwithViewpager(binding.viewPager);
        binding.tablayout.setupWithViewPager(binding.viewPager);
        setSupportActionBar(binding.toolbar);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageSelected(int position) {
              changeFabCon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpwithViewpager(ViewPager viewpager){

     MainActivity.SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
     adapter.addFragment(new ChatFragment(),"chats");
     adapter.addFragment(new StatusFragment(),"status");
     adapter.addFragment(new CallsFragment(),"calls");

     viewpager.setAdapter(adapter);
    }

    private static class SectionPageAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentlist = new ArrayList<>();
        private final List<String> mfragmentTitleList =new ArrayList<>();



        public SectionPageAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentlist.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentlist.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentlist.add(fragment);
            mfragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mfragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_search:Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();break;
            case R.id.menu_options:Toast.makeText(this, "options", Toast.LENGTH_SHORT).show();break;

        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeFabCon(final int index){
        binding.fabButton.hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (index){
                    case 0: binding.fabButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_message_24));
                             binding.fabButton.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                                 }
                             });
                         break;
                    case 1: binding.fabButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_camera_alt_24));break;
                    case 2: binding.fabButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_add_ic_call_24));break;
                }
                binding.fabButton.show();
            }
        },400);

    }
}