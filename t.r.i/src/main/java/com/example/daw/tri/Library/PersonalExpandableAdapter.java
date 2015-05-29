package com.example.daw.tri.Library;

/**
 * Created by EN on 20.4.2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.daw.tri.Activities.Personal;
import com.example.daw.tri.R;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public class PersonalExpandableAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private DatabaseHandler database;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;
    private CheckBox checkBox;

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
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        TextView speaker = (TextView) convertView.findViewById(R.id.speakerOrInfo);
        txtListChild.setText(childText);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        try {
            Long section = database.getNthSectionFromPersonal(groupPosition);
            Long presentation = database.getNthPresentationFromPersonal(section,childPosition);
            speaker.setText(database.getPresentationSpeakerByPresentationId(presentation));
            if (database.isPresentationInPersonal(presentation)){
                checkBox.setChecked(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
        Button headerButton = (Button) convertView.findViewById(R.id.button4);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView hall = (TextView) convertView.findViewById(R.id.hall);
        TextView chairman = (TextView) convertView.findViewById(R.id.chairman);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        lblListHeader.setTextColor(Color.parseColor("#828282"));
        if (headerTitle == "Your personal program is empty."){
            headerButton.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
            hall.setVisibility(View.GONE);
            return convertView;
        }

        try {
            Long section = database.getNthSectionInPersonal(groupPosition);
            time.setText(database.getDateBySectionId(section)+" "+database.getSectionTime(section));
            hall.setText(database.getSectionHall(section));
            chairman.setText(database.getSectionChairmanBySectionId(section));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        headerButton.setText("-");
        headerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Long id = database.getNthSectionFromPersonal(groupPosition);
                    database.removePresentationsFromPersonalBySection(id);
                    database.removeSectionFromPersonal(id);
                } catch (SQLException e) {
                        e.printStackTrace();
                    }
                Intent intent = new Intent(_context, Personal.class);
                _context.startActivity(intent);
                ((Activity)_context).finish();
                }
            });
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
