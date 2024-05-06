package moe.nea.rxcraft

interface Provider<T> : ScopeMember {
	fun get(): T
	fun <V> map(mapper: (T) -> V): Provider<V>
	fun subscribe(observer: Observer<T>): Subscription
}