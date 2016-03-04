package com.example.zachgreen.representapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by zachgreen on 2/25/16.
 */
public class CongressionalViewActivity extends ListActivity {

        ListView list;

        Integer[] imgid={
                R.drawable.bboxer,
                R.drawable.bboxer,
                R.drawable.bboxer
        };

        String[] names ={
                "Sen. Barbara Boxer",
                "Sen. Barbara Boxer",
                "Sen. Barbara Boxer"
        };

        String[] parties ={
                "Democrat",
                "Democrat",
                "Democrat"
        };

        String[] emails ={
                "senator@boxer.senate.gov",
                "senator@boxer.senate.gov",
                "senator@boxer.senate.gov"
        };

        String[] websites ={
                "www.boxer.senate.gov",
                "www.boxer.senate.gov",
                "www.boxer.senate.gov"
        };

        String[] tweets ={
                "Boxer Calls for Immediate Action on @SenStabenow Bill to Protect American Communities from #Lead in #DrinkingWater",
                "Boxer Calls for Immediate Action on @SenStabenow Bill to Protect American Communities from #Lead in #DrinkingWater",
                "Boxer Calls for Immediate Action on @SenStabenow Bill to Protect American Communities from #Lead in #DrinkingWater"
        };



        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.congressional_view);
                // Make a toast with the String
                try {
                        Intent intent = getIntent();
                        Bundle bundle = intent.getExtras();
                        String value = bundle.getString("ZIP_CODE");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "Zip Code: " + value, duration);
                        toast.show();
                } catch (Exception e){
                }

                CustomListAdapter adapter=new CustomListAdapter(this, imgid, names, parties, emails, websites, tweets);
                list=(ListView)findViewById(android.R.id.list);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        String name = names[+position];
                        Intent sendIntent = new Intent(getBaseContext(), DetailedViewActivity.class);
                        sendIntent.putExtra("NAME", name);
                        startActivity(sendIntent);
                        // TODO Auto-generated method stub
//                        String Slecteditem = names[+position];
//                        Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                    }
            });

//            this.setListAdapter(new ArrayAdapter<String>(
//                    this, R.layout.officials_list,
//                    R.id.Itemname,names));
        }
}

