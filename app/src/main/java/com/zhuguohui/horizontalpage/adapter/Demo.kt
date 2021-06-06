package com.zhuguohui.horizontalpage.adapter

/**
 * 实现了多个接口存在相同签名的方法的处理方式
 */
interface A {
    fun foo()
    fun bar()
}

interface B {
    fun foo()
    fun bar()
}

class C : A {
    override fun foo() {
        print("foo")
    }

    override fun bar() { print("bar") }
}

class D : A, B {
    override fun foo() {
    }

    override fun bar() {
    }
}