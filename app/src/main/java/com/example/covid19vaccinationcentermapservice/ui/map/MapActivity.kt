package com.example.covid19vaccinationcentermapservice.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.covid19vaccinationcentermapservice.BuildConfig
import com.example.covid19vaccinationcentermapservice.R
import com.example.covid19vaccinationcentermapservice.data.model.VaccinationCenterData
import com.example.covid19vaccinationcentermapservice.databinding.ActivityMainBinding
import com.example.covid19vaccinationcentermapservice.ui.splash.SplashViewModel
import com.example.covid19vaccinationcentermapservice.util.Define
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MapActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {
    private val viewModel: MapViewModel by viewModels()
    private lateinit var naverMap: NaverMap
    private lateinit var binding: ActivityMainBinding
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는

    private val REQUEST_PERMISSION_LOCATION = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        initNaverMap()
        initObserver()
        initListener()
    }


    private fun initListener() {
        binding.myLocation.setOnClickListener {
            val myLatLng = viewModel.getMyLatLng()
            if (myLatLng != null){
                moveCamera(myLatLng.latitude, myLatLng.longitude)
                return@setOnClickListener
            }

            if (checkPermissionForLocation(this)) {
                startLocationUpdates()

            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.vaccinationLoadEvent.collect{
                    when(it){
                        SplashViewModel.VaccinationLoadEvent.Fail -> showToast()
                        else -> {}
                    }
                }
            }
        }


        viewModel.selectedMarker.observe(this) {
            if (it == null) return@observe
            it.let {
                val cameraUpdate =
                    CameraUpdate.scrollAndZoomTo(LatLng(it.lat.toDouble(), it.lng.toDouble()), 15.0)
                        .animate(CameraAnimation.Fly, 500)
                naverMap.moveCamera(cameraUpdate)
            }
        }

        viewModel.vaccinationCenterList.observe(this) {
            if (it == null) return@observe
            it.forEach {
                initMarker(it, naverMap)
            }
        }
    }


    private fun initNaverMap() {
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAP_API_KEY)

        mLocationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        binding.mapView.getMapAsync(this)

    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        viewModel.getAllVaccinationCenterFromDB()
    }

    private fun initMarker(
        centerData: VaccinationCenterData,
        naverMap: NaverMap,
    ) {

        val marker = Marker()
        marker.tag = centerData
        marker.position = LatLng(centerData.lat.toDouble(), centerData.lng.toDouble())
        marker.icon = makeColor(centerData)
        marker.width = Marker.SIZE_AUTO
        marker.height = Marker.SIZE_AUTO
        marker.map = naverMap
        marker.onClickListener = this

        viewModel.addMarker(marker)
    }


    private fun makeColor(it: VaccinationCenterData): OverlayImage {
        return when (it.centerType) {
            Define.AppData.CENTERTYPE_CENTRAL -> MarkerIcons.RED
            Define.AppData.CENTERTYPE_REGION -> MarkerIcons.BLUE
            else -> MarkerIcons.BLACK
        }
    }

    // 위치 권한이 있는지 확인하는 메서드
    private fun checkPermissionForLocation(context: Context): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }


    private fun startLocationUpdates() {

        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        moveCamera(location.latitude, location.longitude)
        viewModel.setMyLatLng(LatLng(location.latitude, location.longitude))
        viewModel.selectedMarkerClear()
    }

    private fun moveCamera(latitude : Double, longitude : Double){
        val cameraUpdate =
            CameraUpdate.scrollAndZoomTo(LatLng(latitude, longitude), 15.0)
                .animate(CameraAnimation.Fly, 1000)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun showToast(){
        Toast.makeText(this, getString(R.string.error_vaccination_center_load), Toast.LENGTH_LONG).show()
    }



    override fun onClick(overlay: Overlay): Boolean {
        if (overlay is Marker) {
            viewModel.onClickMarker(overlay)
        }

        return false
    }


    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()

            } else {
                Toast.makeText(this, getString(R.string.error_permission_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

}
