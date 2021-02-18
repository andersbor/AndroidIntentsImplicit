package com.example.intentsimplicit;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void browseButtonClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://anbo-easj.dk/"));
        Intent chooser = Intent.createChooser(intent, "Browse with");
        startActivity(chooser);
    }


    public void phoneButtonClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+45123456"));
        startActivity(intent);
    }

    public void mapButtonClicked(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:55.6305952,12.0784041"));
            startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(this, "Cannot show map", Toast.LENGTH_LONG).show();
        }
    }

    public void cameraButtonClicked(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    private static final int PICK_CONTACT_SUBACTIVITY = 2;

    public void contactsButtonClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT_SUBACTIVITY);
    }

    // Meier: Prof Android 4, page 170
    // http://stackoverflow.com/questions/9496350/pick-a-number-and-name-from-contacts-list-in-android-app
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView resultView = findViewById(R.id.choice_textview_result);
        if (resultCode == RESULT_CANCELED) {
            resultView.setText("Cancelled. No data.");
            return;
        }
        if (requestCode == PICK_CONTACT_SUBACTIVITY) {
            /// resultView.setText("You picked a contact: " + data.getData().toString());
            Uri contactData = data.getData();
            // Cursor cursor =  managedQuery(contactData, null, null, null, null); // deprecated
            // http://stackoverflow.com/questions/12714701/deprecated-managedquery-issue
            Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
            if (cursor.moveToFirst()) {
                final int columnNameIndex = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
                String name = cursor.getString(columnNameIndex);
                resultView.setText(name);
            }
            cursor.close();
        }
    }

    public void emailClicked(View view) {
        // Compile a Uri with the 'mailto' schema
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "johndoe@example.com", null));
        // Subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello World!");
        // Body of email
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi! I am sending you a test email.");
        // File attachment
        //emailIntent.putExtra(Intent.EXTRA_STREAM, attachedFileUri);
        // Check if the device has an email client
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            // Prompt the user to select a mail app
            startActivity(Intent.createChooser(emailIntent, "Choose your mail application"));
        } else {
            // Inform the user that no email clients are installed or provide an alternative
            TextView resultView = findViewById(R.id.choice_textview_result);
            resultView.setText("No email app on device");
        }
    }

    public void genericClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Nothing else specified!
        startActivity(intent);
    }
}
