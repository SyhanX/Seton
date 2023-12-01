package com.example.seton.presentation.edit_note

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.seton.R
import com.example.seton.data.datasource.observeWithLifecycle
import com.example.seton.databinding.FragmentEditNoteBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "editnotefragment"

@AndroidEntryPoint
class EditNoteFragment : Fragment() {
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditNoteViewModel by viewModels()
    

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

        onTitleChange()
        onContentChange()

        binding.btnSave.setOnClickListener {
            viewModel.onEvent(EditNoteEvent.SaveNote)
            findNavController().navigate(R.id.action_EditNoteFragment_to_NotesFragment)
        }

    }

    private fun onTitleChange() {
        binding.addTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEvent(EditNoteEvent.EnterTitle(s.toString()))
            }
        })
    }

    private fun onContentChange() {
        binding.addContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEvent(EditNoteEvent.EnterContent(s.toString()))
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}