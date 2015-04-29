package com.example.daw.tri.Library;

/**
 * Created by EN on 20.4.2015.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daw.tri.R;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private DatabaseHandler database;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;
    private CheckBox checkBox;
    private Long idDay;
    private Long idHall;

    public ExpandableAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, Long id, Long hall) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.database = new DatabaseHandler(context);
        idDay = id;
        idHall = hall;
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
        txtListChild.setText(childText);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        checkBox.setChecked(false);
        try {
            Long section = database.getNthSection(groupPosition, idDay, idHall);
            Long presentation = database.getNthPresentation(section,childPosition);
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
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        Button headerButton = (Button) convertView.findViewById(R.id.button4);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        try {
            Long section = database.getNthSection(groupPosition, idDay, idHall);
            time.setText(database.getSectionTime(section));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        headerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Long id = database.getNthSection(groupPosition, idDay, idHall);
                    if (database.isSectionInPersonal(id)) {
                        Toast.makeText(_context, "This section is already in your personal program.", Toast.LENGTH_SHORT).show();
                    } else {
                        database.insertPersonalSection(id);
                       /* String alertMessage = database.checkPersonalForCollisions();
                        if (alertMessage !=""){
                            AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                            builder.setMessage(alertMessage)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }*/
                        Toast.makeText(_context, "Section was added to your personal program.", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
