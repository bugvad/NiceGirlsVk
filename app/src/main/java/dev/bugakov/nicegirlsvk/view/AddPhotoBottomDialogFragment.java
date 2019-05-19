package dev.bugakov.nicegirlsvk.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
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

    private static int sleftPinIndex = 0;
    private static int srightPinIndex = 0;

    public interface onsomeEventListener2 {
        public void event();
    }

    static AddPhotoBottomDialogFragment.onsomeEventListener2 eventListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            eventListener = (AddPhotoBottomDialogFragment.onsomeEventListener2) activity;
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
            Constant.setFrom(sleftPinIndex + 18);
            Constant.setTo(srightPinIndex + 18);
            eventListener.event();
            dismiss();
        });

        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar,
                                              int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue,
                                              String rightPinValue) {
                sleftPinIndex = leftPinIndex;
                srightPinIndex = rightPinIndex;
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
