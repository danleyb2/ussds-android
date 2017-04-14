package com.nyaundibrian.ussds;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nyaundibrian.ussds.Views.FixedSwipeRefreshLayout;
import com.nyaundibrian.ussds.adapters.CompanyUssdsAdapter;
import com.nyaundibrian.ussds.models.Code;
import com.nyaundibrian.ussds.models.Company;
import com.nyaundibrian.ussds.models.Ussd;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A fragment representing a single Company detail screen.
 * This fragment is either contained in a {@link CompanyListActivity}
 * in two-pane mode (on tablets) or a {@link CompanyDetailActivity}
 * on handsets.
 */
public class CompanyDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_COMPANY_IMAGE = "company_image";

    /**
     * The dummy content this fragment is presenting.
     */
    private Company mCompany;
    private RecyclerView recyclerView;
    private FixedSwipeRefreshLayout swipeRefreshLayout;
    private RealmConfiguration realmConfiguration;
    private Realm realm;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CompanyDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realmConfiguration = new RealmConfiguration.Builder(getActivity())
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(realmConfiguration);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mCompany = realm
                    .where(Company.class)
                    .equalTo("id", getArguments().getInt(ARG_ITEM_ID))
                    .findFirst();


            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            ImageView companyLogo = (ImageView) appBarLayout.findViewById(R.id.company_logo);

            appBarLayout.setTitle(mCompany.getName());
            Picasso.with(getContext()).load(mCompany.getIcon()).into(companyLogo);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.company_detail, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.company_ussds);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        /*
        swipeRefreshLayout = (FixedSwipeRefreshLayout)
                rootView.findViewById(R.id.swipeContainerUssdsFragment);
        */
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new CompanyUssdsAdapter(mCompany.getUssds(),getActivity());
        recyclerView.setAdapter(mAdapter);
        /*
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    loadSaveUssds(mCompany);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        */


        if (mCompany != null) {
            try {
                loadSaveUssds(mCompany);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return rootView;
    }

    private void loadSaveUssds(Company company) throws Exception {
        // TODO: 8/9/2016  swipeRefreshLayout.setRefreshing(true);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(company.getUssds_url())
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
                    final JSONArray ussdsArray = companiesResponse.getJSONArray("results");

                    Realm.getInstance(realmConfiguration).executeTransaction(new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {
                            try {
                                Company instance = realm
                                        .where(Company.class)
                                        .equalTo("id", getArguments().getInt(ARG_ITEM_ID))
                                        .findFirst();

                                instance.getUssds().clear();
                                RealmList<Ussd> ussds = new RealmList<>();

                                for (int j = 0; j < ussdsArray.length(); j++) {
                                    JSONObject usd = ussdsArray.getJSONObject(j);
                                    int idUssd = usd.getInt("id");

                                    Ussd ussd = realm.where(Ussd.class).equalTo("id", idUssd).findFirst();
                                    if (ussd == null) {
                                        ussd = realm.createObject(Ussd.class, idUssd);
                                    }

                                    JSONObject codeJsonObject = usd.getJSONObject("code");
                                    int idCode = codeJsonObject.getInt("pk");
                                    Code code = realm.where(Code.class).equalTo("id", idCode).findFirst();
                                    if (code == null) {
                                        code = realm.createObject(Code.class, idCode);
                                        code.setValue(codeJsonObject.getString("value"));
                                        code.setTemplate(codeJsonObject.getBoolean("is_template"));
                                    }

                                    ussd.setCode(code);
                                    ussd.setDescription(usd.getString("description"));

                                    ussds.add(ussd);

                                }
                                instance.getUssds().addAll(ussds);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(responseString);

                // TODO: 8/9/2016  swipeRefreshLayout.setRefreshing(false);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //recyclerView.getAdapter();
                        mAdapter.notifyDataSetChanged();
                        //swipeContainer.setRefreshing(false);
                    }
                });

            }
        });


    }
}
