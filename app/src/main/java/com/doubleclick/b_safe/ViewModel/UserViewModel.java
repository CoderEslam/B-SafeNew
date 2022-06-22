package com.doubleclick.b_safe.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doubleclick.b_safe.Repository.UserRepository;
import com.doubleclick.b_safe.model.User;

/**
 * Created By Eslam Ghazy on 6/14/2022
 */
public class UserViewModel extends ViewModel implements UserRepository.UserInter {

    MutableLiveData<User> mutableLiveData = new MutableLiveData<>();
    UserRepository userRepository = new UserRepository(this);

    public UserViewModel() {
        userRepository.getUser();
    }

    public LiveData<User> getMyUser() {
        return mutableLiveData;
    }

    @Override
    public void myUser(User user) {
        mutableLiveData.setValue(user);
    }
}
