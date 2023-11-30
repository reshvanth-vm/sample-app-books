package com.example.books.base.exception

import kotlin.reflect.KClass

class NonInflatedBindingException(clazz: KClass<*>) : RuntimeException("Binding is not inflated [$clazz]")