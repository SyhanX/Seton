package com.example.seton.feature_notes.domain.model

import com.example.seton.common.presentation.state.ContainerColor
import java.util.Date

val fakeTitles = listOf(
    "Lorem ipsum",
    "Duis sollicitudin neque",
    "Donec id est ornare",
    "Vestibulum id leo aliquam",
    "Pellentesque at tortor nec lectus venenatis",
    "Curabitur nibh lacus",
    "Fusce id leo lacus",
    "Nunc non commodo lacus",
    "Nulla facilisi",
    "Nam hendrerit est feugiat"
).shuffled()

val fakeContents = listOf(
    "consectetur adipiscing elit",
    "egestas massa luctus hendrerit et quis est.",
    "faucibus eros et, imperdiet justo",
    "porta mi vel, sodales odio",
    "convallis ut at leo",
    "lacinia eleifend lectus vel, egestas congue erat",
    "Phasellus posuere felis ligula, et feugiat nisl semper ac. Suspendisse et convallis ante. ",
    "Nam in iaculis mi, sit amet tincidunt sapien",
    "Nulla et erat vel est feugiat tempus sed eu elit",
    "Quisque vehicula est in consequat lobortis. "
).shuffled()

val fakeCreationDates = mutableListOf<Date>().also { list ->
    repeat(10) {
        list.add(
            Date(
                (1577836800000L..1732784127899L).random()
            )
        )
    }
}

val fakeModificationDates = mutableListOf<Date>().also { list ->
    repeat(10) {
        list.add(
            Date(
                (1577836800000L..1732784127899L).random()
            )
        )
    }
}

val colors = ContainerColor.entries.toList()

val fakeNotes = mutableListOf<Note>().also { list ->
    repeat(10) {
        list.add(
            Note(
                title = fakeTitles.shuffled()[it],
                content = fakeContents.shuffled()[it],
                color = colors.shuffled()[it],
                creationDate = fakeCreationDates.shuffled()[it],
                modificationDate = fakeModificationDates.shuffled()[it]
            )
        )
    }
}