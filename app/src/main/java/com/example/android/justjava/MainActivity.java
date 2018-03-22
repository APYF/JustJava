/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.justjava.R;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 98;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, R.string.quantity_high_warning, Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    public void decrement(View view) {

        if (quantity == 1) {
            Toast.makeText(this, R.string.quantity_low_warning, Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        boolean hasWhippedCream = ((CheckBox) findViewById(R.id.whippedCream_checkbox)).isChecked();
        boolean hasChocolate = ((CheckBox) findViewById(R.id.chocolate_checkbox)).isChecked();


        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String name = (((EditText) findViewById(R.id.name_inputfield)).getText()).toString();
        Log.v("MainActivity", getString(R.string.price_summary) + price);

        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);
        // displayMessage(priceMessage);

        Intent intentSendEmail = new Intent(Intent.ACTION_SEND);
        String recipients[] = {getString(R.string.email_receiver)};
        intentSendEmail.setData(Uri.parse("mailto:")); // only email apps should handle this

        intentSendEmail.setType("*/*");
        intentSendEmail.putExtra(Intent.EXTRA_EMAIL, recipients);
        intentSendEmail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " " + getString(R.string.email_subject) +" " + name);
        intentSendEmail.putExtra(Intent.EXTRA_TEXT, priceMessage);
        // this handles if no app found to handle this intent
        if (intentSendEmail.resolveActivity(getPackageManager()) != null) {
            startActivity(intentSendEmail);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * This method calculates the price
     *
     * @return int the price of coffee
     */
    private int calculatePrice(boolean whippedCream, boolean chocolate) {
        int basePrice = 5;

        if (whippedCream)
            basePrice = basePrice + 1;

        if (chocolate)
            basePrice = basePrice + 2;


        return quantity * basePrice;
    }

    /**
     *  This method creates the order summary
     *
     *  @param  price of order
     *  @param hasWhippedCream true if want to add to order
     *  @param hasChocolate  true if want to add to order
     *  @param name of the orderer
     *  @return Order summary message text
     *
     */
    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate, String name) {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += getString(R.string.order_summary_whipped_cream, hasWhippedCream);
        priceMessage += getString(R.string.order_summary_chocolate, hasChocolate);
        priceMessage += getString(R.string.order_summary_quantity, quantity);
        priceMessage += getString(R.string.order_summary_total, price);
        priceMessage += getString(R.string.order_summary_thankyou);

        return priceMessage;
    }

}