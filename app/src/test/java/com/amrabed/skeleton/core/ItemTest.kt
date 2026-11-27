package com.amrabed.skeleton.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ItemTest {

    @Test
    fun defaultItemGeneratesUniqueIdAndNullFields() {
        val first = Item()
        val second = Item()

        assertNotNull(first.id)
        assertNotNull(second.id)
        assertNotEquals(first.id, second.id)
        assertNull(first.title)
        assertNull(first.summary)
        assertNull(first.category)
        assertNull(first.image)
    }

    @Test
    fun itemStoresProvidedFields() {
        val item = Item(
            id = "item-1",
            title = "Relaxing",
            summary = "Hammock on the beach",
            category = "Travel",
            image = "https://example.com/image.jpg"
        )

        assertEquals("item-1", item.id)
        assertEquals("Relaxing", item.title)
        assertEquals("Hammock on the beach", item.summary)
        assertEquals("Travel", item.category)
        assertEquals("https://example.com/image.jpg", item.image)
    }
}
