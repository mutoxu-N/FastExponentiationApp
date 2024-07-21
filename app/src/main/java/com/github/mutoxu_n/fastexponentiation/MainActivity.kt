package com.github.mutoxu_n.fastexponentiation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.mutoxu_n.fastexponentiation.ui.theme.FastExponentiationTheme
import kotlin.math.sqrt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FastExponentiationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Screen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Screen(
    modifier: Modifier = Modifier,
) {
    // 素数
    var p by rememberSaveable { mutableLongStateOf(-1L) }
    var q by rememberSaveable { mutableLongStateOf(-1L) }

    // 平文・暗号文
    var g by rememberSaveable { mutableLongStateOf(-1L) }
    var s by rememberSaveable { mutableLongStateOf(-1L) }

    // 秘密鍵
    var secret by rememberSaveable { mutableLongStateOf(-1L) }

    // 公開鍵
    var public by rememberSaveable { mutableLongStateOf(-1L) }

    val n = p*q
    val phi = (p-1)*(q-1)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        //暗号化
        ExponentialDisplay(
            p = p,
            q = q,
            base = g,
            onBaseChange = { g = it },
            exp = public,
            onExpChange = { public = it },
        )

        //復号化
        ExponentialDisplay(
            p = p,
            q = q,
            base = s,
            onBaseChange = { s = it },
            exp = secret,
            onExpChange = { secret = it }
        )

        // 素数入力
        PrimeDisplay(
            p = p,
            q = q,
            onPChange = { p = it },
            onQChange = { q = it },
        )


        Column {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {

                    s = fastExp(g, public, n)
                    if(gcd(public, phi) == 1L) {
                        // 逆元が存在するなら同期
                        val v = inv(public, phi)
                        secret = v
                    }
                },
                enabled = p>0 && q>0 && gcd(g, n) == 1L,
            ) { Text("Sync") }

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    while(true) {
                        val i = Random.nextLong(1L, phi)
                        if(gcd(i, phi) != 1L) continue

                        val v = inv(i, phi)
                        if(v > 0) {
                            public = i
                            secret = v
                        }
                        break
                    }

                    if(g > 0 && gcd(g, n) == 1L) {
                        s = fastExp(g, public, n)
                    }
                },
                enabled = p>0 && q>0,
            ) { Text("Create Random Keys") }
        }

    }
}

@Composable
private fun ExponentialDisplay(
    modifier: Modifier = Modifier,
    p: Long,
    q: Long,
    base: Long,
    exp: Long,
    onExpChange: (Long) -> Unit,
    onBaseChange: (Long) -> Unit,
) {
    val isBaseError = base <= 0 || gcd(base, p*q) != 1L
    val isExpError = exp <= 0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium,
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.medium,
            )
            .padding(10.dp, 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // base
            DecimalInput(
                modifier = modifier
                    .weight(1f),
                num = base,
                validate = { it > 0 && gcd(it, p*q) == 1L },
                onNumChange = { onBaseChange(it) }
            )

            Text(
                modifier = modifier
                    .wrapContentSize()
                    .padding(3.dp),
                text = "^",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            // exp
            DecimalInput(
                modifier = modifier
                    .weight(1f),
                num = exp,
                onNumChange = { onExpChange(it) }
            )
        }

        // mod
        Row {
            val r = fastExp(base, exp, p*q)
            Text(
                modifier = modifier,
                text = "mod ${
                    if(p>0 && q>0) p*q
                    else "?"
                }=${
                    if(!isBaseError && !isExpError) r 
                    else "?"
                }",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

        }
    }
}

@Composable
private fun PrimeDisplay(
    modifier: Modifier = Modifier,
    p: Long,
    q: Long,
    onPChange: (Long) -> Unit,
    onQChange: (Long) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium,
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.medium,
            )
            .padding(10.dp, 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            // P
            PrimeInput(modifier = modifier.weight(1f), name = "P", p = p) { onPChange(it) }

            Spacer(modifier = Modifier.weight(0.1f))

            // Q
            PrimeInput(modifier = modifier.weight(1f), name = "Q", p = q) { onQChange(it) }
        }

        // detail
        if(p>0 && q>0) {
            Text(
                modifier = modifier,
                text =  "n=${p*q}, φ(n)=${(p-1)*(q-1)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
private fun PrimeInput(
    modifier: Modifier = Modifier,
    name: String,
    p: Long,
    onPChange: (Long) -> Unit,
) {
    var isError by rememberSaveable { mutableStateOf(isPrime(p)) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$name:",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        DecimalInput(
            modifier = modifier
                .weight(1f),
            num = p,
            validate = { isPrime(it) },
        ) {
            isError = !isPrime(it)
            onPChange(it)
        }
    }
}

@Composable
private fun DecimalInput(
    modifier: Modifier = Modifier,
    num: Long,
    validate: (Long) -> Boolean = { it > 0 },
    onNumChange: (Long) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = if(num > 0) "$num" else "",
        onValueChange = {
            if(it.isEmpty()) onNumChange(-1L)
            else {
                val n = convertToLong(it)
                if(n > 0)
                    onNumChange(n)
            }
        },
        textStyle = TextStyle.Default.copy(
            fontSize = MaterialTheme.typography.displaySmall.fontSize,
        ),
        isError = !validate(num),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal,
        ),
        singleLine = true,
    )
}

private fun convertToLong(s: String): Long {
    return s.toLongOrNull() ?: -1L
}

// 素数判定
private fun isPrime(num: Long): Boolean {
    if(num == 2L) return true
    if(num < 2L || num%2L == 0L) return false

    for(i in 3 until sqrt(num.toDouble()).toLong() step 2)
        if(num % i == 0L) return false
    return true
}

// 高速指数演算
private fun fastExp(base: Long, exp: Long, num: Long): Long {
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

// 拡張ユークリッド互除法によるMOD逆元計算
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

// 最大公約数
private fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ScreenPreview() {
    FastExponentiationTheme {
        Screen()
    }
}