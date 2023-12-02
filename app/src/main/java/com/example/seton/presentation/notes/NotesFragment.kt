package com.example.seton.presentation.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.seton.R
import com.example.seton.databinding.FragmentNotesBinding
import com.example.seton.domain.util.observeWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : Fragment(), MenuProvider {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private val notesAdapter = NotesAdapter()

    private val viewModel: NotesViewModel by viewModels()

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
        submitData()
        changeListVisibility()

        binding.fabCreateNote.setOnClickListener {
            findNavController().navigate(R.id.action_NotesFragment_to_EditNoteFragment)
        }
    }

    private fun submitData() {
        lifecycleScope.launch {
            viewModel.state.collectLatest {
                notesAdapter.submitList(it.noteList)
            }
        }

    }

    private fun setUpRecyclerView() {
        recyclerView.apply {
            adapter = notesAdapter
            viewModel.state.observeWithLifecycle(this@NotesFragment) {
                layoutManager = if (it.isLinearLayout) {
                    LinearLayoutManager(requireContext())
                } else {
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                }
            }
        }
    }

    private fun changeListVisibility() {
        viewModel.state.observeWithLifecycle(this) { listState ->
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

        menuItem.icon = if (viewModel.state.value.isLinearLayout) {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_view_agenda)
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_dashboard)
        }
        viewModel.onEvent(NoteEvent.ChangeLayout)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.notes_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_about -> {
                findNavController().navigate(R.id.action_NotesFragment_to_aboutFragment)
                true
            }

            R.id.action_change_layout -> {
                changeLayout(menuItem)
                true
            }

            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}