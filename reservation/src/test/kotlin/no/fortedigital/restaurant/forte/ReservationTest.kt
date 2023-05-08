package no.fortedigital.restaurant.forte

import kotlinx.datetime.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours

internal class ReservationTest {
    @Test
    fun `should throw on max capacity on reservations`() {
        val maxCapacity = assertThrows<IllegalArgumentException> {
            val startTime = Clock.System.todayIn(TimeZone.currentSystemDefault()).atTime(hour = 19, minute = 0)
            val endTime = Clock.System.todayIn(TimeZone.currentSystemDefault()).atTime(hour = 20, minute = 0)
            val guests = TotalGuests(13)
            Reservation(startTime = startTime, endTime = endTime, totalGuests = guests)
        }
        assertEquals("Reservation of 13 is not within boundary of 1 until 12", maxCapacity.message)
    }

    @Test
    fun `should throw on dates without of bounds`() {
        val datesNotWithinBounds = assertThrows<IllegalArgumentException> {
            val startTime = Clock.System.todayIn(TimeZone.currentSystemDefault()).atTime(hour = 19, minute = 0)
            val endTime = Clock.System.todayIn(TimeZone.currentSystemDefault()).atTime(hour = 20, minute = 0)
            val guests = TotalGuests(1)
            Reservation(startTime = endTime, endTime = startTime, totalGuests = guests)
        }

        assertEquals("Reservation dates are illegal", datesNotWithinBounds.message)
    }

    @Test
    fun `should throw on too low capacity reservation`() {
        val tooLowCapacity = assertThrows<IllegalArgumentException> {
            val startTime = Clock.System.todayIn(TimeZone.currentSystemDefault()).atTime(hour = 19, minute = 0)
            val endTime = Clock.System.todayIn(TimeZone.currentSystemDefault()).atTime(hour = 20, minute = 0)
            val guests = TotalGuests(0)
            Reservation(startTime = startTime, endTime = endTime, totalGuests = guests)
        }
        assertEquals("Reservation of 0 is not within boundary of 1 until 12", tooLowCapacity.message)
    }

    @Test
    fun `should create a reservation successfully`() {
        val startTime = Clock.System.todayIn(TimeZone.currentSystemDefault()).atTime(hour = 19, minute = 0)
        val endTime = Clock.System.todayIn(TimeZone.currentSystemDefault()).atTime(hour = 20, minute = 30)
        val guests = TotalGuests(2)
        val reservation = Reservation(startTime = startTime, endTime = endTime, totalGuests = guests)
        assertEquals(1.5.hours, reservation.duration)
    }
}