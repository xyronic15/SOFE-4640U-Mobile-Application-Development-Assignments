package com.example.mortgagecalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mortgagecalculator.databinding.FragmentSecondBinding;

import org.w3c.dom.Text;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_second,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

//        Collect the arguments passed form the first fragment
        Bundle bundle = getArguments();
        double p_amount = bundle.getDouble("principal");
        double rate = bundle.getDouble("rate");
        int years = bundle.getInt("years");

//        Calculate the monthly payment
        double monthly_pay = calculate(p_amount, rate, years);

//        Output the values from the user as well as the calculated result on the screen
        TextView principal_given = (TextView) view.findViewById(R.id.principal_given);
        principal_given.setText("Principal Amount: $" + String.format("%.2f", p_amount));
        TextView rate_given = (TextView) view.findViewById(R.id.rate_given);
        rate_given.setText("Yearly Interest Rate: " + String.format("%.2f", rate) + "%");
        TextView period_given = (TextView) view.findViewById(R.id.period_given);
        period_given.setText("Amortization Period: " + Integer.toString(years) + " years");
        TextView monthly = (TextView) view.findViewById(R.id.monthly_pay);
        monthly.setText("Your monthly mortgage payment is $" + String.format("%.2f", monthly_pay));

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Calculation method to calculate the monthly mortgage payment
    public double calculate(double principal, double interest, int years){
        double p = principal;
        double i = (interest / 100) / 12;
        double m = years * 12;

        double payment = p * ((i * Math.pow(1 + i, m)) / (Math.pow(1 + i, m) - 1));

        return payment;
    }

}