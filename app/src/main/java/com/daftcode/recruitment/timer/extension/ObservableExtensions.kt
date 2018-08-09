package com.daftcode.recruitment.timer.extension

import io.reactivex.Observable
import io.reactivex.exceptions.OnErrorNotImplementedException

private val onNextStub: (Any) -> Unit = {}
private val onErrorStub: (Throwable) -> Unit = { throw OnErrorNotImplementedException(it) }
private val onCompleteStub: () -> Unit = {}

fun <T : Any> Observable<T>.retrySubscribe(onNext: (T) -> Unit = onNextStub,
                                           onError: (Throwable) -> Unit = onErrorStub,
                                           onComplete: () -> Unit = onCompleteStub) =
        this.doOnNext(onNext)
                .doOnComplete(onComplete)
                .doOnError(onError)
                .retry()
                .subscribe()!!
