package io.shivam.travelplanner;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.ProjectConfig;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.exception.HasuraJsonException;
import io.shivam.travelplanner.dummy.DummyContent;
import io.shivam.travelplanner.skeletons.Node;
import io.shivam.travelplanner.skeletons.Route;


public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener{


    HasuraClient client;
    Graph graph;
    private ItemFragment newFragment;

    public static SimpleWeightedGraph sWGraph;



    @Override
    public void onListFragmentInteraction(Stack<String> item) {

    }

    Button connectBtn,searchButton;
    Spinner srcSpinner,destSpinner;
    View qFrame;
    ArrayList<Route> routeList;
    String src="6",dest="9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProjectConfig config = new ProjectConfig.Builder()
                .setProjectName("cahoot54") // or it can be .setCustomBaseDomain("myCustomDomain.com")
                .build();

        Hasura.setProjectConfig(config)
                .enableLogs() // not included by default
                .initialise(this);

        client=Hasura.getClient();

        setContentView(R.layout.activity_main);

        connectBtn=(Button) findViewById(R.id.connect);
        qFrame=findViewById(R.id.qframe);
        searchButton=(Button)findViewById(R.id.search);
        srcSpinner=(Spinner)findViewById(R.id.sourceSpinner) ;
        destSpinner=(Spinner)findViewById(R.id.destinationSpinner);

        populateCityNames();

        qFrame.setVisibility(View.GONE);
        try {

            getAllRouts();
        }catch (Exception e)
        {
            System.out.print(e.toString());
        }

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getAllRouts();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         graph=new Graph();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                src=srcSpinner.getSelectedItem().toString();
                dest=destSpinner.getSelectedItem().toString();

                for (Map.Entry<Integer, String> entry : DummyContent.NODE_MAP.entrySet()) {
                    if (entry.getValue().equals(src)) {
                        src=entry.getKey()+"";
                    }
                }

                for (Map.Entry<Integer, String> entry : DummyContent.NODE_MAP.entrySet()) {
                    if (entry.getValue().equals(dest)) {
                        dest=entry.getKey()+"";
                    }
                }



                try {
                    getAllRouts();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void getAllRouts() throws JSONException {
        client.useDataService().setRequestBody(new JSONObject("{\n" +
                "    \"type\": \"select\",\n" +
                "    \"args\": {\n" +
                "        \"table\": \"route\",\n" +
                "        \"columns\": [\n" +
                "            \"from\",\n" +
                "            \"to\",\n" +
                "            \"distance\",\n" +
                "            \"cost\"\n" +
                "        ],\n" +
                "        \"where\": {},\n" +
                "        \"order_by\": [\n" +
                "            {\n" +
                "                \"column\": \"to\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}")).expectResponseTypeArrayOf(Route.class).enqueue(new Callback<List<Route>, HasuraException>() {
            @Override
            public void onSuccess(List<Route> routes) {




                for (Route r: routes
                     ) {
                    graph.addEdge(r.to+"",r.from+"");

                    

                    Log.e(MainActivity.class.getName(),r.to+"");

                }
                qFrame.setVisibility(View.VISIBLE);
                ( (TextView)findViewById(R.id.connect)).setText("Connected");

                Log.i(MainActivity.class.getName(),"success res");

                responseToRecyclerView();
            }

            @Override
            public void onFailure(HasuraException e) {


                try {
                    String str="[\n" +
                            "    {\n" +
                            "        \"from\": 1,\n" +
                            "        \"to\": 4,\n" +
                            "        \"distance\": 500,\n" +
                            "        \"cost\": 900\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 1,\n" +
                            "        \"to\": 9,\n" +
                            "        \"distance\": 3700,\n" +
                            "        \"cost\": 8000\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 1,\n" +
                            "        \"to\": 8,\n" +
                            "        \"distance\": 8500,\n" +
                            "        \"cost\": 1600\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 1,\n" +
                            "        \"to\": 7,\n" +
                            "        \"distance\": 890,\n" +
                            "        \"cost\": 2000\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 1,\n" +
                            "        \"to\": 6,\n" +
                            "        \"distance\": 1056,\n" +
                            "        \"cost\": 3000\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 4,\n" +
                            "        \"to\": 5,\n" +
                            "        \"distance\": 480,\n" +
                            "        \"cost\": 1200\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 4,\n" +
                            "        \"to\": 7,\n" +
                            "        \"distance\": 960,\n" +
                            "        \"cost\": 1500\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 5,\n" +
                            "        \"to\": 6,\n" +
                            "        \"distance\": 75,\n" +
                            "        \"cost\": 300\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 6,\n" +
                            "        \"to\": 10,\n" +
                            "        \"distance\": 1056,\n" +
                            "        \"cost\": 2600\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 6,\n" +
                            "        \"to\": 7,\n" +
                            "        \"distance\": 60,\n" +
                            "        \"cost\": 123\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 6,\n" +
                            "        \"to\": 1,\n" +
                            "        \"distance\": 1056,\n" +
                            "        \"cost\": 5000\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 10,\n" +
                            "        \"to\": 7,\n" +
                            "        \"distance\": 1000,\n" +
                            "        \"cost\": 750\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 10,\n" +
                            "        \"to\": 6,\n" +
                            "        \"distance\": 1056,\n" +
                            "        \"cost\": 2000\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 7,\n" +
                            "        \"to\": 8,\n" +
                            "        \"distance\": 1080,\n" +
                            "        \"cost\": 3000\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 7,\n" +
                            "        \"to\": 9,\n" +
                            "        \"distance\": 950,\n" +
                            "        \"cost\": 2000\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 7,\n" +
                            "        \"to\": 11,\n" +
                            "        \"distance\": 3700,\n" +
                            "        \"cost\": 3700\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 7,\n" +
                            "        \"to\": 4,\n" +
                            "        \"distance\": 950,\n" +
                            "        \"cost\": 7000\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 8,\n" +
                            "        \"to\": 9,\n" +
                            "        \"distance\": 600,\n" +
                            "        \"cost\": 5000\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 8,\n" +
                            "        \"to\": 1,\n" +
                            "        \"distance\": 850,\n" +
                            "        \"cost\": 4564\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 9,\n" +
                            "        \"to\": 8,\n" +
                            "        \"distance\": 600,\n" +
                            "        \"cost\": 7418\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 9,\n" +
                            "        \"to\": 7,\n" +
                            "        \"distance\": 950,\n" +
                            "        \"cost\": 8526\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 9,\n" +
                            "        \"to\": 1,\n" +
                            "        \"distance\": 3700,\n" +
                            "        \"cost\": 8524\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 9,\n" +
                            "        \"to\": 11,\n" +
                            "        \"distance\": 1100,\n" +
                            "        \"cost\": 9635\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 11,\n" +
                            "        \"to\": 7,\n" +
                            "        \"distance\": 820,\n" +
                            "        \"cost\": 2000\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"from\": 11,\n" +
                            "        \"to\": 9,\n" +
                            "        \"distance\": 1100,\n" +
                            "        \"cost\": 2500\n" +
                            "    }\n" +
                            "]";


                    try {

                        Gson gson=new Gson();

                        routeList = new ArrayList<>();
                        String rawBody = str;
                        Log.i("Response", rawBody);

                        JsonArray array = new JsonParser().parse(rawBody).getAsJsonArray();
                        for (JsonElement jsonElement : array) {
                            Route r = gson.fromJson(jsonElement, Route.class);
                            routeList.add(r);
                        }
                       // return responseList; TODO
                    } catch (JsonSyntaxException e3) {
                        String msg = "JSON structure not as expected. Schema changed maybe? : " + e.getMessage();
                        throw new HasuraJsonException(404, msg, e);
                    } catch (JsonParseException e2) {
                        String msg = "Server didn't return valid JSON : " + e.getMessage();
                        throw new HasuraJsonException(404, msg, e);
                    }

                } catch (HasuraJsonException e1) {
                    e1.printStackTrace();
                }


                for (Route r:
                        routeList
                     ) {

                    graph.addEdge(r.from+"",r.to+"");

                }

                Log.i(MainActivity.class.getName(),new AllPaths(graph,"1","10").getLists().toString());
                qFrame.setVisibility(View.VISIBLE);
                ( (TextView)findViewById(R.id.connect)).setText("It seems that there is some problem in the network. You are switched to offline mode.");

                responseToRecyclerView();


            }
        });
    }


    void responseToRecyclerView()
    {

        if(newFragment!=null)
        {
            newFragment.getView().setVisibility(View.GONE);
        }

        DummyContent.ITEMS.clear();
        DummyContent.ITEMS.addAll( new AllPaths(graph,src,dest).getLists());
        Log.d(MainActivity.class.getName(),DummyContent.ITEMS.toString());
         newFragment = ItemFragment.newInstance(routeList);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_recycler,newFragment).commit();


    }

    void getAllNodes()
    {

    }

  void  populateCityNames()
    {

        String str="[\n" +
                "    {\n" +
                "        \"city_name\": \"raipur\",\n" +
                "        \"id\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"city_name\": \"nagpur\",\n" +
                "        \"id\": 4\n" +
                "    },\n" +
                "    {\n" +
                "        \"city_name\": \"durg\",\n" +
                "        \"id\": 5\n" +
                "    },\n" +
                "    {\n" +
                "        \"city_name\": \"bilaspur\",\n" +
                "        \"id\": 6\n" +
                "    },\n" +
                "    {\n" +
                "        \"city_name\": \"ambikapur\",\n" +
                "        \"id\": 7\n" +
                "    },\n" +
                "    {\n" +
                "        \"city_name\": \"mumbai\",\n" +
                "        \"id\": 8\n" +
                "    },\n" +
                "    {\n" +
                "        \"city_name\": \"udaipur\",\n" +
                "        \"id\": 9\n" +
                "    },\n" +
                "    {\n" +
                "        \"city_name\": \"kolkata\",\n" +
                "        \"id\": 10\n" +
                "    },\n" +
                "    {\n" +
                "        \"city_name\": \"Ghazipur\",\n" +
                "        \"id\": 11\n" +
                "    }\n" +
                "]";

            Gson gson=new Gson();

            String rawBody = str;
            Log.i("Response", rawBody);

            JsonArray array = new JsonParser().parse(rawBody).getAsJsonArray();
            for (JsonElement jsonElement : array) {
                Node r = gson.fromJson(jsonElement, Node.class);

              DummyContent.NODE_MAP.put(r.id,r.city_name);

            }

        Object[] list=    DummyContent.NODE_MAP.values().toArray();

        String[] strs=new String[list.length];


        for(int i=0;i<list.length;i++)
        {
            strs[i]=list[i].toString();
        }

        ArrayAdapter<String> gameKindArray= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,strs);
        gameKindArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srcSpinner.setAdapter(gameKindArray);
        destSpinner.setAdapter(gameKindArray);
        // return responseList; TODO

    }

}
