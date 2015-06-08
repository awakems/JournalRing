package ua.com.awake.journalring;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by Void on 06.06.2015.
 */
public class DialogActivity extends AppCompatActivity {

    TextView tvType;
    TextView tvDate;
    TextView tvNumber;
    Button btCancel;
    Button btSave;
    EditText etConvDetails;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        final Date dateObj = new Date(getIntent().getLongExtra("date", -1));
        final String number = getIntent().getExtras().getString("number");
        final String type = getIntent().getExtras().getString("type");
        context = getApplicationContext();

        tvType = (TextView) findViewById(R.id.tvTypeCall);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvNumber = (TextView) findViewById(R.id.tvNameDialer);
        btSave = (Button) findViewById(R.id.btSave);
        btCancel = (Button) findViewById(R.id.btCancel);
        etConvDetails = (EditText) findViewById(R.id.etConvResult);

        tvType.setText(type);
        tvDate.setText(dateFormat.format(dateObj));
        tvNumber.setText(number);

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        // Query the filter URI
        String[] projection = new String[]{ ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int indexName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String nameDialer = cursor.getString(indexName);
            tvNumber.setText(nameDialer);
        }
        cursor.close();


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Realm realm = Realm.getInstance(getApplicationContext());
                realm.beginTransaction();
                RealmModel obj = realm.createObject(RealmModel.class);
                obj.setNumber(number);
                obj.setTypeCall(type);
                obj.setStartCall(dateObj);
                obj.setConv_result(etConvDetails.getText().toString());
                realm.commitTransaction();
                realm.close();

                Intent intent = new Intent(DialogActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
