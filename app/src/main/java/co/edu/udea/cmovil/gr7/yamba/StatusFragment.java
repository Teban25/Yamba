package co.edu.udea.cmovil.gr7.yamba;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientException;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {
        private static final String TAG = StatusFragment.class.getSimpleName();
        private Button mButtonTweet;
        private EditText mTextStatus;
        private TextView mTextCount;
        private int mDefaultColor;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_status, container, false);

            mButtonTweet = (Button) v.findViewById(R.id.buttonTweet);
            mTextStatus = (EditText) v.findViewById(R.id.editStatus);
            mTextCount = (TextView) v.findViewById(R.id.textCount);
            mTextCount.setText(Integer.toString(140));
            mDefaultColor = mTextCount.getTextColors().getDefaultColor();

            mButtonTweet.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String status = mTextStatus.getText().toString();
                    PostTask postTask = new PostTask();
                    postTask.execute(status);
                    Log.d(TAG, "onClicked");
                }

            });

            mTextStatus.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    int count = 140 - s.length();
                    mTextCount.setText(Integer.toString(count));

                    if (count < 50) {
                        mTextCount.setTextColor(Color.RED);
                    } else {
                        mTextCount.setTextColor(mDefaultColor);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                }

            });

            Log.d(TAG, "onCreated");

            return v;
        }

        class PostTask extends AsyncTask<String, Void, String> {
            private ProgressDialog progress;

            @Override
            protected void onPreExecute() {
                progress = ProgressDialog.show(getActivity(), "Posting",
                        "Please wait...");
                progress.setCancelable(true);
            }

            // Executes on a non-UI thread
            @Override
            protected String doInBackground(String... params) {
                try {
                    SharedPreferences prefs = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    String username = prefs.getString("username", "");
                    String password = prefs.getString("password", "");

                    YambaClient cloud = new YambaClient(username, password);
                    cloud.postStatus(params[0]);

                    Log.d(TAG, "Successfully posted to the cloud: " + params[0]);
                    return "Successfully posted";
                } catch (Exception e) {
                    Log.e(TAG, "Failed to post to the cloud", e);
                    e.printStackTrace();
                    return "Failed to post";
                }
            }

            // Called after doInBackground() on UI thread
            @Override
            protected void onPostExecute(String result) {
                progress.dismiss();
                if (getActivity() != null && result != null)
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            }

        }
    }

}
