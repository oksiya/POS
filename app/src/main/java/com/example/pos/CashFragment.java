package com.example.pos;

import android.database.SQLException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pos.databinding.FragmentCashBinding;
public class CashFragment extends Fragment {

    private FragmentCashBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCashBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button.setText(PettyCash.pettyCash);
        binding.button.setOnClickListener(view1 -> {

            String account = binding.account.getText().toString();
            String strAmount = binding.amount.getText().toString().trim();
            Double amount = HelperFunctions.stringToDouble(strAmount);

            if(TextUtils.isEmpty(account) || TextUtils.isEmpty(strAmount)){

                Toast toast = Toast.makeText(getActivity(), "Fill Up Every Field!", Toast.LENGTH_SHORT);
                toast.show();

            }else{

                try(SQLHelper database = new SQLHelper(this.getContext())) {
                    boolean record;
                    if(binding.button.getText().equals("ADD")){
                        record = database.recordTransaction("Add Cash", HelperFunctions.getDate(),
                                "PETTY CASH", account, 101, amount, null);
                        Toast toast;
                        if(record){

                            toast = Toast.makeText(getActivity(), "Cash Added!", Toast.LENGTH_SHORT);
                            toast.show();
                            NavHostFragment.findNavController(CashFragment.this)
                                    .navigate(R.id.after_petty_cash);

                        }else {

                            toast = Toast.makeText(getActivity(), "ERROR : Could Not Add Cash!", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }else {

                        record = database.recordTransaction("Withdraw Cash", HelperFunctions.getDate(),
                                "PETTY CASH", account, 102, null, amount);
                        Toast toast;
                        if(record){

                            toast = Toast.makeText(getActivity(), "Successfully Withdraw!", Toast.LENGTH_SHORT);
                            toast.show();
                            NavHostFragment.findNavController(CashFragment.this)
                                    .navigate(R.id.after_petty_cash);

                        }else {

                            toast = Toast.makeText(getActivity(), "ERROR : Could Not Withdraw Cash!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }


                }catch (NumberFormatException e){

                    Toast toast = Toast.makeText(getActivity(), "Invalid Amount!", Toast.LENGTH_SHORT);
                    toast.show();

                }catch (SQLException e){

                    Toast toast = Toast.makeText(getActivity(), "ERROR : Could Not Record!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }


        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}