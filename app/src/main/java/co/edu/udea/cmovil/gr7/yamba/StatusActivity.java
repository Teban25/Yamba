package co.edu.udea.cmovil.gr7.yamba;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientException;


public class StatusActivity extends Activity implements OnClickListener {
    private static final String TAG="StatusActivity";
    private EditText editTextStatus;
    private Button buttonTweet;
    private TextView textCount;
    private int defaultTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        editTextStatus=(EditText)findViewById(R.id.editStatus);
        buttonTweet=(Button)findViewById(R.id.buttonTweet);
        textCount=(TextView) findViewById(R.id.textCount);
        buttonTweet.setEnabled(false);
        buttonTweet.setOnClickListener(this);

        defaultTextColor=textCount.getTextColors().getDefaultColor();
        editTextStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Deshabilito el boton cuando esta 0 supera los 140
                if(editTextStatus.length()==0 || editTextStatus.length()>140){
                    buttonTweet.setEnabled(false);
                }else{
                    buttonTweet.setEnabled(true);
                }
                int count=140-editTextStatus.length();
                textCount.setText(Integer.toString(count));
                textCount.setTextColor(Color.GREEN);
                if(count < 18)
                    textCount.setTextColor(Color.RED);
                else
                    textCount.setTextColor(defaultTextColor);
            }
        });
    }

    @Override
    public void onClick(View view){
        //Oculto el teclado cuando se da click al boton
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextStatus.getWindowToken(), 0);
        String status=editTextStatus.getText().toString();
        Log.d(TAG,"onClicked with status: "+status);
        new PostTask().execute(status);
        //Limpio el campo cuando se envia.
        editTextStatus.setText("");
    }

    private final class PostTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            YambaClient yambaCloud=new YambaClient("student","password");
            try{
                yambaCloud.postStatus(params[0]);
                return "Sucessfully posted";
            }catch(YambaClientException e){
                e.printStackTrace();
                return "Failed to post to yamba service";
            }
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(StatusActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
