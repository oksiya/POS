package com.example.pos;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pos.databinding.FragmentCategoryBinding;
import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;
    public static ArrayList<Item> items;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Button> iButtons = new ArrayList<>();
        items = new ArrayList<>();

        iButtons.add(binding.item1); iButtons.add(binding.item2); iButtons.add(binding.item3); iButtons.add(binding.item4);
        iButtons.add(binding.item5); iButtons.add(binding.item6); iButtons.add(binding.item7); iButtons.add(binding.item8);
        iButtons.add(binding.item9); iButtons.add(binding.item10); iButtons.add(binding.item11); iButtons.add(binding.item12);
        iButtons.add(binding.item13); iButtons.add(binding.item14); iButtons.add(binding.item15); iButtons.add(binding.item16);
        iButtons.add(binding.item17); iButtons.add(binding.item18); iButtons.add(binding.item19); iButtons.add(binding.item20);
        iButtons.add(binding.item21); iButtons.add(binding.item22); iButtons.add(binding.item23); iButtons.add(binding.item24);

        for(int r = 0; r < Purchase.names.size(); r++){

            iButtons.get(r).setText(Purchase.names.get(r));
        }

        try (SQLHelper database = new SQLHelper(this.getContext())){
            for(int i = 0; i < iButtons.size(); i++){
                int finalI = i;
                String name = iButtons.get(finalI).getText().toString();
                if(name.equals("Item")){
                    iButtons.get(finalI).setVisibility(View.GONE);
                }
                iButtons.get(i).setOnClickListener(view1 -> {

                    if(name.equals("Item")){
                        Toast toast = Toast.makeText(getActivity(), "ERROR : Invalid Selection!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        items.add(database.selectedItem(name));
                    }

                });


            }
            binding.done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    NavHostFragment.findNavController(CategoryFragment.this)
                            .navigate(R.id.after_item);
                }
            });

        }catch (CursorIndexOutOfBoundsException e){

            Toast toast = Toast.makeText(getActivity(), "ERROR : Invalid Selection!", Toast.LENGTH_SHORT);
            toast.show();
        }catch (NullPointerException e){

            Toast toast = Toast.makeText(getActivity(), "ERROR : Invalid Selection!", Toast.LENGTH_SHORT);
            toast.show();
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