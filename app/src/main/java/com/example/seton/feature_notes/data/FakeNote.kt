package com.example.seton.feature_notes.data

import com.example.seton.common.presentation.state.ContainerColor
import com.example.seton.feature_notes.domain.model.Note
import java.util.Calendar

val fakeNote = Note(
    title = "Lorem ipsum",
    content = "dolor sit amet",
    color = ContainerColor.Default,
    creationDate = Calendar.getInstance().time,
    modificationDate = null
)

