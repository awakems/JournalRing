package ua.com.awake.journalring;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Void on 07.06.2015.
 */
public class DetailActivity extends AppCompatActivity {

    Realm realm;
    Context context;
    int position;
    String name;
    TextView tvName;
    TextView tvTypeCall;
    TextView tvResultCall;
    TextView tvTime;
    TextView tvTitleBar;

    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView mImageView = (ImageView) findViewById(R.id.ivBackbtn);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        context = getApplicationContext();
        realm = Realm.getInstance(context);
        position = getIntent().getIntExtra("position", -1);
        name = getIntent().getStringExtra("name");

        RealmResults<RealmModel> query = realm.where(RealmModel.class).findAllSorted("startCall", false);

        tvName = (TextView) findViewById(R.id.tvDetailName);
        tvTypeCall = (TextView) findViewById(R.id.tvDetailTypeCall);
        tvResultCall = (TextView) findViewById(R.id.tvDetailResultCall);
        tvTime = (TextView) findViewById(R.id.tvDetailTime);
        tvTitleBar = (TextView) findViewById(R.id.tvTitleBar);
        ivPhoto = (ImageView) findViewById(R.id.ivDetailPhoto);

        tvName.setText(name);
        tvTypeCall.setText(query.get(position).getTypeCall());
        tvResultCall.setText(query.get(position).getConv_result());
        tvTitleBar.setText(query.get(position).getTypeCall());
        //transform date
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = query.get(position).getStartCall();

        tvTime.setText(dateFormat.format(date));

        // Android 2.0 and later
        // Check Contacts Book
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(query.get(position).getNumber()));

        // Query the filter URI
        String[] projection = new String[]{ContactsContract.PhoneLookup.PHOTO_URI};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int indexNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String URI_PHOTO = cursor.getString(indexNumber);

            if(URI_PHOTO!=null) {
                ivPhoto.setImageURI(Uri.parse(URI_PHOTO));
            } else ivPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_nophoto));
        }
        cursor.close();

    }
}
