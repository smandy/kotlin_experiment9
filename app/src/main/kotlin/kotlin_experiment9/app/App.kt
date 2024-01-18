package kotlin_experiment9.app

import kotlin_experiment9.utilities.StringUtils
import org.apache.commons.text.WordUtils

fun twice(): String {
    return "Foo"
}

data class Person( val first : String, val last : String)

fun main() {
    val tokens = StringUtils.split(MessageUtils.getMessage())
    val result = StringUtils.join(tokens)
    println(WordUtils.capitalize(result))
    println(twice())

    val xs = Person("andy","smith")

    val (first, last) = xs

    print("Name is $first - $last")
}
