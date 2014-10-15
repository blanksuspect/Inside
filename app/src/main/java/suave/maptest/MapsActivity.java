package suave.maptest;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import android.os.AsyncTask;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LatLng HOME = new LatLng(41.291402, -72.960382);// Original (UNH): (41.291402, -72.961182)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        goToUNH();

        try {
            URL bkmn = new URL("http://img1.wikia.nocookie.net/__cb20120128201030/muppet/images/9/92/Bigbirdnewversion.png");  // Image location of Buckman floor 2
            new DownloadBMP(mMap).execute(bkmn);
        }
        catch (IOException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    // ---------------------------------------------------------------------------------------------
    // My functions start here

    private void goToUNH() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HOME, 18.0f));
    }

}


class DownloadBMP extends AsyncTask<URL, Void, Bitmap> {
    Bitmap map;
    GoogleMap googleM;
    LatLng HOME = new LatLng(41.291402, -72.960382);

    public DownloadBMP(GoogleMap g) {
        this.googleM = g;
    }

    protected Bitmap doInBackground(URL... u) {
        Bitmap bit = null;
        try {
            bit = BitmapFactory.decodeStream(u[0].openConnection().getInputStream());
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bit;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        GroundOverlayOptions BuckmanMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(result))
                .position(HOME, 100f);
        googleM.addGroundOverlay(BuckmanMap);
    }
}