package com.nyaundibrian.ussds.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyaundibrian.ussds.CompanyDetailActivity;
import com.nyaundibrian.ussds.CompanyDetailFragment;
import com.nyaundibrian.ussds.CompanyListActivity;
import com.nyaundibrian.ussds.R;
import com.nyaundibrian.ussds.models.Company;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

/**
 * Created by ndiek on 7/22/2016.
 */
public class CompaniesAdapter
        extends RecyclerView.Adapter<CompaniesAdapter.ViewHolder> {

    private final List<Company> mValues;
    private CompanyListActivity companyListActivity;


    public CompaniesAdapter(Realm db, CompanyListActivity companyListActivity) {
        // TODO: 8/2/2016
        mValues = db.where(Company.class).findAll();
        this.companyListActivity = companyListActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_list_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Company company = mValues.get(position);
        holder.mItem = company;
        holder.mIdView.setText(String.valueOf(company.getUssd_count()) + " ussds");
        holder.mContentView.setText(company.getName());

        Picasso.with(companyListActivity)
                .load(company.getIcon())
                //.error( R.drawable.error ) todo add a drawable to display on image load error
                //.placeholder(R.drawable.progress_animation)
                .into(holder.iconImageView);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (companyListActivity.ismTwoPane()) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(CompanyDetailFragment.ARG_ITEM_ID, company.getId());
                    //arguments.putString(CompanyDetailFragment.ARG_COMPANY_IMAGE, company.getIcon());

                    CompanyDetailFragment fragment = new CompanyDetailFragment();
                    fragment.setArguments(arguments);
                    companyListActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.company_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, CompanyDetailActivity.class);
                    intent.putExtra(CompanyDetailFragment.ARG_ITEM_ID, company.getId());

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView iconImageView;

        public Company mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            iconImageView = (ImageView) view.findViewById(R.id.imageViewIcon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}


