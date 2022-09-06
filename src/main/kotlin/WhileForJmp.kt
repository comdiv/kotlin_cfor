import kotlin.experimental.and

fun main() {
    val data = ubyteArrayOf(
        0x8Eu, //goto 14
        0x13u,
        'w'.b,
        'o'.b,
        'r'.b,
        0x22u,
        'h'.b,
        'a'.b,
        'h'.b,
        'a'.b,
        0x12u,
        'l'.b,
        'd'.b,
        0xFFu, //exit
        0x16u,
        'h'.b,
        'e'.b,
        'l'.b,
        'l'.b,
        'o'.b,
        ' '.b,
        0x81u,
    )
    val result = mutableListOf<UByte>()

    var i = 0
    while (i < data.size) {
        val code = data[i]
        val (cmd, arg) = arrayOf((code / 16u).toInt(), (code and 0x0Fu).toInt())
        if (cmd == 0x0F){
            break
        }
        if (cmd == 0x08) {
            i = arg - 1 //will be i++
            i++
            continue
        }
        i += cmd * arg
        if (cmd!= 1) {
            i++
            continue
        }
        result.addAll(data[i-arg+1 .. i])
        i++
    }

    val resultString = String(result.toUByteArray().toByteArray())
    check(resultString == "hello world") {
        "`$resultString` != hello world !"
    }
    println(resultString)
}

private val Char.b: UByte get() = this.code.toByte().toUByte()

private operator fun UByteArray.get(range : IntRange) : Iterable<UByte> {
    return object: Iterable<UByte> {
        override fun iterator(): Iterator<UByte> = object:Iterator<UByte>{
            var current = range.first-1
            override fun hasNext(): Boolean = current < range.endInclusive
            override fun next(): UByte {
                current++
                return  this@get[current]
            }
        }
    }
}
