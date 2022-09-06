import kotlin.experimental.and

fun main() {
    val data = byteArrayOf(
        0x13,
        'w'.toByte(),
        'o'.toByte(),
        'r'.toByte(),
        0x22,
        'h'.toByte(),
        'a'.toByte(),
        'h'.toByte(),
        'a'.toByte(),
        0x12,
        'l'.toByte(),
        'd'.toByte()
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
        if (size != 1) {
            continue
        }
        result.addAll(data.slice(i + 1..i + count))
    }

    val resultString = String(result.toByteArray())
    check(resultString == "world") {
        "`$resultString` != world !"
    }
    println(resultString)
}



