package com.ursa_studio.booster.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.ursa_studio.booster.R;
import com.ursa_studio.booster.map.Constants;
import com.ursa_studio.booster.model.Boost;

public class BoostSummaryFragment extends Fragment implements View.OnClickListener {

  private TextView textViewDeliveryTime;

  private TextView textViewFuelType;
  private TextView textViewPaymentMethod;
  private Boost boost;

  @Override public View onCreateView (LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_boost_summary, null);
  }

  @Override public void onViewCreated (View view, Bundle savedInstanceState){
    super.onViewCreated(view, savedInstanceState);

    textViewDeliveryTime = (TextView) view.findViewById(R.id.textViewDeliveryTime);

    textViewFuelType = (TextView) view.findViewById(R.id.textViewFuelType);
    textViewPaymentMethod = (TextView) view.findViewById(R.id.textViewPaymentMethod);
    view.findViewById(R.id.btnNext).setOnClickListener(this);

    Bundle bundle = this.getArguments();
    if(bundle != null){
      boost = bundle.getParcelable(Constants.BOOST_OBJECT);
      populateUI();
    }
  }

  private void populateUI (){

    textViewDeliveryTime.setText(boost.getDeliverTime());
    textViewFuelType.setText(boost.getFuelType());
    textViewPaymentMethod.setText(boost.getPaymentType());
  }

  @Override public void onClick (View view){
    switch(view.getId()){
      case R.id.btnNext:

        showDialog(getString(R.string.cancel_title),getString(R.string.cancel_question));
        break;
    }
  }

  private void showDialog (String title, String message) {

    new AlertDialog.Builder (getContext()).setTitle (title).setMessage (message)

        .setPositiveButton (getString (R.string.yes), new DialogInterface.OnClickListener () {
          public void onClick (DialogInterface dialog, int which) {

            getActivity().finish();
          }
        })

        .setNegativeButton (getString (R.string.no), new DialogInterface.OnClickListener () {
          public void onClick (DialogInterface dialog, int which) {
            dialog.cancel ();
          }
        }).setIcon (android.R.drawable.ic_dialog_map).show ();
  }
}
