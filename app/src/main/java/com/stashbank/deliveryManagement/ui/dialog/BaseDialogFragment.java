package com.stashbank.deliveryManagement.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.stashbank.deliveryManagement.R;

/**

 */
public abstract class BaseDialogFragment extends DialogFragment {
    protected abstract String getDialogTag();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        //DialogFragment dismissed on orientation change when setRetainInstance(true) is set (compatibility library)
        // Issue 17423: http://code.google.com/p/android/issues/detail?id=17423
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);

        super.onDestroyView();
    }

    public void show(FragmentActivity fragmentActivity) {
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(getDialogTag());
        if (prev != null) {
            ft.remove(prev);
        }
        // show the dialog. http://code.google.com/p/android/issues/detail?id=23761
        show(ft, getDialogTag());
    }

    public int show(FragmentTransaction transaction, String tag) {
        return show(transaction, tag, true);
    }


    public int show(FragmentTransaction transaction, String tag, boolean allowStateLoss) {
        transaction.add(this, tag);
        return allowStateLoss ? transaction.commitAllowingStateLoss() : transaction.commit();
    }

    public void hide(FragmentActivity fragmentActivity) {
        if (fragmentActivity != null) {
            FragmentManager fm = fragmentActivity.getSupportFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag(getDialogTag());
            if (prev != null) {
                ft.remove(prev);
            }
            ft.commitAllowingStateLoss();
        }
    }
}
