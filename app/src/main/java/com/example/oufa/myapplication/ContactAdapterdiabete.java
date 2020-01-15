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
 * Created by oufa on 31/03/2018.
 */

public class ContactAdapterdiabete extends ArrayAdapter {
    List lis=new ArrayList();
    public ContactAdapterdiabete(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void add(Contactsdiabete object) {
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
            row=layoutInflater.inflate(R.layout.row_layoutdiabete,parent,false);
            contactholder=new Contactholder();
            contactholder.tx_date=(TextView)row.findViewById(R.id.tx_date);
            contactholder.tx_glyc=(TextView)row.findViewById(R.id.tx_glyc);
            contactholder.tx_status=(TextView)row.findViewById(R.id.tx_status);
            row.setTag(contactholder);

        }
        else {
            contactholder=(Contactholder)row.getTag();
        }
        Contactsdiabete contacts=(Contactsdiabete)this.getItem(position);
        contactholder.tx_glyc.setText(contacts.getGlyc());
        contactholder.tx_date.setText(contacts.getDate());
        contactholder.tx_status.setText(contacts.getStatus());



        return row;
    }
    static class Contactholder{
        TextView tx_date,tx_glyc,tx_status;
    }
}
