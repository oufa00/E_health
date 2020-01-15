package com.example.oufa.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oufa on 01/04/2018.
 */

public class ContactAdaptertension extends ArrayAdapter {
    List lis=new ArrayList();
    public ContactAdaptertension(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void add(Contactstension object) {
        super.add(object);
        lis.add(object);
    }

    @Override
    public int getCount() {
        return lis.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return lis.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row=convertView;
        Contactholder contactholder;
        if(row==null){
            LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.row_layouttension,parent,false);
            contactholder=new Contactholder();
            contactholder.tx_date=(TextView)row.findViewById(R.id.tx_date);
            contactholder.tx_max=(TextView)row.findViewById(R.id.tx_max);
            contactholder.tx_min=(TextView)row.findViewById(R.id.tx_min);
            row.setTag(contactholder);

        }
        else {
            contactholder=(Contactholder)row.getTag();
        }
        Contactstension contacts=(Contactstension) this.getItem(position);
        contactholder.tx_min.setText(contacts.getMin());
        contactholder.tx_date.setText(contacts.getDate());
        contactholder.tx_max.setText(contacts.getMax());
        return row;
    }
    static class Contactholder{
        TextView tx_date,tx_max,tx_min;
    }
}
