package com.example.lab3;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.text.DecimalFormat;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Stack<Double> numberStack;
    private Stack<String> operatorStack;
    private boolean isNewNumber = true;
    private boolean isResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        numberStack = new Stack<>();
        operatorStack = new Stack<>();
    }

    public void clickNumber(View view) {
        if (isResult) {
            clear();
        }
        String buttonText = ((Button) view).getText().toString();
        String currentText = editText.getText().toString();

        if (isNewNumber) {
            currentText = "";
            isNewNumber = false;
        }

        currentText += buttonText;
        editText.setText(currentText);
    }

    public void onOperatorClick(View view) {
        if (!isNewNumber) {
            String operator = ((Button) view).getText().toString();
            if (!operatorStack.isEmpty() && hasPrecedence(operator, operatorStack.peek())) {
                evaluate();
            }
            operatorStack.push(operator);
            numberStack.push(Double.parseDouble(editText.getText().toString()));
            isNewNumber = true;
        }
    }

    public void calculate(View view) {
        if (!isNewNumber && !operatorStack.isEmpty()) {
            numberStack.push(Double.parseDouble(editText.getText().toString()));
            evaluate();
            isResult = true;
        }
    }

    public void clear() {
        editText.setText("0");
        numberStack.clear();
        operatorStack.clear();
        isNewNumber = true;
        isResult = false;
    }

    public void back(View view) {
        if (!isNewNumber) {
            String currentText = editText.getText().toString();
            if (currentText.length() > 1) {
                editText.setText(currentText.substring(0, currentText.length() - 1));
            } else {
                editText.setText("0");
                isNewNumber = true;
            }
        }
    }

    public void changeSign(View view) {
        if (!isNewNumber) {
            String currentText = editText.getText().toString();
            if (!currentText.equals("0")) {
                if (currentText.charAt(0) == '-') {
                    editText.setText(currentText.substring(1));
                } else {
                    editText.setText("-" + currentText);
                }
            }
        }
    }

    private void evaluate() {
        if (numberStack.size() >= 2 && operatorStack.size() >= 1) {
            double b = numberStack.pop();
            double a = numberStack.pop();
            String operator = operatorStack.pop();
            double result = 0.0;

            switch (operator) {
                case "+":
                    result = a + b;
                    break;
                case "-":
                    result = a - b;
                    break;
                case "*":
                    result = a * b;
                    break;
                case "/":
                    if (b != 0) {
                        result = a / b;
                    } else {
                        editText.setText("Error");
                        clear();
                        return;
                    }
                    break;
                case "√":
                    if (a >= 0) {
                        result = Math.sqrt(a);
                    } else {
                        editText.setText("Error");
                        clear();
                        return;
                    }
                    break;
            }
            editText.setText(formatResult(result));
            numberStack.push(result);
        }
    }

    private String formatResult(double result) {
        DecimalFormat df = new DecimalFormat("#.##########");
        return df.format(result);
    }

    private boolean hasPrecedence(String op1, String op2) {
        if ((op1.equals("+") || op1.equals("-")) && (op2.equals("*") || op2.equals("/"))) {
            return false;
        }
        return true;
    }
}


