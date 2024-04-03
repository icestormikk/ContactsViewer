package com.ribakova.contactsviewer.userInterface

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.ribakova.contactsviewer.R
import com.ribakova.contactsviewer.database.ContactViewModel
import com.ribakova.contactsviewer.databinding.FragmentEditContactBinding
import com.ribakova.contactsviewer.domain.Contact
import com.ribakova.contactsviewer.domain.ContactStatus
import com.ribakova.contactsviewer.tools.TextValidator
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditContactFragment : Fragment() {
    private var _binding: FragmentEditContactBinding? = null
    private val binding get() = _binding!!
    private val arguments by navArgs<EditContactFragmentArgs>()
    private lateinit var contactsViewModel: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditContactBinding.inflate(inflater, container, false)
        contactsViewModel = ViewModelProvider(this)[ContactViewModel::class.java]
        val root = binding.root
        val existingContact = arguments.existingContact
        val statusesAdapter = ArrayAdapter(
            root.context,
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            enumValues<ContactStatus>().map { getString(it.labelId) }
        )

        val contactName = binding.contactName
        val contactSurname = binding.contactSurname
        val contactPatronymic = binding.contactPatronymic
        val contactCellPhone = binding.contactCellPhone
        val contactHomePhone = binding.contactHomePhone
        val contactStatusSpinner = binding.contactStatusSpinner
        val editButton = binding.editContactButton
        val deleteButton = binding.deleteContactButton

        arrayOf(contactName, contactSurname, contactCellPhone).forEach {
            it.addTextChangedListener(object : TextValidator(it) {
                override fun validate(textView: TextView?, text: String?) {
                    if (textView == null) return
                    if (text.isNullOrBlank()) {
                        textView.error = getString(R.string.app_field_is_not_empty_or_null)
                    }
                }
            })
        }
        contactStatusSpinner.adapter = statusesAdapter
        deleteButton.apply {
            visibility = if (existingContact == null) View.GONE else View.VISIBLE
            setOnClickListener {
                lifecycleScope.launch {
                    runBlocking {
                        if (existingContact != null) {
                            contactsViewModel.deleteContact(existingContact)
                        }
                    }
                    root.findNavController().navigate(R.id.HomeFragment)
                }
            }
        }
        editButton.apply {
            text = if (existingContact == null) {
                getString(R.string.fragment_edit_contact_create_button_label)
            } else {
                getString(R.string.fragment_edit_contact_update_button_label)
            }
            setOnClickListener {
                val isFieldsCorrect = !contactName.text.isNullOrBlank()
                        && !contactSurname.text.isNullOrBlank()
                        && !contactCellPhone.text.isNullOrBlank()
                val selectedStatus = enumValues<ContactStatus>().find {
                    getString(it.labelId) == contactStatusSpinner.selectedItem.toString()
                }
                if (!isFieldsCorrect || selectedStatus == null) {
                    showMessageOnScreen(root.context, getString(R.string.app_fields_have_error))
                    return@setOnClickListener
                }
                val contact = Contact(
                    name = contactName.text.toString(),
                    surname = contactSurname.text.toString(),
                    patronymic = if (contactPatronymic.text.isEmpty()) null else contactPatronymic.text.toString(),
                    cellPhone = contactCellPhone.text.toString(),
                    homePhone = if (contactHomePhone.text.isEmpty()) null else contactHomePhone.text.toString(),
                    status = selectedStatus
                )

                lifecycleScope.launch {
                    runBlocking {
                        if (existingContact == null) {
                            contactsViewModel.addContact(contact)
                        } else {
                            contactsViewModel.updateContact(contact.copy(id = existingContact.id))
                        }
                    }
                    root.findNavController().navigate(R.id.HomeFragment)
                }
            }
        }

        existingContact?.apply {
            contactName.text = name.toEditable()
            contactSurname.text = surname.toEditable()
            contactPatronymic.text = (patronymic ?: "").toEditable()
            contactCellPhone.text = cellPhone.toEditable()
            contactHomePhone.text = (homePhone ?: "").toEditable()

            val selectedStatusPosition = statusesAdapter.getPosition(getString(status.labelId))
            contactStatusSpinner.setSelection(selectedStatusPosition)
        }

        return root
    }

    private fun showMessageOnScreen(context: Context, message: String) =
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()

    private fun String.toEditable(): Editable =
        Editable.Factory.getInstance().newEditable(this)
}