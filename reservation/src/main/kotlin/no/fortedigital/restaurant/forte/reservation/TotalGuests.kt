package no.fortedigital.restaurant.forte.reservation

const val MAX_TABLE_CAPACITY = 12

@JvmInline
value class TotalGuests(val amount: Int) {
    init {
        require(amount in 1..MAX_TABLE_CAPACITY) {
            "Reservation of $amount is not within boundary of 1 until $MAX_TABLE_CAPACITY"
        }
    }
}
