package com.nyaundibrian.ussds;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.nyaundibrian.ussds.Views.FixedSwipeRefreshLayout;
import com.nyaundibrian.ussds.Views.listeners.EndlessRecyclerViewScrollListener;
import com.nyaundibrian.ussds.adapters.CompaniesAdapter;
import com.nyaundibrian.ussds.config.Config;
import com.nyaundibrian.ussds.models.Company;
import com.nyaundibrian.ussds.models.Me;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An activity representing a list of Companies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CompanyDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CompanyListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public static RealmConfiguration realmConfig;
    private FixedSwipeRefreshLayout swipeContainer;

    Realm realm;

    public RecyclerView recyclerView;
    private int REQUEST_CODE_LOGIN = 7895;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_company_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        swipeContainer = (FixedSwipeRefreshLayout) findViewById(R.id.swipeContainer);

        recyclerView = (RecyclerView) findViewById(R.id.company_list);
        assert recyclerView != null;

        if (findViewById(R.id.company_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(realmConfig);

        Me me = realm.where(Me.class).findFirst();

        if (me == null) {
            /*todo
            Intent intent = new Intent(this,LoginActivity.class);
            startActivityForResult(intent,REQUEST_CODE_LOGIN);
            */
        }


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(new CompaniesAdapter(realm, this));
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {


            }
        });
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // once the network request has completed successfully.
                try {
                    loadSaveData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void loadSaveData() throws Exception {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Config.getHostname() + Config.Companies())
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String responseString = response.body().string();
                try {
                    JSONObject companiesResponse = new JSONObject(responseString);
                    final JSONArray companiesArray = companiesResponse.getJSONArray("results");

                    Realm.getInstance(realmConfig).executeTransaction(new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {
                            try {
                                for (int i = 0; i < companiesArray.length(); i++) {
                                    JSONObject cmp = companiesArray.getJSONObject(i);
                                    int id = cmp.getInt("id");

                                    Company company = realm.where(Company.class).equalTo("id", id).findFirst();
                                    if (company == null) {
                                        company = realm.createObject(Company.class, id);

                                    }

                                    company.setCreated_at(cmp.getString("created_at"));
                                    company.setName(cmp.getString("name"));
                                    company.setUssd_count(cmp.getInt("ussd_count"));
                                    company.setIcon(cmp.getString("icon"));
                                    company.setUssds_url(cmp.getString("ussds_url"));
                                    company.setWebsite(cmp.getString("website"));


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(responseString);
                CompanyListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                });

            }
        });


    }

    public boolean ismTwoPane() {
        return mTwoPane;
    }

}
