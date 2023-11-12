package com.github.mutoxu_n.fastexponentiationapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.github.mutoxu_n.fastexponentiationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var errorDrawable: Drawable

    private enum class Type {
        BASE, EXP, NUM
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        errorDrawable = AppCompatResources.getDrawable(
            this@MainActivity, androidx.appcompat.R.drawable.abc_edit_text_material)!!
            .apply { setTint(getColor(R.color.red)) }

        // upper side
        binding.baseUpper.addTextChangedListener(OnTextEdited(binding.baseUpper, Type.BASE, true))
        binding.expUpper.addTextChangedListener(OnTextEdited(binding.expUpper, Type.EXP, true))
        binding.numUpper.addTextChangedListener(OnTextEdited(binding.numUpper, Type.NUM, true))

        viewModel.resultUpper.observe(this) { it?.let {
            binding.resultUpper.text = it.toString()
        } }

        // bottom side
        binding.baseBottom.addTextChangedListener(OnTextEdited(binding.baseBottom, Type.BASE, false))
        binding.expBottom.addTextChangedListener(OnTextEdited(binding.expBottom, Type.EXP, false))
        binding.numBottom.addTextChangedListener(OnTextEdited(binding.numBottom, Type.NUM, false))

        viewModel.resultBottom.observe(this) { it?.let {
            binding.resultBottom.text = it.toString()
        } }

        // sync button
        binding.sync.setOnClickListener {
            binding.baseBottom.setText(binding.resultUpper.text)
            binding.numBottom.text = binding.numUpper.text
        }

        binding.sync.setOnLongClickListener {
            it?.performClick()
            val c = viewModel.couple()
            if(c != 0L || binding.expBottom.text.toString().isNotEmpty()) {
                binding.expBottom.setText(c.toString())
                if(c == 0L) binding.expBottom.background = errorDrawable
            }
            true
        }

        setContentView(binding.root)
    }

    private inner class OnTextEdited(view: View, type: Type, isUpper: Boolean): TextWatcher {
        private val view: View
        private val type: Type
        private val isUpper: Boolean

        init {
            this.view = view
            this.type = type
            this.isUpper = isUpper
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(text: Editable?) {
            try {
                // 入力をViewModelに渡す
                val value = text.toString().toLong()
                view.setBackgroundResource(com.google.android.material.R.drawable.abc_edit_text_material)
                when(type) {
                    Type.BASE -> viewModel.setBase(value, isUpper)
                    Type.EXP -> viewModel.setExp(value, isUpper)
                    Type.NUM -> viewModel.setNum(value, isUpper)
                }

            } catch (e: NumberFormatException) {
                // 入力エラー時に赤く表示
                if(text.toString().isNotEmpty()) view.background = errorDrawable
            }
        }
    }
}