package com.example.pos;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pos.databinding.FragmentOptionsBinding;

public class Options extends Fragment {

    private FragmentOptionsBinding binding;
    private SharedPreferences sharedPreferences;

    private static final String PREF_SELL_BUTTON = "pref_sell_button";
    private static final String PREF_PETTY_CASH_BUTTON = "pref_petty_cash_button";
    private static final String PREF_RECEIVE_STOCK_BUTTON = "pref_receive_stock_button";
    private static final String PREF_RECORDS_BUTTON = "pref_records_button";
    private static final String PREF_ANALYSIS_BUTTON = "pref_analysis_button";
    private static final String PREF_CUSTOMER_MANAGEMENT_BUTTON = "pref_customer_management_button";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences("Onboarding", getContext().MODE_PRIVATE);

        // Button to navigate to the "Sell" screen
        binding.buttonSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExplanationDialog("Sell Button", "This button allows you to process sales transactions for your products.", PREF_SELL_BUTTON);
                NavHostFragment.findNavController(Options.this).navigate(R.id.option_to_sell);
            }
        });

        // Button to navigate to the "Petty Cash" screen
        binding.buttonPettyCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExplanationDialog("Petty Cash Button", "By clicking this button, you can manage your cash flow and handle small expenses.", PREF_PETTY_CASH_BUTTON);
                NavHostFragment.findNavController(Options.this).navigate(R.id.option_to_cash);
            }
        });

        // Button to navigate to the "Receive Stock" screen
        binding.buttonReceiveStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExplanationDialog("Receive Stock Button", "Tapping on this button allows you to manage the inventory of your products.", PREF_RECEIVE_STOCK_BUTTON);
                NavHostFragment.findNavController(Options.this).navigate(R.id.option_to_receive);
            }
        });

        // Button to navigate to the "Records" screen
        binding.buttonRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExplanationDialog("Records Button", "Selecting this button provides access to various sales and transaction records.", PREF_RECORDS_BUTTON);
                NavHostFragment.findNavController(Options.this).navigate(R.id.option_to_records);
            }
        });

        binding.buttonAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExplanationDialog("Analysis Button", "By clicking this button, you can view the analysis of your business.", PREF_ANALYSIS_BUTTON);
                NavHostFragment.findNavController(Options.this).navigate(R.id.to_view_analysis);
            }
        });

        binding.buttonCustomerManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExplanationDialog("Customer Management Button", "By clicking this button, you can manage your customers who on credit.", PREF_CUSTOMER_MANAGEMENT_BUTTON);
                NavHostFragment.findNavController(Options.this).navigate(R.id.option_to_cash);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showExplanationDialog(String title, String explanation, String prefKey) {
        boolean explanationShown = sharedPreferences.getBoolean(prefKey, false);
        if (!explanationShown) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(title)
                    .setMessage(explanation)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

            // Save the flag indicating that the explanation has been shown for this button
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(prefKey, true);
            editor.apply();
        }
    }
}
