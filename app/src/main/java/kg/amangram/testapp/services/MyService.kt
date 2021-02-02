package kg.amangram.testapp.services

import android.R
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import kg.amangram.testapp.Constants
import kg.amangram.testapp.MainActivity
import kg.amangram.testapp.data.location.LocationDTO
import kg.amangram.testapp.data.location.LocationUpdateEvent
import org.greenrobot.eventbus.EventBus


class MyService : Service() {

    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 3000
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null

    override fun onCreate() {
        super.onCreate()
        initData()
    }
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val currentLocation: Location = locationResult.lastLocation
            Log.d(
                "Locations",
                currentLocation.latitude.toString() + "," + currentLocation.longitude
            )

            val location = LocationDTO()
            location.latitude = currentLocation.latitude
            location.longitude = currentLocation.longitude

            EventBus.getDefault().post(LocationUpdateEvent(location))
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        prepareForegroundNotification()
        startLocationUpdates()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mFusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mFusedLocationClient!!.requestLocationUpdates(
            locationRequest,
            locationCallback, Looper.myLooper()
        )
    }

    private fun prepareForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                Constants.CHANNEL_ID,
                "Location Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            Constants.SERVICE_LOCATION_REQUEST_CODE,
            notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setContentTitle("TestApp")
            .setContentTitle("description")
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(Constants.LOCATION_SERVICE_NOTIF_ID, notification)
    }
    override fun onBind(intent: Intent): IBinder? {
       return null
    }

    private fun initData() {
        locationRequest = LocationRequest.create()
        locationRequest?.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)
    }
}