package com.ursa_studio.booster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.ursa_studio.booster.map.MapsMainActivity;

public class SplashActivity extends AppCompatActivity {

  @Override protected void onCreate (Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    ImageView image=(ImageView)findViewById(R.id.imageSplash);

    Animation animSplash= AnimationUtils.loadAnimation(this, R.anim.splash_anim);
    image.startAnimation(animSplash);

    animSplash.setAnimationListener(new Animation.AnimationListener() {

      @Override
      public void onAnimationStart(Animation animation) {


      }

      @Override
      public void onAnimationRepeat(Animation animation) {


      }

      @Override
      public void onAnimationEnd(Animation animation) {

        startActivity(new Intent(SplashActivity.this,MapsMainActivity.class));
        SplashActivity.this.finish();
      }
    });
  }
}
