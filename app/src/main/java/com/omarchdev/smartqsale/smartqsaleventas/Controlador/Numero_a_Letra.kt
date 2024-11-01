package com.omarchdev.smartqsale.smartqsaleventas.Controlador
import java.util.regex.Pattern

class Numero_a_Letra {

    private val UNIDADES = arrayOf("", "un ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve ")
    private val DECENAS = arrayOf("diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ", "diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ", "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa ")
    private val CENTENAS = arrayOf("", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ", "seiscientos ", "setecientos ", "ochocientos ", "novecientos ")

    fun Convertir(numero: String, mayusculas: Boolean): String? {
        var numero = numero
        var literal: String? = ""
        val parte_decimal: String
        //si el numero utiliza (.) en lugar de (,) -> se reemplaza
        numero = numero.replace(".", ",")
        //si el numero no tiene parte decimal, se le agrega ,00
        if (numero.indexOf(",") == -1) {
            numero = "$numero,00"
        }
        //se valida formato de entrada -> 0,00 y 999 999 999,00
        if (Pattern.matches("\\d{1,9},\\d{1,2}", numero)) {
            //se divide el numero 0000000,00 -> entero y decimal
            val Num = numero.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            //de da formato al numero decimal
            parte_decimal = Num[1] + "/100 "
            //se convierte el numero a literal
            if (Integer.parseInt(Num[0]) == 0) {//si el valor es cero
                literal = "cero "
            } else if (Integer.parseInt(Num[0]) > 999999) {//si es millon
                literal = getMillones(Num[0])
            } else if (Integer.parseInt(Num[0]) > 999) {//si es miles
                literal = getMiles(Num[0])
            } else if (Integer.parseInt(Num[0]) > 99) {//si es centena
                literal = getCentenas(Num[0])
            } else if (Integer.parseInt(Num[0]) > 9) {//si es decena
                literal = getDecenas(Num[0])
            } else {//sino unidades -> 9
                literal = getUnidades(Num[0])
            }
            //devuelve el resultado en mayusculas o minusculas
            return if (mayusculas) {
                (literal +" con " +parte_decimal).toUpperCase()
            } else {
                literal + parte_decimal
            }
        } else {//error, no se puede convertir
            return ""
        }
    }

    /* funciones para convertir los numeros a literales */

    private fun getUnidades(numero: String): String {// 1 - 9
        //si tuviera algun 0 antes se lo quita -> 09 = 9 o 009=9
        val num = numero.substring(numero.length - 1)
        return UNIDADES[Integer.parseInt(num)]
    }

    private fun getDecenas(num: String): String {// 99
        val n = Integer.parseInt(num)
        if (n < 10) {//para casos como -> 01 - 09
            return getUnidades(num)
        } else if (n > 19) {//para 20...99
            val u = getUnidades(num)
            return if (u == "") { //para 20,30,40,50,60,70,80,90
                DECENAS[Integer.parseInt(num.substring(0, 1)) + 8]
            } else {
                DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u
            }
        } else {//numeros entre 11 y 19
            return DECENAS[n - 10]
        }
    }

    private fun getCentenas(num: String): String {// 999 o 099
        return if (Integer.parseInt(num) > 99) {//es centena
            if (Integer.parseInt(num) == 100) {//caso especial
                " cien "
            } else {
                CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1))
            }
        } else {//por Ej. 099
            //se quita el 0 antes de convertir a decenas
            getDecenas(Integer.parseInt(num).toString() + "")
        }
    }

    private fun getMiles(numero: String): String {// 999 999
        //obtiene las centenas
        val c = numero.substring(numero.length - 3)
        //obtiene los miles
        val m = numero.substring(0, numero.length - 3)
        var n = ""
        //se comprueba que miles tenga valor entero
        if (Integer.parseInt(m) > 0) {
            n = getCentenas(m)
            return n + "mil " + getCentenas(c)
        } else {
            return "" + getCentenas(c)
        }

    }

    private fun getMillones(numero: String): String { //000 000 000
        //se obtiene los miles
        val miles = numero.substring(numero.length - 6)
        //se obtiene los millones
        val millon = numero.substring(0, numero.length - 6)
        var n = ""
        if (millon.length > 1) {
            n = getCentenas(millon) + "millones "
        } else {
            n = getUnidades(millon) + "millon "
        }
        return n + getMiles(miles)
    }
}