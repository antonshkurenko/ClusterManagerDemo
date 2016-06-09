package io.github.tonyshkurenko.clustermanagerdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by: Anton Shkurenko (tonyshkurenko)
 * Project: ClusterManagerDemo
 * Date: 6/7/16
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class CustomClusterRenderer extends DefaultClusterRenderer<MapsActivity.StringClusterItem> {

  private final IconGenerator mClusterIconGenerator;
  private final Context mContext;

  public CustomClusterRenderer(Context context, GoogleMap map,
      ClusterManager<MapsActivity.StringClusterItem> clusterManager) {
    super(context, map, clusterManager);

    mContext = context;
    mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
  }

  @Override protected void onBeforeClusterItemRendered(MapsActivity.StringClusterItem item,
      MarkerOptions markerOptions) {

    final BitmapDescriptor markerDescriptor =
        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);

    markerOptions.icon(markerDescriptor).snippet(item.title);
  }

  @Override protected void onBeforeClusterRendered(Cluster<MapsActivity.StringClusterItem> cluster,
      MarkerOptions markerOptions) {

    mClusterIconGenerator.setBackground(
        ContextCompat.getDrawable(mContext, R.drawable.background_circle));

    mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);

    final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
  }
}