package com.orl.chigger.tri.Library;

/**
 * Created by EN on 22.5.2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.orl.chigger.tri.R;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class PresentationAdapter extends ArrayAdapter<String> {
    String author;
    DatabaseHandler database;
    public PresentationAdapter(Context context, List<String> users, String author) {
        super(context, 0, users);
        database = new DatabaseHandler(context);
        this.author = author;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String presentation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_speakers, parent, false);
        }
        try {
            Long presentationId = database.getPresentationIdByPresentationPosition(author,position);
            Long sectionId = database.getSectionIdByPresentationId(presentationId);
            String sectionName = database.getSectionName(sectionId);
            String sectionDate = database.getDateBySectionId(sectionId);
            String sectionHall = database.getSectionHall(sectionId);
            String sectionTime = database.getSectionTime(sectionId);
            TextView presentationLabel = (TextView) convertView.findViewById(R.id.lblListItem);
            TextView info = (TextView) convertView.findViewById(R.id.speakerOrInfo);
            TextView date = (TextView) convertView.findViewById(R.id.dateTimeHall);
            presentationLabel.setTextColor(Color.parseColor("#66CCFF"));
            presentationLabel.setText(presentation);
            //Day - Time - Hall
            date.setText(sectionDate + " " +sectionTime + " " +sectionHall);
            info.setText("Section: " + sectionName);
            CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkBox);
            checkbox.setChecked(false);
            if (database.isPresentationInPersonal(presentationId)){
                checkbox.setChecked(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}