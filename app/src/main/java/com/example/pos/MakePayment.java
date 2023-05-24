package com.example.pos;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pos.databinding.FragmentMakePaymentBinding;

import java.util.ArrayList;


public class MakePayment extends Fragment {

    private FragmentMakePaymentBinding binding;
    public static double sum;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMakePaymentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Button> arrButtons = new ArrayList<>();
        double [] arrMoney = {0.10, 0.20, 0.50, 1.00, 2.00, 5.00, 10.00, 20.00, 50.00, 100.00, 200.00};
        ArrayList<Double> tempMoney = new ArrayList<>();


        arrButtons.add(binding.tenCents);
        arrButtons.add(binding.twentyCents);
        arrButtons.add(binding.fiftyCents);
        arrButtons.add(binding.oneRand);
        arrButtons.add(binding.twoRands);
        arrButtons.add(binding.fiveRands);
        arrButtons.add(binding.tenRands);
        arrButtons.add(binding.twentyRands);
        arrButtons.add(binding.fiftyRands);
        arrButtons.add(binding.hundredRands);
        arrButtons.add(binding.twoHundredRands);

        for(int i = 0; i < arrButtons.size(); i++){
            int finalI = i;
            arrButtons.get(i).setOnClickListener(view1 -> {

                tempMoney.add(arrMoney[finalI]);
                sum = HelperFunctions.sum(tempMoney);
                String strMoney = HelperFunctions.doubleToString(sum);
                binding.editMoneyBox.setText(strMoney);

                });

        }

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Replace "FragmentB" with the ID of the fragment you want to navigate to
                if(tempMoney.isEmpty()){
                    sum = 0.0;
                    Navigation.findNavController(requireView()).navigate(R.id.PurchaseFragment);
                } else {
                    Toast toast = Toast.makeText(getActivity(), "ERROR : Select Payment Type!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        binding.done.setOnClickListener(view12 -> {

            if(tempMoney.isEmpty()){
                Toast toast = Toast.makeText(getActivity(), "ERROR : Please Select Amount!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                NavHostFragment.findNavController(MakePayment.this)
                        .navigate(R.id.after_pay);
            }



        });

        binding.clear.setOnClickListener(view13 -> {
                binding.editMoneyBox.getText().clear();
                tempMoney.clear();
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}