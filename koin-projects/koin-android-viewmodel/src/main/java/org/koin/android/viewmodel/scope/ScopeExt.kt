/*
 * Copyright 2017-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.koin.android.viewmodel.scope

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelStoreOwner
import org.koin.android.viewmodel.ViewModelParameter
import org.koin.android.viewmodel.createViewModelProvider
import org.koin.android.viewmodel.resolveInstance
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import kotlin.reflect.KClass

/**
 * Scope extensions to help for ViewModel
 *
 * @author Arnaud Giuliani
 */

inline fun <reified T : ViewModel> Scope.viewModel(
    owner: ViewModelStoreOwner,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { getViewModel<T>(owner, qualifier, parameters) }
}

inline fun <reified T : ViewModel> Scope.getViewModel(
    owner: ViewModelStoreOwner,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    return getViewModel(owner, T::class, qualifier, parameters)
}

fun <T : ViewModel> Scope.getViewModel(
    owner: ViewModelStoreOwner,
    clazz: KClass<T>,
    qualifier: Qualifier? = null,
    parameters: ParametersDefinition? = null
): T {
    return getViewModel(
        ViewModelParameter(
            clazz,
            qualifier,
            parameters,
            owner.viewModelStore
        )
    )
}

fun <T : ViewModel> Scope.getViewModel(viewModelParameters: ViewModelParameter<T>): T {
    val viewModelProvider = createViewModelProvider(viewModelParameters)
    return viewModelProvider.resolveInstance(viewModelParameters)
}