package io.thera.zen.core;

/**
 * Created by marcostagni on 26/11/13.
 * Revisited and optimized by Giovanni Barillari on 22/07/2014.
 *
 * Copyright © 2013-2014. Thera Technologies.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.maps.GoogleMap;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.thera.zen.geo.ZenMap;


public abstract class ZenFragment extends Fragment {
    //: parameters are parameters are injected by Zen
    protected List<Object> parameters =  new ArrayList<Object>();
    //: current is stored on instance savings. MUST contains only serializable
    protected Map<String, Object> current = new HashMap<String, Object>();

    //: class basic variables, injected by Zen
    private  String title;
    private int layoutId;

    //: reference to Fragment view, inited looking at layoutId
    private View rootView;

    //: Google maps support vars
    public boolean hasMap = false;
    private ZenMap mapFrag;
    protected GoogleMap map;
    private int mapContainerId;

    //: booleans needed by Zen to collect Fragment state
    private boolean isNew = true;
    private boolean isResumed = false;

    //: public method to gain Fragment title
    public String getTitle () { return title;}

    //: empty constructor (gi0baro: do we need that?)
    public ZenFragment () {}

    //: set Fragment base variables, called by Zen
    public void setVariables ( String title, Integer layoutId) {
        //ZenLog.l("SETTING VARIABLES " + title + " - " + ((Object) this).getClass().getCanonicalName())  ;
        this.title = title;
        this.layoutId = layoutId;
    }

    //: save current and layout on suspensions (needed for restore)
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //ZenLog.l("Saving FRAGMENT STATE");
        super.onSaveInstanceState(outState);
        outState.putInt("layoutid", this.layoutId);
        outState.putSerializable("current", (Serializable) current);
    }

    //: load view defined by layoutId, init helpers
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            //long p = System.nanoTime();
            //ZenLog.l("CREATING VIEW "+((Object) this).getClass().getCanonicalName());
            //ZenLog.l(this.layoutId);
            if (savedInstanceState != null) {
                //ZenLog.l("RESUMING FRAGMENT");
                isResumed = true;
                isNew = false;
                if (this.layoutId == 0) {
                    //ZenLog.l("RELOADING LAYOUT ON RESUME");
                    this.layoutId = savedInstanceState.getInt("layoutid");
                    current = (HashMap<String, Object>) savedInstanceState.getSerializable("current");
                }
            } else {
                isResumed = false;
                isNew = true;
            }
            //ZenLog.l(this.layoutId);
            rootView = inflater.inflate(layoutId, container, false);
            //long d = System.nanoTime();
            //ZenLog.l("TIME to inflate view "+(d-p));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }

        //: load parameters from Zen
        parameters = ZenApplication.navigation().getParameters();

        //: always launch preLoad method
        preLoad();
        //: if Fragment is resumed, we load helpers now (otherwise we'll wait for animations)
        if (!isNew) {
            //ZenLog.l("FRAGMENT NOT NEW");
            getElements();
            buildElements();
        }
        return rootView;
    }

    //: ensure memory cleaning for custom view implementation
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }

    //: useful shortcut find views in Fragment layout
    public Object findViewById(int id) {
        return rootView.findViewById(id);
    }

    //: override of animation on Fragment creation, load helpers if needed
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim != 0) {
            Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (isNew) {
                        getElements();
                        buildElements();
                        isNew = false;
                    }
                    renderMap();
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            return anim;
        }
        else {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
    }

    //: Zenfragment helpers
    //  : preLoad is called on fragment creation
    public void preLoad() {}
    //  : getElements and buildElements are called consequentially after Zen animations or resume
    public abstract void getElements();
    public abstract void buildElements();

    //: useful shortcuts to send parameters to another Fragment via Zen
    public static void sendParameters(Object o) {
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(o);

        ZenApplication.navigation().setParameters(parameters);
    }
    public static void sendParameters(Object[] o) {
        List<Object> parameters = new ArrayList<Object>();
        for (int i=0; i<o.length; i++) {
            parameters.add(o[i]);
        }

        ZenApplication.navigation().setParameters(parameters);

    }
    public static void sendParameters(List<Object> o) {
        ZenApplication.navigation().setParameters(o);
    }

    //: Google Maps methods - except for strange uses, user should override only setMap
    //  : used to add a map on fragment, shoud be called in preLoad() helper
    public void addMap(int containerId) {
        hasMap = true;
        mapContainerId = containerId;
    }
    //  : setup map fragment ensuring activity is loaded
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (hasMap) {
            setMapFrag();
        }
    }
    //  : ensure map rendering on Fragment resume
    @Override
    public void onResume() {
        super.onResume();
        if (hasMap) {
            loadMap();
            if (isResumed) {
                renderMap();
            }
        }
    }
    //  : detach the map from fragment on detach (needed on old Android versions to avoid view errors)
    @Override
    public void onDetach() {
        super.onDetach();
        if (hasMap) {
            detachMap();
        }
    }
    //  : this create the map Fragment inside the container defined by user
    public void setMapFrag() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager fm = getChildFragmentManager();
                mapFrag = (ZenMap) fm.findFragmentById(mapContainerId);
                if (mapFrag == null) {
                    //ZenLog.l("MAP FRAGMENT NULL: CREATING IT");
                    mapFrag = ZenMap.create();
                    fm.beginTransaction().add(mapContainerId, mapFrag).commit();
                    // fix
                    map = null;
                }
            }
        });
    }
    //  : load the map
    public void loadMap() {
        if (map == null) {
            map = mapFrag.getMap();
        }
    }
    //  : called by zen to load user's options
    public void renderMap() {
        if (map != null) {
            setMap();
        }
    }
    //  : user should override this method to set preferences/load data on map
    public void setMap() {
        map.setMyLocationEnabled(true);
    }
    //  : ensure cleaning and avoid layout errors
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
        map = null;
        mapFrag = null;
    }

}
