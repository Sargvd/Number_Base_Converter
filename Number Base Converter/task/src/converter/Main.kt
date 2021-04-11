package converter
import java.math.BigInteger
import java.math.BigDecimal

fun toBaseInt(num: String, fBase: Int, tBase: Int): String {
    val alph=("0123456789abcdefghijklmnopqrstuvwxyz")
    var run = BigInteger.ZERO
    val tb = BigInteger("$tBase")
    val fb = BigInteger("$fBase")

    var i = BigInteger("1")
    //Convert from any base to dec
    for (c in num.reversed()) {
        run += BigInteger("${alph.indexOf(c)}") *i
        i *= fb
    }

    //Convert from dec to any base
    var result = ""
    while (run/tb > BigInteger.ZERO) {
        result += alph[(run%tb).toInt()]
        run /= tb
    }
    result += alph[(run%tb).toInt()]
    return result.reversed()
}

fun toBaseDec(num: String, fBase: Int, tBase: Int): String {
    //Index of char in alph = decimal value, base up to 10 + 24
    val alph=("0123456789abcdefghijklmnopqrstuvwxyz")
    //Requested precision
    val precision = 5

    //Have to be casted to BigDec for arithmetic operations
    val tb = BigDecimal("$tBase")
    val fb = BigDecimal("$fBase")

    //Calculating fractional part in dec
    var decFrac = BigDecimal("0").setScale(precision)
    for (i in 0 until num.length) {
        val decFromAlph = BigDecimal("${alph.indexOf(num[i])}").setScale(precision)
        decFrac += decFromAlph/fb.pow(i+1)
    }

    //Decimal fractional part to target
    var run = ""
    for (i in 0 until precision) {
        //Hey, it's finite!
        if (decFrac == BigDecimal.ZERO) break
        decFrac *= tb
        val rem = decFrac.remainder(BigDecimal.ONE)
        run += (alph[(decFrac-rem).toInt()])
        decFrac = rem
    }

    return(run)
}

fun toBase(num: String, fBase: Int, tBase: Int): String {
    //No fractional part
    if ('.' !in num) return toBaseInt(num,fBase,tBase)

    //Integer + fractional parts
    val intPart = toBaseInt(num.split('.')[0],fBase,tBase)
    val decPart = toBaseDec(num.split('.')[1],fBase,tBase)

    return ("$intPart.$decPart")
}

fun convert(bases: List<String>) {
    //Bases selected loop
    while(true) {
        println("Enter number in base ${bases[0]} to convert to base ${bases[1]} (To go back type /back)")
        when ( val str = readLine()!!) {
            "/back" -> break
            else -> {
                val result = toBase(str, bases[0].toInt(),bases[1].toInt())
                println("Conversion result: $result")
            }
        }
    }
  }

fun main() {
    //Main menu loop
    while(true) {
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit")
        when(val str = readLine()!!) {
            "/exit" -> break
            else -> convert(str.split(" "))
        }
    }


}