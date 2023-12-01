package com.example.seton.presentation.edit_note

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.seton.R
import com.example.seton.data.datasource.observeWithLifecycle
import com.example.seton.databinding.FragmentEditNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteFragment : Fragment(), MenuProvider {
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditNoteViewModel by viewModels()

    private val args by navArgs<EditNoteFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)

        viewModel.noteTitle.observeWithLifecycle(this) { titleState ->
            binding.addTitle.setText(titleState.text)
        }
        viewModel.noteContent.observeWithLifecycle(this) { contentState ->
            binding.addContent.setText(contentState.text)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.btnSave.setOnClickListener {
            val title = binding.addTitle.text.toString()
            val content = binding.addContent.text.toString()

            viewModel.onEvent(EditNoteEvent.EnterTitle(title))
            viewModel.onEvent(EditNoteEvent.EnterContent(content))

            viewModel.onEvent(EditNoteEvent.SaveNote)
            findNavController().navigate(R.id.action_EditNoteFragment_to_NotesFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        if (args.currentNoteId == -1) return
        menuInflater.inflate(R.menu.edit_note_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_delete -> {
                viewModel.onEvent(EditNoteEvent.DeleteNote)
                findNavController().navigate(R.id.action_EditNoteFragment_to_NotesFragment)
                true
            }
            else -> false
        }
    }
}