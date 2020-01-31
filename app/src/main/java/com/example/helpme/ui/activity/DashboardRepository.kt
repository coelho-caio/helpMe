package com.example.helpme.ui.activity

import android.content.Context
import com.example.helpme.Database.DatabaseHelpMe
import com.example.helpme.Database.DependentDatabase
import com.example.helpme.model.Dependent

class DashboardRepository {

    fun configuraDataBase(context: Context, dependents: MutableList<Dependent>): MutableList<Dependent> {
        val dbDependent = DependentDatabase(context)
        val cursor = dbDependent.getAllDependent()
        cursor!!.moveToFirst()
        while (cursor.moveToNext()) {

            val name = (cursor.getString(cursor.getColumnIndex(DatabaseHelpMe.DBHelpMe.COLUMN_NAME)))
            val email = (cursor.getString(cursor.getColumnIndex(DatabaseHelpMe.DBHelpMe.COLUMN_EMAIL)))
            val phone = (cursor.getString(cursor.getColumnIndex(DatabaseHelpMe.DBHelpMe.COLUMN_PHONE)))
            val userId = (cursor.getInt(cursor.getColumnIndex(DatabaseHelpMe.DBHelpMe.COLUMN_USER)))
            dependents.add(Dependent(name, email, phone, userId))
        }
        cursor.close()
        return dependents
    }
}