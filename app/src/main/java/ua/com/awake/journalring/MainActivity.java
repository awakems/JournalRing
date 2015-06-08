package ua.com.awake.journalring;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    ListView lvJournal;
    Realm realm;
    Context context;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
//        toolbar.setTitle(getResources().getString(R.string.app_name));
//        toolbar.setTitleTextColor(android.R.color.white);
//        toolbar.setTitleTextAppearance();
//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backbtn));
//        toolbar.setPadding(50, 0, 40, 10);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

//        Realm.deleteRealmFile(getApplicationContext());

        context = getApplicationContext();
        realm = Realm.getInstance(context);

        RealmResults<RealmModel> query = realm.where(RealmModel.class).findAllSorted("startCall", false);

        lvJournal = (ListView) findViewById(R.id.lvJournal);
        AdapterRealm adapter = new AdapterRealm(context, query, true);
        lvJournal.setAdapter(adapter);

        lvJournal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) view.findViewById(R.id.tvItemNumber);

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("name", textView.getText());
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
