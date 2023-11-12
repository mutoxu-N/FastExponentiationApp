package com.github.mutoxu_n.fastexponentiationapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.sqrt

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
        val b = if(isUpper) baseUpper.value else baseBottom.value
        val e = if(isUpper) expUpper.value else expBottom.value
        val n = if(isUpper) numUpper.value else numBottom.value
        if(b == null || e == null || n == null) return

        // output
        (if(isUpper) _resultUpper else _resultBottom).value = fastExp(b, e, n)
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

    // 対となる鍵を探す
    fun couple(): Long {
        val exp = expUpper.value
        val num = numUpper.value
        if(exp == null || num  == null) return 0
        return inv(exp, eulerPhi(num))
    }

    // 高速指数演算
    private fun fastExp(base: Long, exp: Long, num: Long): Long {
        // 高速指数演算
        var r = 1L
        var b = base
        var e = exp

        while(e > 0) {
            if(e and 1 == 1L) {
                r *= b
                r %= num
            }
            e = e shr 1
            b *= b
            b %= num
        }

        return r
    }

    // 逆元計算
    private fun inv(base: Long, num: Long): Long {
        var r1 = num
        var r2 = base
        var u1 = 0L
        var u2 = 1L
        var q: Long
        var w: Long

        while (r1 > 0) {
            q = r2 / r1
            w = r2 - q*r1
            r2 = r1
            r1 = w
            w = u2 - q*u1
            u2 = u1
            u1 = w
        }

        val r = (u2 + num) % num
        if(base * r % num == 1L) return r
        return 0
    }

    // オイラーのφ関数
    private fun eulerPhi(num: Long): Long {
        // 素因数分解
        val p = mutableListOf<Pair<Long, Long>>()
        var n = num
        var i: Long

        if (n % 2L == 0L) {
            i = 1
            while (n % 2L == 0L) {
                n /= 2L
                i *= 2
            }
            p.add(Pair(2, i))
        }

        var j = 3L
        while (n > 1L) {
            if(isPrime(n, j)) {
                p.add(Pair(n, n))
                break
            }

            if (n % j == 0L) {
                i = 1
                while (n % j == 0L) {
                    n /= j
                    i *= j
                }
                p.add(Pair(j, i))
            }
            j += 2L
        }

        // phi function
        var t = 1L
        for(pair in p)
            t *= pair.second - pair.second/pair.first

        return t
    }

    private fun isPrime(num: Long, start: Long): Boolean {
        if(num == 2L) return true
        if(num < 2L || num%2L == 0L) return false

        for(i in start/2*2+1 until sqrt(num.toDouble()).toLong() step 2)
            if(num % i == 0L) return false
        return true
    }

}