package com.ribakova.contactsviewer.userInterface

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ribakova.contactsviewer.R
import com.ribakova.contactsviewer.database.ContactViewModel
import com.ribakova.contactsviewer.databinding.FragmentHomeBinding
import com.ribakova.contactsviewer.domain.Contact
import com.ribakova.contactsviewer.domain.ContactStatus
import com.ribakova.contactsviewer.domain.sorting.SearchableContactFields
import com.ribakova.contactsviewer.domain.sorting.SortableContactFields
import com.ribakova.contactsviewer.domain.sorting.SortingDirection
import com.ribakova.contactsviewer.tools.ContactAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactsViewModel: ContactViewModel
    private val aggregatedContacts = MutableLiveData(mutableListOf<Contact>())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        contactsViewModel = ViewModelProvider(this)[ContactViewModel::class.java]
        val root = binding.root
        val fieldsToSortAdapter = ArrayAdapter(
            root.context,
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            arrayOf(
                getString(R.string.fragment_home_not_selected_variant),
                *enumValues<SortableContactFields>().map { getString(it.labelId) }.toTypedArray()
            )
        )
        val fieldsToSearchAdapter = ArrayAdapter(
            root.context,
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            arrayOf(
                getString(R.string.fragment_home_not_selected_variant),
                *enumValues<SearchableContactFields>().map { getString(it.labelId) }.toTypedArray()
            )
        )
        val sortingDirectionsAdapter = ArrayAdapter(
            root.context,
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            enumValues<SortingDirection>().map { getString(it.labelId) }
        )
        val statusesAdapter = ArrayAdapter(
            root.context,
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            arrayOf(
                getString(R.string.fragment_home_not_selected_variant),
                *enumValues<ContactStatus>().map { getString(it.labelId) }.toTypedArray()
            )
        )

        val contactsList = binding.contactsList
        val aggregationLayout = binding.aggregationLayout
        val aggregationButton = binding.aggregationButton
        val fieldToSortSpinner = binding.fieldToSortSpinner
        val sortingDirectionSpinner = binding.sortingDirectionSpinner
        val statusFilterSpinner = binding.statusFilterSpinner
        val contactSearchField = binding.contactSearchField
        val fieldToSearchSpinner = binding.fieldToSearchSpinner
        val applyAggregationButton = binding.applyAggregationButton

        fieldToSortSpinner.adapter = fieldsToSortAdapter
        sortingDirectionSpinner.adapter = sortingDirectionsAdapter
        statusFilterSpinner.adapter = statusesAdapter
        fieldToSearchSpinner.adapter = fieldsToSearchAdapter

        contactsViewModel.getAllContacts().observe(viewLifecycleOwner) {
            contactsList.apply {
                adapter = ContactAdapter(it, root.context)
                layoutManager = LinearLayoutManager(root.context)
            }
        }

        aggregationLayout.visibility = View.GONE
        aggregationButton.setOnClickListener {
            aggregationLayout.visibility =
                if (aggregationLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        applyAggregationButton.setOnClickListener {
            val selectedFieldToSort = enumValues<SortableContactFields>().find { field ->
                getString(field.labelId) == fieldToSortSpinner.selectedItem.toString()
            } ?: return@setOnClickListener
            val selectedSortDirection = enumValues<SortingDirection>().find { dir ->
                getString(dir.labelId) == sortingDirectionSpinner.selectedItem.toString()
            } ?: return@setOnClickListener
            val selectedFilterStatus = enumValues<ContactStatus>().find { status ->
                getString(status.labelId) == statusFilterSpinner.selectedItem.toString()
            }
            val selectedFieldToSearch = enumValues<SearchableContactFields>().find { status ->
                getString(status.labelId) == fieldToSearchSpinner.selectedItem.toString()
            } ?: return@setOnClickListener
            val searchValue = contactSearchField.text

            contactsViewModel.getAllContacts().observe(viewLifecycleOwner) {
                var resultList = it

                if (selectedFieldToSort != SortableContactFields.NONE) {
                    resultList = resultList.sortedWith(
                        selectedFieldToSort.getComparator(selectedSortDirection, root.context)
                    )
                }
                if (selectedFilterStatus != null && getString(selectedFilterStatus.labelId) != getString(R.string.fragment_home_not_selected_variant)) {
                    resultList = resultList.filter { contact -> contact.status.name == selectedFilterStatus.name }
                }
                if (selectedFieldToSearch != SearchableContactFields.NONE) {
                    resultList = selectedFieldToSearch.onSearch(resultList, searchValue.toString())
                }

                aggregatedContacts.value = resultList.toMutableList()
            }
        }

        contactsViewModel.getAllContacts().observe(viewLifecycleOwner) {
            contactsList.apply {
                adapter = ContactAdapter(it, root.context)
                layoutManager = LinearLayoutManager(root.context)
            }
        }
        aggregatedContacts.observe(viewLifecycleOwner) {
            contactsList.adapter = ContactAdapter(it, root.context)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}