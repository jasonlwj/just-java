package com.example.jasonl.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    int coffeeQty = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void increment(View view) {
        if (coffeeQty < 100) {
            coffeeQty++;
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.increase_limit), Toast.LENGTH_SHORT).show();
        }

        display(coffeeQty);
    }

    public void decrement(View view) {
        if (coffeeQty > 0) {
            coffeeQty--;
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.decrease_limit), Toast.LENGTH_SHORT).show();
        }

        display(coffeeQty);
    }

    public void submitOrder(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        TextView nameTextView = (TextView) findViewById(R.id.name_edit_text);

        int price = calculatePrice(whippedCreamCheckBox.isChecked(), chocolateCheckBox.isChecked());
        String priceMessage = createOrderSummary(nameTextView.getText().toString(), price, whippedCreamCheckBox.isChecked(), chocolateCheckBox.isChecked());

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_msg_subject, nameTextView.getText()));
        emailIntent.putExtra(Intent.EXTRA_TEXT, priceMessage);

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

    private String createOrderSummary(String name, int price, boolean hasWhippedCream, boolean hasChocolate) {
        return getString(R.string.order_summary_name, name) +
                "\n" + getString(R.string.order_summary_whipped_cream) + hasWhippedCream +
                "\n" + getString(R.string.order_summary_chocolate) + hasChocolate +
                "\n" + getString(R.string.order_summary_quantity, Integer.toString(coffeeQty)) +
                "\n" + getString(R.string.order_summary_total, NumberFormat.getCurrencyInstance().format(price)) +
                "\n" + getString(R.string.thank_you);
    }

    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;

        if (addWhippedCream) basePrice += 1;
        if (addChocolate) basePrice += 2;

        return basePrice * coffeeQty;
    }

    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}
