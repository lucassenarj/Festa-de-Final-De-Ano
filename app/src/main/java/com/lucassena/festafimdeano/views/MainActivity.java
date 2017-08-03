package com.lucassena.festafimdeano.views;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lucassena.festafimdeano.R;
import com.lucassena.festafimdeano.constants.FimDeAnoConstants;
import com.lucassena.festafimdeano.util.SecurityPreferences;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private SecurityPreferences mSecurityPreferences;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mViewHolder.textToday = (TextView) findViewById(R.id.text_today);
        this.mViewHolder.textDaysLeft = (TextView) findViewById(R.id.text_days_left);
        this.mViewHolder.buttonConfirm = (Button) findViewById(R.id.button_confirm);
        this.mViewHolder.checkParticipate = (CheckBox) findViewById(R.id.check_participate);
        this.mViewHolder.buttonConfirm.setOnClickListener(this);
        this.mSecurityPreferences = new SecurityPreferences(this);
        this.mViewHolder.textToday.setText(SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime()));
        String daysLeft = String.format("%s %s", String.valueOf(this.getDaysLeftToEndOfYear()), getString(R.string.dias));
        this.mViewHolder.textDaysLeft.setText(daysLeft);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

    }

    @Override
    protected void onResume(){
        super.onResume();
        this.verifyPresence();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_confirm) {

            String presence = this.mSecurityPreferences.getStorageString(FimDeAnoConstants.PRESENCE);

            Intent intent = new Intent(this, DetailsActivity.class);

            intent.putExtra(FimDeAnoConstants.PRESENCE, presence);

            startActivity(intent);
        }
    }

    private int getDaysLeftToEndOfYear(){
        Calendar calendarToday = Calendar.getInstance();
        int today = calendarToday.get(Calendar.DAY_OF_YEAR);

        Calendar calendarLastDay = Calendar.getInstance();
        int dayDecember31 = calendarLastDay.getActualMaximum(Calendar.DAY_OF_YEAR);

        return dayDecember31 - today;
    }

    public void verifyPresence() {
        String presence = this.mSecurityPreferences.getStorageString(FimDeAnoConstants.PRESENCE);
        if (presence.equals("")) {
            this.mViewHolder.buttonConfirm.setText(R.string.nao_confirmado);
        } else if (presence.equals(FimDeAnoConstants.CONFIRMED_WILL_GO)) {
            this.mViewHolder.buttonConfirm.setText(R.string.sim);
        } else {
            this.mViewHolder.buttonConfirm.setText(R.string.nao);
        }
    }

    private static class ViewHolder {
        TextView textToday;
        TextView textDaysLeft;
        Button buttonConfirm;
        CheckBox checkParticipate;
    }
}
