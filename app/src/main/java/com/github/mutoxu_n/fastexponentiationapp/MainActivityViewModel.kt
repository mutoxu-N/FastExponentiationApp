package com.github.mutoxu_n.fastexponentiationapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    private var _baseUpper: MutableLiveData<Long> = MutableLiveData(null)
    private val baseUpper: LiveData<Long> get() = _baseUpper

    private var _expUpper: MutableLiveData<Long> = MutableLiveData(null)
    private val expUpper: LiveData<Long> get() = _expUpper

    private var _numUpper: MutableLiveData<Long> = MutableLiveData(null)
    private val numUpper: LiveData<Long> get() = _numUpper

    private var _resultUpper: MutableLiveData<Long> = MutableLiveData(null)
    val resultUpper: LiveData<Long> get() = _resultUpper

    private var _baseBottom: MutableLiveData<Long> = MutableLiveData(null)
    private val baseBottom: LiveData<Long> get() = _baseBottom

    private var _expBottom: MutableLiveData<Long> = MutableLiveData(null)
    private val expBottom: LiveData<Long> get() = _expBottom

    private var _numBottom: MutableLiveData<Long> = MutableLiveData(null)
    private val numBottom: LiveData<Long> get() = _numBottom

    private var _resultBottom: MutableLiveData<Long> = MutableLiveData(null)
    val resultBottom: LiveData<Long> get() = _resultBottom

    private fun calc(isUpper: Boolean) {
        var b = if(isUpper) baseUpper.value else baseBottom.value
        var e = if(isUpper) expUpper.value else expBottom.value
        val n = if(isUpper) numUpper.value else numBottom.value
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
        (if(isUpper) _resultUpper else _resultBottom).value = r
    }

    fun setBase(base: Long, isUpper: Boolean) {
        (if(isUpper) _baseUpper else _baseBottom).value = base
        calc(isUpper)
    }

    fun setExp(exp: Long, isUpper: Boolean) {
        (if(isUpper) _expUpper else _expBottom).value = exp
        calc(isUpper)
    }

    fun setNum(num: Long, isUpper: Boolean) {
        (if(isUpper) _numUpper else _numBottom).value = num
        calc(isUpper)
    }

}