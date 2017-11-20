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

public class FragmentCalculation extends Fragment implements View.OnClickListener {

    private StringBuilder stringBuilder;

    private TextView tvCost;

    private Float number = null;
    private Boolean optionButtonPressed = false;
    private String lastOptionButton = null;

    private Button[] btnNumber;
    private Button[] btnOperation;

    private Button btnAddDot;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculation, container, false);

        tvCost = getActivity().findViewById(R.id.tvCost);

        btnNumber = new Button[10];
        btnOperation = new Button[5];

        btnNumber[0] = view.findViewById(R.id.btnNumber0);
        btnNumber[1] = view.findViewById(R.id.btnNumber1);
        btnNumber[2] = view.findViewById(R.id.btnNumber2);
        btnNumber[3] = view.findViewById(R.id.btnNumber3);
        btnNumber[4] = view.findViewById(R.id.btnNumber4);
        btnNumber[5] = view.findViewById(R.id.btnNumber5);
        btnNumber[6] = view.findViewById(R.id.btnNumber6);
        btnNumber[7] = view.findViewById(R.id.btnNumber7);
        btnNumber[8] = view.findViewById(R.id.btnNumber8);
        btnNumber[9] = view.findViewById(R.id.btnNumber9);

        btnOperation[0] = view.findViewById(R.id.btnAdd);
        btnOperation[1] = view.findViewById(R.id.btnSubstract);
        btnOperation[2] = view.findViewById(R.id.btnMultiply);
        btnOperation[3] = view.findViewById(R.id.btnDivide);
        btnOperation[4] = view.findViewById(R.id.btnEquals);

        btnAddDot = view.findViewById(R.id.btnAddDot);

        for (Button button : btnNumber) {
            button.setOnClickListener(this);
        }

        for (Button button : btnOperation) {
            button.setOnClickListener(this);
        }

        btnAddDot.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        String text;

        switch (view.getId()) {
            case R.id.btnAdd:
            case R.id.btnSubstract:
            case R.id.btnMultiply:
            case R.id.btnDivide:
                text = tvCost.getText().toString();
                if (!text.isEmpty()) optionButtonPressed(view);
                break;
            case R.id.btnEquals:
                if (lastOptionButton != null && !optionButtonPressed) {
                    try {
                        doLastOptionButtonAction();
                        displayNumber();
                        number = null;
                        optionButtonPressed = false;
                        lastOptionButton = null;
                    } catch (ArithmeticException e) {
                        reset();
                    }
                }
                break;
            default:
                if (optionButtonPressed) {
                    tvCost.setText("");
                    optionButtonPressed = false;
                }
                addText(((Button) view).getText().toString());
                break;
        }
    }

    private void optionButtonPressed(View view) {
        if (!optionButtonPressed) {
            try {
                doLastOptionButtonAction();
            } catch (ArithmeticException e) {
                reset();
                return;
            }
            if (number == null) {
                number = Float.parseFloat(tvCost.getText().toString());
            } else {
                displayNumber();
            }
        }
        lastOptionButton = ((Button) view).getText().toString();
        optionButtonPressed = true;
    }

    private void doLastOptionButtonAction() {
        if (lastOptionButton != null && !tvCost.getText().toString().isEmpty()) {
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
                    Float divider = Float.parseFloat(tvCost.getText().toString());
                    if (divider == 0F) throw new ArithmeticException();
                    number /= Float.parseFloat(tvCost.getText().toString());
                    break;
            }
        }
    }

    public void reset() {
        number = null;
        lastOptionButton = null;
        optionButtonPressed = false;
        tvCost.setText("");
    }

    private void displayNumber() {
        if (String.valueOf(number).matches("^\\d+\\.0{0,2}$"))
            tvCost.setText(String.valueOf(number.intValue()));
        else tvCost.setText(String.valueOf(number));
    }

    /**
     * The function checks whether a line can be added if yes then add
     *
     * @param text string which must be added to exists string
     */
    private void addText(String text) {
        /*
         * A regex checking if the expression starts from zero. The next number could be just
         * one dot and two numbers or noting, else checked expression starts from [1-9] and then
         * can be any digit (max 6 digits). Then one dot and two numbers or nothing.
         */
        String regex = "(^0(\\.\\d{0,2})?$)|(^[1-9]\\d{0,6}(\\.\\d{0,2})?$)";

        stringBuilder = new StringBuilder(tvCost.getText());

        if (stringBuilder.toString().isEmpty() && text.equals(".")) stringBuilder.append("0");
        stringBuilder.append(text);
        if (stringBuilder.toString().matches(regex)) tvCost.setText(stringBuilder);
    }
}
