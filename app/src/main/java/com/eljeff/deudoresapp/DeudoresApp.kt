package com.eljeff.deudoresapp

import android.app.Application
import androidx.room.Room

import com.eljeff.deudoresapp.data.local.DebtorDatabase

class DeudoresApp: Application() {

    companion object{
        // constructor
        lateinit var database: DebtorDatabase
    }

    override fun onCreate() {
        super.onCreate()

        //this.deleteDatabase("debtor_db")

        database = Room.databaseBuilder(this, DebtorDatabase::class.java, "debtor_db")
        .allowMainThreadQueries()
        //.fallbackToDestructiveMigration()
        .build()
    }
}