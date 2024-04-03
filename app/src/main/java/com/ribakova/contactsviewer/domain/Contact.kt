package com.ribakova.contactsviewer.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "contacts_table")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "surname")
    val surname: String,

    @ColumnInfo(name = "patronymic")
    val patronymic: String?,

    @ColumnInfo(name = "cellPhone")
    val cellPhone: String,

    @ColumnInfo(name = "homePhone")
    val homePhone: String?,

    @ColumnInfo(name = "status")
    val status: ContactStatus
) : Parcelable {
    fun getInitials() = String.format("%s %s %s", name, surname, patronymic ?: "")
}
