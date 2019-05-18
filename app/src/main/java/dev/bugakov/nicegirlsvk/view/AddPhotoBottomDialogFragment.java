package dev.bugakov.nicegirlsvk.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appyvet.materialrangebar.RangeBar;

import dev.bugakov.nicegirlsvk.R;
import dev.bugakov.nicegirlsvk.model.Constant;

public class AddPhotoBottomDialogFragment extends BottomSheetDialogFragment {
    public static AddPhotoBottomDialogFragment newInstance() {
        return new AddPhotoBottomDialogFragment();
    }

    private static int a = 0;
    private static int b = 0;

    public interface onsomeEventListener2 {
        public void someEvent2();
    }

    static AddPhotoBottomDialogFragment.onsomeEventListener2 someEventListener2;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener2 = (AddPhotoBottomDialogFragment.onsomeEventListener2) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onsomeEventListener2");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.filter_sheet, container, false);

        RangeBar rangebar = rootView.findViewById(R.id.rangebar);

        Button resultButton = rootView.findViewById(R.id.button);

        resultButton.setOnClickListener(v -> {
            Constant.setFrom(a + 18);
            Constant.setTo(b + 18);
            someEventListener2.someEvent2();
            dismiss();
        });

        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar,
                                              int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue,
                                              String rightPinValue) {
                a = leftPinIndex;
                b = rightPinIndex;
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }
        });

        return rootView;
    }
}
