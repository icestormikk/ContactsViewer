package com.ribakova.contactsviewer.domain.sorting

import android.content.Context
import com.ribakova.contactsviewer.R
import com.ribakova.contactsviewer.domain.Contact

enum class SortableContactFields(val labelId: Int) {
    NAME(R.string.fragment_home_sorting_by_name),
    SURNAME(R.string.fragment_home_sorting_by_surname),
    STATUS(R.string.fragment_home_sorting_by_status),
    NONE(R.string.fragment_home_not_selected_variant);

    fun getComparator(direction: SortingDirection, context: Context): Comparator<Contact> {
        return when (this) {
            NAME -> {
                when (direction) {
                    SortingDirection.ASCENDING -> compareBy { it.name }
                    SortingDirection.DESCENDING -> compareByDescending { it.name }
                }
            }
            SURNAME -> {
                when (direction) {
                    SortingDirection.ASCENDING -> compareBy { it.surname }
                    SortingDirection.DESCENDING -> compareByDescending { it.surname }
                }
            }
            STATUS -> {
                when (direction) {
                    SortingDirection.ASCENDING -> compareBy { context.getString(it.status.labelId) }
                    SortingDirection.DESCENDING -> compareByDescending { context.getString(it.status.labelId) }
                }
            }
            NONE -> { throw Error("Cannot give a comparator from NONE variant") }
        }
    }
}