@OptIn(ExperimentalUnsignedTypes::class)
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

    for ((i, code, mut) in data.navigator()) {
        val cmd = code shr 4
        val arg = code and 0xFL
        if (cmd == 0x0F) {
            break
        }
        if (cmd == 0x08) {
            mut.i = arg - 1
            continue
        }
        mut.i += cmd * arg
        if (cmd != 1) {
            continue
        }
        result.addAll(data[i + 1..i + arg])
    }

    val resultString = String(result)
    check(resultString == "hello world") {
        "`$resultString` != hello world !"
    }
    println(resultString)
}

private val Char.b: UByte get() = this.code.toByte().toUByte()
private operator fun String.Companion.invoke(ubytes: Collection<UByte>): String {
    return String(ubytes.toUByteArray().toByteArray())
}

private infix fun UByte.shr(bits: Int): Int {
    require(bits in 0..8) {
        "byte is only 8 bit"
    }
    return this.toInt() shr bits
}

private infix fun UByte.shl(bits: Int): Int {
    require(bits in 0..8) {
        "byte is only 8 bit"
    }
    return this.toInt() shl bits
}

private infix fun UByte.and(int:Long): Int = this.toLong().and(int).toInt()



private operator fun UByteArray.get(range: IntRange): Iterable<UByte> {
    return object : Iterable<UByte> {
        override fun iterator(): Iterator<UByte> = object : Iterator<UByte> {
            var current = range.first - 1
            override fun hasNext(): Boolean = current < range.endInclusive
            override fun next(): UByte {
                current++
                return this@get[current]
            }
        }
    }
}

private fun UByteArray.navigator(): UByteArrayNavigator = UByteArrayNavigator(this)

private class UByteArrayNavigator(private val data: UByteArray) : Iterable<UByteArrayNavigator.Nav> {
    inner class Nav : Iterator<Nav> {
        var i = -1
        var c: UByte = 0x00u
            private set

        override fun hasNext(): Boolean {
            return i < data.size - 1
        }

        override fun next(): Nav {
            i++
            c = data[i]
            return this
        }

        inner class Mutator {
            var i
                get() = this@Nav.i
                set(value: Int) {
                    this@Nav.i = value
                }
        }

        private val mutator = Mutator()
        operator fun component1() = i
        operator fun component2() = c
        operator fun component3() = mutator
    }

    override fun iterator(): Iterator<Nav> {
        return Nav()
    }
}



