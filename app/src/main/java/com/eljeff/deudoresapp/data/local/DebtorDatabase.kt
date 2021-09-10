package com.eljeff.deudoresapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eljeff.deudoresapp.data.local.dao.DebtorDao
import com.eljeff.deudoresapp.data.local.dao.UserDao
import com.eljeff.deudoresapp.data.local.entities.Debtor
import com.eljeff.deudoresapp.data.local.entities.User

@Database(entities = [Debtor::class, User::class], version = 1)

abstract class DebtorDatabase:RoomDatabase() {

    abstract fun DebtorDao(): DebtorDao
    abstract fun UserDao(): UserDao

}
