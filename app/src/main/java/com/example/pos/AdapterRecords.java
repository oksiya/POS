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

public class AdapterRecords extends ArrayAdapter<SSRecords> {

    private static class SalesHolder {
        TextView account;
        TextView date;
        TextView category;
        TextView name;
        TextView code;
        TextView price;
        TextView debit;
        TextView credit;
    }

    private static class StockHolder {
        TextView quantity;
        TextView category;
        TextView date;
        TextView name;
        TextView code;
        TextView price;
    }

    private Context context;
    private int resource;
    private List<SSRecords> objects;
    private boolean isSalesMode;

    public AdapterRecords(@NonNull Context context, int resource, @NonNull ArrayList<SSRecords> objects, boolean isSalesMode) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.isSalesMode = isSalesMode;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String account = getItem(position).getAccount();
        String date = getItem(position).getDate();
        String category = getItem(position).getCategory();
        String name = getItem(position).getName();
        int code = getItem(position).getCode();
        Double price = getItem(position).getPrice();
        Double debit = getItem(position).getDebit();
        Double credit = getItem(position).getCredit();
        int quantity = getItem(position).getQuantity();

        AdapterRecords.SalesHolder salesHolder;
        AdapterRecords.StockHolder stockHolder;

        if (isSalesMode) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(resource, parent, false);

                salesHolder = new AdapterRecords.SalesHolder();
                salesHolder.account = convertView.findViewById(R.id.account);
                salesHolder.date = convertView.findViewById(R.id.date);
                salesHolder.category = convertView.findViewById(R.id.category);
                salesHolder.name = convertView.findViewById(R.id.name);
                salesHolder.code = convertView.findViewById(R.id.code);
                salesHolder.debit = convertView.findViewById(R.id.debit);
                salesHolder.credit = convertView.findViewById(R.id.credit);

                convertView.setTag(salesHolder);
            } else {
                salesHolder = (AdapterRecords.SalesHolder) convertView.getTag();
            }

            salesHolder.account.setText(account);
            salesHolder.date.setText(date);
            salesHolder.category.setText(category);
            salesHolder.name.setText(name);
            salesHolder.code.setText(String.valueOf(code));
            salesHolder.debit.setText(String.valueOf(debit));
            salesHolder.credit.setText(String.valueOf(credit));
        } else {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(resource, parent, false);

                stockHolder = new AdapterRecords.StockHolder();
                stockHolder.date = convertView.findViewById(R.id.dateStock);
                stockHolder.category = convertView.findViewById(R.id.categoryStock);
                stockHolder.name = convertView.findViewById(R.id.nameStock);
                stockHolder.code = convertView.findViewById(R.id.codeStock);
                stockHolder.price = convertView.findViewById(R.id.priceStock);
                stockHolder.quantity = convertView.findViewById(R.id.quantityStock);


                convertView.setTag(stockHolder);
            } else {
                stockHolder = (AdapterRecords.StockHolder) convertView.getTag();
            }

            stockHolder.date.setText(date);
            stockHolder.category.setText(category);
            stockHolder.name.setText(name);
            stockHolder.code.setText(String.valueOf(code));
            stockHolder.price.setText(String.valueOf(price));
            stockHolder.quantity.setText(String.valueOf(quantity));


        }

        return convertView;
    }
}

