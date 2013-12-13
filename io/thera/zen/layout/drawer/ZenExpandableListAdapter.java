package io.thera.zen.layout.drawer;

/**
 * Created by marcostagni on 04/12/13.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import io.thera.zen.core.ZenAppManager;
import io.thera.zen.core.ZenLog;
import io.thera.zen.core.ZenResManager;
import io.thera.zen.core.ZenSettingsManager;

public class ZenExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private Map<String, List<String>> _listDataChild;

    public ZenExpandableListAdapter(Context context, List<String> listDataHeader, Map<String, List<String>> listChildData) {

        ZenLog.l("listDataHeader" +(listDataHeader == null));
        this._context = context;
        this._listDataHeader = new ArrayList<String>();
        this._listDataHeader = listDataHeader;

        this._listDataChild = new HashMap<String, List<String>>();
        this._listDataChild = listChildData;
        ZenLog.l("SIZE " + this._listDataHeader.size());
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(ZenResManager.getLayoutId("drawer_listview_item"), null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(ZenResManager.getResourceId("group_item"));

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(ZenResManager.getLayoutId("drawer_listview_group"), null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(ZenResManager.getResourceId("group_header"));

        String fontName = ZenSettingsManager.getFont(convertView.getId()); //errore

        lblListHeader.setTypeface(Typeface.createFromAsset(ZenAppManager.getActivity().getAssets(), "fonts/" + fontName), 1);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
