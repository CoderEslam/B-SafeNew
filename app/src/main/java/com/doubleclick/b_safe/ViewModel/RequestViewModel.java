package com.doubleclick.b_safe.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.b_safe.Repository.ReuestsRepository;
import com.doubleclick.b_safe.model.Requests;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 6/16/2022
 */
public class RequestViewModel extends ViewModel implements ReuestsRepository.RequestInter {

    MutableLiveData<ArrayList<Requests>> mutableLiveData = new MutableLiveData<>();
    ReuestsRepository reuestsRepository = new ReuestsRepository(this);

    public RequestViewModel() {
        reuestsRepository.getRequests();
    }

    public LiveData<ArrayList<Requests>> getRequest() {
        return mutableLiveData;
    }

    @Override
    public void getRequests(ArrayList<Requests> requests) {
        mutableLiveData.setValue(requests);
    }
}
