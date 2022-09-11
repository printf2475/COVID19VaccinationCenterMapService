package com.example.covid19vaccinationcentermapservice.adapter

import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.covid19vaccinationcentermapservice.data.model.VaccinationCenterData

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("marker_info_visibility_bind")
    fun setVisible(view: ConstraintLayout, vaccinationCenterData: VaccinationCenterData?) {
        view.visibility = if (vaccinationCenterData !=null) View.VISIBLE
        else View.GONE
    }
}


