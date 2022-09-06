import kotlin.experimental.and

fun main() {
    val data = byteArrayOf(
        0x13,
        'w'.b,
        'o'.b,
        'r'.b,
        0x22,
        'h'.b,
        'a'.b,
        'h'.b,
        'a'.b,
        0x12,
        'l'.b,
        'd'.b
    )
    val result = mutableListOf<Byte>()

    var skip = 0
    for ((i, code) in data.withIndex()) {
        if (skip > 0) {
            skip--
            continue
        }
        val (size, count) = arrayOf((code / 16), (code and 0x0F).toInt())
        skip = size * count
        if (size!= 1) {
            continue
        }
        result.addAll(data[i+1..i+count])
    }

    val resultString = String(result.toByteArray())
    check(resultString == "world") {
        "`$resultString` != world !"
    }
    println(resultString)
}

private val Char.b: Byte get() = this.code.toByte()

private operator fun ByteArray.get(range : IntRange) : Iterable<Byte> {
    return object: Iterable<Byte> {
        override fun iterator(): Iterator<Byte> = object:Iterator<Byte>{
            var current = range.first-1
            override fun hasNext(): Boolean = current < range.endInclusive
            override fun next(): Byte {
                current++
                return  this@get[current]
            }
        }
    }
}
