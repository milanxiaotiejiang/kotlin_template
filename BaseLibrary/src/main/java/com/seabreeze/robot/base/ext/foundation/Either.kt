package com.seabreeze.robot.base.ext.foundation

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/16
 * desc   :
 * </pre>
 */
sealed class Either<out A, out B> {
    internal abstract val isRight: Boolean
    internal abstract val isLeft: Boolean

    fun isLeft(): Boolean = isLeft
    fun isRight(): Boolean = isRight

    inline fun <C> fold(ifLeft: (A) -> C, ifRight: (B) -> C): C = when (this) {
        is Right -> ifRight(b)
        is Left -> ifLeft(a)
    }

    fun swap(): Either<B, A> = fold({ Right(it) }, { Left(it) })

    @Suppress("DataClassPrivateConstructor")
    data class Left<out A> @PublishedApi internal constructor(val a: A) : Either<A, Nothing>() {
        override val isLeft
            get() = true
        override val isRight
            get() = false

        companion object {
            operator fun <A> invoke(a: A): Either<A, Nothing> = Left(a)
        }
    }

    @Suppress("DataClassPrivateConstructor")
    data class Right<out B> @PublishedApi internal constructor(val b: B) : Either<Nothing, B>() {
        override val isLeft
            get() = false
        override val isRight
            get() = true

        companion object {
            operator fun <B> invoke(b: B): Either<Nothing, B> = Right(b)
        }
    }

    companion object {

        fun <L> left(left: L): Either<L, Nothing> = Left(left)

        fun <R> right(right: R): Either<Nothing, R> = Right(right)

        fun <L, R> cond(test: Boolean, ifTrue: () -> R, ifFalse: () -> L): Either<L, R> =
            if (test) right(ifTrue()) else left(ifFalse())

    }
}

fun <L> Left(left: L): Either<L, Nothing> = Either.left(left)

fun <R> Right(right: R): Either<Nothing, R> = Either.right(right)
