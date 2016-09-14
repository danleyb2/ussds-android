package com.nyaundibrian.ussds.adapters;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyaundibrian.ussds.R;
import com.nyaundibrian.ussds.models.Ussd;

import io.realm.RealmList;

/**
 * Created by ndiek on 7/22/2016.
 */
public class CompanyUssdsAdapter extends RecyclerView.Adapter<CompanyUssdsAdapter.ViewHolder> {
    private RealmList<Ussd> mDataset;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextViewDescription;
        public TextView mTextViewCode;
        public ImageButton mButtonCall;


        public ViewHolder(View v) {
            super(v);
            mTextViewDescription = (TextView) v.findViewById(R.id.info_text);
            mTextViewCode = (TextView) v.findViewById(R.id.tvCode);
            mButtonCall = (ImageButton) v.findViewById(R.id.imageButton);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CompanyUssdsAdapter(RealmList<Ussd> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CompanyUssdsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ussd_list_item_card, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ussd ussd = mDataset.get(position);

        holder.mTextViewCode.setText(ussd.getCode().getValue());
        holder.mTextViewDescription.setText(ussd.getDescription());

        /*
        if (ussd.getCode().isTemplate()){

        }else {
            */
        holder.mButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*
                    String number = "23454568678";
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));

                    startActivity(intent);*/
                // custom dialog
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Android custom dialog example!");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.mipmap.ic_launcher);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
//        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
