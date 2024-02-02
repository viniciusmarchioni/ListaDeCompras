package com.viniciusmarchioni.compras

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    suspend fun assincrona(time:Long){

        println("Inicio da assincrona")

        delay(time)

        println("Fim da assincrona")
    }

    @Test
    fun main() = runBlocking {
        println("Inicio da principal")

        launch {
            assincrona(10000L)
        }

        println("Fim da principal")
    }


}