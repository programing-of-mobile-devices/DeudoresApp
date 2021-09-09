package com.eljeff.deudoresapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eljeff.deudoresapp.data.dao.DebtorDao
import com.eljeff.deudoresapp.data.dao.UserDao
import com.eljeff.deudoresapp.data.entities.Debtor
import com.eljeff.deudoresapp.data.entities.User

@Database(entities = [Debtor::class, User::class], version = 1)

abstract class DebtorDatabase:RoomDatabase() {

    abstract fun DebtorDao(): DebtorDao
    abstract fun UserDao(): UserDao

}
