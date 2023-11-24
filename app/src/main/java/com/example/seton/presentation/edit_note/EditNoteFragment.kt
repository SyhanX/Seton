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
import com.example.seton.databinding.FragmentEditNoteBinding
import dagger.hilt.android.AndroidEntryPoint

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteTitle = viewModel.noteTitle.value
        val noteContent = viewModel.noteContent.value

        binding.btnSave.setOnClickListener {
            viewModel.onEvent(EditNoteEvent.SaveNote)
            findNavController().navigate(R.id.action_EditNoteFragment_to_NotesFragment)
        }

        binding.addTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEvent(EditNoteEvent.EnterTitle(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.addContent.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEvent(EditNoteEvent.EnterContent(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        }

        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}