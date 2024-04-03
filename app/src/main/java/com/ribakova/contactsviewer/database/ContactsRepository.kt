package com.ribakova.contactsviewer.database

import com.ribakova.contactsviewer.domain.Contact

class ContactsRepository(private val contactsDao: ContactsDao) {
    fun getAllContacts() = contactsDao.getAll()

    suspend fun addContact(contact: Contact) = contactsDao.insert(contact)

    suspend fun updateContact(contact: Contact) = contactsDao.update(contact)

    suspend fun deleteContact(contact: Contact) = contactsDao.delete(contact)
}