package com.example.tubespbp.entity

import com.example.tubespbp.R

class Bengkel(var name: String, var alamat: String,var jamBuka: String,var jenis:String, var image: Int) {
    companion object {
        @JvmField
        var listOfBengkel= arrayOf(
            Bengkel("RQ Otomotif","Babarsari","08:00-18:00","Bengkel Mobil", R.drawable.rq),
            Bengkel("Suzuki Adi Sucipto","LAKS ADI SUCIPTO","08:00-18:00","Bengkel Mobil", R.drawable.suzuki),
            Bengkel("GT Airvindo AC Specialist","RING ROAD TIMUR","08:00-20:00","Bengkel Mobil", R.drawable.gt),
            Bengkel("Rick's Garage","Jl. Gatak no 11 rt 13","09:00-23:00","Bengkel Mobil", R.drawable.rick),
            Bengkel("Hyundai Yogyakarta","LAKS ADI SUCIPTO","08:00-18:00","Bengkel Mobil", R.drawable.hyundai),
            Bengkel("Bengkel JOGJA MOTOR","Tambak Bayan I no. 5","08:00-17:00","Bengkel Motor", R.drawable.amj),
            Bengkel("Ahass Buana","Wonocatur","07:00-17:00","Bengkel Motor", R.drawable.ahass),
            Bengkel("Ahass 2764","Raya Tajem","08:00-17:00","Bengkel Motor", R.drawable.aha),
            Bengkel("Yamaha Mulia Jaya","Adi Sucipto","08:00-17:00","Bengkel Motor", R.drawable.yama),
            Bengkel("Honda Anugrah","Ring Road Selatan","08:00-17:00","Bengkel Mobil", R.drawable.honda),

            )
    }
}