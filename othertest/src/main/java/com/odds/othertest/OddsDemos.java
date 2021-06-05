
package com.odds.othertest;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OddsDemos extends ListActivity {
    private static final String EXTRA_TEST_PATH = "com.odds.othertest.Path";
    private static final String TAG = "OddsDemos";
    public static final String CATEGORY_ODDS_TESTALL = "com.odds.othertest.category.example";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        String path = intent.getStringExtra(EXTRA_TEST_PATH);
        
        if (path == null) {
            path = "";
        }
        Log.i(TAG, "intent "+ intent + " path "+ path);
        setListAdapter(new SimpleAdapter(this, getData(path),
                android.R.layout.simple_list_item_1, new String[] { "title" },
                new int[] { android.R.id.text1 }));
        getListView().setTextFilterEnabled(true);
    }

    protected List getData(String prefix) {
        List<Map> myData = new ArrayList<Map>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(CATEGORY_ODDS_TESTALL);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (null == list)
            return myData;

        String[] prefixPath;
        
        if (prefix.equals("")) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
        }
        
        int len = list.size();
        
        Map<String, Boolean> entries = new HashMap<String, Boolean>();

        for (int i = 0; i < len; i++) {
            ResolveInfo info = list.get(i);
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null
                    ? labelSeq.toString()
                    : info.activityInfo.name;
            
            if (prefix.length() == 0 || label.startsWith(prefix)) {
                
                String[] labelPath = label.split("/");

                String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];

                if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
                    addItem(myData, nextLabel, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name));
                } else {
                    if (entries.get(nextLabel) == null) {
                        addItem(myData, nextLabel, browseIntent(prefix.equals("") ? nextLabel : prefix + "/" + nextLabel));
                        entries.put(nextLabel, true);
                    }
                }
            }
        }
        Logger.d(TAG, "getData " + myData, "odds");
        Collections.sort(myData, sDisplayNameComparator);
        
        return myData;
    }

    private final static Comparator<Map> sDisplayNameComparator = new Comparator<Map>() {
        private final Collator   collator = Collator.getInstance();

        public int compare(Map map1, Map map2) {
            return collator.compare(map1.get("title"), map2.get("title"));
        }
    };

    protected Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }
    
    protected Intent browseIntent(String path) {
        Intent result = new Intent();
        result.setClass(this, OddsDemos.class);
        result.putExtra(EXTRA_TEST_PATH, path);
        return result;
    }

    protected void addItem(List<Map> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map map = (Map) l.getItemAtPosition(position);

        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }
}
