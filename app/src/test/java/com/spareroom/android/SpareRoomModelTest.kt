package com.spareroom.android

import com.spareroom.android.model.SpareRoomModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Test
import org.junit.runner.RunWith

class SpareRoomModelTest {

    // Model Test Case
    @Test
    fun `SpareRoomModel Equal and Not Equal`() {
        // Arrange
        val spareRoomModel1 = SpareRoomModel("https://images.unsplash.com/photo-1582642017153-e36e8796b3f8",
            "Manhattan 1","+44 20 8759 9036","The Penny Farthing","2021-06-18T17:00:00Z","2021-06-18T22:00:00Z","free")
        val spareRoomModel2 = SpareRoomModel("https://images.unsplash.com/photo-1582642017153-e36e8796b3f8",
            "Manhattan 2","+44 20 8759 9036","The Penny Farthing","2021-06-18T17:00:00Z","2021-06-18T22:00:00Z","free")
        val spareRoomModel3 = SpareRoomModel("https://images.unsplash.com/photo-1582642017153-e36e8796b3f8",
            "Manhattan 1","+44 20 8759 9036","The Penny Farthing","2021-06-18T17:00:00Z","2021-06-18T22:00:00Z","free")

        // Assert
        assertEquals(spareRoomModel1, spareRoomModel3)
        assertNotSame(spareRoomModel1,spareRoomModel2)
        print("Test Pass for SpareRoomModel Equal and Not Equal")
    }

}

