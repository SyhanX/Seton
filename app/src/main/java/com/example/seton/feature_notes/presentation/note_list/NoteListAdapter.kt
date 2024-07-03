package com.example.seton.feature_notes.presentation.note_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.seton.databinding.NoteCardBinding
import com.example.seton.feature_notes.presentation.note_list.state.NoteCardState

private const val TAG = "ListAdapter"

class NoteListAdapter :
    ListAdapter<NoteCardState, NoteListAdapter.NoteStateViewHolder>(NoteListDiffCallback) {

    inner class NoteStateViewHolder(val binding: NoteCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NoteCardState) {
            binding.apply {
                cardTitle.text = item.title
                cardContent.text = item.content
                noteCard.isChecked = item.isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteStateViewHolder {
        val view = NoteCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteStateViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteStateViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)

        holder.binding.noteCard.setOnClickListener {
            item.onClick(item.id) {
//                val direction =
//                    NoteListFragmentDirections.actionNotesFragmentToEditNoteFragment(item.id)
//                holder.itemView.findNavController().navigate(direction)
            }
        }

        holder.binding.noteCard.setOnLongClickListener {
            item.onLongClick(item.id)
            true
        }
    }

    override fun getItemViewType(position: Int): Int = position

}

private object NoteListDiffCallback : DiffUtil.ItemCallback<NoteCardState>() {
    override fun areItemsTheSame(oldItem: NoteCardState, newItem: NoteCardState): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: NoteCardState, newItem: NoteCardState): Boolean =
        oldItem.hashCode() == newItem.hashCode() && oldItem == newItem

    override fun getChangePayload(oldItem: NoteCardState, newItem: NoteCardState): Any? =
        if (oldItem.isChecked != newItem.isChecked) true else null
}