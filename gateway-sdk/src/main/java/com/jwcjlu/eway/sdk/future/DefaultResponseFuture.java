package com.jwcjlu.gateway.sdk.future;

import com.jwcjlu.gateway.sdk.callback.Callback;
import com.jwcjlu.gateway.sdk.http.Response;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultResponseFuture implements ResponseFuture{
    private final static long defaultTimeOut=50000;
    private ReentrantLock  lock=new ReentrantLock();
    private Condition done=lock.newCondition();
    private Response reponse;
    private boolean isDone=false;
    public Callback callback;
    public DefaultResponseFuture(){

    }
    public DefaultResponseFuture(Callback callback){
        this.callback=callback;
    }
    @Override
    public Response get() throws Exception{
        // TODO Auto-generated method stub
        return get(defaultTimeOut);
    }

    @Override
    public Response get(long timeOut) throws Exception {
        // TODO Auto-generated method stub
        lock.lock();
        try{
            if(!isDone()){

                done.await(defaultTimeOut, TimeUnit.MILLISECONDS);
            }
        }catch(Exception e){

        }finally{
            lock.unlock();
        }
        return fromReturnToResponse();
    }

    @Override
    public boolean isDone() {
        // TODO Auto-generated method stub
        return isDone;
    }
    private Response  fromReturnToResponse() throws Exception{
        if(reponse==null){
            throw new Exception("dianyongshibei");
        }
        return reponse;
    }


    public  void receive(Response response) {
        // TODO Auto-generated method stub
        if(callback!=null){
            callback.callback(response);
            return;
        }
        lock.lock();
        try{
            this.reponse=response;
            isDone=true;
            done.signal();
        }finally{
            lock.unlock();

        }

    }

}
