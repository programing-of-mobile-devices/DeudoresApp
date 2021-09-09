package com.eljeff.deudoresapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.eljeff.deudoresapp.data.entities.User

@Dao
interface UserDao {

    @Insert
    fun createUser(user: User)

    @Query("SELECT * FROM table_user" )
    fun getUsers() : MutableList<User>

    @Query("SELECT * FROM table_user WHERE email LIKE :email")
    fun searchUser(email: String) : User

}