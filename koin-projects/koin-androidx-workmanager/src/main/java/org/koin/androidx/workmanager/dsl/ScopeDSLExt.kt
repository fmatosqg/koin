package org.koin.androidx.workmanager.dsl

import androidx.work.ListenableWorker
import org.koin.androidx.workmanager.KoinWorkerFactory
import org.koin.core.scope.Scope
import org.koin.dsl.ScopeDSL

/**
 * @author : Fabio de Matos
 * @since : 16/02/2020
 **/
inline fun <reified T : ListenableWorker> ScopeDSL.worker(crossinline createWorker: Scope.() -> T) {

    KoinWorkerFactory.getInstance().addWorker(T::class.java) { scope ->
        scope.createWorker()
    }
}
