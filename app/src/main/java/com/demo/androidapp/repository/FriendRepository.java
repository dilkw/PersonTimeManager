package com.demo.androidapp.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.dao.AppDatabase;
import com.demo.androidapp.dao.FriendDao;
import com.demo.androidapp.model.FindFriendInfo;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.model.returnObject.ReturnListObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRepository {

    private Api api;

    private FriendDao friendDao;

    private MutableLiveData<ReturnData<List<Friend>>> returnDataLiveData;

    private MutableLiveData<ReturnData> deleteReturnDataLiveData;

    private FriendRepository authRepository;

    public FriendRepository(Application application) {
        this.api = MyApplication.getApi();
        this.friendDao = Room
                .databaseBuilder(application.getApplicationContext(), AppDatabase.class, "task")
                .build()
                .friendDao();
        returnDataLiveData = new MutableLiveData<ReturnData<List<Friend>>>();
    }

    public MutableLiveData<ReturnData<List<Friend>>> getReturnDataLiveData() {
        if (returnDataLiveData == null) {
            Log.d("imageView", "getReturnDataLiveData: returnDataLiveData为空");
        }
        return returnDataLiveData;
    }

    //查找好友信息
    public LiveData<ReturnData<FindFriendInfo>> getFriendInfoByUid(String fUid) {
        if (returnDataLiveData == null) {
            Log.d("imageView", "getReturnDataLiveData: returnDataLiveData为空");
        }
        return api.getFriendInfoByUid(fUid);
    }

    public FriendRepository getInstance() {
        if (authRepository == null) {
            return new FriendRepository(MyApplication.getApplication());
        }
        return this;
    }

    //根据cookie在服务器中获取好友列表,cookie在发送请求的时候自动添加到header中
    public void getAllFriendsByUidInServer() {
        Log.d("imageView", getClass().getName() + "FriendRepository: 获取好友列表" );
        api.getAllFriends().enqueue(new Callback<ReturnData<ReturnListObject<Friend>>>() {
            @Override
            public void onResponse(Call<ReturnData<ReturnListObject<Friend>>> call, Response<ReturnData<ReturnListObject<Friend>>> response) {
                Log.d("imageView", "FriendRepository: 获取好友列表成功");
                returnDataLiveData.postValue(new ReturnData<List<Friend>>(response.body().getCode(),response.body().getMsg(),response.body().getData().getItems()));
//                if (response.body().getData().getTotal() > 0) {
//                    Friend[] friends = new Friend[response.body().getData().getTotal()];
//                    response.body().getData().getItems().toArray(friends);
//                    deleteALLFriendsAndAdd(friends);
//                }
            }
            @Override
            public void onFailure(Call<ReturnData<ReturnListObject<Friend>>> call, Throwable t) {
                t.printStackTrace();
                Log.d("imageView", "FriendRepository: 获取任务清单失败" );
                returnDataLiveData.postValue(new ReturnData<>(RCodeEnum.ERROR));
            }
        });
    }

    //根据cookie在服务器中获取好友列表,cookie在发送请求的时候自动添加到header中
    public LiveData<ReturnData<ReturnListObject<Friend>>> getAllFriendsLiveDataByUidInServer() {
        return api.getAllFriendsLiveData();
    }

    //在本地数据库中删除所有数据并更新数据
    public void deleteAllFriendsAndAdd(Friend... friends) {
        Log.d("imageView", "数据库删除数据");
        new FriendRepository.DeleteALLFriendsAndAdd(friendDao,this).execute(friends);
    }

    //根据uid在本地数据库中获取好友列表
    public LiveData<List<Friend>> getAllFriendsByUidInDB() {
        String uid = MyApplication.getApplication().getUser().getUid();;
        Log.d("imageView", "getAllFriendsByUidInDB: 数据库获取数据");
        return friendDao.getAllFriendLiveDataByUid(uid);
    }

    //根据uid和FName在本地数据库中获取好友列表
    public LiveData<List<Friend>> getAllFriendsInDBByUidAndFName(String fName) {
        String uid = MyApplication.getApplication().getUser().getUid();;
        Log.d("imageView", "getAllFriendsInDBByUidAndFName: 数据库获取数据");
        return friendDao.getAllFriendsInDBByUidAndFName(uid,"%" + fName + "%");
    }

    //在服务器中删除好友(单个)
    public LiveData<ReturnData<Object>> deleteFriendByFIdInServer(String fUid) {
        Log.d("imageView", "deleteFriendsByIdsInServer: 服务器删除数据" + fUid);
        return api.deleteFriendsByFUId(fUid);
    }

    //在数据库中删除数据
    public void deleteFriendByUidInDB(Friend... friends) {
        new DeleteAllFriends(friendDao).execute(friends);
    }

    //添加到服务器好友列表
    public LiveData<ReturnData<Friend>> addFriendToServer(String fid) {
        String userName = MyApplication.getApplication().getUser().getName();
        String imgUrl = MyApplication.getApplication().getUser().getImg_url();
        return api.addFriend(fid,userName,imgUrl);
    }

    public void updateFriendsInDB(Friend...friends) {
        new UpdateFriendList(friendDao).execute(friends);
    }

    //插入本地数据库好友列表
    public Long[] addFriendsToDB(Friend... friends) {
        Long[] longs = null;
        try {
            longs = new InsertFriendList(friendDao).execute(friends).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return longs;
    }

    public static class InsertFriendList extends AsyncTask<Friend,Void, Long[]> {

        FriendDao friendDao;

        InsertFriendList(FriendDao friendDao) {
            this.friendDao = friendDao;
        }

        @Override
        protected Long[] doInBackground(Friend... friends) {
            return this.friendDao.addFriends(friends);
        }
    }

    public static class GetAllFriendByUid extends AsyncTask<String,Void,Void> {

        FriendDao friendDao;

        private MutableLiveData<ReturnData<List<Friend>>> returnDataLiveData;

        GetAllFriendByUid(FriendDao friendDao,MutableLiveData<ReturnData<List<Friend>>> returnDataLiveData) {
            this.friendDao = friendDao;
            this.returnDataLiveData = returnDataLiveData;
        }

        @Override
        protected Void doInBackground(String... strings) {
//            List<Task> tasks = taskDao.getAllTaskListByUid(strings[0]);
//            Log.d("imageView", "doInBackground: 本地数据库数据长度" + tasks.size());
//            returnDataLiveData.postValue(new ReturnData<List<Task>>(RCodeEnum.DB_OK,tasks));
            return null;
        }
    }

    public static class UpdateFriendList extends AsyncTask<Friend,Void,Void> {

        FriendDao friendDao;

        UpdateFriendList(FriendDao friendDao) {
            this.friendDao = friendDao;
        }

        @Override
        protected Void doInBackground(Friend... friends) {
            this.friendDao.updateAllFriendFromServers(friends);
            return null;
        }
    }

    public static class DeleteAllFriends extends AsyncTask<Friend,Void,Void> {

        FriendDao friendDao;

        DeleteAllFriends(FriendDao friendDao) {
            this.friendDao = friendDao;
        }

        @Override
        protected Void doInBackground(Friend... friends) {
            friendDao.deleteFriend(friends);
            return null;
        }
    }

    public void reLogin(){
        String userName = MyApplication.getApplication().getUser().getUid();
        String password = MyApplication.getApplication().getUser().getPassword();
//        api.signIn()
    }


    //删除并更新数据
    private void deleteALLFriendsAndAdd(Friend ... friends) {
        new DeleteALLFriendsAndAdd(friendDao,this).execute(friends);
    }
    //删除并更新数据
    public static class DeleteALLFriendsAndAdd extends AsyncTask<Friend,Void,Void> {

        FriendDao friendDao;

        FriendRepository friendRepository;

        DeleteALLFriendsAndAdd(FriendDao friendDao,FriendRepository friendRepository) {
            this.friendDao = friendDao;
            this.friendRepository = friendRepository;
        }

        Friend[] friends;

        @Override
        protected Void doInBackground(Friend... friends) {
            this.friends = friends;
            friendDao.deleteAllFriendsByUid(MyApplication.getApplication().getUser().getUid());
            //friendDao.deleteAllFriendsByUid();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            friendRepository.addFriendsToDB(this.friends);
        }
    }
}
