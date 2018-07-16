package com.example.tk_employee.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class ExpGroupAdapter extends BaseExpandableListAdapter{

    private Context context;
    private ArrayList<ParentClass> ContentClass;

    public ExpGroupAdapter(Context context, ArrayList<ParentClass> groups) {
        this.context = context;
        this.ContentClass = groups;
    }

    @Override
    public int getGroupCount() {
        return ContentClass.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChildClass> chList = ContentClass.get(groupPosition).getChildItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return ContentClass.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ChildClass> chList = ContentClass.get(groupPosition).getChildItems();
        return chList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentClass group = (ParentClass) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.list_group, null);
        }
        TextView placeName = (TextView) convertView.findViewById(R.id.pPlaceName);
        TextView placeRating=(TextView)convertView.findViewById(R.id.pPlaceRating);
        placeName.setText(group.pName);
        placeRating.setText("Rating "+group.pRating);

        placeName.setTypeface(null, Typeface.BOLD);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildClass child = (ChildClass) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        TextView placeName = (TextView) convertView.findViewById(R.id.cPlaceName);
        TextView placeRating=(TextView)convertView.findViewById(R.id.cPlaceRating);
        TextView placeAddr=(TextView)convertView.findViewById(R.id.cPlaceAddress);

        placeName.setText(child.cName);
        placeRating.setText("Rating "+child.cRating);
        placeAddr.setText(child.cLocation);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
