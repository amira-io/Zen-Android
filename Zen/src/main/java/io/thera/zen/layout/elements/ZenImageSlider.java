package io.thera.zen.layout.elements;

/**
 * Created by marcostagni on 10/01/14.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.thera.zen.R;
import io.thera.zen.core.ZenAppManager;
import io.thera.zen.core.ZenLog;
import io.thera.zen.core.ZenResManager;

/**
 * Created by marcostagni on 10/01/14.
 */
public class ZenImageSlider {

    private Map<String,Drawable> thumbImageMap;
    private Map<String,Drawable> largeImageMap;

    String currentImage;
    private ViewGroup relative;
    private ViewGroup linear;


    class imageTask extends AsyncTask<String, Void, Drawable> {

        private String method;
        private Object caller;
        private String url;


        public imageTask(String method, Object caller) {

            this.method     = method;
            this.caller     = caller;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            try {

                this.url = params[0];
                System.out.println("URL PARAMS " + params[0]);
                System.out.println("URL THIS.URL" + this.url);
                HttpURLConnection connection = (HttpURLConnection) new URL(params[0]).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap x;
                x = BitmapFactory.decodeStream(input);
                BitmapDrawable bd = new BitmapDrawable(x);

                return bd.mutate();

            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("get image error");
                return null;
            }
        }

        @Override
        protected void onPostExecute( Drawable result ) {
            try {



                Class[] params = new Class[2];
                params[0] = Drawable.class;
                params[1] = String.class;
                Object[] values = new Object[2];
                values[0] = result;
                values[1] = this.url;
                System.out.println("URL THIS.URL reflection" + this.url);

                for (Method m : this.caller.getClass().getDeclaredMethods()) {
                    System.out.println(m.getName());
                }

                if (this.caller instanceof Class) {

                    ((Class) this.caller).getMethod(this.method, params).invoke(this.caller,values);
                }
                else {

                    this.caller.getClass().getMethod(this.method, params).invoke(this.caller, values);
                }

            }
            catch (Exception e) {
                //container.setText(e.printStackTrace());
                //container.setText(e.getLocalizedMessage());
                e.printStackTrace();
                return;
            }
        }
    }

    public ZenImageSlider( ViewGroup relative, ViewGroup linear ) {

        this.linear         = linear;
        this.relative       = relative;
        //galleryMap        = new HashMap<Integer, Drawable>();
        thumbImageMap       = new HashMap<String,   Drawable>();
        largeImageMap       = new HashMap<String,   Drawable>();
    }



    public void addImage ( Drawable d , String imgId) {
        try {
            System.out.println("ADDIMAGE");
            ImageView imageView = new ImageView(relative.getContext());
            //imageView.setImageResource(R.drawable.color_baloons);
            //galleryMap.put(index , d);//drawableFromUrl("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash3/c0.54.180.180/s160x160/943494_10200298310432837_1799896952_a.jpg"));
            final String imageId = imgId;
            thumbImageMap.put(imgId , d);
            imageView.setImageDrawable(thumbImageMap.get(imgId));

            linear.addView(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println(view.getId() + " IMAGE");

                    final RelativeLayout r;
                    RelativeLayout.LayoutParams lpRelative;

                    r = new RelativeLayout(relative.getContext());
                    lpRelative =  new RelativeLayout.LayoutParams(relative.getWidth() , relative.getHeight());
                    r.setBackgroundResource(R.drawable.white_rect);

                    ImageView detailImage =  new ImageView(relative.getContext());
                    detailImage.setImageDrawable(largeImageMap.get(imageId));
                    LinearLayout.LayoutParams lpDetail = new LinearLayout.LayoutParams(relative.getWidth(), relative.getHeight());
                    detailImage.setLayoutParams(lpDetail);
                    r.addView(detailImage);

                    ImageView closeImage = new ImageView(relative.getContext());
                    closeImage.setImageDrawable(ZenResManager.getDrawable("close_image"));
                    RelativeLayout.LayoutParams lpClose = new RelativeLayout.LayoutParams(50,50);
                    lpClose.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    lpClose.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    closeImage.setLayoutParams(lpClose);
                    r.addView(closeImage);

                    closeImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlphaAnimation anim = new AlphaAnimation(1.0f , 0.0f);
                            anim.setDuration(350);
                            anim.setFillAfter(true);
                            r.startAnimation(anim);
                            anim.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    r.removeAllViews();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                        }
                    });
                    r.setLayoutParams(lpRelative);
                    relative.addView(r);//addContentView(r, lpRelative);
                    AlphaAnimation anim = new AlphaAnimation(0.0f , 1.0f);
                    anim.setDuration(350);
                    anim.setFillAfter(true);
                    r.startAnimation(anim);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public void addImageFromUrl ( String thumbUrl, String largeUrl ) {
        //getDrawableFromUrl(url);

        currentImage = thumbUrl;
        if (ZenAppManager.isConnected()) {

            if (largeUrl!=null) {

                new imageTask("storeLargeImage", this).execute(largeUrl);
            }
            else {

                new imageTask( "storeLargeImage" , this).execute(thumbUrl);

            }

        }
        else {


        }
    }

    public void storeLargeImage ( Drawable d , String imgId) {

        largeImageMap.put(imgId, d);
        // TEMP FIX (currentImage is changed by next image!!)
        //new imageTask("addImage" , this ).execute(currentImage);
        new imageTask("addImage" , this ).execute(imgId);
    }

    public void addImageArray ( String[] array ) {
        for (int i =0; i < array.length ; i++) {
            addImageFromUrl(array[i],null);
        }
    }

    public void addImageList (List<String> list) {
        for (int i =0; i < list.size() ; i++) {
            addImageFromUrl(list.get(i),null);
        }
    }

    public void addImageArrays ( String[] thumb_array , String[] large_array ) {
        for (int i =0; i < thumb_array.length ; i++) {
            addImageFromUrl(thumb_array[i],large_array[i]);
        }
    }

    public void addImageLists (List<String> thumb_list , List<String> large_list) {
        ZenLog.l("SLIDER list length " + thumb_list.size() + " - " + large_list.size());
        for (int i =0; i < thumb_list.size() ; i++) {
            ZenLog.l("getting images");
            addImageFromUrl(thumb_list.get(i),large_list.get(i));
        }
    }
}


