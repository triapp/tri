package com.orl.chigger.tri.Library;

/**
 * Created by EN on 20.4.2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orl.chigger.tri.Activities.Personal;
import com.orl.chigger.tri.R;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public class PersonalExpandableAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private DatabaseHandler database;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;
    private Button delete;

    public PersonalExpandableAdapter(Context context, List<String> listDataHeader,
                             HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.database = new DatabaseHandler(context);
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
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.personal_child, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.nameOfPersonalPresentation);
        TextView speaker = (TextView) convertView.findViewById(R.id.personalPresentationInfo);
        speaker.setTextColor(Color.parseColor("#99CCFF"));
        txtListChild.setTextColor(Color.parseColor("#66CCFF"));
        txtListChild.setText(childText);
        delete = (Button) convertView.findViewById(R.id.delete);
        try {
            final Long section = database.getNthSectionFromPersonal(groupPosition);
            final Long presentation = database.getNthPresentationFromPersonal(section,childPosition);
            speaker.setText("Speaker: " + database.getPresentationSpeakerByPresentationId(presentation));
        } catch (SQLException e) {
            e.printStackTrace();
        }



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(_context, "Presentation was removed from your personal programme.", Toast.LENGTH_SHORT).show();
                try {
                    final Long section = database.getNthSectionFromPersonal(groupPosition);
                    final Long presentation = database.getNthPresentationFromPersonal(section,childPosition);
                    database.removePresentationFromPersonal(presentation);
                    if(!database.doesHavePersonalSectionPresentations(section)){
                        database.removeSectionFromPersonal(section);
                        Intent intent = new Intent(_context, Personal.class);
                        Bundle b = new Bundle();
                        b.putInt("expanded",-1);
                        intent.putExtras(b);
                        _context.startActivity(intent);
                        ((Activity)_context).finish();
                    } else {
                        Intent intent = new Intent(_context, Personal.class);
                        Bundle b = new Bundle();
                        b.putInt("expanded",database.getPositionInPersonalBySectionId(section));
                        intent.putExtras(b);
                        _context.startActivity(intent);
                        ((Activity)_context).finish();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }});

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
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView hall = (TextView) convertView.findViewById(R.id.hall);
        TextView chairman = (TextView) convertView.findViewById(R.id.chairman);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        lblListHeader.setTextColor(Color.parseColor("#828282"));
        if (headerTitle == "Your personal program is empty."){
            chairman.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
            hall.setVisibility(View.GONE);
            return convertView;
        }

        try {
            Long section = database.getNthSectionInPersonal(groupPosition);
            time.setText(database.getDateBySectionId(section)+"  "+database.getSectionTime(section) + " " + database.getSectionHall(section));
            hall.setVisibility(View.GONE);
            chairman.setText("Chairman: " + database.getSectionChairmanBySectionId(section));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


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
