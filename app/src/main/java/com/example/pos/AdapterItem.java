package com.example.pos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdapterItem extends ArrayAdapter<Item> {

    private static class ViewHolder{
        TextView name;
        TextView code;
        TextView price;
    }

    private  Context context;
    private  int resource;
    private List<Item> objects;

    public AdapterItem(@NonNull Context context, int resource, @NonNull ArrayList<Item> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(getItem(position) != null){

            String category = getItem(position).getCategory();
            String name = getItem(position).getName();
            int code = getItem(position).getCode();
            double price = getItem(position).getPrice();

            Item item = new Item(category, name, code, price);

            ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(resource, parent, false);
                holder = new ViewHolder();
                holder.name = convertView.findViewById(R.id.nameView);
                holder.code = convertView.findViewById(R.id.codeView);
                holder.price = convertView.findViewById(R.id.priceView);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();

            }

            holder.name.setText(item.getName());
            holder.code.setText(HelperFunctions.intToString(item.getCode()));
            holder.price.setText(HelperFunctions.doubleToString(item.getPrice()));

            return convertView;
        }

        return null;
    }

    public void removeItem(int position) {
        objects.remove(position);
        notifyDataSetChanged();
    }
}

