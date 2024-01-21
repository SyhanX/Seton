package com.example.seton.feature_notes.presentation.note_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.seton.databinding.NoteCardBinding
import com.example.seton.feature_notes.presentation.note_list.state.NoteItemState

class NoteListAdapter :
    ListAdapter<NoteItemState, NoteListAdapter.NoteStateViewHolder>(NoteListDiffCallback) {

    inner class NoteStateViewHolder(val binding: NoteCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NoteItemState) {
            binding.apply {
                noteTitle.text = item.title
                noteContent.text = item.content
                noteCard.isChecked = item.isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteStateViewHolder {
        val view = NoteCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteStateViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteStateViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.binding.noteCard.setOnClickListener {
            val action =
                NoteListFragmentDirections.actionNotesFragmentToEditNoteFragment(item.id)
            holder.itemView.findNavController().navigate(action)
        }

        holder.binding.noteCard.setOnLongClickListener {
            item.onLongClick(item.id)
            true
        }

    }

    override fun getItemViewType(position: Int): Int = position
}

private object NoteListDiffCallback : DiffUtil.ItemCallback<NoteItemState>() {
    override fun areItemsTheSame(oldItem: NoteItemState, newItem: NoteItemState): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: NoteItemState, newItem: NoteItemState): Boolean =
        oldItem.hashCode() == newItem.hashCode() && oldItem == newItem

    override fun getChangePayload(oldItem: NoteItemState, newItem: NoteItemState): Any? {
        return if (oldItem.isChecked != newItem.isChecked) true else null
    }
}