package com.ribakova.contactsviewer.domain.sorting

import com.ribakova.contactsviewer.R
import com.ribakova.contactsviewer.domain.Contact

enum class SearchableContactFields(
    val labelId: Int,
    val onSearch: List<Contact>.(currentString: String) -> List<Contact>
) {
    NAME(
        R.string.fragment_home_search_by_name,
        {currentString -> this.filter { it.name.contains(currentString) }}
    ),
    SURNAME(
        R.string.fragment_home_search_by_surname,
        {currentString -> this.filter { it.surname.contains(currentString) }}
    ),
    PATRONYMIC(
        R.string.fragment_home_search_by_patronymic,
        {currentString -> this.filter {
            it.patronymic != null && it.patronymic.contains(currentString)
        }}
    ),
    CELL_PHONE(
        R.string.fragment_home_search_by_cell_phone,
        {currentString -> this.filter { it.cellPhone.contains(currentString) }}
    ),
    HOME_PHONE(
        R.string.fragment_home_search_by_home_phone,
        {currentString -> this.filter {
            it.homePhone != null && it.homePhone.contains(currentString)
        }}
    ),
    NONE(R.string.fragment_home_not_selected_variant, { this })
}