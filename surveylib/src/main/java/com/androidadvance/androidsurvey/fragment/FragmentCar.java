package com.ryerson.litrans.carboncount.survey.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ryerson.litrans.carboncount.R;
import com.ryerson.litrans.carboncount.survey.Answers;
import com.ryerson.litrans.carboncount.survey.SurveyActivity;
import com.ryerson.litrans.carboncount.survey.models.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentCar extends Fragment {

    String TAG = "FragmentCar";

    private Question q_data;
    private FragmentActivity mContext;
    private Button button_continue;
    private TextView textview_q_title;
    private LinearLayout linearLayout_checkboxes;
    private final ArrayList<CheckBox> allCb = new ArrayList<>();
    private final ArrayList<RadioGroup> allRg = new ArrayList<>();
    private RadioGroup compact, suv, truck, sedan;
    ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_car, container, false);

        compact = (RadioGroup) rootView.findViewById(R.id.compact);
        suv = (RadioGroup) rootView.findViewById(R.id.suv);
        truck = (RadioGroup) rootView.findViewById(R.id.truck);
        sedan = (RadioGroup) rootView.findViewById(R.id.sedan);

        allCb.add((CheckBox) rootView.findViewById(R.id.compactCheck));
        allCb.add((CheckBox) rootView.findViewById(R.id.suvCheck));
        allCb.add((CheckBox) rootView.findViewById(R.id.truckCheck));
        allCb.add((CheckBox) rootView.findViewById(R.id.sedanCheck));

        button_continue = (Button) rootView.findViewById(R.id.button_continue);
        textview_q_title = (TextView) rootView.findViewById(R.id.textview_q_title);
        //linearLayout_checkboxes = (LinearLayout) rootView.findViewById(R.id.linearLayout_checkboxes);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SurveyActivity) mContext).go_to_next();
            }
        });

        return rootView;
    }

    private void collect_data() {

        String the_choices = "";
        int must_be_checked = 0;
        int enough_checked = 0;
        for(CheckBox cb : allCb) {
            if(cb.isChecked()){
                if(cb.getText().toString().equals("Compact"))
                {
                    RadioButton rb = (RadioButton) rootView.findViewById(compact.getCheckedRadioButtonId());
                    if(rb != null) {
                        enough_checked++;
                        String carType = "compact:" + rb.getText().toString() + "_";
                        the_choices = the_choices + carType;
                    }
                }
                else if(cb.getText().toString().equals("Sedan"))
                {
                    RadioButton rb = (RadioButton) rootView.findViewById(sedan.getCheckedRadioButtonId());
                    if(rb != null) {
                        enough_checked++;
                        String carType = "sedan:" + rb.getText().toString() + "_";
                        the_choices = the_choices + carType;
                    }
                }
                else if(cb.getText().toString().equals("SUV"))
                {
                    RadioButton rb = (RadioButton) rootView.findViewById(suv.getCheckedRadioButtonId());
                    if(rb != null) {
                        enough_checked++;
                        String carType = "suv:" + rb.getText().toString() + "_";
                        the_choices = the_choices + carType;
                    }
                }
                else if(cb.getText().toString().equals("Truck"))
                {
                    RadioButton rb = (RadioButton) rootView.findViewById(truck.getCheckedRadioButtonId());
                    if(rb != null) {
                        enough_checked++;
                        String carType = "truck:" + rb.getText().toString() + "_";
                        the_choices = the_choices + carType;
                    }
                }
                must_be_checked++;
            }
        }

        if (the_choices.length() > 2) {
            //the_choices = the_choices.substring(0, the_choices.length() - 2);
            Answers.getInstance().put_answer(textview_q_title.getText().toString(), the_choices);
        }

        if (q_data.getRequired()) {
            if (must_be_checked == enough_checked) {
                button_continue.setVisibility(View.VISIBLE);
            } else {
                button_continue.setVisibility(View.GONE);
            }
        }
        Log.d(TAG, "must_be_checked: " + must_be_checked);
        Log.d(TAG, "checked: " + enough_checked);

        Log.d(TAG, "collect_data: " + the_choices);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        q_data = (Question) getArguments().getSerializable("data");

        textview_q_title.setText(q_data != null ? q_data.getQuestionTitle() : "");

        if (q_data.getRequired()) {
            button_continue.setVisibility(View.GONE);
        }

        List<String> qq_data = q_data.getChoices();
        if (q_data.getRandomChoices()) {
            Collections.shuffle(qq_data);
        }

        for (CheckBox cb : allCb) {

            if(cb.getText().toString().toLowerCase().equals("compact")){
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        collect_data();
                        if(isChecked) {
                            if(compact.getCheckedRadioButtonId() == -1){
                                button_continue.setVisibility(View.GONE);
                            }
                            compact.setVisibility(View.VISIBLE);
                            compact.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    collect_data();
                                }
                            });
                        }
                        else {
                            compact.setVisibility(View.GONE);
                        }
                    }
                });
            }
            else if(cb.getText().toString().toLowerCase().equals("sedan")){
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        collect_data();
                        if(isChecked) {
                            if(sedan.getCheckedRadioButtonId() == -1){
                                button_continue.setVisibility(View.GONE);
                            }
                            sedan.setVisibility(View.VISIBLE);
                            sedan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    collect_data();
                                }
                            });
                        }
                        else {
                            sedan.setVisibility(View.GONE);
                        }
                    }
                });
            }
            else if(cb.getText().toString().toLowerCase().equals("truck")){
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        collect_data();
                        if(isChecked) {
                            if(truck.getCheckedRadioButtonId() == -1){
                                button_continue.setVisibility(View.GONE);
                            }
                            truck.setVisibility(View.VISIBLE);
                            truck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    collect_data();
                                }
                            });
                        }
                        else {
                            truck.setVisibility(View.GONE);
                        }
                    }
                });
            }
            else if(cb.getText().toString().toLowerCase().equals("suv"))
            {
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        collect_data();
                        if(isChecked) {
                            if(suv.getCheckedRadioButtonId() == -1){
                                button_continue.setVisibility(View.GONE);
                            }
                            suv.setVisibility(View.VISIBLE);
                            suv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    collect_data();
                                }
                            });
                        }
                        else {
                            suv.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }

    }


}