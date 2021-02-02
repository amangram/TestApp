package kg.amangram.testapp.ui.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kg.amangram.testapp.Constants.PERMISSION_CODE
import kg.amangram.testapp.services.MyService
import kg.amangram.testapp.R
import kg.amangram.testapp.data.location.LocationUpdateEvent
import kg.amangram.testapp.databinding.FragmentLocationBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LocationFragment : Fragment() {

    private var isEnabled = false
    private var binding: FragmentLocationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_location, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLocationBinding.bind(view)
        binding?.btnStart?.setOnClickListener {
            if (isLocationEnabled()) {
                if (checkPermissions()) {
                    buttonClick()
                } else {
                    requestPermissions()
                }
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

        }
    }

    private fun buttonClick() {
        if (isEnabled) {
            binding?.btnStart?.text = getString(R.string.start)
            requireActivity().stopService(Intent(activity, MyService::class.java))
        } else {
            binding?.btnStart?.text = getString(R.string.stop)
            requireActivity().startService(Intent(activity, MyService::class.java))
        }
        isEnabled = !isEnabled
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LocationUpdateEvent?) {
        binding?.textNotifications?.text =
            "Lat: ${event?.location?.latitude} Lon: ${event?.location?.longitude}"
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                buttonClick()
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
        requireActivity().stopService(Intent(activity, MyService::class.java))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}