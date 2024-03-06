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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.seton.R
import com.example.seton.common.domain.util.StorageManager
import com.example.seton.common.domain.util.observeWithLifecycle
import com.example.seton.common.domain.util.showAlertDialog
import com.example.seton.databinding.FragmentNotesBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "NotesFragment"

@AndroidEntryPoint
class NoteListFragment : Fragment(), MenuProvider {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private val notesAdapter = NoteListAdapter()

    private val viewModel: NoteListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        recyclerView = binding.noteRecycler

        setUpRecyclerView()
//        manageGestures()
        submitData()
        changeListVisibility()

        binding.fabCreateNote.setOnClickListener {
            findNavController().navigate(R.id.action_NotesFragment_to_EditNoteFragment)
        }

    }

    private fun manageGestures() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        showAlertDialog(
                            context = requireContext(),
                            title = R.string.yes,
                            message = R.string.no
                        ) {
                            Snackbar.make(
                                binding.root,
                                "Work in progress",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(recyclerView)
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