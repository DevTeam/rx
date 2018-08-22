package devteam.rx

fun <T> emptyObserver(): Observer<T> =
        object : Observer<T> {
            override fun onNext(value: T) = Unit
            override fun onError(error: Exception) = Unit
            override fun onCompleted() = Unit
        }