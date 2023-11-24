package com.example.seton.presentation.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.seton.databinding.NoteCardBinding
import com.example.seton.domain.model.Note

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding: NoteCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {

        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Note>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = NoteCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {
            noteTitle.text = currentItem.title
            noteContent.text = currentItem.content
        }
    }
}