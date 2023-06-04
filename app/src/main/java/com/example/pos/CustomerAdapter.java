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

public class CustomerAdapter extends ArrayAdapter<Customer> {
    private static class ViewHolder{
        TextView tittle;
        TextView name;
        TextView surname;
        TextView number;
        TextView amount;
    }

    private Context context;
    private  int resource;
    private List<Customer> objects;

    public CustomerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Customer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(getItem(position) != null){

            String tittle = getItem(position).getTittle();
            String name = getItem(position).getName();
            String surname = getItem(position).getSurname();
            String phone = getItem(position).getPhone();
            double amount = getItem(position).getAmount();


            Customer customer = new Customer(tittle, name, surname, phone, amount);

            ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(resource, parent, false);
                holder = new ViewHolder();
                holder.tittle = convertView.findViewById(R.id.client_view_titte);
                holder.name = convertView.findViewById(R.id.client_view_initials);
                holder.surname = convertView.findViewById(R.id.client_view_surname);
                holder.number = convertView.findViewById(R.id.client_view_number);
                holder.amount = convertView.findViewById(R.id.client_view_amount);

                convertView.setTag(holder);
            }else {
                holder = (CustomerAdapter.ViewHolder) convertView.getTag();

            }

            holder.tittle.setText(customer.getTittle());
            holder.name.setText(customer.getName());
            holder.surname.setText(customer.getSurname());
            holder.number.setText(customer.getPhone());
            String tempAmount = HelperFunctions.roundToTwoDecimalPlaces(customer.getAmount());
            holder.amount.setText(tempAmount);


            return convertView;
        }

        return null;
    }
}
