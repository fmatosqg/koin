package org.koin.sample.androidx.workmanager

import kotlinx.coroutines.channels.Channel

/**
 * @author : Fabio de Matos
 * @since : 16/02/2020
 **/
class DummyService {

    private val channel = Channel<Int>(Channel.RENDEZVOUS)
    suspend fun getAnswer(): Int {
        return channel.receive()
    }

    suspend fun setAnswer(answer: Int) {
        channel.send(answer)
    }
}