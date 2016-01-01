package nathaniel.watispend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

// Graph Activity java file.
public class GraphActivity extends AppCompatActivity {

    private void onSettingsClick(View v){
        Intent settingsClickIntent = new Intent(GraphActivity.this, SettingsActivity.class);
        GraphActivity.this.startActivity(settingsClickIntent);
    }

    private void onTransactionsClick(View v){
        Intent transactionsClickIntent = new Intent(GraphActivity.this, TransactionsActivity.class);
        GraphActivity.this.startActivity(transactionsClickIntent);
    }

    private void setButtonListeners(){
        ImageView settingsButton = (ImageView)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onSettingsClick(v);
            }
        });

        ImageView transactionsButton = (ImageView)findViewById(R.id.transactionsButton);
        transactionsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onTransactionsClick(v);
            }
        });
    }

    private void setLabels(){
        TextView mealPlan = (TextView) findViewById(R.id.MealPlan);
        TextView totalBalance = (TextView) findViewById(R.id.TotalBalance);
        TextView flex = (TextView) findViewById(R.id.Flex);
        TextView suggestedDaily = (TextView) findViewById(R.id.SuggestedDaily);
        TextView currentDaily = (TextView) findViewById(R.id.CurrentDaily);

        UserValues vals = UserValues.getInstance();
        DecimalFormat df = new DecimalFormat("0.00");
        mealPlan.setText(df.format(vals.mealPlan));
        totalBalance.setText(df.format(vals.totalBalance));
        flex.setText(df.format(vals.flex));
        suggestedDaily.setText(df.format(vals.suggestDaily));
        currentDaily.setText(df.format(vals.currentDaily));
    }

    private void setupChart(){
        BarChart chart = (BarChart) findViewById(R.id.chart);
        chart.setBackgroundColor(Color.WHITE);
        chart.setTouchEnabled(true);
        chart.setDrawValueAboveBar(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setDescription("");
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setPinchZoom(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setStartAtZero(true);
        yAxis.setLabelCount(5, false);
        yAxis.setTextColor(Color.BLACK);

        YAxis right = chart.getAxisRight();
        right.setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        ArrayList<BarEntry> data = new ArrayList<>();
        ArrayList<Double> rawData = UserValues.getInstance().chartData;

        ArrayList<String> xVals = new ArrayList<String>();
        Calendar currentDate = Calendar.getInstance();
        for(int i = 0; i<7; i++){
            data.add(new BarEntry(rawData.get(i).floatValue(), i));
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");
            xVals.add(dateFormat.format(currentDate.getTime()));
            currentDate.add(Calendar.DATE, 1);
        }
        BarDataSet dataSet = new BarDataSet(data, "");
        dataSet.setColor(Color.parseColor("#FCD450"));
        dataSet.setDrawValues(false);

        BarData barData = new BarData(xVals, dataSet);
        chart.setData(barData);
        chart.invalidate();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        if(hasFocus && UserValues.getInstance().chartChange){
            UserValues.getInstance().chartChange = false;
            recreate();
        }
    }

    public String makeRequest(String path, JSONObject params) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(path);
        JSONObject holder = params;
        StringEntity se = new StringEntity(holder.toString());

        System.out.println("SE:");
        String inputLine ;
        BufferedReader br = new BufferedReader(new InputStreamReader(se.getContent()));
        try {
            while ((inputLine = br.readLine()) != null) {
                System.out.println(inputLine);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpost.setHeader("accept", "application/json");
        httpost.setHeader("Content-Type", "application/json");

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        return (String) httpclient.execute(httpost, responseHandler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        setButtonListeners();
        setLabels();
        setupChart();
    }


}
