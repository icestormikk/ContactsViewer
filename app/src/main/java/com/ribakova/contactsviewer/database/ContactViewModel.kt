package com.ribakova.contactsviewer.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ribakova.contactsviewer.domain.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ContactViewModel(application: Application): AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val contactsRepository: ContactsRepository

    init {
        val contactsDao = ContactsDatabase.getInstance(application).getContactsDao()
        contactsRepository = ContactsRepository(contactsDao)
    }

    fun getAllContacts() = contactsRepository.getAllContacts()

    fun addContact(contact: Contact) =
        uiScope.async {
            contactsRepository.addContact(contact)
        }

    fun updateContact(contact: Contact) =
        uiScope.async {
            contactsRepository.updateContact(contact)
        }

    fun deleteContact(contact: Contact) =
        uiScope.async {
            contactsRepository.deleteContact(contact)
        }
}