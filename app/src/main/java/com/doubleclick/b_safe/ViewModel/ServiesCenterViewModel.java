package com.doubleclick.b_safe.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.b_safe.Repository.ServiesCenterRepository;
import com.doubleclick.b_safe.model.ServiceCenter;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 6/15/2022
 */
public class ServiesCenterViewModel extends ViewModel implements ServiesCenterRepository.getCenters {


    MutableLiveData<ArrayList<ServiceCenter>> mutableLiveData = new MutableLiveData<>();

    ServiesCenterRepository serviesCenterRepository = new ServiesCenterRepository(this);

    public ServiesCenterViewModel() {
    }

    public void Search(String name) {
        serviesCenterRepository.getServiceCenter(name);
    }

    public LiveData<ArrayList<ServiceCenter>> getServiceCenter() {
        return mutableLiveData;
    }


    @Override
    public void getServiesCenter(ArrayList<ServiceCenter> serviceCenters) {
        mutableLiveData.setValue(serviceCenters);
    }
}
