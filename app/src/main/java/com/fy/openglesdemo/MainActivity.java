package com.fy.openglesdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {

    // map keys
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String CLASS_NAME = "class_name";

    private static final String[][] TESTS = {
            {"1 Draw a Triangle", "Draw a simple Triangle with openGL ES"},
            {"2 Draw a Rectangle", "Draw a Rectangle with openGL ES"},
            {"3 Draw a Picture", "Draw a Picture with openGL ES"},
            {"4 Draw a Picture", "Draw a Picture with openGL ES, Support gesture control"},
    };

    private static final Class<?>[] classes = {
            ActivityTriangle.class,
            ActivityRectangle.class,
            ActivityPicture.class,
            ActivityPictureGesture.class,
    };

    /**
     * Compares two list items.
     */
    private static final Comparator<Map<String, Object>> TEST_LIST_COMPARATOR =
            new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> map1, Map<String, Object> map2) {
                    String title1 = (String) map1.get(TITLE);
                    String title2 = (String) map2.get(TITLE);
                    return title1.compareTo(title2);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setListAdapter(new SimpleAdapter(this, createActivityList(),
                android.R.layout.two_line_list_item, new String[]{TITLE, DESCRIPTION},
                new int[]{android.R.id.text1, android.R.id.text2}));
    }

    /**
     * Creates the list of activities from the string arrays.
     */
    private List<Map<String, Object>> createActivityList() {
        List<Map<String, Object>> testList = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < classes.length; i++) {
            String[] test = TESTS[i];
            Class<?> cls = classes[i];
            Map<String, Object> tmp = new HashMap<String, Object>();
            tmp.put(TITLE, test[0]);
            tmp.put(DESCRIPTION, test[1]);
            Intent intent = new Intent();
            intent.setClass(this, cls);
            tmp.put(CLASS_NAME, intent);
            testList.add(tmp);
        }

        Collections.sort(testList, TEST_LIST_COMPARATOR);

        return testList;
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) listView.getItemAtPosition(position);
        Intent intent = (Intent) map.get(CLASS_NAME);
        startActivity(intent);
    }

}
