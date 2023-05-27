package com.example.pos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pos.databinding.FragmentViewRecordsBinding;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ViewRecords extends Fragment {


    private FragmentViewRecordsBinding binding;
    private static final int REQUEST_MANAGE_EXTERNAL_STORAGE = 1;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentViewRecordsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        display();
        binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(ViewRecords.this)
                        .navigate(R.id.after_view);
            }
        });

        binding.updatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                showUpdatePriceDialog();

            }
        });

    }
    private void salesPDF(ListView listView) {
        try {
            // Create a new document
            Document document = new Document();
            // Get the path to the file
            String path = Environment.getExternalStorageDirectory().toString() + "/sales.pdf";
            // Create a new file
            File file = new File(path);
            file.createNewFile();
            // Create a file output stream
            FileOutputStream fOut = new FileOutputStream(file);
            // Associate the output stream with the document
            PdfWriter.getInstance(document, fOut);
            // Open the document
            document.open();

            // Get the ListView adapter
            AdapterRecords adapter = (AdapterRecords) listView.getAdapter();

            // Create a table with 7 columns
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100); // Set table width to 100% of the page

            // Add column headings
            String[] columnHeadings = {"Account", "Date", "Category", "Name", "Code", "Debit", "Credit"};
            for (String heading : columnHeadings) {
                PdfPCell headingCell = new PdfPCell(new Paragraph(heading));
                table.addCell(headingCell);
            }

            double totalDebit = 0.0;
            double totalCredit = 0.0;

            // Iterate over the items in the adapter and add them to the table
            for (int i = 0; i < adapter.getCount(); i++) {
                SSRecords record = adapter.getItem(i);

                // Add each column value to the table
                table.addCell(record.getAccount());
                table.addCell(record.getDate());
                table.addCell(record.getCategory());
                table.addCell(record.getName());
                table.addCell(String.valueOf(record.getCode()));
                table.addCell(String.valueOf(record.getDebit()));
                table.addCell(String.valueOf(record.getCredit()));

                // Calculate the total debit and credit
                totalDebit += record.getDebit();
                totalCredit += record.getCredit();
            }

            // Add a row for the total debit and credit
            PdfPCell totalLabelCell = new PdfPCell(new Paragraph("Total:"));
            totalLabelCell.setColspan(5); // Span 5 columns for the label cell
            totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalLabelCell);

            PdfPCell totalDebitCell = new PdfPCell(new Paragraph(String.valueOf(totalDebit)));
            totalDebitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalDebitCell);

            PdfPCell totalCreditCell = new PdfPCell(new Paragraph(String.valueOf(totalCredit)));
            totalCreditCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalCreditCell);

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();
            // Notify the user that the PDF has been saved
            Toast.makeText(this.getContext(), "PDF saved to " + path, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }

    private void stockPDF(ListView listView) {
        try {
            // Create a new document
            Document document = new Document();
            // Get the path to the file
            String path = Environment.getExternalStorageDirectory().toString() + "/stock.pdf";
            // Create a new file
            File file = new File(path);
            file.createNewFile();
            // Create a file output stream
            FileOutputStream fOut = new FileOutputStream(file);
            // Associate the output stream with the document
            PdfWriter.getInstance(document, fOut);
            // Open the document
            document.open();

            // Get the ListView adapter
            AdapterRecords adapter = (AdapterRecords) listView.getAdapter();

            // Create a table with 7 columns
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100); // Set table width to 100% of the page

            // Add column headings
            String[] columnHeadings = {"Category", "Name", "Code", "Price"};
            for (String heading : columnHeadings) {
                PdfPCell headingCell = new PdfPCell(new Paragraph(heading));
                table.addCell(headingCell);
            }

            double totalPrice = 0.0;

            // Iterate over the items in the adapter and add them to the table
            for (int i = 0; i < adapter.getCount(); i++) {
                SSRecords record = adapter.getItem(i);

                // Add each column value to the table
                table.addCell(record.getCategory());
                table.addCell(record.getName());
                table.addCell(String.valueOf(record.getCode()));
                table.addCell(String.valueOf(record.getPrice()));

                // Calculate the total debit and credit
                totalPrice += record.getPrice();
            }

            // Add a row for the total price
            PdfPCell totalLabelCell = new PdfPCell(new Paragraph("Total:"));
            totalLabelCell.setColspan(3); // Span 3 columns for the label cell
            totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalLabelCell);

            PdfPCell totalDebitCell = new PdfPCell(new Paragraph(String.valueOf(totalPrice)));
            totalDebitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalDebitCell);

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();
            // Notify the user that the PDF has been saved
            Toast.makeText(this.getContext(), "PDF saved to " + path, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }

    private void showUpdatePriceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.update_price, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final EditText editTextCode = dialogView.findViewById(R.id.editTextNewPriceCode);
        final EditText editTextNewPrice = dialogView.findViewById(R.id.editTextNewPrice);
        Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        final AlertDialog dialog = builder.create();
        dialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeEdit = editTextCode.getText().toString();
                int code = Integer.parseInt(codeEdit);

                String newPriceStr = editTextNewPrice.getText().toString();
                double newPrice = Double.parseDouble(newPriceStr);

                SQLHelper database = new SQLHelper(getContext());
                database.updateItemPrice(code, newPrice);

                Toast.makeText(getContext(), "Price updated successfully", Toast.LENGTH_SHORT).show();
                onResume();
                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void display(){
        RecordsViewModel recordsViewModel = new ViewModelProvider(requireActivity()).get(RecordsViewModel.class);

        recordsViewModel.getMode().observe(getViewLifecycleOwner(), mode -> {
            if ("sales".equals(mode)) {
                binding.updatePrice.setVisibility(View.GONE);
                ArrayList<SSRecords> salesList;
                ListView listView = binding.recordsViewer;
                try (SQLHelper database = new SQLHelper(this.getContext())){
                    salesList = database.getSalesData();
                    AdapterRecords adapter = new AdapterRecords(Objects.requireNonNull(this.getContext()), R.layout.sales_records, salesList, true);
                    listView.setAdapter(adapter);

                    binding.download.setOnClickListener(view2 -> {


                        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Objects.requireNonNull(this.getActivity()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_MANAGE_EXTERNAL_STORAGE);
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (Environment.isExternalStorageManager()) {
                                // Permission already granted
                                salesPDF(listView);
                            } else {
                                // Request permission
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_MANAGE_EXTERNAL_STORAGE);
                            }
                        }
                    });

                }catch(SQLException e){

                    e.printStackTrace();
                }
            } else if ("stock".equals(mode)) {
                binding.updatePrice.setVisibility(View.VISIBLE);
                ArrayList<SSRecords> stockList;
                ListView listView = binding.recordsViewer;
                try (SQLHelper database = new SQLHelper(this.getContext())){
                    stockList = database.getStockData();
                    AdapterRecords adapter = new AdapterRecords(Objects.requireNonNull(this.getContext()), R.layout.stock_records, stockList, false);
                    listView.setAdapter(adapter);

                    binding.download.setOnClickListener(view2 -> {


                        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Objects.requireNonNull(this.getActivity()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_MANAGE_EXTERNAL_STORAGE);
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (Environment.isExternalStorageManager()) {
                                // Permission already granted
                                stockPDF(listView);
                            } else {
                                // Request permission
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_MANAGE_EXTERNAL_STORAGE);
                            }
                        }
                    });

                }catch(SQLException e){

                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        display();
    }
}