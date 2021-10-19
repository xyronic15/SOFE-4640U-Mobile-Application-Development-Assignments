package com.example.mortgagecalculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mortgagecalculator.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

//        Initiate the values for the sinner
        Integer[] periodOptions = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30};

        View view = inflater.inflate(R.layout.fragment_first, container, false);

//        Set the values of the spinner component
        Spinner period = (Spinner) view.findViewById(R.id.period_in);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, periodOptions);
        period.setAdapter(adapter);

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Get the user's input components
        EditText principal = (EditText) view.findViewById(R.id.principal_in);
        EditText interest = (EditText) view.findViewById(R.id.interest_in);
        Spinner period = (Spinner) view.findViewById(R.id.period_in);


        view.findViewById(R.id.calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Initialize the toast message and output if the principal amount and/or interest rate is empty
                Toast errormsg;
                if (TextUtils.isEmpty(principal.getText())){
                    errormsg = Toast.makeText(getActivity(), "Please enter principal amount", Toast.LENGTH_SHORT);
                    errormsg.show();
                }
                if (TextUtils.isEmpty(interest.getText())){
                    errormsg = Toast.makeText(getActivity(), "Please enter your interest rate", Toast.LENGTH_SHORT);
                    errormsg.show();
                }
//                If all fields are filled then collect the inputs and pass them on to the next fragment using a Bundle object
                if (!TextUtils.isEmpty(principal.getText()) && !TextUtils.isEmpty(interest.getText())) {
                    double p_amount = Double.parseDouble(principal.getText().toString());
                    double rate = Double.parseDouble(interest.getText().toString());
                    int years = Integer.parseInt(period.getSelectedItem().toString());

                    Bundle bundle = new Bundle();
                    bundle.putDouble("principal", p_amount);
                    bundle.putDouble("rate", rate);
                    bundle.putInt("years", years);

                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
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