package com.tavro.outstanding.navigation

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.definition.KoinDefinition
import org.koin.core.definition.indexKey
import org.koin.core.parameter.ParametersHolder
import org.koin.core.parameter.emptyParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.ext.getFullName
import kotlin.reflect.KClass

fun KClass<out BaseConfig>.qualifier() = ConfigQualifier(this)

data class ConfigQualifier(val type: KClass<out BaseConfig>) : Qualifier {
    override val value: QualifierValue = type.getFullName()

    override fun toString(): String {
        return "config-q:'$value'"
    }
}

fun KoinComponent.getComponent(
    config: BaseConfig,
    componentContext: ComponentContext,
    extraParameters: (() -> ParametersHolder) = { emptyParametersHolder() }
) = get<Component>(
    qualifier = config::class.qualifier(),
    parameters = { extraParameters.invoke().add(config).add(componentContext) }
)

/**
 * Registers this component factory under a [ConfigQualifier] derived from [clazz], so that
 * [getComponent] can resolve the correct [Component] for any [BaseConfig] at runtime.
 */
@OptIn(KoinInternalApi::class)
infix fun <S : Component, C : BaseConfig> KoinDefinition<out S>.bindConfig(clazz: KClass<C>): KoinDefinition<out S> {
    val mapping = indexKey(Component::class, clazz.qualifier(), factory.beanDefinition.scopeQualifier)
    module.mappings[mapping] = factory
    return this
}
