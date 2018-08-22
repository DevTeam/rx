package devteam.rx

fun <T>subjectOf(): Subject<T> = object : Subject<T> {
    private val observers: MutableList<Observer<T>> = mutableListOf()
    private var isCompleted: Boolean = false

    override fun subscribe(observer: Observer<T>): Disposable {
        synchronized(observers) {
            if (isCompleted) {
                observer.onCompleted()
                return@synchronized
            }

            observers.add(observer)
        }

        return disposableOf {
            synchronized(observers) {
                observers.remove(observer)
            }
        }
    }

    override fun onNext(value: T) =
            synchronized(observers) {
                for (observer in observers) {
                    observer.onNext(value)
                }
            }

    override fun onError(error: Exception) =
            synchronized(observers) {
                for (observer in observers) {
                    observer.onError(error)
                }

                finish()
            }

    override fun onCompleted() =
            synchronized(observers) {
                for (observer in observers) {
                    observer.onCompleted()
                }

                finish()
            }

    private fun finish() {
        observers.clear()
        isCompleted = true
    }
}