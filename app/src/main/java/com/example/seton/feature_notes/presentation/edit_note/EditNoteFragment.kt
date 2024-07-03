package com.example.seton.feature_notes.presentation.edit_note

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.example.seton.R
import com.example.seton.common.domain.util.observeWithLifecycle
import com.example.seton.common.domain.util.showAlertDialog
import com.example.seton.databinding.FragmentEditNoteBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "edit_note_fragment"

@AndroidEntryPoint
class EditNoteFragment : Fragment(), MenuProvider {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditNoteViewModel by viewModels()
    private val args by navArgs<EditNoteFragmentArgs>()

    private lateinit var pickerMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var selectedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickerMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                binding.noteImage.setImageBitmap(selectedBitmap)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        if (args.currentNoteId != -1) {
            viewModel.noteState.observeWithLifecycle(this) {
                restoreState(
                    title = it.title,
                    content = it.content,
                    imageFileName = it.imageFileName
                )
            }
        }

        manageViewBinding()
    }

    private fun restoreState(
        title: String,
        content: String,
        imageFileName: String? = null
    ) {
        binding.etTitle.setText(title)
        binding.etContent.setText(content)
        viewModel.getBitmapFromDevice(
            context = requireContext(),
            fileName = imageFileName
        )?.let { bitmap ->
            binding.noteImage.apply {
                visibility = View.VISIBLE
                setImageBitmap(bitmap)
            }
        }
    }

    private fun manageViewBinding() {
        binding.apply {
            etContent.movementMethod = ScrollingMovementMethod()
            btnSave.setOnClickListener {
                selectedBitmap?.let {
                    savePickedImageToDevice()
                }
                saveNote()
            }

            bottomBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_add_image -> {
                        pickerMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        true
                    }

                    R.id.action_add_reminder -> {
                        Snackbar.make(
                            binding.root,
                            "Work in progress",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun saveNote() {
        val isTitleEmpty = TextUtils.isEmpty(binding.etTitle.text)
        val isContentEmpty = TextUtils.isEmpty(binding.etContent.text)

        if (isTitleEmpty || isContentEmpty) {
            Snackbar.make(
                binding.root,
                R.string.must_fill_all_fields,
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }
        viewModel.noteState.observeWithLifecycle(this) {
            if (it.imageFileName == null) {
                viewModel.saveNote(
                    title = binding.etTitle.text.toString(),
                    content = binding.etContent.text.toString(),
                    imageFileName = selectedBitmap?.toString()
                )
            } else {
                viewModel.saveNote(
                    title = binding.etTitle.text.toString(),
                    content = binding.etContent.text.toString(),
                    imageFileName = it.imageFileName
                )
            }
        }
//        findNavController().navigate(R.id.action_EditNoteFragment_to_NotesFragment)
    }

    private fun savePickedImageToDevice() {
        viewModel.saveBitmapToDevice(
            context = requireContext(),
            bitmap = selectedBitmap,
            fileName = selectedBitmap.toString()
        )
    }

    private fun deleteNote() {
        showAlertDialog(
            context = requireContext(),
            message = R.string.ask_delete_note,
            title = R.string.confirm_action
        ) {
            viewModel.noteState.observeWithLifecycle(this) { state ->
                state.imageFileName?.let {
                    viewModel.deleteBitmapFromDevice(
                        context = requireContext(),
                        fileName = it
                    )
                }
            }
            viewModel.deleteNoteById(args.currentNoteId)
//            findNavController().navigate(R.id.action_EditNoteFragment_to_NotesFragment)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_delete -> {
                deleteNote()
                true
            }

            else -> false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = saveNote()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        if (args.currentNoteId == -1) return
        menuInflater.inflate(R.menu.edit_note_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}