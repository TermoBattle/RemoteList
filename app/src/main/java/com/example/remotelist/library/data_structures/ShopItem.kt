package com.example.remotelist.library.data_structures

class ShopCounter constructor(val maxCount: Int, count: Int) {
    val count: Int =
        if (count >= 0)
            count
        else
            throw IllegalArgumentException("count should be only > 0 and <= maxCount")

    constructor() : this(maxCount = 0)

    constructor(maxCount: Int) : this(maxCount = maxCount, count = 0)

    override fun toString(): String = "$count/$maxCount"

    val isFull: Boolean
        inline get() = count == maxCount

    val isNotFull: Boolean
        inline get() = !isFull

    val isBinary: Boolean
        inline get() = maxCount == 1

    val isNotBinary: Boolean
        inline get() = !isBinary
}

class ShopItem private constructor(
    val counter: ShopCounter,
    val name: String,
    val description: String
) {
    constructor() : this(
        counter = ShopCounter(),
        name = "",
        description = ""
    )

    constructor(maxCount: Int, name: String, description: String) : this(
        counter = ShopCounter(maxCount),
        name = name,
        description = description
    )

    val oneMoreBought: ShopItem
    get() = ShopItem(
        ShopCounter(
            maxCount = counter.maxCount,
            count = counter.run {
                if (this.count + 1 < this.maxCount)
                    count + 1
                else
                    count
            }
        ),
        name,
        description
    )
}
