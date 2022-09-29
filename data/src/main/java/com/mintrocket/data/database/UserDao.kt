package com.mintrocket.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mintrocket.data.model.db.UserDb

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserDb)

    @Delete
    fun delete(user: UserDb)

    @Transaction
    fun update(user: UserDb) {
        nukeTable()
        insert(user)
    }

    @Query("SELECT * FROM profile_table")
    fun getUserLD(): LiveData<UserDb>

    @Query("SELECT * FROM profile_table")
    fun getUser(): UserDb?

    @Query("DELETE FROM profile_table")
    fun nukeTable()
}