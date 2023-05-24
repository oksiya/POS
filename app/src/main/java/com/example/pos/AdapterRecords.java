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

    private static class SalesHolder{

        TextView account;
        TextView date;
        TextView category;
        TextView name;
        TextView code;
        TextView price;
        TextView debit;
        TextView credit;
    }

    private static class StockHolder{

        TextView category;
        TextView date;
        TextView name;
        TextView code;
        TextView price;
    }
    private  Context context;
    private  int resource;
    private List<SSRecords> objects;

    public AdapterRecords(@NonNull Context context, int resource, @NonNull ArrayList<SSRecords> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String account = getItem(position).getAccount();
        String date = getItem(position).getDate();
        String category = getItem(position).getCategory();
        String name = getItem(position).getName();
        int code = getItem(position).getCode();
        double price = getItem(position).getPrice();
        Double debit = getItem(position).getDebit();
        Double credit = getItem(position).getCredit();

        SSRecords stock = new SSRecords(date, category, name,code,price);
        SSRecords sales = new SSRecords(account, date, category, name,code,debit, credit);

        AdapterRecords.SalesHolder salesHolder;
        AdapterRecords.StockHolder stockHolder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);

            stockHolder = new AdapterRecords.StockHolder();
            stockHolder.date = convertView.findViewById(R.id.dateStock);
            stockHolder.category = convertView.findViewById(R.id.categoryStock);
            stockHolder.name = convertView.findViewById(R.id.nameStock);
            stockHolder.code = convertView.findViewById(R.id.codeStock);
            stockHolder.price = convertView.findViewById(R.id.priceStock);

            salesHolder = new AdapterRecords.SalesHolder();
            salesHolder.account = convertView.findViewById(R.id.account);
            salesHolder.date = convertView.findViewById(R.id.date);
            salesHolder.category = convertView.findViewById(R.id.category);
            salesHolder.name = convertView.findViewById(R.id.name);
            salesHolder.code = convertView.findViewById(R.id.code);
            salesHolder.debit = convertView.findViewById(R.id.debit);
            salesHolder.credit = convertView.findViewById(R.id.credit);


            convertView.setTag(salesHolder);
        }else {
            salesHolder = (AdapterRecords.SalesHolder) convertView.getTag();
           // stockHolder = (AdapterRecords.StockHolder) convertView.getTag();

        }

        //stockHolder.date.setText(stock.getDate());
        //stockHolder.category.setText(stock.getCategory());
        //stockHolder.name.setText(stock.getName());
        //stockHolder.code.setText(HelperFunctions.intToString(stock.getCode()));
        //stockHolder.price.setText(HelperFunctions.doubleToString(stock.getDebit()));


        salesHolder.account.setText(sales.getAccount());
        salesHolder.date.setText(sales.getDate());
        salesHolder.category.setText(sales.getCategory());
        salesHolder.name.setText(sales.getName());
        salesHolder.code.setText(HelperFunctions.intToString(sales.getCode()));
        salesHolder.debit.setText(HelperFunctions.doubleToString(sales.getDebit()));
        salesHolder.credit.setText(HelperFunctions.doubleToString(sales.getCredit()));


        return convertView;
    }
}
