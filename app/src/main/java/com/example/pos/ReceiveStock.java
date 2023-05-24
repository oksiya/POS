package com.example.pos;

import android.database.SQLException;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.pos.databinding.FragmentReceiveStockBinding;

public class ReceiveStock extends Fragment {

    private FragmentReceiveStockBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentReceiveStockBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try (SQLHelper database = new SQLHelper(this.getContext())){

            binding.receive.setOnClickListener(view1 -> {

                String category = binding.categoryGroup.getText().toString();
                String name = binding.itemName.getText().toString();
                String strCode = binding.itemCode.getText().toString();
                int code = HelperFunctions.stringToInteger(strCode);
                String strPrice = binding.itemPrice.getText().toString();
                double price = HelperFunctions.stringToDouble(strPrice);
                String strQuantity = binding.itemQuantity.getText().toString();
                int quantity = HelperFunctions.stringToInteger(strQuantity);

                if(TextUtils.isEmpty(category) ||
                        TextUtils.isEmpty(name) ||
                        TextUtils.isEmpty(strCode) ||
                        TextUtils.isEmpty(strPrice)){

                    Toast toast = Toast.makeText(getActivity(), "Fill Up Every Field!", Toast.LENGTH_SHORT);
                    toast.show();

                }else{
                    boolean checkCat = database.checkCategory(category);
                    if(checkCat){
                        //CATEGORY EXIST SO WE ONLY INSERT IN THE STOCK
                        for(int i = 0; i < quantity; i++){
                            database.insertItem(category.trim(), name.trim(), code, price);
                        }
                    }else {
                        //CATEGORY DOESN'T EXIST, WE INSERT IT AND THE STOCK
                        database.insertCategory(category);
                        for(int i = 0; i < quantity; i++){
                            database.insertItem(category, name, code, price);
                        }

                    }

                }

            });

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}