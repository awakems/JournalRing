package ua.com.awake.journalring;

import android.content.Context;
import android.content.Intent;

import java.util.Date;

import io.realm.Realm;

/**
 * Created by Void on 04.06.2015.
 */
public class CallReceiver extends PhoneCallReceiver {

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {

        long millis = start.getTime();
        Intent intent = new Intent(ctx, DialogActivity.class);
        intent.putExtra("date", millis);
        intent.putExtra("number", number);
        intent.putExtra("type", ctx.getResources().getString(R.string.type_call_in));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);

    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {

        long millis = start.getTime();
        Intent intent = new Intent(ctx, DialogActivity.class);
        intent.putExtra("date", millis);
        intent.putExtra("number", number);
        intent.putExtra("type", ctx.getResources().getString(R.string.type_call_out));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);

    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {

        Realm realm = Realm.getInstance(ctx);
        realm.beginTransaction();
        RealmModel dial = realm.createObject(RealmModel.class);
        dial.setNumber(number);
        dial.setStartCall(start);
        dial.setTypeCall(ctx.getResources().getString(R.string.type_call_missed));
        realm.commitTransaction();
        realm.close();

        Intent intent = new Intent(ctx, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);

    }
}
