package org.koin.androidx.workmanager.dsl

import android.util.Log
import androidx.work.ListenableWorker
import kotlinx.coroutines.*

import org.koin.androidx.workmanager.KoinWorkerFactory
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Definition
import org.koin.core.definition.SuspendDefinition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.dsl.ScopeDSL
import org.koin.dsl.module

/**
 * @author : Fabio de Matos
 * @since : 16/02/2020
 **/
inline fun <reified T : ListenableWorker> ScopeDSL.worker(crossinline createWorker: Scope.() -> T) {

    KoinWorkerFactory.getInstance().addWorker(T::class.java) { scope ->
        scope.createWorker()
    }
}

val m = module {

    factory { 1 }

    factorySlow { slowBuild() }
}

suspend
fun slowBuild(): Payload {

    Log.i("Slow", "Start")
    delay(3_000)
    Log.i("Slow", "End")
    return Payload(1)
}

data class Payload(val i: Int)

inline fun <reified T> Module.factorySlow(
    qualifier: Qualifier? = null,
    override: Boolean = false,
    noinline definition: SuspendDefinition<T>
): BeanDefinition<T> {


    val fastDefinition: Definition<T> = { par ->

        CoroutineScope(Dispatchers.IO)
            .async {
                definition(par)
            }
            .let {
                runBlocking {
                    it.await()
                }
            }


    }

    return single(
        qualifier,
        override = override,
        createdAtStart = false,
        coroutine = true,
        definition = fastDefinition,
        definition2= definition
    )
}
