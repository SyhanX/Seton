package com.example.seton.feature_notes.presentation.edit_note

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.seton.R
import com.example.seton.common.domain.util.observeWithLifecycle
import com.example.seton.common.domain.util.showAlertDialog
import com.example.seton.databinding.FragmentEditNoteBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "edit_note_fragment"

@AndroidEntryPoint
class EditNoteFragment : Fragment(), MenuProvider {
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditNoteViewModel by viewModels()
    private val args by navArgs<EditNoteFragmentArgs>()

    private lateinit var pickerMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var pickedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickerMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                pickedBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                binding.noteImage.setImageBitmap(pickedBitmap)
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

        getVMState()
        manageViewBinding()

    }

    private fun manageViewBinding() {
        binding.etTitle.doOnTextChanged { text, _, _, _ ->
            viewModel.saveTitleToState(text.toString())
        }
        binding.etContent.apply {
            doOnTextChanged { text, _, _, _ ->
                viewModel.saveContentToState(text.toString())
            }
            movementMethod = ScrollingMovementMethod()
        }
        binding.btnSave.setOnClickListener {
            if (pickedBitmap != null) {
                savePickedImage()
                saveNote()
            } else {
                saveNote()
            }
        }
        binding.bottomBar.setOnMenuItemClickListener {
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

    //Call restoreState() only when state isn't empty and cancel the coroutine
    private fun getVMState() {
        if (args.currentNoteId != -1) {
            lifecycleScope.launch {
                viewModel.noteState.collectLatest { state ->
                    if (state.title.isNotBlank() && state.content.isNotBlank()) {
                        restoreState(
                            title = state.title,
                            content = state.content,
                            imageFileName = state.imageFileName.toString()
                        )
                        cancel()
                    }
                }
            }
        }
    }

    private fun restoreState(
        title: String,
        content: String,
        imageFileName: String
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
        Log.d(TAG, "State restored")
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
        viewModel.saveNote()
        Snackbar.make(
            binding.root,
            R.string.note_saved,
            Snackbar.LENGTH_SHORT
        ).show()
        findNavController().navigate(R.id.action_EditNoteFragment_to_NotesFragment)
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
            viewModel.deleteNote()
            findNavController().navigate(R.id.action_EditNoteFragment_to_NotesFragment)
        }
    }

    private fun savePickedImage() {
        val fileName = pickedBitmap.toString()
        viewModel.saveBitmapToDevice(
            context = requireContext(),
            bitmap = pickedBitmap,
            fileName = fileName
        )
        viewModel.saveImageToState(fileName)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                saveNote()
                true
            }

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