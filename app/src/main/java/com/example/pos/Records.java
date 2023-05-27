package com.example.pos;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pos.databinding.FragmentFirstBinding;
import com.example.pos.databinding.FragmentRecordsBinding;

import org.jetbrains.annotations.Nullable;

public class Records extends Fragment {

    private FragmentRecordsBinding binding;
    private RecordsViewModel recordsViewModel;
    private SharedPreferences sharedPreferences;

    private static final String PREF_SALES_BUTTON = "pref_sales_button";
    private static final String PREF_STOCK_BUTTON = "pref_stock_button";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRecordsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordsViewModel = new ViewModelProvider(requireActivity()).get(RecordsViewModel.class);

        binding.buttonRecordSales.setOnClickListener(view1 -> {
            recordsViewModel.setMode("sales");
            NavHostFragment.findNavController(Records.this)
                    .navigate(R.id.to_view);
        });

        binding.buttonRecordStock.setOnClickListener(view2 -> {
            recordsViewModel.setMode("stock");
            NavHostFragment.findNavController(Records.this)
                    .navigate(R.id.to_view);
        });
    }

    /*
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences("Onboarding", getContext().MODE_PRIVATE);

        binding.buttonRecordSales.setOnClickListener(view1 -> {

            ViewRecords salesFragment = new ViewRecords();
            Bundle bundle = new Bundle();
            bundle.putString("mode", "sales");
            salesFragment.setArguments(bundle);
            System.out.println("----------------ARGUMENTS IS-------------------");
            System.out.println(this.getArguments());
            System.out.println(salesFragment.getArguments());
            showExplanationDialog("Sales Button", "This button allows you to view the sales you have made.", PREF_SALES_BUTTON);
             NavHostFragment.findNavController(Records.this)
                   .navigate(R.id.to_view);
        });

        binding.buttonRecordStock.setOnClickListener(view2 -> {

            ViewRecords stockFragment = new ViewRecords();
            Bundle bundle = new Bundle();
            bundle.putString("mode", "stock");
            stockFragment.setArguments(bundle);
            System.out.println("----------------ARGUMENTS IS-------------------");
            System.out.println(stockFragment.getArguments());
            showExplanationDialog("Stock Button", "This button allows you to view the stock available.", PREF_STOCK_BUTTON);
             NavHostFragment.findNavController(Records.this)
                   .navigate(R.id.to_view);

        });

    }

     */

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}