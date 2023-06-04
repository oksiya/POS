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
import android.widget.Toast;

import com.example.pos.databinding.FragmentPettyCashBinding;

public class PettyCash extends Fragment {

    private FragmentPettyCashBinding binding;
    private SharedPreferences sharedPreferences;
    public  static String pettyCash;
    private static final String PREF_ADD_CASH_BUTTON = "pref_add_cash_button";
    private static final String PREF_WITHDRAW_CASH_BUTTON = "pref_withdraw_cash_button";

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentPettyCashBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences("Onboarding", getContext().MODE_PRIVATE);

        binding.buttonAddCash.setOnClickListener(view1 -> {
            pettyCash = "ADD";
            Toast toast = Toast.makeText(getActivity(), "Add Cash", Toast.LENGTH_SHORT);
            toast.show();
            showExplanationDialog("Add Cash Button", "This button allows you to input money that belong to the business.", PREF_ADD_CASH_BUTTON);
            NavHostFragment.findNavController(PettyCash.this)
                   .navigate(R.id.to_petty_cash);
        });

        binding.buttonDrawCash.setOnClickListener(view2 -> {
            pettyCash = "WITHDRAW";
            Toast toast = Toast.makeText(getActivity(), "Withdraw Cash", Toast.LENGTH_SHORT);
            toast.show();
            showExplanationDialog("Withdraw Cash Button", "This button allows you to take out money out of the business for expenses.", PREF_WITHDRAW_CASH_BUTTON);
            NavHostFragment.findNavController(PettyCash.this)
                   .navigate(R.id.to_petty_cash);
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}