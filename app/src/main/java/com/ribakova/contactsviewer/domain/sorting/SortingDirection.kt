package com.ribakova.contactsviewer.domain.sorting

import com.ribakova.contactsviewer.R

enum class SortingDirection(val labelId: Int) {
    ASCENDING(R.string.fragment_home_dir_asc),
    DESCENDING(R.string.fragment_home_dir_desc)
}