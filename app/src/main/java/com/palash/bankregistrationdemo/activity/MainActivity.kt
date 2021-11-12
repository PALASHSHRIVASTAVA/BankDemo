package com.palash.bankregistrationdemo.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import kotlinx.android.synthetic.main.activity_main.*
import android.text.SpannableStringBuilder
import android.widget.TextView

import android.text.style.ForegroundColorSpan

import android.text.SpannableString
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.palash.bankregistrationdemo.R
import com.palash.bankregistrationdemo.viewmodel.MainViewModel
import io.reactivex.Observable
import org.koin.android.viewmodel.ext.android.viewModel
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.schedulers.Schedulers

import io.reactivex.internal.util.NotificationLite.disposable
import io.reactivex.observers.DisposableObserver
import java.lang.Exception
import android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS

import android.text.InputType.TYPE_CLASS_TEXT
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.coroutines.runBlocking
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    val mainViewModel: MainViewModel by viewModel()
    var day1 = 0
    var month1 = 0
    var year1 = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nextBtn.isEnabled = false
        nextBtn.setOnClickListener(this)
        panTextView.setOnClickListener(this)
        initObserver()

        onTextChangeListner()

    }




    fun initObserver()
    {
        mainViewModel.disposable.add(
            mainViewModel.getSpannableStringObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<SpannableStringBuilder>()
                {
                    override fun onNext(t: SpannableStringBuilder) {
                        tAndc?.setText(t)
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        Log.e("on Complete","Complete")
                    }

                })
        )

        mainViewModel.panCardLiveData.observe(this, Observer { boolean->
            if(boolean !=null)
            {
                mainViewModel.isPanCardValid = boolean
           // Log.e("Vlaue of Pancard",""+boolean)
                if(!boolean && editTextTextPanNumber.text.toString().length>9)
                {
                    Toast.makeText(this,"Pan card number is not valid.",Toast.LENGTH_SHORT).show()
                }
            }
            if(mainViewModel.isAllFieldValid())
            {
                nextBtn.isEnabled = true

            }
            else{
                nextBtn.isEnabled = false

            }
        })


    }


    fun onTextChangeListner()
    {
       val  currentYear = Calendar.getInstance().get(Calendar.YEAR)-18;

        editTextTextPanNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(character: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if(character?.length!! > 0)
                {
                   runBlocking{
                       mainViewModel.getText(character.toString())
                   }
                }
                else{
                    nextBtn.isEnabled = false
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
        )
        birth_day.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(character: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if(character?.length!! > 0)
                {

                    day1 = character.toString().toInt()
                    if(day1>0  && day1<=31)
                    {
                        if(year1>= 1900 && year1%4!=0 && month1==2 && day1>28)
                        {
                            birth_day.text.clear()
                            mainViewModel.isDateValid = false
                            birth_day.error ="date must not be greater then 28"
                        }
                        else if(year1>= 1900 && year1%4==0 && month1==2 && day1>28)
                        {
                            birth_day.text.clear()
                            mainViewModel.isDateValid = false
                            birth_day.error ="date must not be greater then 29"
                        }
                        else{
                            mainViewModel.isDateValid = true
                        }
                    }
                    else{
                        mainViewModel.isDateValid = false
                        birth_day.text.clear()
                        birth_day.error = "Date can not be greater then 31"
                    }



                }
                else{
                    mainViewModel.isDateValid = false
                    nextBtn.isEnabled = false
                }
                if(mainViewModel.isAllFieldValid())
                {
                    nextBtn.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
        )
        birth_month.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(character: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if(character?.length!! >= 1)
                {
                    month1 = character.toString().toInt()
                    if(month1>0  && month1<=12)
                    {
                    mainViewModel.isMonthValid = true
                        if(month1==2 && day1>29)
                        {
                            birth_day.text.clear()
                            mainViewModel.isDateValid = false
                        }
                    }
                    else{
                        nextBtn.isEnabled = false
                        mainViewModel.isMonthValid = false
                        birth_month.error = "Month can not be greater then 12"
                    }


                }
                else{
                    nextBtn.isEnabled = false
                    mainViewModel.isMonthValid = false
                }

                if(mainViewModel.isAllFieldValid())
                {
                    nextBtn.isEnabled = true
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
        )
        birth_year.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(character: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if(character?.length!! > 3)
                {
                     year1 = character.toString().toInt()

                    Log.e("Year ",""+year1)
                    Log.e("currentYear ",""+currentYear)
                    if(year1 >=1900 && year1 <= currentYear)
                    {
                        mainViewModel.isYearValid = true
                        if(year1%4!=0 && month1==2 && day1>28)
                        {
                            birth_day.text.clear()
                            mainViewModel.isDateValid = false
                            birth_day.error ="date must not be greater then 28"
                        }
                        else if(year1>= 1900 && year1%4==0 && month1==2 && day1>28)
                        {
                            birth_day.text.clear()
                            mainViewModel.isDateValid = false
                            birth_day.error ="date must not be greater then 29"
                        }


                    }
                    else{
                        nextBtn.isEnabled = false
                        mainViewModel.isYearValid = false
                    }


                }
                else{
                    nextBtn.isEnabled = false
                    mainViewModel.isYearValid = false
                }

                if(mainViewModel.isAllFieldValid())
                {
                    nextBtn.isEnabled = true
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
        )

    }

    override fun onClick(view: View?) {
        when (view?.id)
        {
            R.id.nextBtn -> {
            if(mainViewModel.isAllFieldValid())
            {
                Toast.makeText(this,"Details submitted successfully",Toast.LENGTH_SHORT).show()
                finish()

            }else{
                Toast.makeText(this,"All field must be valid",Toast.LENGTH_SHORT).show()
            }

            }
            R.id.panTextView->{
                finish()
            }
        }

    }


}