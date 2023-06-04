package com.example.pos;

import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pos.databinding.FragmentClientManagementBinding;

import java.util.ArrayList;
import java.util.List;

public class ClientManagement extends Fragment {


    private FragmentClientManagementBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentClientManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        display();

        binding.addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAddClientDialog();
                display();

            }
        });

        binding.deleteCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDeleteClientDialog();
                display();
            }
        });

        binding.updateCustomerNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showUpdateNumberDialog();
                display();
            }
        });

        binding.paymentCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPaymentDialog();
                display();
            }
        });

        binding.doneCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 NavHostFragment.findNavController(ClientManagement.this)
                       .navigate(R.id.after_customer);

            }
        });

    }

    private void display(){
        ListView listView = binding.clientList;
        try (SQLHelper database = new SQLHelper(this.getContext())){

            ArrayList<Customer> customers = database.getAllClients();
            CustomerAdapter customerAdapter = new CustomerAdapter(getContext(), R.layout.client_view, customers);
            listView.setAdapter(customerAdapter);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showAddClientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.add_client, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final EditText editTittle = dialogView.findViewById(R.id.client_add_tittle);
        final EditText editName = dialogView.findViewById(R.id.client_add_name);
        final EditText editSurname = dialogView.findViewById(R.id.client_add_surname);
        final EditText editPhone = dialogView.findViewById(R.id.client_add_phone);
        ImageButton buttonUpdate = dialogView.findViewById(R.id.adding_client);
        ImageButton buttonCancel = dialogView.findViewById(R.id.cancel_adding);

        final AlertDialog dialog = builder.create();
        dialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tittleEdit = editTittle.getText().toString().trim();
                String nameEdit = editName.getText().toString().trim();
                String surnameEdit = editSurname.getText().toString().trim();
                String phoneEdit = editPhone.getText().toString().trim();

                if (TextUtils.isEmpty(tittleEdit) || TextUtils.isEmpty(nameEdit) ||
                        TextUtils.isEmpty(surnameEdit) || TextUtils.isEmpty(phoneEdit)) {
                    Toast.makeText(getActivity(), "Fill Up Every Field!", Toast.LENGTH_SHORT).show();
                } else {

                    // Create a confirmation dialog
                    AlertDialog.Builder confirmationBuilder = new AlertDialog.Builder(getContext());
                    confirmationBuilder.setTitle("Confirmation");
                    confirmationBuilder.setMessage("Are you sure you want to add this customer?\n\n" +
                            "Title: " + tittleEdit + "\n" +
                            "Name: " + nameEdit + "\n" +
                            "Surname: " + surnameEdit + "\n" +
                            "Phone: " + phoneEdit);
                    confirmationBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SQLHelper database = new SQLHelper(getContext());

                            // Perform validation if needed


                            boolean insert = database.insertClient(tittleEdit, nameEdit, surnameEdit, phoneEdit, 0.0);
                            if (insert) {
                                Toast.makeText(getContext(), "Customer Successfully Added", Toast.LENGTH_SHORT).show();
                                display();
                            } else {
                                Toast.makeText(getContext(), "ERROR: Customer NOT Added", Toast.LENGTH_SHORT).show();
                            }
                            onResume();
                            dialog.dismiss();
                        }
                    });
                    confirmationBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog confirmationDialog = confirmationBuilder.create();
                    confirmationDialog.show();

                }

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    private void showDeleteClientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.delete_client, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final EditText editPhone = dialogView.findViewById(R.id.client_number_delete);
        Button deleteClient = dialogView.findViewById(R.id.delete_client);

        final AlertDialog dialog = builder.create();
        dialog.show();

        deleteClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneEdit = editPhone.getText().toString().trim();

                SQLHelper database = new SQLHelper(getContext());
                Customer customer = database.getCustomerByPhone(phoneEdit);
                // Show confirmation dialog
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getContext());
                confirmBuilder.setTitle("Confirm Client Deletion");
                confirmBuilder.setMessage("Delete  " +customer.toString()+ " ?");
                confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean delete = database.deleteClientByNumber(phoneEdit);
                        if(delete){
                            Toast.makeText(getContext(), "Client Successfully Deleted", Toast.LENGTH_SHORT).show();
                            display();

                        }else {
                            Toast.makeText(getContext(), "ERROR: Client NOT Found", Toast.LENGTH_SHORT).show();
                        }
                        onResume();
                        dialogInterface.dismiss();
                        dialog.dismiss();
                    }
                });
                confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dialog.dismiss();
                    }
                });
                AlertDialog confirmDialog = confirmBuilder.create();
                confirmDialog.show();
            }
        });
    }


    private void showUpdateNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.update_number, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final EditText editOldNumber = dialogView.findViewById(R.id.editTextOldNumber);
        final EditText editNewNumber = dialogView.findViewById(R.id.editTextNewNumber);
        Button buttonUpdate = dialogView.findViewById(R.id.update_number);
        Button buttonCancel = dialogView.findViewById(R.id.cancel_number_update);

        final AlertDialog dialog = builder.create();
        dialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldEdit = editOldNumber.getText().toString();
                String newEdit = editNewNumber.getText().toString();

                SQLHelper database = new SQLHelper(getContext());

                Customer customer = database.getCustomerByPhone(oldEdit);
                // Show confirmation dialog
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getContext());
                confirmBuilder.setTitle("Confirm Number Update");
                confirmBuilder.setMessage("Update the " + customer.getName()+ " number from " + oldEdit + " to " + newEdit + "?");
                confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean update = database.updateClientNumber(oldEdit, newEdit);
                        if(update){
                            Toast.makeText(getContext(), "Number Successfully Updated", Toast.LENGTH_SHORT).show();
                            display();
                        }else {
                            Toast.makeText(getContext(), "ERROR: Number NOT Found", Toast.LENGTH_SHORT).show();
                        }

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
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void showPaymentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.client_payment, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final EditText editNumberPay = dialogView.findViewById(R.id.client_number_pay);
        final EditText editAmountPay = dialogView.findViewById(R.id.client_amount_pay);
        Button buttonPay = dialogView.findViewById(R.id.pay_client_number);
        Button buttonCancel = dialogView.findViewById(R.id.cancel_number_pay);

        final AlertDialog dialog = builder.create();
        dialog.show();

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneEdit = editNumberPay.getText().toString().trim();
                String amountStr = editAmountPay.getText().toString().trim();
                double amountPay = HelperFunctions.stringToDouble(amountStr);

                SQLHelper database = new SQLHelper(getContext());
                Customer customer = database.getCustomerByPhone(phoneEdit);

                // Show confirmation dialog
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getContext());
                confirmBuilder.setTitle("Confirm Payment");
                confirmBuilder.setMessage("Client Details:\n" +
                        "Name: " + customer.getName() + "\n" +
                        "Phone: " + customer.getPhone() + "\n\n" +
                        "Amount to Add: " + amountPay);
                confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean pay = database.addAmountToClient(phoneEdit, amountPay);
                        if (pay) {
                            Toast.makeText(getContext(), "Successfully Paid Amount", Toast.LENGTH_SHORT).show();
                            database.recordTransaction("Payment", HelperFunctions.getDate(), "CLIENT",
                                    customer.getName(), HelperFunctions.stringToInteger(customer.getPhone()), amountPay, null);
                            display();
                        } else {
                            Toast.makeText(getContext(), "ERROR: Paying Amount", Toast.LENGTH_SHORT).show();
                        }
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
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}