package moe.nea.rxcraft

interface RxScope : AutoCloseable {
	override fun close()

	fun bind(scopeMember: ScopeMember)
}
