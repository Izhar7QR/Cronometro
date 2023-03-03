package com.example.cronometro;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private MaterialButtonToggleGroup toggleGroup;
    private MaterialButton startButton, pauseButton, markButton, resetButton, stopButton, resumeButton;
    private ListView marksList;
    private TextView emptyText;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> marks;
    private long lastMark = 0;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer);
        toggleGroup = findViewById(R.id.toggle_group);
        startButton = findViewById(R.id.start_button);
        pauseButton = findViewById(R.id.pause_button);
        markButton = findViewById(R.id.mark_button);
        resetButton = findViewById(R.id.reset_button);
        stopButton = findViewById(R.id.stop_button);
        resumeButton = findViewById(R.id.resume_button);
        marksList = findViewById(R.id.marks_list);
        emptyText = findViewById(R.id.empty_text);

        // Initialize the ArrayList and the ArrayAdapter for the marks list
        marks = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, marks);
        marksList.setAdapter(adapter);

        // Set the listeners for the buttons in the toggle group
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                toggleGroup.check(R.id.start_button);
                startButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                markButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.GONE);
                marks.clear();
                adapter.notifyDataSetChanged();
                emptyText.setVisibility(View.GONE);
                lastMark = 0;
                isPaused = false;
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                toggleGroup.check(View.NO_ID);
                startButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.GONE);
                markButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.VISIBLE);
                emptyText.setVisibility(View.GONE);
                isPaused = true;
            }
        });

        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                long millis = elapsedMillis - lastMark;
                lastMark = elapsedMillis;
                String markText = String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d",
                        millis / 3600000, (millis % 3600000) / 60000, (millis % 60000) / 1000, millis % 1000);
                marks.add(markText);
                adapter.notifyDataSetChanged();
                emptyText.setVisibility(View.GONE);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                lastMark = 0;
                // Clear the marks list and update the adapter
                marks.clear();
                adapter.notifyDataSetChanged();
                emptyText.setVisibility(View.VISIBLE);
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                toggleGroup.check(View.NO_ID);
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                markButton.setVisibility(View.GONE);
                resetButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                resumeButton.setVisibility(View.GONE);
                marks.clear();
                adapter.notifyDataSetChanged();
                emptyText.setVisibility(View.VISIBLE);
                lastMark = 0;
                isPaused = false;
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                chronometer.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
                chronometer.start();
                toggleGroup.check(R.id.start_button);
                startButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                markButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.GONE);
                emptyText.setVisibility(View.GONE);
                isPaused = false;
            }
        });

        // Set the listener for the marks list to remove marks on long press
        marksList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                marks.remove(position);
                adapter.notifyDataSetChanged();
                if (marks.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }
}
