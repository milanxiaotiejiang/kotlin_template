package com.seabreeze.robot.base.ext.foundation

/**
 * 实现：  Boolean.yes{}.otherwise{}
 */
sealed class BooleanY<out T>//起桥梁作用的中间类，定义成协变

object Otherwise : BooleanY<Nothing>()//Nothing是所有类型的子类型，协变的类继承关系和泛型参数类型继承关系一致

class TransferDataY<T>(val data: T) : BooleanY<T>()//data只涉及到了只读的操作

//声明成inline函数
inline fun <T> Boolean.yes(block: () -> T): BooleanY<T> = when {
    this -> {
        TransferDataY(block.invoke())
    }
    else -> Otherwise
}

inline fun <T> BooleanY<T>.otherwise(block: () -> T): T = when (this) {
    is Otherwise ->
        block()
    is TransferDataY ->
        this.data
}


/**
 * 实现：  Boolean.otherwise{}.yes{}
 */
sealed class BooleanExtO<out T>//起桥梁作用的中间类，定义成协变

object Yes : BooleanExtO<Nothing>()//Nothing是所有类型的子类型，协变的类继承关系和泛型参数类型继承关系一致

class TransferDataO<T>(val data: T) : BooleanExtO<T>()//data只涉及到了只读的操作

//声明成inline函数
inline fun <T> Boolean.otherwise(block: () -> T): BooleanExtO<T> = when {
    !this -> {
        TransferDataO(block.invoke())
    }
    else -> Yes
}

inline fun <T> BooleanExtO<T>.yes(block: () -> T): T = when (this) {
    is Yes ->
        block()
    is TransferDataO ->
        this.data
}