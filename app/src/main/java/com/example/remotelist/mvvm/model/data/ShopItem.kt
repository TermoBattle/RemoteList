package com.example.remotelist.mvvm.model.data

class ShopCounter(val maxCount: Int, count: Int = 0) {
    val count: Int = if (count <= maxCount) count else maxCount
    constructor() : this(maxCount = 0)

    private fun copy(count: Int = this.count, maxCount: Int = this.maxCount): ShopCounter =
        ShopCounter(
            count = count,
            maxCount = maxCount
        )

    fun increased(): ShopCounter = this.copy(count = this.count + 1)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShopCounter

        if (count != other.count) return false
        if (maxCount != other.maxCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = maxCount.hashCode()
        result = 31 * result + count.hashCode()
        return result
    }

    val isFull: Boolean
        get() = count == maxCount
    val isNotFull: Boolean
        get() = !isFull
}

data class ShopItem(
    val shopCounter: ShopCounter,
    val name: String,
    val description: String
) {
    constructor() : this(
        shopCounter = ShopCounter(),
        name = "",
        description = ""
    )

    constructor(maxCount: Int, name: String, description: String) : this(
        ShopCounter(maxCount),
        name,
        description
    )

}

val ShopItem.increased: ShopItem
    inline get() = copy(shopCounter = shopCounter.increased())