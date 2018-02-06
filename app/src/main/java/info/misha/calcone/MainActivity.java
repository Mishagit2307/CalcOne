package info.misha.calcone;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView resultField; //текстове поле для виводу результата
    EditText numberField; //поле для введення числа
    TextView operationField; //поле вивід знака операцїї
    Double operand = null; //операнд
    String lastOperation = "="; //остання операція

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultField = findViewById(R.id.resultField); //отримуємо поля по id з activity_main.xml
        numberField =  findViewById(R.id.numberField);
        operationField =  findViewById(R.id.operationField);
    }
            //збереження стану
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("OPERATION", lastOperation);
        if(operand!=null)
            outState.putDouble("OPERAND", operand);
        super.onSaveInstanceState(outState);
    }
          //отримання збереженного стану
    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand= savedInstanceState.getDouble("OPERAND");
        resultField.setText(operand.toString());
        operationField.setText(lastOperation);
    }
         //обробка натискання на числову кнопку
    public void onNumberClick(View view){

        Button button = (Button)view;
        numberField.append(button.getText());

        if(lastOperation.equals("=") && operand!=null){
            operand = null;
        }
    }
          //обробка натискання на кнопку стерти
    public void onClearClick(View view) {
        this.lastOperation = "";
        this.operand = null;

        resultField = findViewById(R.id.resultField);
        numberField =  findViewById(R.id.numberField);
        operationField =  findViewById(R.id.operationField);

        resultField.setText("");
        numberField.setText("");
        operationField.setText("");
    }
          //обробка натискання на кнопку операцїї
    public void onOperationClick(View view){

        Button button = (Button)view;
        String op = button.getText().toString();
        String number = numberField.getText().toString();
          //якщо введено не числа
        if(number.length()>0){
            number = number.replace(',', '.');
            try{
                performOperation(Double.valueOf(number), op);
            }catch (NumberFormatException ex){
                numberField.setText("");
            }
        }
        lastOperation = op;
        operationField.setText(lastOperation);
    }

    @SuppressLint("SetTextI18n")
    private void performOperation(Double number, String operation){
        boolean divideByZero = false;
         // якщо операнд раніше не був встановлений
        if(operand ==null){
            operand = number;
        }
        else{
            if(lastOperation.equals("=")){
                lastOperation = operation;
            }
            switch(lastOperation){
                case "=":
                    operand =number;
                    break;
                case "/":
                    if(number==0){
                        operand =0.0;
                        divideByZero = true;
                    }
                    else{
                        operand /=number;
                    }
                    break;
                case "*":
                    operand *=number;
                    break;
                case "+":
                    operand +=number;
                    break;
                case "-":
                    operand -=number;
                    break;
            }
        }
          // перевірка ділення на ноль
        if (divideByZero) {
            resultField.setText("ERROR: Divide by zero");
        } else {
            resultField.setText(operand.toString().replace('.', ','));
        }
        numberField.setText("");
    }
}