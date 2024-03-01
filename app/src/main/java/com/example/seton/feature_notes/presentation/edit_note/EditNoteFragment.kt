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
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
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
    private var pickerBitmap: Bitmap? = null

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
        viewModel.noteState.observeWithLifecycle(this) {
            val isContentEmpty = TextUtils.isEmpty(binding.addContent.text)
            val isTitleEmpty = TextUtils.isEmpty(binding.addTitle.text)

            if (it.imageFileName != null) {
                val bitmap = viewModel.getBitmapFromDevice(
                    context = requireContext(),
                    fileName = it.imageFileName
                )
                Log.d(TAG, "bitmap: $bitmap")
                binding.noteImage.setImageBitmap(bitmap)
            }
            if (isTitleEmpty) {
                binding.addTitle.setText(it.title)
            }
            if (isContentEmpty) {
                binding.addContent.setText(it.content)
            }
        }
        binding.apply {
            addTitle.doAfterTextChanged {
                viewModel.enterTitle(it.toString())
            }
            addContent.doAfterTextChanged {
                viewModel.enterContent(it.toString())
            }
            addContent.movementMethod = ScrollingMovementMethod()
            btnSave.setOnClickListener { saveNote() }
            bottomBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_add_image -> {
                        pickerMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        true
                    }

                    R.id.action_add_reminder -> {
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun saveNote() {
        val isTitleEmpty = TextUtils.isEmpty(binding.addTitle.text)
        val isContentEmpty = TextUtils.isEmpty(binding.addContent.text)

        if (isTitleEmpty || isContentEmpty) {
            Snackbar.make(
                binding.root,
                R.string.must_fill_all_fields,
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }
        saveImageToDeviceAndDatabase()
        viewModel.saveNote()
        Snackbar.make(binding.root, R.string.note_saved, Snackbar.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_EditNoteFragment_to_NotesFragment)
    }

    private fun saveImageToDeviceAndDatabase() {
        val fileName = "bitmapFrom${binding.addTitle.text.toString()}"
        viewModel.saveBitmapToDevice(
            context = requireContext(),
            bitmap = pickerBitmap,
            fileName = fileName
        )
        viewModel.insertImage(fileName)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                saveNote()
                true
            }

            R.id.action_delete -> {
                showAlertDialog(
                    context = requireContext(),
                    message = R.string.ask_delete_note,
                    title = R.string.confirm_action
                ) {
                    viewModel.deleteNote()
                    findNavController().navigate(R.id.action_EditNoteFragment_to_NotesFragment)
                }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickerMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                pickerBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                binding.noteImage.setImageBitmap(pickerBitmap)
            }
        }
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