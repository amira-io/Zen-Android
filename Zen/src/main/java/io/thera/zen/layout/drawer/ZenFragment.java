package io.thera.zen.layout.drawer;

/**
 * Created by marcostagni on 26/11/13.
 *
 * Copyright © 2013. Thera Technologies.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.maps.GoogleMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.thera.zen.core.ZenLog;
import io.thera.zen.core.ZenNavigationManager;
import io.thera.zen.core.ZenResManager;
import io.thera.zen.geo.ZenMap;


public abstract class ZenFragment extends Fragment {
    /*
        NAVIGATION PARAMETERS.
     */

    //private  Map<String, Map<Class,Object>> parameters = new HashMap<String, Map<Class, Object>>();

    protected List<Object> parameters =  new ArrayList<Object>();

    private  String title;

    public   String getTitle () {

        return title;

    }

    private FragmentActivity currentActivity;

    private int layoutId;

    private View rootView;

    // map support vars
    //added for maps
    public boolean hasMap = false;

    private ZenMap mapFrag;

    protected GoogleMap map;

    private int mapContainerId;

    private boolean isNew = true;

    private Bitmap onAnimScreen;

    public ZenFragment () {

		/*
		 * EMPTY CONSTRUCTOR
		 */

    }

    public void setVariables ( FragmentActivity a , String title, Integer layoutId) {

        ZenLog.l("SETTING VARIABLES " + title + " - " + ((Object) this).getClass().getCanonicalName())  ;
        this.currentActivity 	= a;
        this.title 				= title;
        this.layoutId 			= layoutId;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ZenLog.l("Saving FRAGMENT STATE");
        super.onSaveInstanceState(outState);
        outState.putInt("layoutid", this.layoutId);
    }

    public Object findViewById(int id) {
        return rootView.findViewById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try{
            long p = System.nanoTime();

            ZenLog.l("CREATING VIEW "+((Object) this).getClass().getCanonicalName());
            ZenLog.l(this.layoutId);
            if (savedInstanceState != null) {
                ZenLog.l("RESUMING FRAGMENT");
                if (this.layoutId == 0) {
                    ZenLog.l("RELOADING LAYOUT ON RESUME");
                    this.layoutId = savedInstanceState.getInt("layoutid");
                }
            }
            ZenLog.l(this.layoutId);

            rootView = inflater.inflate(layoutId, container, false);

            long d = System.nanoTime();
            ZenLog.l("TIME to inflate view "+(d-p));

        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }

        /*
         * 	RETURNING VIEW ELEMENT.
         *  SUBCLASS MUST PROVIDE IMPLEMENTATION
         *  TO GETELEMENTS AND SETUPELEMENTS,
         */
        //ZenAppManager.moveDrawer(true);
        //parameters = ZenNavigationManager.getParameters();

        preLoad();
        if (!isNew) {
            getElements();
            buildElements();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // avoid memory leaks
        rootView = null;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim != 0) {

            Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);

            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (isNew) {
                        getElements();
                        buildElements();
                        isNew = false;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            return anim;
        }
        else {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
    }


	/*
	 * THESE METHODS MUST BE IMPLEMENTED IN SUBCLASS.
	 */

    //basic setter and getter methods
	/*
	 *
	 */

    public void preLoad() {

    }

    public abstract void getElements();


    public abstract void buildElements();

    /**
     * BISOGNA CONTROLLARE CHE OGNI OGGETTO USATO IN PROCESS
     *
     * E OTTENUTO IN GETELEMENTS(); NON SIA NULL.
     *
     * process() viene chiamato PRIMA di getElements();
     *
     * se l'elemento di layout preso è null, va recuperato nuovamente.
     *
     * insert
     *
     * if (canProcess) { .. code here ..}
     *
     */

	/*
	 * HANDLER METHODS.
	 */

    public abstract void handleTouch();

    public static void sendParameters(Object o) {

        List<Object> parameters = new ArrayList<Object>();
        parameters.add(o);

        ZenNavigationManager.setParameters(parameters);
    }

    public static void sendParameters(Object[] o) {
        List<Object> parameters = new ArrayList<Object>();
        for (int i=0; i<o.length; i++) {
            parameters.add(o[i]);
        }

        ZenNavigationManager.setParameters(parameters);

    }

    public void addMap(int containerId) {

        hasMap = true;
        mapContainerId = containerId;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        ZenLog.l("IM HERE!!!!");

        super.onActivityCreated(savedInstanceState);

        if (hasMap) {
            setMapFrag();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (hasMap) {
            loadMap();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (hasMap) {
            detachMap();
        }
    }

    public void setMapFrag() {
        FragmentManager fm = getChildFragmentManager();
        mapFrag = (ZenMap) fm.findFragmentById(mapContainerId);
        if (mapFrag == null) {
            ZenLog.l("MAP FRAGMENT NULL: CREATING IT");
            mapFrag = ZenMap.create();
            fm.beginTransaction().add(mapContainerId, mapFrag).commit();
            // fix
            map = null;
        }
    }

    public void loadMap() {
        if (map == null) {
            ZenLog.l("MAP: LOADING");
            map = mapFrag.getMap();
            if (map != null) {
                setMap();
                //loadMapBitmap();
            }
        }
    }

    public void setMap() {
        ZenLog.l("SETMAP CALLED");

        map.setMyLocationEnabled(true);

    }

    public void detachMap() {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    @Override
    public void onPause() {
        if (hasMap) {
            //loadMapBitmap();
            ((View) findViewById(mapContainerId)).setBackgroundDrawable(new BitmapDrawable(onAnimScreen));
        }
        super.onPause();
    }

    public void loadMapBitmap() {
        //int w = getView().getWidth();
        onAnimScreen = Bitmap.createBitmap(200,150,Bitmap.Config.ARGB_8888);

        GoogleMap.SnapshotReadyCallback scb = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                ZenLog.l("MAH?!");
                onAnimScreen = bitmap;
            }
        };

        map.snapshot(scb);

    }
    */

}
