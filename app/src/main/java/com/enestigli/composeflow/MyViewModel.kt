package com.enestigli.composeflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    val countDownTimerFlow  = flow<Int> {

        val countDownFrom = 10
        var counter = countDownFrom
        emit(countDownFrom)
        while(counter > 0){
            delay(1000)
            counter--
            emit(counter)
        }


    }
    init{
        collectInViewModel()
    }

    private fun collectInViewModel(){


        //way 1 - the most common
        viewModelScope.launch {
                                                        //En çok kullanılan bu / the most common
           countDownTimerFlow
               .filter {
                   it % 3 == 0
               }
               .map {
                   it+it
               }
               .collect{
               println("counter is : ${it}")
           }


            // way 2
/*
            countDownTimerFlow.collectLatest {
                delay(2000)
                println("counter is : ${it}")   //bir problem olursa ne olucağına dair bir kullanım şekli / a usage pattern of what happens if there is a problem
            }
        }
*/
        // way 3
/*
        countDownTimerFlow.onEach {
            println(it)                      // Bu da yukarıda ki işlemin aynısını yapıyor  , collect işlemini yapıyor. / do same job like below , doing a collect
        }.launchIn(viewModelScope)

 */


    }

}
    //LiveData comparisons
    private val _liveData = MutableLiveData<String>("kotlinLiveData")
    val liveData: LiveData<String> = _liveData              //Bu işlem başka bir yerden livedata mızın değiştirilebilmesini engelliyor , onu garanti altına alıyoruz


    fun changeLiveDataValue(){
        _liveData.value = "Live Data"
    }

    //flowlarda yani aşağıdaki MutableStateFlow gibi ilk değeri vermek zorundasınız ama mutableLiveData da vermek zorunda değilsiniz.
    private val _stateFlow = MutableStateFlow("kotlinStateFlow")
    val stateFlow = _stateFlow.asStateFlow()

    //SharedFlow is highly configurable version of stateFlow.
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()


    fun changeStateFlowValue(){
        _stateFlow.value = "State Flow"
    }


    fun changeSharedFlowValue (){
        viewModelScope.launch {
            _stateFlow.emit("Shared Flow")
        }

    }



}