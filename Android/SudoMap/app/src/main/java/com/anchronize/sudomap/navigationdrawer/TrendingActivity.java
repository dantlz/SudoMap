package com.anchronize.sudomap.navigationdrawer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anchronize.sudomap.EventDetailActivity;
import com.anchronize.sudomap.NavigationDrawer;
import com.anchronize.sudomap.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TrendingActivity extends NavigationDrawer {
    private ArrayList<String> events = new ArrayList<String>();
    private static final String FIREBASE_URL = "https://anchronize.firebaseio.com";
    private Map<String, Integer> map; //<eventID, number of posts so far>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        //set up Firebase reference for chat
        Firebase mRootRef = new Firebase(FIREBASE_URL);
        Firebase mChatRef = new Firebase(FIREBASE_URL).child("chat");
        Firebase mEventRef = new Firebase(FIREBASE_URL).child("events");
        //initialize the map
        //<eventID, number of posts so far>
        map = new HashMap<>();


        //query the server once
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot chatSnapshot = dataSnapshot.child("chat");
                DataSnapshot eventSnapshot = dataSnapshot.child("events");
                for (DataSnapshot chatInnerSnapshot : chatSnapshot.getChildren()) {
                    String eventID = chatInnerSnapshot.getKey();
                    Integer num = Integer.valueOf((int) chatInnerSnapshot.getChildrenCount());
                    map.put(eventID, num);
                }
                map = sortByValue(map);
                List<String> eventList = new ArrayList<String>();
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    String id = entry.getKey();
                    //eventList.add(id);
                    String eventTitle = (String) eventSnapshot.child(id).child("title").getValue();
                    eventList.add(eventTitle);
                }
                Log.d("eventList", eventList.toString());
                MyListAdaper la = new MyListAdaper(getApplicationContext(), R.layout.event_list_item, eventList);
                final ListView trendingListView = (ListView) findViewById(R.id.mylist);
                //addItemsToList();
                trendingListView.setAdapter(la);
                trendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String eventID = (String) trendingListView.getItemAtPosition(position);
                        Toast.makeText(TrendingActivity.this, "List item was clicked at " + eventID, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(), EventDetailActivity.class);
                        i.putExtra(EventDetailActivity.EVENTID_KEY, eventID);
                        startActivity(i);
                    }
                });
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


/*        ListView trendingListView = (ListView)findViewById(R.id.mylist);
        addItemsToList();
        trendingListView.setAdapter(new MyListAdaper(this, R.layout.event_list_item, events));
        trendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TrendingActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
            }
        });*/

        PieChart pieChart = (PieChart) findViewById(R.id.chart);
        // creating list of entry
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));

        PieDataSet dataset = new PieDataSet(entries, "# of Calls");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        dataset.setValueTextSize(13);

        PieData data = new PieData(labels, dataset);
        pieChart.setData(data); // set the data and list of lables into chart
        pieChart.setDescription("Description");  // set the description
        pieChart.animateY(2000);
    }

    private void addItemsToList() {
        for (int i = 0; i < 55; i++) {
            events.add("Number " + i);
        }
    }

    private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;

        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);

            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_title);
                viewHolder.button = (ImageButton) convertView.findViewById(R.id.list_item_btn_right);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT).show();
                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }

    public class ViewHolder {

        TextView title;
        ImageButton button;
    }


    //following implementation of sorting by map value is taken from
    //http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list =
                new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
