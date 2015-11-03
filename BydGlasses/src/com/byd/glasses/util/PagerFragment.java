package com.byd.glasses.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.byd.glasses.R;
import com.byd.glasses.activity.CloudActivity;
import com.byd.glasses.activity.HudActivity;
import com.byd.glasses.activity.TeachActivity;

public class PagerFragment extends Fragment {

    private static final String TAG = "-->PagerFragment";
    private static final String TEXT = "text";
    private static final String NUMBER = "number";
    private static final String[] FUNC = {"云服务","","教学"};
    private static final int[] BACK_IMAGE = {R.drawable.cloud_service,R.drawable.hud,R.drawable.jiaoxue};
    private CharSequence sequence;
    private int number;
    
    public PagerFragment() {
        // TODO Auto-generated constructor stub
    }
    
    public static PagerFragment newInstance(int arg0){
        PagerFragment fragment = new PagerFragment();
        Bundle bundle = new Bundle();
        bundle.putCharSequence(TEXT, FUNC[arg0]);
        bundle.putInt(NUMBER, arg0);
        fragment.setArguments(bundle);
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.v(TAG, "onCreateView");
        sequence = getArguments().getCharSequence(TEXT);
        number = getArguments().getInt(NUMBER);
        View view = inflater.inflate(R.layout.fragment, null);
        Button mButton = (Button)view.findViewById(R.id.btnConfirm);
        mButton.setBackgroundResource(BACK_IMAGE[number]);
        mButton.setText(sequence);
        mButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch(number){
                    case 0:
                        startActivity(new Intent(getActivity(),CloudActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(),HudActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(),TeachActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
        return view;
    }

}
