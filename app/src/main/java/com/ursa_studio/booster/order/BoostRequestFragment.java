package com.ursa_studio.booster.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.ursa_studio.booster.R;
import com.ursa_studio.booster.map.Constants;
import com.ursa_studio.booster.model.Boost;
import java.util.ArrayList;
import java.util.List;

public class BoostRequestFragment extends Fragment implements View.OnClickListener {


  private Switch switchTime;

  private Switch switchFuelType;
  private TextView textViewPaymentType;
  private Spinner spinner;
  Button btnNext;
  private int counter = 0;

  private List<String> paymentMethods;
  private Boost boost;

  @Override public View onCreateView (LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_boost_request, null);
  }

  @Override public void onViewCreated (View view, Bundle savedInstanceState){
    super.onViewCreated(view, savedInstanceState);


    switchTime = (Switch) view.findViewById(R.id.switchTime);
    switchFuelType = (Switch) view.findViewById(R.id.switchFuelType);
    textViewPaymentType = (TextView) view.findViewById(R.id.textViewPaymentType);
    spinner = (Spinner) view.findViewById(R.id.spinner);
    btnNext = (Button) view.findViewById(R.id.btnNext);
    btnNext.setOnClickListener(this);

    Bundle bundle = this.getArguments();
    if(bundle != null){
      boost = bundle.getParcelable(Constants.BOOST_OBJECT);
    }

    populateSpinner();

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected (AdapterView<?> adapterView, View view, int position, long l){

        if(counter == 0){

        } else{

          btnNext.setText(getString(R.string.order_a_boost));
          boost.setPaymentType((String) adapterView.getItemAtPosition(position));
        }
        counter++;
      }
      @Override public void onNothingSelected (AdapterView<?> adapterView){

      }
    });
  }

  private void populateSpinner (){

    paymentMethods = new ArrayList<>();

    paymentMethods.add("VISA **** 1234 12/17");
    paymentMethods.add("VISA **** 7689 13/19");
    paymentMethods.add("AMEX **** 1488 10/22");

    ArrayAdapter<String> spinnerArrayAdapter =
        new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
            paymentMethods);
    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(spinnerArrayAdapter);
  }

  private void populateObject (){

    if(switchFuelType.isChecked()){
      boost.setFuelType(getString(R.string.premium91));
    } else{
      boost.setFuelType(getString(R.string.regular87));
    }

    if(switchTime.isChecked()){
      boost.setDeliverTime(getString(R.string.afternoon));
    } else{
      boost.setDeliverTime(getString(R.string.morning));
    }
  }

  @Override public void onClick (View view){
    switch(view.getId()){
      case R.id.btnNext:
        if(counter > 0){

          showBoostDetails();
        }

        break;
    }
  }

  private void showBoostDetails (){

    populateObject();

    Bundle bundle = new Bundle();
    bundle.putParcelable(Constants.BOOST_OBJECT, boost);

    BoostSummaryFragment fragment = new BoostSummaryFragment();
    fragment.setArguments(bundle);

    getFragmentManager().beginTransaction()
        .replace(R.id.flContent, fragment, "BOOST")
        .addToBackStack("tag2")
        .commit();
  }
}
