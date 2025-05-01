package com.example.prjversami.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.prjversami.R;
import com.example.prjversami.views.AvisoDeErro;

public class NavigationUtil {

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

    public static void activityErro(Context context){
        Intent intent = new Intent(context, AvisoDeErro.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

}
