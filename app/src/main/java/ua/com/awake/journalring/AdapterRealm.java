package ua.com.awake.journalring;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by Void on 06.06.2015.
 */
public class AdapterRealm extends RealmBaseAdapter<RealmModel> implements ListAdapter {


    public AdapterRealm(Context context, RealmResults<RealmModel> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    private static class ViewHolder {
        TextView tvNumber;
        TextView tvHeader;
        TextView tvTypeCall;
        TextView tvTime;
        TextView tvFooter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_main, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tvItemNumber);
            viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.tvItemDate);
            viewHolder.tvTypeCall = (TextView) convertView.findViewById(R.id.tvItemTypeCall);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvItemTimeCall);
            viewHolder.tvFooter = (TextView) convertView.findViewById(R.id.tvItemFooter);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RealmModel item = realmResults.get(position);
        Date dateObj = item.getStartCall();
        DateFormat dateFormatHeader = new SimpleDateFormat("dd MMMM yyyy");
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        // First item date
        if (position == 0){
            viewHolder.tvHeader.setVisibility(View.VISIBLE);
            viewHolder.tvHeader.setText(dateFormatHeader.format(dateObj));
        } else viewHolder.tvHeader.setVisibility(View.GONE);

        // Check end compare dates
        if( position + 1 < realmResults.size() ){

            RealmModel item2 = realmResults.get(position + 1);
            Date dateObjNext = item2.getStartCall();

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            String dateInString = dateFormatHeader.format(dateObj);
            String dateInString2 = dateFormatHeader.format(dateObjNext);

            try {
                Date date1 = sdf.parse(dateInString);
                Date date2 = sdf.parse(dateInString2);

                if (date1.after(date2)){
                    viewHolder.tvFooter.setVisibility(View.VISIBLE);
                    viewHolder.tvFooter.setText(dateFormatHeader.format(dateObjNext));
                } else viewHolder.tvFooter.setVisibility(View.GONE);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        viewHolder.tvNumber.setText(item.getNumber());
        viewHolder.tvTypeCall.setText(item.getTypeCall());
        viewHolder.tvTime.setText(dateFormat.format(dateObj));

        // Android 2.0 and later
        // Init Contacts Book and search name of the person by number
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(item.getNumber()));

        // Query the filter URI
        String[] projection = new String[]{ ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int indexName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String nameDialer = cursor.getString(indexName);
            viewHolder.tvNumber.setText(nameDialer);
        }
        cursor.close();

        return convertView;
    }
}
