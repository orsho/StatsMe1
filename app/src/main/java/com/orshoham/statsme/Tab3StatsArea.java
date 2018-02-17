package com.orshoham.statsme;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;


public class Tab3StatsArea extends Fragment {

    LineGraphSeries<DataPoint> series;
    BarGraphSeries<DataPoint> series2;
    LineGraphSeries<DataPoint> series3;

    DBGames dbGames;

    public void styleGraphes (GraphView graphView){
        graphView.getViewport().setMinX(1);
        graphView.getViewport().setMinY(1);
        graphView.setTitleColor(Color.parseColor("#88A550"));
        graphView.setTitleTextSize(40);
    }


    public void firstGraph(List<GamesSQL> gameList, GraphView graph1){
        series = new LineGraphSeries<DataPoint>();

        graph1.setTitle("my winners per game:");
        styleGraphes(graph1);

        series.setThickness(12);
        series.setColor(Color.parseColor("#88A550"));

        int x=0;
        int y=0;
        for(int i=0;i<gameList.size();i++){
            x=x+1;
            y=gameList.get(i).getMyWinners();
            Log.i("my y series", Integer.toString(y));
            series.appendData(new DataPoint(x,y), true, gameList.size());
        }
        graph1.addSeries(series);
    }

    public void secondGraph(List<GamesSQL> gameList, GraphView graph2){
        series2 = new BarGraphSeries<DataPoint>();

        graph2.setTitle("My times I got to the net per game:");
        styleGraphes(graph2);
        series2.setColor(Color.parseColor("#88A550"));

        int x=0;
        int y=0;
        for(int i=0;i<gameList.size();i++){
            x=x+1;
            y=gameList.get(i).getMyTotalNet();
            Log.i("my y2 series", Integer.toString(y));
            series2.appendData(new DataPoint(x,y), true, gameList.size());
        }
        graph2.addSeries(series2);
    }

    public void ThirdGraph(List<GamesSQL> gameList, GraphView graph3){
        series3 = new LineGraphSeries<DataPoint>();

        graph3.setTitle("rival forced and unforced errors per game:");
        styleGraphes(graph3);
        series3.setThickness(12);
        series3.setColor(Color.parseColor("#88A550"));

        int x=0;
        int y=0;
        for(int i=0;i<gameList.size();i++){
            x=x+1;
            y=gameList.get(i).getRivalForced()+gameList.get(i).getRivalUNForced();
            Log.i("my y3 series", Integer.toString(y));
            series3.appendData(new DataPoint(x,y), true, gameList.size());
        }
        graph3.addSeries(series3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_stats_area, container, false);

        dbGames = new DBGames(getActivity());
        List<GamesSQL> gameList = dbGames.getAllGames();

        GraphView graph1 = (GraphView) rootView.findViewById(R.id.graph1Id);
        GraphView graph2 = (GraphView) rootView.findViewById(R.id.graph2Id);
        GraphView graph3 = (GraphView) rootView.findViewById(R.id.graph3Id);

        firstGraph(gameList, graph1);
        secondGraph(gameList, graph2);
        ThirdGraph(gameList, graph3);

        return rootView;
    }

}
