package com.hhstu.cyy.school.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.hhstu.cyy.school.tool.Constants;
import com.hhstu.cyy.school.tool.Utils;

import java.io.File;
/**
 * Created by Administrator on 2015/11/13.
 */
public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView = new ImageView(this);
//      imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//      imageView.setImageResource(R.mipmap.guide1);
        setContentView(imageView);
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(1000);
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File sdcardDir = Environment.getExternalStorageDirectory();
                    String path_downloads = sdcardDir.getPath() + "/" + Constants.DIR_BASE + "/" + Constants.DIR_DOWNLOADS;
                    String path_images = sdcardDir.getPath() + "/" + Constants.DIR_BASE + "/" + Constants.DIR_IMG;
                    File dir_downloads = new File(path_downloads);
                    if (!dir_downloads.exists()) {
                        dir_downloads.mkdirs();
                    }
                    File dir_images = new File(path_images);
                    if (!dir_images.exists()) {
                        dir_images.mkdirs();
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences sp = getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE);
                if (sp.getInt(Constants.SP_KEY_OLD_VERSION, 0) != Utils.getAppVersionCode(LaunchActivity.this)) {
                    Utils.sysout(sp.getInt(Constants.SP_KEY_OLD_VERSION, 0));
                    Utils.sysout(Utils.getAppVersionCode(LaunchActivity.this));
                    Intent intent = new Intent(LaunchActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        imageView.setAnimation(animationSet);
        imageView.startAnimation(animationSet);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
