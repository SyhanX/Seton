package com.example.seton.presentation.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seton.R
import com.example.seton.databinding.FragmentNotesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val TAG = "notesfragment"

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

        recyclerView = binding.noteRecycler

        setUpRecyclerView()
        observeData()
        
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.fabCreateNote.setOnClickListener {
            findNavController().navigate(R.id.action_NotesFragment_to_EditNoteFragment)
        }
    }

    private fun setUpRecyclerView() {
        recyclerView.apply {
            adapter = notesAdapter
            layoutManager = if (viewModel.state.value.isLinearLayout) {
                LinearLayoutManager(requireContext())
            } else {
                GridLayoutManager(requireContext(), 2)
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.state.collectLatest {
                notesAdapter.submitList(it.noteList)
            }
        }
    }
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.notes_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_settings -> {
                //do something

                true
            }
            R.id.action_about -> {
                //do something

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