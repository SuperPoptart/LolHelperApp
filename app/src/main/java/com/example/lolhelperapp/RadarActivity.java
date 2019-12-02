package com.example.lolhelperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;

public class RadarActivity extends AppCompatActivity {
    private RadarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_radar);

        setTitle("Skill Graph");

        chart = findViewById(R.id.chart1);
        chart.setBackgroundColor(Color.rgb(46, 38, 81));

        chart.getDescription().setEnabled(false);

        chart.setWebLineWidth(1f);
        chart.setWebColor(Color.rgb(188, 142, 225));
        chart.setWebLineWidthInner(1f);
        chart.setWebColorInner(Color.rgb(188, 142, 225));
        chart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MarkerView mv = new RadarMarkerView(this, R.layout.radar_markerview);
//        mv.setChartView(chart); // For bounds control
//        chart.setMarker(mv); // Set the marker to the chart

        setData();

        chart.animateXY(1400, 1400, Easing.EaseInOutQuad);

        XAxis xAxis = chart.getXAxis();
//        xAxis.setTypeface(tfLight);
        xAxis.setTextSize(20f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new ValueFormatter() {

            private final String[] mActivities = new String[]{"F", "V", "O", "A", "C", "U"};

            @Override
            public String getFormattedValue(float value) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = chart.getYAxis();
//        yAxis.setTypeface(tfLight);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(12f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setTypeface(tfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);
    }

    public void backOneActivity(View view) {
        finish();
    }

    public void showKey(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("F = Farming or CS\nV = Vision\nO = Objectives\nA = Aggression\nC = Consistency\nU = Utility or Versatility\nTap anywhere to continue");
        AlertDialog shower = alertDialogBuilder.create();
        shower.show();
    }

    private void setData() {

        float mul = 80;
        float min = 20;
        int cnt = 6;

        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        ArrayList<RadarEntry> entries2 = new ArrayList<>();
        float smallest = 100;
        int indexSmallest = 0;
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = (float) (Math.random() * mul) + min;
            entries1.add(new RadarEntry(val1));

            float val2 = (float) (Math.random() * mul) + min;
            entries2.add(new RadarEntry(val2));

            if(val2 < smallest){
                smallest = val2;
                indexSmallest = i;
            }
        }

        TextView helper = findViewById(R.id.helperText);
        switch (indexSmallest){
            case 0:
                helper.setText("To truly increase you ability to get a lot of gold you'll need to learn champion specific ways to farm. A quick and easy way to find these out for your champion is to download a high level player's replay. This can now be done by accessing their matchhistory in the league client. Just skip to the part where they last hit under tower, push during the laning phase, clear side waves, or clear the jungle. Take 10 min to observe and learn and this can save you a large amount of minions over your league career.");
                break;
            case 1:
                helper.setText("You can leave wards in the middle of the lane just outside of tower range to track enemy movement. This is useful in mid lane where you can track players crossing the center of the map, or to see if the enemy Mid is roaming. This isn't just the Mid laner's job though, anyone can pitch in during the mid to late game. It's rare to see players use this trick below Diamond level, so be apart of the few who take advantage of this now.");
                break;
            case 2:
                helper.setText("Even if the rest of the enemy team is there, not having their jungler forces them into a 4v5 or giving away a free Baron. If you see the enemy jungler on the bot side of the map clearing gromp / krugs or even taking bot lane farm you can look to rush Baron if your comp can do it at a decent speed. However, be wary against teams with champions like Gangplank, Ziggs, or anything else with strong poke since they can heavily punish a Baron start even without their jungler.");
                break;
            case 3:
                helper.setText("Information is your greatest ally in League. If you can estimate the distance your allies need to travel versus your enemies to collapse on your duel then you can always choose fights where your team will show up first. Not only does this give you confidence in that your duel but it's a way to possible bait a bigger teamfight where your team has the advantage.");
                break;
            case 4:
                helper.setText("To increase your consistency in lane you’ll need to remove any outside forces that could impact your performance like deaths from roams or ganks. One trick is to ask yourself where the enemy jungler could be everytime you want to take a trade. If you do not have wards up, and you notice you are the only gankable lane do not take the trade.");
                break;
            case 5:
                helper.setText("For every lane in League of Legends there is a pool of 20 or more champions to play that each have a unique playstyle. No matter what lane you are playing your goal should be to play at least one in each category for your lane. Say you are a jungler, you should be able to play at least one tank, damage carry, and ability power champion. All lanes have something like this so try some champions in a different category than normal so you can get a bigger pool.");
                break;
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "Past 20 Games");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "Past 5 Games");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
//        data.setValueTypeface(tfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);
        chart.invalidate();
    }

}
