package com.example.prjversami.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.prjversami.R;

public class FragmentUtil {

    public static void carregarFragment(FragmentManager manager, int idFrameLayout, Fragment fragment){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(idFrameLayout, fragment);
        transaction.commit();
    }

    public static void carregarFragment(FragmentManager manager, int idFrameLayout, Fragment fragment, Bundle bundle){
        FragmentTransaction transaction = manager.beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(idFrameLayout, fragment);
        transaction.commit();
    }

}
