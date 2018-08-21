package com.ryerson.litrans.carboncount.survey.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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

public class FragmentGender extends Fragment {

    String TAG = "FragmentGender";

    private Question q_data;
    private FragmentActivity mContext;
    private Button button_continue;
    private EditText self_description;
    private TextView textview_q_title;
    private RadioGroup radioGroup;
    private final ArrayList<RadioButton> allRb = new ArrayList<>();
    private boolean at_least_one_checked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gender, container, false);

        button_continue = (Button) rootView.findViewById(R.id.button_continue);
        self_description = (EditText) rootView.findViewById(R.id.gender_answer);
        textview_q_title = (TextView) rootView.findViewById(R.id.textview_q_title);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SurveyActivity) mContext).go_to_next();
            }
        });

        return rootView;
    }

    private void collect_data() {

        //----- collection & validation for is_required
        String the_choice = "";
        at_least_one_checked = false;
        for (RadioButton rb : allRb) {
            if(rb.getText().toString().equals("Prefer to self-describe")) {
                if (rb.isChecked()) {
                    at_least_one_checked = true;
                    the_choice = self_description.getText().toString();
                }
            }
            if (rb.isChecked()) {
                at_least_one_checked = true;
                the_choice = rb.getText().toString();
            }
        }

        if (the_choice.length() > 0) {
            Answers.getInstance().put_answer(textview_q_title.getText().toString(), the_choice);
        }

        if (q_data.getRequired()) {
            if (at_least_one_checked) {
                button_continue.setVisibility(View.VISIBLE);
            } else {
                button_continue.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        mContext = getActivity();
        q_data = (Question) getArguments().getSerializable("data");

        textview_q_title.setText(q_data.getQuestionTitle());

        List<String> qq_data = q_data.getChoices();
        if (q_data.getRandomChoices()) {
            Collections.shuffle(qq_data);
        }

        for (String choice : qq_data) {
            RadioButton rb = new RadioButton(mContext);
            rb.setText(Html.fromHtml(choice));
            rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioGroup.addView(rb);
            allRb.add(rb);

            if(rb.getText().toString().equals("Prefer to self-describe")){
                rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            self_description.setVisibility(View.VISIBLE);
                            self_description.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    if (self_description.getText().toString() != "") {
                                        collect_data();
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                }
                            });
                        }
                    }
                });
            }
            else {
                rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                            self_description.setVisibility(View.INVISIBLE);
                        collect_data();
                    }
                });
            }
        }

        if (q_data.getRequired()) {
            if (at_least_one_checked) {
                button_continue.setVisibility(View.VISIBLE);
            } else {
                button_continue.setVisibility(View.GONE);
            }
        }


    }


}