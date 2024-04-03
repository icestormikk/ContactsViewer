package com.ribakova.contactsviewer.tools

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ribakova.contactsviewer.databinding.LayoutContactCardBinding
import com.ribakova.contactsviewer.domain.Contact
import com.ribakova.contactsviewer.userInterface.HomeFragmentDirections

class ContactAdapter(
    private val contacts: List<Contact>,
    private val context: Context
): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val contactCardBinding: LayoutContactCardBinding
    ) : RecyclerView.ViewHolder(contactCardBinding.root){
        fun bind(contact: Contact) {
            with (contactCardBinding) {
                contactInitials.text = contact.getInitials()
                contactPhones.text = String.format("%s%s", contact.cellPhone, if (contact.homePhone != null) " / ${contact.cellPhone}" else "")
                contactStatus.text = context.getString(contact.status.labelId)
                contactLinearLayout.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToEditContactFragment(contact)
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutContactCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }
}