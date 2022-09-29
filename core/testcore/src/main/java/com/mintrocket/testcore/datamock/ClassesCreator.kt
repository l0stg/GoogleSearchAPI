package com.mintrocket.testcore.datamock

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmName

val defaultsMap = mutableMapOf<String, Any>()

fun setDefaultForClass(clazz: KClass<*>, value: Any) {
    defaultsMap[clazz.jvmName] = value
}

inline fun <reified T : Any> makeDefault(): T {
    return createDefault(T::class, getKType<T>()) as T
}

fun createDefault(
    klass: KClass<*>,
    type: KType
): Any {
    defaultsMap[klass.jvmName]?.let { return it }
    createPrimitive(klass, type)?.let { return it }
    klass.constructors.sortedByDescending { it.parameters.size }
        .forEach {
            runCatching {
                val args = it.parameters
                    .map { createDefaultForParam(it.type, klass, type) }
                    .toTypedArray()
                return it.call(*args)
            }.onFailure {
                it.printStackTrace()
            }
        }
    throw Error("Failed to create instance of $klass ")
}

private fun createDefaultForParam(
    paramType: KType,
    classRef: KClass<*>,
    type: KType
): Any? {
    val classifier = paramType.classifier
    return when (classifier) {
        is KClass<*> -> createDefault(classifier, paramType)
        is KTypeParameter -> {
            val typeParameterName = classifier.name
            val typeParameterId =
                classRef.typeParameters.indexOfFirst { it.name == typeParameterName }
            val parameterType = type.arguments[typeParameterId].type ?: getKType<Any>()
            createDefault(parameterType.classifier as KClass<*>, parameterType)
        }
        else -> throw Error("Type of the classifier $classifier is not supported")
    }
}

private fun createPrimitive(
    klass: KClass<*>,
    type: KType
): Any? {
    return when (klass) {
        Float::class -> 0f
        Double::class -> 0.0
        Long::class -> 0L
        Byte::class -> 0
        Short::class -> 0
        Int::class -> 0
        Char::class -> 'a'
        Boolean::class -> false
        String::class -> "dummy string"
        List::class -> createList(klass, type)
        else -> {
            if (klass.isSubclassOf(Enum::class)) {
                createEnum(klass as KClass<Enum<*>>)
            } else {
                null
            }
        }
    }
}

private fun createEnum(enumClass: KClass<Enum<*>>): Any {
    return enumClass.java.enumConstants!![0]
}

private fun createList(
    classRef: KClass<*>,
    type: KType
): List<Any?> {
    val elemType = type.arguments[0].type!!
    return mutableListOf()
}