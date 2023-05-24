package com.example.pos;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pos.databinding.FragmentViewRecordsBinding;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
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
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentViewRecordsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<SSRecords> recordList = new ArrayList<>();
        ListView listView = binding.recordsViewer;
        try (SQLHelper database = new SQLHelper(this.getContext())){

            recordList = database.getDataFromDatabase();
            AdapterRecords adapterItem = new AdapterRecords(Objects.requireNonNull(this.getContext()), R.layout.sales_records, recordList);
            listView.setAdapter(adapterItem);
            System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
            System.out.println(recordList);
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            binding.done.setOnClickListener(view1 ->{
                NavHostFragment.findNavController(ViewRecords.this)
                        .navigate(R.id.after_view);
            });

            binding.download.setOnClickListener(view2 -> {


                if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_MANAGE_EXTERNAL_STORAGE);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
                            && Environment.isExternalStorageManager()) {
                        // Permission already granted
                        generatePdf(listView);
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

        }
    }
    private void generatePdf(ListView listView) {
        try {
            // Create a new document
            Document document = new Document();
            // Get the path to the file
            String path = Environment.getExternalStorageDirectory().toString() + "/listView.pdf";
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
            // Iterate over the items in the adapter and add them to the document
            for (int i = 0; i < adapter.getCount(); i++) {
                String item = adapter.getItem(i).toString();
                Paragraph paragraph = new Paragraph(item);
                document.add(paragraph);
            }
            // Close the document
            document.close();
            // Notify the user that the PDF has been saved
            Toast.makeText(this.getContext(), "PDF saved to " + path, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}