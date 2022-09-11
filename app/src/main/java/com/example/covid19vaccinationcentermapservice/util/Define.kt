package com.example.covid19vaccinationcentermapservice.util

interface Define {

    interface AppData{
        companion object {
            const val PER_PAGE : Int = 10
            const val LOAD_PAGE_COUNT = 10
            const val DATABASE_NAME ="ViccinavionDB"
        }
    }
}