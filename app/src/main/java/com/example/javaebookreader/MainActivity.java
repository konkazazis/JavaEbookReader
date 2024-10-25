package com.example.javaebookreader;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.javaebookreader.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> pages = new ArrayList<>();
    private int currentPage = 0;
    private TextView textViewPageContent;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPageContent = findViewById(R.id.textViewPageContent);
        seekBar = findViewById(R.id.seekBar);
        Button btnPrevPage = findViewById(R.id.btnPrevPage);
        Button btnNextPage = findViewById(R.id.btnNextPage);

        // Load eBook text from assets folder
        loadEbookFromAssets();

        // Set up SeekBar max value and listener
        seekBar.setMax(pages.size() - 1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentPage = progress;
                displayPage(currentPage);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Previous Page Button
        btnPrevPage.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                displayPage(currentPage);
                seekBar.setProgress(currentPage);
            }
        });

        // Next Page Button
        btnNextPage.setOnClickListener(v -> {
            if (currentPage < pages.size() - 1) {
                currentPage++;
                displayPage(currentPage);
                seekBar.setProgress(currentPage);
            }
        });

        // Display the first page
        displayPage(currentPage);
    }

    private void loadEbookFromAssets() {
        try {
            AssetManager assetManager = getAssets();
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open("algorithms.pdf")));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            int charsPerPage = 1000;  // Adjust this value based on how much text you want per page
            int currentCharCount = 0;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
                currentCharCount += line.length();

                // If we've hit the character limit for one "page", create a new page
                if (currentCharCount >= charsPerPage) {
                    pages.add(stringBuilder.toString());
                    stringBuilder.setLength(0);  // Clear the StringBuilder for the next page
                    currentCharCount = 0;
                }
            }

            // Add any remaining text as the last page
            if (stringBuilder.length() > 0) {
                pages.add(stringBuilder.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayPage(int pageNumber) {
        if (pageNumber < pages.size()) {
            textViewPageContent.setText(pages.get(pageNumber));
        }
    }
}
