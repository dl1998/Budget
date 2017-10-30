package com.android.budget.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.budget.R;

/**
 * Created by dimal on 19.10.2017.
 */

public class FragmentCalculation extends Fragment implements View.OnClickListener{

    private StringBuilder stringBuilder;

    private TextView tvCost;

    private Float number = null;
    private Boolean optionButtonPressed = false;
    private String lastOptionButton = null;

    private Button btnNumber0;
    private Button btnNumber1;
    private Button btnNumber2;
    private Button btnNumber3;
    private Button btnNumber4;
    private Button btnNumber5;
    private Button btnNumber6;
    private Button btnNumber7;
    private Button btnNumber8;
    private Button btnNumber9;
    private Button btnAddDot;
    private Button btnPlus;
    private Button btnMinus;
    private Button btnMultiply;
    private Button btnDivide;
    private Button btnEquals;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.calculation_fragment, container, false);

        //stringBuilder = new StringBuilder();

        tvCost = getActivity().findViewById(R.id.tvCost);

        btnNumber0 = view.findViewById(R.id.btnNumber0);
        btnNumber1 = view.findViewById(R.id.btnNumber1);
        btnNumber2 = view.findViewById(R.id.btnNumber2);
        btnNumber3 = view.findViewById(R.id.btnNumber3);
        btnNumber4 = view.findViewById(R.id.btnNumber4);
        btnNumber5 = view.findViewById(R.id.btnNumber5);
        btnNumber6 = view.findViewById(R.id.btnNumber6);
        btnNumber7 = view.findViewById(R.id.btnNumber7);
        btnNumber8 = view.findViewById(R.id.btnNumber8);
        btnNumber9 = view.findViewById(R.id.btnNumber9);
        btnAddDot = view.findViewById(R.id.btnAddDot);
        btnPlus = view.findViewById(R.id.btnPlus);
        btnMinus = view.findViewById(R.id.btnMinus);
        btnMultiply = view.findViewById(R.id.btnMultiply);
        btnDivide = view.findViewById(R.id.btnDivide);
        btnEquals = view.findViewById(R.id.btnEquals);

        btnNumber0.setOnClickListener(this);
        btnNumber1.setOnClickListener(this);
        btnNumber2.setOnClickListener(this);
        btnNumber3.setOnClickListener(this);
        btnNumber4.setOnClickListener(this);
        btnNumber5.setOnClickListener(this);
        btnNumber6.setOnClickListener(this);
        btnNumber7.setOnClickListener(this);
        btnNumber8.setOnClickListener(this);
        btnNumber9.setOnClickListener(this);
        btnAddDot.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnMultiply.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnEquals.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view){

        switch (view.getId()){
            case R.id.btnPlus:
            case R.id.btnMinus:
            case R.id.btnMultiply:
            case R.id.btnDivide:
                optionButtonPressed(view);
                break;
            case R.id.btnEquals:
                if(lastOptionButton != null) {
                    try {
                        doLastOptionButtonAction();
                        displayNumber();
                        number = null;
                        optionButtonPressed = false;
                        lastOptionButton = null;
                    } catch (ArithmeticException e){
                        tvCost.setText("");
                    }
                    break;
                }
            default:
                if(optionButtonPressed){
                    tvCost.setText("");
                    optionButtonPressed = false;
                }
                stringBuilder = new StringBuilder(tvCost.getText());
                addText(((Button)view).getText().toString());
                break;
        }
    }

    public void optionButtonPressed(View view){
        try {
            if (!optionButtonPressed) {
                doLastOptionButtonAction();
                if (number == null) {
                    number = Float.parseFloat(tvCost.getText().toString());
                    tvCost.setText("");
                } else {
                    displayNumber();
                }
            }
            lastOptionButton = ((Button) view).getText().toString();
            optionButtonPressed = true;
        } catch (NumberFormatException e){

        }
    }

    public void doLastOptionButtonAction() throws ArithmeticException{
        if (lastOptionButton != null) {
            switch (lastOptionButton) {
                case "+":
                    number += Float.parseFloat(tvCost.getText().toString());
                    break;
                case "-":
                    number -= Float.parseFloat(tvCost.getText().toString());
                    break;
                case "*":
                    number *= Float.parseFloat(tvCost.getText().toString());
                    break;
                case "/":
                    try {
                        Float divider = Float.parseFloat(tvCost.getText().toString());
                        if (divider == 0F)throw new ArithmeticException();
                        number /= Float.parseFloat(tvCost.getText().toString());
                    } catch (ArithmeticException e){
                        number = null;
                        lastOptionButton = null;
                        optionButtonPressed = false;
                        throw e;
                    }
                    break;
            }
        }
    }

    public void displayNumber(){
        if(String.valueOf(number).matches("^\\d+\\.0{0,2}$")) tvCost.setText(String.valueOf(number.intValue()));
        else tvCost.setText(String.valueOf(number));
    }

    /**
     * The function checks whether a line can be added if yes then add
     *
     * @param text string which must be added to exists string
     */
    public void addText(String text){
        /*
         * A regex checking if the expression starts from zero. The next number could be just
         * one dot and two numbers or noting, else checked expression starts from [1-9] and then
         * can be any digit (max 6 digits). Then one dot and two numbers or nothing.
         */
        String regex = "(^0(\\.\\d{0,2})?$)|(^[1-9]\\d{0,6}(\\.\\d{0,2})?$)";

        stringBuilder = new StringBuilder(tvCost.getText());
        if(stringBuilder.toString().isEmpty() && text.equals("."))stringBuilder.append("0");
        stringBuilder.append(text);
        if (stringBuilder.toString().matches(regex)) tvCost.setText(stringBuilder);
    }
}
