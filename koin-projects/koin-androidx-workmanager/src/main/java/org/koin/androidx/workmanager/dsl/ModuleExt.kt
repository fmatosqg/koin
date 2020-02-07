package org.koin.androidx.workmanager.dsl

import org.koin.androidx.workmanager.AbstractWorkerFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.ScopeDSL
import org.koin.dsl.module

/**
 * @author : Fabio de Matos
 * @since : 16/02/2020
 **/
fun workerModule(scopeSet: ScopeDSL.() -> Unit): Module {

    return module {

        scope(named<AbstractWorkerFactory>(), scopeSet)
    }
}
