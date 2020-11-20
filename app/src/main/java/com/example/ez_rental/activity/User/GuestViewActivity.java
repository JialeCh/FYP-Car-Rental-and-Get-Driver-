package com.example.ez_rental.activity.User;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ez_rental.FragmentPages.AccountFragment;
import com.example.ez_rental.FragmentPages.CarFragment;
import com.example.ez_rental.FragmentPages.HomeFragment;
import com.example.ez_rental.FragmentPages.ReservationFragment;
import com.example.ez_rental.FragmentPages.TransactionFragment;
import com.example.ez_rental.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import nl.joery.animatedbottombar.AnimatedBottomBar;
@SuppressLint("ClickableViewAccessibility")
public class GuestViewActivity extends AppCompatActivity  {
    private float downRawX, downRawY;
    private float dX, dY;
    private FrameLayout frameLayout;
    private HomeFragment accountFragment;
    private static final String TAG = UserViewActivity.class.getSimpleName();
    AnimatedBottomBar animatedBottomBar;
    FragmentManager fragmentManager;
    private FloatingActionButton fab;
    private final static float CLICK_DRAG_TOLERANCE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);
        animatedBottomBar = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.framelayout);
        fab = findViewById(R.id.floatingActionButton3);
        if (savedInstanceState == null) {
            animatedBottomBar.selectTabById(R.id.nav_home, true);
            fragmentManager = getSupportFragmentManager();
            accountFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.framelayout, accountFragment)
                    .commit();
        }
        clickListener();

    }

    private void clickListener() {
        animatedBottomBar.setOnTabSelectListener((lastIndex, lastTab, newIndex, newTab) -> {
            Fragment fragment = null;
            switch (newTab.getId()) {
                case R.id.nav_payment:
                   fragment = new TransactionFragment();
                    break;
                case R.id.nav_booking:
                   fragment = new ReservationFragment();
                    break;
                case R.id.nav_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.nav_search:
                    fragment = new CarFragment();
                    break;
                case R.id.nav_account:
                    fragment = new AccountFragment();
                    break;
            }

            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.framelayout, fragment)
                        .commit();
            } else {
                Log.e(TAG, "Error in creating Fragment");
            }
        });
        fab.setOnTouchListener((view, motionEvent) -> {

            View viewParent;
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    downRawX = motionEvent.getRawX();
                    downRawY = motionEvent.getRawY();
                    dX = view.getX() - downRawX;
                    dY = view.getY() - downRawY;

                    return true;
                case MotionEvent.ACTION_MOVE:
                    int viewWidth = view.getWidth();
                    int viewHeight = view.getHeight();

                    viewParent = (View) view.getParent();
                    int parentWidth = viewParent.getWidth();
                    int parentHeight = viewParent.getHeight();

                    float newX = motionEvent.getRawX() + dX;
                    newX = Math.max(0, newX);
                    newX = Math.min(parentWidth - viewWidth, newX);

                    float newY = motionEvent.getRawY() + dY;
                    newY = Math.max(0, newY);
                    newY = Math.min(parentHeight - viewHeight, newY);

                    view.animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(0)
                            .start();
                    return true;

                case MotionEvent.ACTION_UP:


                    float upRawX = motionEvent.getRawX();
                    float upRawY = motionEvent.getRawY();

                    float upDX = upRawX - downRawX;
                    float upDY = upRawY - downRawY;

                    if ((Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) && performClick())
                        return true;
                    View viewParent2 = (View) view.getParent();
                    float borderY, borderX;
                    float oldX = view.getX(), oldY = view.getY();
                    float finalX, finalY;

                    borderY = Math.min(view.getY() - viewParent2.getTop(), viewParent2.getBottom() - view.getY());
                    borderX = Math.min(view.getX() - viewParent2.getLeft(), viewParent2.getRight() - view.getX());

                    float fab_margin = 15;

                    if (borderX > borderY) {
                        if (view.getY() > viewParent2.getHeight() / 2) {
                            finalY = viewParent2.getBottom() - view.getHeight();
                            finalY = Math.min(viewParent2.getHeight() - view.getHeight(), finalY) - fab_margin; // Don't allow the FAB past the bottom of the parent
                        } else {
                            finalY = viewParent2.getTop();
                            finalY = Math.max(0, finalY) + fab_margin; // Don't allow the FAB past the top of the parent
                        }
                        finalX = oldX;
                        if (view.getX() + viewParent2.getLeft() < fab_margin)
                            finalX = viewParent2.getLeft() + fab_margin;
                        if (viewParent2.getRight() - view.getX() - view.getWidth() < fab_margin)
                            finalX = viewParent2.getRight() - view.getWidth() - fab_margin;
                    } else {
                        if (view.getX() > viewParent2.getWidth() / 2) {
                            finalX = viewParent2.getRight() - view.getWidth();
                            finalX = Math.max(0, finalX) - fab_margin;
                        } else {
                            finalX = viewParent2.getLeft();
                            finalX = Math.min(viewParent2.getWidth() - view.getWidth(), finalX) + fab_margin;
                        }
                        finalY = oldY;
                        if (view.getY() + viewParent2.getTop() < fab_margin)
                            finalY = viewParent2.getTop() + fab_margin;
                        if (viewParent2.getBottom() - view.getY() - view.getHeight() < fab_margin)
                            finalY = viewParent2.getBottom() - view.getHeight() - fab_margin;
                    }

                    view.animate()
                            .x(finalX)
                            .y(finalY)
                            .setDuration(400)
                            .start();
                    return false;

                default:
                    return false;
            }

        });




    }

    private boolean performClick() {
        fab.animate()
                .rotationBy(180)
                .setDuration(300)
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction(() -> {
                    fab.setImageDrawable(getDrawable(R.drawable.ic_fabwhite));
                    fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                })
                .start();

        proceedReservation();
        return true;

    }

    private void proceedReservation(){
        final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                .setTitle("Warning Message")
                .setMessage("Only Registered User can make reservation !!")
                .setPositiveButton("Ok", null)
                .setIcon(getResources().getDrawable(R.drawable.ic_warning2))
                .show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setOnClickListener(x -> {
            dialog.dismiss();

        });

    }
}
