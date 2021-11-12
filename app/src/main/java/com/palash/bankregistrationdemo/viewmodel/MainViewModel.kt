package com.palash.bankregistrationdemo.viewmodel

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.core.content.ContextCompat
import com.palash.bankregistrationdemo.R
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import android.widget.Toast
import androidx.lifecycle.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Created by Palash on 12-11-2021.
 */
class MainViewModel(val context:Context)  : ViewModel()
{
    val disposable = CompositeDisposable();
    var valueDeferred  : Deferred<Boolean> ?= null
    var isPanCardValid = false
    var isDateValid = false
    var isMonthValid = false
    var isYearValid = false

    val panCardLiveData:MutableLiveData<Boolean> by lazy {
         MutableLiveData<Boolean>()
    }



    override fun onCleared() {
        super.onCleared()

        disposable.clear();

    }



    fun changeColorOfTAndC(): SpannableStringBuilder
    {
        val text = context.getString(R.string.t_and_c)
        val txt2 =  " Learn more."

        val builder = SpannableStringBuilder()

        val str1 = SpannableString(text)
        str1.setSpan(ForegroundColorSpan(Color.BLACK), 0, str1.length, 0)
        builder.append(str1)

        val str2 = SpannableString(txt2)
        val color = ContextCompat.getColor(context, R.color.purple_700)
        str2.setSpan(ForegroundColorSpan(color), 0, str2.length, 0)
        builder.append(str2)
/*        tAndc.setText(builder, TextView.BufferType.SPANNABLE)*/
        return builder
    }


    fun getSpannableStringObservable() : Observable<SpannableStringBuilder> {

    val  builder = changeColorOfTAndC();

    return Observable.create(object :ObservableOnSubscribe<SpannableStringBuilder> {
        override fun subscribe(emitter: ObservableEmitter<SpannableStringBuilder>) {
            if (!emitter.isDisposed()) {

                emitter.onNext(builder);

            }

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        }

    });
    }

    suspend fun getText(pan:String)
    {


        valueDeferred = viewModelScope.async { validatePenCard(pan) }

        val finalValue = valueDeferred?.await( )
        isPanCardValid = finalValue!!

        panCardLiveData.value = finalValue



    }

     fun validatePenCard(pan:String):Boolean {

        val pattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        val matcher: Matcher = pattern.matcher(pan)
        if (matcher.matches()) {
          return true
        }

        return false
    }

    fun isAllFieldValid():Boolean
    {
        Log.e("isPanCardValid",""+isPanCardValid)
        Log.e("isDateValid",""+isDateValid)
        Log.e("isMonthValid",""+isMonthValid)
        Log.e("isYearValid",""+isYearValid)
        if( isPanCardValid == true && isDateValid == true && isMonthValid == true && isYearValid==true)
        {
            return true
        }
        else{
            return false
        }

    }





}