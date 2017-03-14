package com.ursa_studio.booster.order;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.ursa_studio.booster.R;
import com.ursa_studio.booster.map.Constants;
import com.ursa_studio.booster.model.Boost;

/**
 * Booster
 * com.ursa_studio.booster.order
 * Created by janko on 13.03.17..
 * Description:
 * Usage:
 */

public class OrderActivity extends AppCompatActivity {
  private String TAG = "MAIN";
  protected FragmentManager fragmentManager;
  private Boost boost;

  @Override protected void onCreate (Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    //noinspection ConstantConditions
    getSupportActionBar().setTitle(getString(R.string.app_name));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Bundle extras = getIntent ().getExtras ();

    boost = extras.getParcelable (Constants.BOOST_OBJECT);


    showBoostDetails();
  }

  private void showBoostDetails (){

    Bundle bundle = new Bundle();
    bundle.putParcelable(Constants.BOOST_OBJECT, boost);

    Fragment fragment = new BoostRequestFragment();
    fragment.setArguments(bundle);
    fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .replace(R.id.flContent, fragment, "BOOST")
        .addToBackStack("tag")
        .commit();
  }

  @Override public void onBackPressed (){

    int count = getFragmentManager().getBackStackEntryCount();

    if(count == 0){
      super.onBackPressed();
    } else{

      getFragmentManager().popBackStack();
    }
  }

  @Override public boolean onCreateOptionsMenu (Menu menu){
    getMenuInflater().inflate(R.menu.main_menu, menu);

    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected (MenuItem item){
    switch(item.getItemId()){

      default:

        return super.onOptionsItemSelected(item);
    }
  }
}
