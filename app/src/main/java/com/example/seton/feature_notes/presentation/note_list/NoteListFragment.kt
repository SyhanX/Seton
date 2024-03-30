package com.example.seton.feature_notes.presentation.note_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.seton.R
import com.example.seton.common.domain.util.StorageManager
import com.example.seton.common.domain.util.observeWithLifecycle
import com.example.seton.common.domain.util.showAlertDialog
import com.example.seton.databinding.FragmentNoteListBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "NotesFragment"

@AndroidEntryPoint
class NoteListFragment : Fragment(), MenuProvider {
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private val notesAdapter = NoteListAdapter()

    private val viewModel: NoteListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        recyclerView = binding.noteRecycler

        setUpRecyclerView()
        submitData()
        changeListVisibility()
        toggleSelectionMode()

        binding.fabCreateNote.setOnClickListener {
            findNavController().navigate(R.id.action_NotesFragment_to_EditNoteFragment)
        }

    }

    private fun toggleSelectionMode() {
        val defaultToolbar =
            requireActivity().findViewById<MaterialToolbar>(R.id.default_toolbar)
        val selectionToolbar =
            requireActivity().findViewById<MaterialToolbar>(R.id.selection_mode_toolbar)

        selectionToolbar.setNavigationOnClickListener {
            viewModel.uncheckAllNotes()
        }

        viewModel.noteListState.observeWithLifecycle(this) { state ->
            val areListsEqualSize = state.noteList.size == state.selectedNoteList.size
            selectionToolbar.menu.findItem(R.id.action_select_all).also {
                if (!areListsEqualSize) {
                    it.isChecked = false
                    it.setIcon(R.drawable.ic_checkbox_empty)
                } else {
                    it.isChecked = true
                    it.setIcon(R.drawable.ic_checkbox_filled)
                }
            }
            selectionToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_select_all -> {
                        if (it.isChecked) {
                            viewModel.uncheckAllNotes()
                        } else {
                            viewModel.checkAllNotes()
                        }
                        true
                    }

                    R.id.action_delete_selected -> {
                        showAlertDialog(
                            context = requireContext(),
                            message = R.string.ask_delete_selected_notes,
                            title = R.string.delete_notes,
                            positiveText = R.string.delete,
                            negativeText = R.string.cancel
                        ) {
                            state.selectedNoteList.forEach { noteId ->
                                viewModel.deleteNoteById(noteId)
                            }
                            viewModel.uncheckAllNotes()
                        }
                        true
                    }

                    else -> false
                }
            }

            if (state.selectedNoteList.isNotEmpty()) {
                defaultToolbar.visibility = View.INVISIBLE
                selectionToolbar.visibility = View.VISIBLE
                selectionToolbar.title =
                    getString(R.string.selected_items_count, state.selectedNoteList.size)
            } else {
                defaultToolbar.visibility = View.VISIBLE
                selectionToolbar.visibility = View.INVISIBLE
            }
        }
    }

    private fun submitData() {
        viewModel.noteListState.observeWithLifecycle(this) {
            notesAdapter.submitList(it.noteList)
        }
    }

    private fun setUpRecyclerView() {
        recyclerView.apply {
            adapter = notesAdapter
            viewModel.noteListState.observeWithLifecycle(this@NoteListFragment) {
                layoutManager = if (it.isLinearLayout) {
                    LinearLayoutManager(requireContext())
                } else {
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                }
            }
        }
    }

    private fun changeListVisibility() {
        viewModel.noteListState.observeWithLifecycle(this) { listState ->
            if (listState.noteList.isEmpty()) {
                binding.noteRecycler.visibility = View.GONE
                binding.emptyListText.visibility = View.VISIBLE
            } else {
                binding.noteRecycler.visibility = View.VISIBLE
                binding.emptyListText.visibility = View.GONE
            }
        }
    }

    private fun changeLayout(menuItem: MenuItem?) {
        if (menuItem == null) return

        menuItem.icon = if (viewModel.noteListState.value.isLinearLayout) {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_view_agenda)
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_dashboard)
        }
        viewModel.changeLayout()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.note_list_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.action_NotesFragment_to_settingsFragment)
                true
            }

            R.id.action_about -> {
                findNavController().navigate(R.id.action_NotesFragment_to_aboutFragment)
                true
            }

            R.id.action_change_layout -> {
                changeLayout(menuItem)
                true
            }

            R.id.action_delete_notes -> {
                showAlertDialog(
                    context = requireContext(),
                    message = R.string.ask_delete_all_notes,
                    positiveText = R.string.delete,
                    negativeText = R.string.cancel
                ) {
                    viewModel.deleteAllNotes()
                    StorageManager.deleteEverythingFromAppDirectory(requireContext())
                }
                true
            }

            else -> false
        }
    }

    private fun uncheckAllNotes() {
        if (viewModel.noteListState.value.selectedNoteList.isNotEmpty()) {
            viewModel.uncheckAllNotes()
        } else {
            requireActivity().moveTaskToBack(true)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this) {
                uncheckAllNotes()
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}