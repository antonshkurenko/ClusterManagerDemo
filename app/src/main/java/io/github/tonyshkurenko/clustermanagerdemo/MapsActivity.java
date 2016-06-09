package io.github.tonyshkurenko.clustermanagerdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

  private GoogleMap mMap;

  private ClusterManager<StringClusterItem> mClusterManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);

    final SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }

  @Override public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    mClusterManager = new ClusterManager<>(this, mMap);

    final CustomClusterRenderer renderer = new CustomClusterRenderer(this, mMap, mClusterManager);

    mClusterManager.setRenderer(renderer);

    mClusterManager.setOnClusterClickListener(
        new ClusterManager.OnClusterClickListener<StringClusterItem>() {
          @Override public boolean onClusterClick(Cluster<StringClusterItem> cluster) {

            Toast.makeText(MapsActivity.this, "Cluster click", Toast.LENGTH_SHORT).show();

            // if true, do not move camera

            return false;
          }
        });

    mClusterManager.setOnClusterItemClickListener(
        new ClusterManager.OnClusterItemClickListener<StringClusterItem>() {
          @Override public boolean onClusterItemClick(StringClusterItem clusterItem) {

            Toast.makeText(MapsActivity.this, "Cluster item click", Toast.LENGTH_SHORT).show();

            // if true, click handling stops here and do not show info view, do not move camera
            // you can avoid this by calling:
            // renderer.getMarker(clusterItem).showInfoWindow();

            return false;
          }
        });

    mClusterManager.getMarkerCollection()
        .setOnInfoWindowAdapter(new CustomInfoViewAdapter(LayoutInflater.from(this)));

    mClusterManager.setOnClusterItemInfoWindowClickListener(
        new ClusterManager.OnClusterItemInfoWindowClickListener<StringClusterItem>() {
          @Override public void onClusterItemInfoWindowClick(StringClusterItem stringClusterItem) {
            Toast.makeText(MapsActivity.this, "Clicked info window: " + stringClusterItem.title,
                Toast.LENGTH_SHORT).show();
          }
        });

    mMap.setOnInfoWindowClickListener(mClusterManager);
    mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
    mMap.setOnCameraChangeListener(mClusterManager);
    mMap.setOnMarkerClickListener(mClusterManager);

    for (int i = 0; i < 10; i++) {
      final LatLng latLng = new LatLng(-34 + i, 151 + i);
      mClusterManager.addItem(new StringClusterItem("Marker #" + (i + 1), latLng));
    }

    mClusterManager.cluster();
  }

  static class StringClusterItem implements ClusterItem {

    final String title;
    final LatLng latLng;

    public StringClusterItem(String title, LatLng latLng) {
      this.title = title;
      this.latLng = latLng;
    }

    @Override public LatLng getPosition() {
      return latLng;
    }
  }
}
