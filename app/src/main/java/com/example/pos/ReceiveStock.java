package com.example.pos;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.pos.databinding.FragmentReceiveStockBinding;

public class ReceiveStock extends Fragment {

    private FragmentReceiveStockBinding binding;
    private SharedPreferences sharedPreferences;

    private static final String PREF_RECEIVE_STOCK_ONBOARDING = "pref_receive_stock_onboarding";
    private static final String[] STEP_MESSAGES = {
            "Enter the category of the item you want to receive e.g. [FRUITS].",
            "Enter the name of the item e.g. [Orange].",
            "Enter the unique code of the item e.g. [12345] bar code.",
            "Enter the unit price of the item e.g. R [3.50] per orange.",
            "Enter the quantity of the item e.g. [25] for 25 oranges."
    };
    private int currentStep = 0;

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

        sharedPreferences = requireContext().getSharedPreferences("Onboarding", getContext().MODE_PRIVATE);

        // Check if onboarding has already been completed
        boolean onboardingCompleted = sharedPreferences.getBoolean(PREF_RECEIVE_STOCK_ONBOARDING, false);

        if (!onboardingCompleted) {
            showCurrentStepOnboarding();
        }

        try (SQLHelper database = new SQLHelper(this.getContext())) {
            binding.receive.setOnClickListener(view1 -> {
                // Retrieve the entered information
                String category = binding.categoryGroup.getText().toString().trim();
                String name = binding.itemName.getText().toString().trim();
                String strCode = binding.itemCode.getText().toString().trim();
                String strPrice = binding.itemPrice.getText().toString().trim();
                String strQuantity = binding.itemQuantity.getText().toString().trim();

                // Check if any field is empty
                if (TextUtils.isEmpty(category) || TextUtils.isEmpty(name) ||
                        TextUtils.isEmpty(strCode) || TextUtils.isEmpty(strPrice) ||
                        TextUtils.isEmpty(strQuantity)) {
                    Toast.makeText(getActivity(), "Fill Up Every Field!", Toast.LENGTH_SHORT).show();
                } else {
                    // Parse the entered values
                    int code = HelperFunctions.stringToInteger(strCode);
                    double price = HelperFunctions.stringToDouble(strPrice);
                    int quantity = HelperFunctions.stringToInteger(strQuantity);

                    // Create the item object
                    Item item = new Item(category.trim(), name.trim(), code, price, quantity);

                    // Show the confirmation dialog
                    showConfirmationDialog(item);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showCurrentStepOnboarding() {
        String stepMessage = STEP_MESSAGES[currentStep];
        showOnboardingDialog("Step " + (currentStep + 1), stepMessage);
    }

    private void showOnboardingDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (currentStep < STEP_MESSAGES.length - 1) {
                            currentStep++;
                            showCurrentStepOnboarding();
                        } else {
                            // Onboarding completed
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(PREF_RECEIVE_STOCK_ONBOARDING, true);
                            editor.apply();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmationDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Item")
                .setMessage("Please confirm the following item:\n\n" + item.toConfirm())
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Process the confirmed item
                        processReceivedItem(item);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing, dialog will be dismissed
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void processReceivedItem(Item item) {
        // Perform the necessary operations to receive the item
        // ...
        try (SQLHelper database = new SQLHelper(this.getContext())) {
                boolean checkCat = database.checkCategory(item.getCategory());
                if(checkCat){
                    //CATEGORY EXIST SO WE ONLY INSERT IN THE STOCK
                    database.insertItem(item.getCategory(), item.getName(), item.getCode(), item.getPrice(), item.getQuantity());
                }else {
                    //CATEGORY DOESN'T EXIST, WE INSERT IT AND THE STOCK
                    database.insertCategory(item.getCategory());
                    database.insertItem(item.getCategory(), item.getName(), item.getCode(), item.getPrice(), item.getQuantity());

                }

                Toast.makeText(getActivity(), "Stock Received!", Toast.LENGTH_SHORT).show();

                // Clear the input fields
                binding.categoryGroup.getText().clear();
                binding.itemName.getText().clear();
                binding.itemCode.getText().clear();
                binding.itemPrice.getText().clear();
                binding.itemQuantity.getText().clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}