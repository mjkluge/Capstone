package com.example.capstone;

import java.util.Observable;

public class SingletonObserver extends Observable {
    private static SingletonObserver instance;

    public static SingletonObserver getInstance()
    {
        if (instance == null)
        {
            //synchronized block to remove overhead
            synchronized (SingletonObserver.class)
            {
                if(instance==null)
                {
                    // if instance is null, initialize
                    instance = new SingletonObserver();
                }

            }
        }
        return instance;
    }
    public void change(){
        this.setChanged();
    }
}
