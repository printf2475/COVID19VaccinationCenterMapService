package com.example.covid19vaccinationcentermapservice.util

interface Define {

    interface AppData{
        companion object {
            const val PER_PAGE : Int = 10
            const val LOAD_PAGE_COUNT = 10
            const val PROGRESS_DELAY = 20L
            const val PROGRESS_FINISH_COUNT = 100
            const val DATABASE_NAME ="ViccinavionDB"
            const val CENTERTYPE_REGION = "지역"
            const val CENTERTYPE_CENTRAL = "중앙/권역"
        }
    }
}