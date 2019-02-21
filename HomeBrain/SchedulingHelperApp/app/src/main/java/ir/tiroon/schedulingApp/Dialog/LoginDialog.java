package ir.tiroon.schedulingApp.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.security.SecureRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.tiroon.schedulingApp.Fragment.DeviceListFragment;
import ir.tiroon.schedulingApp.JavaUtil.LocalSchedulerWebServicesCallUtil;
import ir.tiroon.schedulingApp.JavaUtil.Oauth2Util;
import ir.tiroon.schedulingApp.JavaUtil.OkHttpUtil;
import ir.tiroon.schedulingApp.JavaUtil.Util;
import ir.tiroon.schedulingApp.MainActivity;
import ir.tiroon.schedulingApp.Model.Device;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import schedulingApp.tiroon.ir.R;

import static ir.tiroon.schedulingApp.JavaUtil.LocalSchedulerWebServicesCallUtil.authServerBaseURL;
import static ir.tiroon.schedulingApp.JavaUtil.LocalSchedulerWebServicesCallUtil.localSchedulerBaseURL;

public class LoginDialog extends Dialog {

    @BindView(R.id.loginUsername)
    EditText username;

    @BindView(R.id.loginBaseurl)
    EditText base_url;

    @BindView(R.id.loginPassword)
    EditText password;

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.loginStatus)
    TextView loginStatus;

    Activity context;

    public LoginDialog(final Activity context) {
        super(context);

        this.context = context;

        setTitle("Enter User Information");

        setContentView(R.layout.login_dailog);

        ButterKnife.bind(this);

        loginStatus.setText(LocalSchedulerWebServicesCallUtil.loginStatus);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.setBaseUrls(base_url.getText().toString().trim(),context);

                final ProgressDialog pd = new ProgressDialog(context);
                pd.setMessage("Logging In");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(false);
                pd.show();

                try {
                    final Oauth2Util oauth2Util = new Oauth2Util("homesecret", "homebrain", authServerBaseURL + "/oauth/token",
                            username.getText().toString().trim(), password.getText().toString().trim());

                    final String[] refreshToken = {"ABC"};

                    Thread t1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                refreshToken[0] = oauth2Util.gainRefreshToken();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t1.start();
                    t1.join();

                    OkHttpUtil.asyncGet(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            pd.dismiss();
                            LocalSchedulerWebServicesCallUtil.loginStatus = response.code()== 200 ? "online":"offline";
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loginStatus.setText(LocalSchedulerWebServicesCallUtil.loginStatus);
                                    context.recreate();
                                    DeviceListFragment.needRefresh = true;
                                }
                            });
                        }
                    }, localSchedulerBaseURL + "tellRefreshToken/" + refreshToken[0], LocalSchedulerWebServicesCallUtil.homeClient);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }
}
