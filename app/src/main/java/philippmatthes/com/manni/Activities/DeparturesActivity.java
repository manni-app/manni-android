package philippmatthes.com.manni.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import philippmatthes.com.manni.Adapters.DepartureAdapter;
import philippmatthes.com.manni.R;
import philippmatthes.com.manni.jVVO.Models.Departure;
import philippmatthes.com.manni.jVVO.Models.RouteChange;

public class DeparturesActivity extends SpinnerActivity {

    String stopName;

    ListView departureListView;
    DepartureAdapter adapter;
    ArrayList<Departure> departures;
    SwipeRefreshLayout layout;

    private void setUp() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_departures);

        departureListView = (ListView) findViewById(R.id.stop_list_view);

        departures = new ArrayList<>();

        adapter = new DepartureAdapter(this, departures);

        departureListView.setAdapter(adapter);

        layout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        layout.setOnRefreshListener(
                () -> loadDepartures(stopName)
        );

        setTitle(stopName);

        departureListView.setOnItemClickListener((adapterView, view, i, l) -> {
            ArrayList<String> routeChanges = departures.get(i).getRouteChanges();
            if (routeChanges != null && !routeChanges.isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), RouteChangeActivity.class);
                intent.putStringArrayListExtra("changes", routeChanges);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void startAnimatingSpinner() {
        super.startAnimatingSpinner();
        departureListView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void stopAnimatingSpinner() {
        super.stopAnimatingSpinner();
        departureListView.setVisibility(View.VISIBLE);
    }

    private void loadDepartures(String stopName) {
        startAnimatingSpinner();

        RequestQueue queue = Volley.newRequestQueue(this);

        Departure.monitorByName(stopName, queue, response -> {
            if (response.getResponse().isPresent()) {
                List<Departure> responseDepartures = response.getResponse().get().getDepartures();
                if (responseDepartures != null) {
                    departures = new ArrayList<>();
                    departures.addAll(responseDepartures);
                    adapter.clear();
                    adapter.addAll(departures);
                    adapter.notifyDataSetChanged();
                    stopAnimatingSpinner();
                }
            } else {
                System.out.println(response.getError().get().getDescription());
            }

            if (layout.isRefreshing()) {
                layout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopName = IntentHandler.getStopName();
        setUp();
        loadDepartures(stopName);
    }


}
