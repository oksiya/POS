package com.example.pos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
                String strMoney = HelperFunctions.roundToTwoDecimalPlaces(sum);
                binding.editMoneyBox.setText("R "+strMoney);

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

        binding.onCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void showFindDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.on_credit_payment, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final EditText editNumberPay = dialogView.findViewById(R.id.editTextOnCredit);
        Button buttonSearch = dialogView.findViewById(R.id.buttonSearch);
        Button buttonCancelSearch = dialogView.findViewById(R.id.buttonCancelSearch);

        final AlertDialog dialog = builder.create();
        dialog.show();

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneEdit = editNumberPay.getText().toString().trim();

                SQLHelper database = new SQLHelper(getContext());
                Customer customer = database.getCustomerByPhone(phoneEdit);

                boolean exists = database.checkCustomerExists(phoneEdit);
                if(exists){


                    Toast.makeText(getContext(), "Client Found", Toast.LENGTH_SHORT).show();
                    // Show confirmation dialog
                    AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getContext());
                    confirmBuilder.setTitle("Confirm Client Details");
                    confirmBuilder.setMessage(customer.toString());
                    confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {



                            binding.editMoneyBox.setText(customer.getName());

                            onResume();
                            dialogInterface.dismiss();
                            dialog.dismiss();
                        }
                    });
                    confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog confirmDialog = confirmBuilder.create();
                    confirmDialog.show();

                }else{

                    Toast.makeText(getContext(), "ERROR: Client Not Found", Toast.LENGTH_SHORT).show();

                }

            }
        });

        buttonCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}