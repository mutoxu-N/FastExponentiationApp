package com.github.mutoxu_n.fastexponentiationapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    private var _base: MutableLiveData<Long> = MutableLiveData(null)
    val base: LiveData<Long> get() = _base

    private var _exp: MutableLiveData<Long> = MutableLiveData(null)
    val exp: LiveData<Long> get() = _exp

    private var _num: MutableLiveData<Long> = MutableLiveData(null)
    val num: LiveData<Long> get() = _num

    private var _result: MutableLiveData<Long> = MutableLiveData(null)
    val result: LiveData<Long> get() = _result

    private fun calc() {
        var b = base.value
        var e = exp.value
        val n = num.value
        if(b == null || e == null || n == null) return

        // 高速指数演算
        var r = 1L
        while(e!! > 0) {
            if(e and 1 == 1L) {
                r *= b
                r %= n
            }
            e = e shr 1
            b *= b
            b %= n
        }

        // output
        _result.value = r
    }

    fun setBase(base: Long) {
        _base.value = base
        calc()
    }

    fun setExp(exp: Long) {
        _exp.value = exp
        calc()
    }

    fun setNum(num: Long) {
        _num.value = num
        calc()
    }

}