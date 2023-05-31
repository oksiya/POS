package com.example.pos;




import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.pos.databinding.FragmentPurchaseBinding;
import java.util.ArrayList;
import java.util.Objects;


public class Purchase extends Fragment {

    private OnBackPressedCallback onBackPressedCallback;
    private FragmentPurchaseBinding binding;
    private static final ArrayList<Item> containerItem = new ArrayList<>();
    public static ArrayList<String> names;
    private AdapterItem adapterItem = null;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentPurchaseBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
       onSaveInstanceState(savedInstanceState);

        ListView listView = binding.listView;
        ArrayList<Button> buttons = new ArrayList<>();
        ArrayList<String> category;
        buttons.add(binding.cat1);
        buttons.add(binding.cat2);
        buttons.add(binding.cat3);
        buttons.add(binding.cat4);
        buttons.add(binding.cat5);
        buttons.add(binding.cat6);
        buttons.add(binding.cat7);
        buttons.add(binding.cat8);
        buttons.add(binding.cat9);
        buttons.add(binding.cat10);
        buttons.add(binding.cat11);
        buttons.add(binding.cat12);


        try (SQLHelper database = new SQLHelper(this.getContext())){

            category = (ArrayList<String>) database.getAllCategories();
            names = new ArrayList<>();
            int size = category.size();
            for(int i = 0; i < size; i++){
                buttons.get(i).setText(category.get(i));
            }

            for(int i = 0; i < buttons.size(); i++){
                int finalI = i;
                if(buttons.get(i).getText().equals("Category")){
                    buttons.get(i).setVisibility(View.GONE);
                }
                buttons.get(i).setOnClickListener(view1 -> {

                    try{
                        String catName = category.get(finalI);
                        getNames(database, catName);
                        NavHostFragment.findNavController(Purchase.this)
                                .navigate(R.id.to_items);
                    }catch (IndexOutOfBoundsException e){

                        Toast toast = Toast.makeText(getActivity(), "ERROR : Invalid Selection!", Toast.LENGTH_SHORT);
                        toast.show();
                    }


                });

            }

            binding.done.setOnClickListener(view110 -> {
                double dblTest = HelperFunctions.stringToDouble(total(containerItem));
                Toast toast;
                if (dblTest > 0) {
                    toast = Toast.makeText(getActivity(), "Cash is Short!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    toast = Toast.makeText(getActivity(), "Change is " + binding.textView.getText(), Toast.LENGTH_SHORT);
                    toast.show();

                    boolean insert = false;
                    int deleteCount = 1; // Specify the number of items to delete
                    for (int counter = 0; counter < containerItem.size() - 1; counter++) {
                        Item i = containerItem.get(counter);

                        if (i.getName().equals("CASH")) {
                            continue;
                        }

                        insert = database.recordTransaction("Sale", HelperFunctions.getDate(),
                                i.getCategory(), i.getName(), i.getCode(), i.getPrice(), null);

                        database.deleteRecord(i.getCode(), deleteCount);
                    }

                    if (insert) {
                        toast = Toast.makeText(getActivity(), "Items Recorded!", Toast.LENGTH_SHORT);
                        toast.show();
                        containerItem.clear();
                        adapterItem.clear();
                        MakePayment.sum = 0.0;
                        String defaultText = getResources().getString(R.string.total);
                        binding.textView.setText(defaultText);
                        binding.textView.setTextColor(Color.LTGRAY);
                    } else {
                        toast = Toast.makeText(getActivity(), "ERROR: Could Not Record!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });




        }catch (SQLException e){
            e.printStackTrace();
        }

        ArrayList<Item> tempItem = CategoryFragment.items;
        if(tempItem != null) {


            containerItem.addAll(tempItem);
            tempItem.clear();
            if(MakePayment.sum != 0.0){
                containerItem.add(enteredCash());
            }

            adapterItem = new AdapterItem(Objects.requireNonNull(this.getContext()), R.layout.adapterview, containerItem);
            listView.setAdapter(adapterItem);

            double dblTotal = HelperFunctions.stringToDouble(total(containerItem));
            if(dblTotal < 0){
                binding.textView.setTextColor(Color.RED);
            }else {

                binding.textView.setTextColor(Color.CYAN);
            }

            binding.textView.setText(total(containerItem));


        }

        binding.pay.setOnClickListener(view110 -> {

            if(containerItem.isEmpty()){
                Toast toast = Toast.makeText(getActivity(), "ERROR : No Items Selected!", Toast.LENGTH_SHORT);
                toast.show();
            } else {

                NavHostFragment.findNavController(Purchase.this)
                        .navigate(R.id.to_payment);
            }


        });

        binding.delete.setOnClickListener(view110 -> {

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((AdapterItem) listView.getAdapter()).removeItem(position);
                    binding.textView.setText(total(containerItem));
                    if(containerItem.isEmpty()){
                        String defaultText = getResources().getString(R.string.total);
                        binding.textView.setText(defaultText);
                        binding.textView.setTextColor(Color.LTGRAY);
                        MakePayment.sum = 0.0;
                    }
                }
            });

        });

    }

    private void getNames(SQLHelper database, String category){
        ArrayList<Item> items = (ArrayList<Item>) database.specificItems(category);
        if(!names.isEmpty()){
            names.clear();
        }
        for(int r = 0; r < items.size(); r++){

            Item i = items.get(r);
            String name = i.getName();
            if(!names.contains(name)){

                names.add(name);
            }

        }

    }

    private String total(ArrayList<Item> arrItem){
        String strSum = null;
        double sum = 0.0;
        for (Item i:
             arrItem) {
            if(i!=null){
                sum = sum + i.getPrice();
                strSum = HelperFunctions.roundToTwoDecimalPlaces(sum);
            }

        }
        return strSum;
    }

    private Item enteredCash(){

        String name = "CASH";
        int code = 0;
        double price = (-MakePayment.sum);
        int quantity = 0;
        return new Item(null, name, code, price, quantity);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button press
                NavHostFragment.findNavController(Purchase.this)
                        .navigate(R.id.back_home);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
