package com.demoapp11_dev_23213;

import android.app.Application;
import android.content.Context;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost =
      new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }
      };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
  }
class LoginActivity : BaseClassActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.login_activity)
    val button = findViewById<ImageView>(R.id.plusbutton)
    val forgotpassword=findViewById<TextView>(R.id.forgotpassword)
    button.setOnClickListener {
        val i = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(i)
    }
    forgotpassword.setOnClickListener{
        val i = Intent(applicationContext, ForgotPassword::class.java)
        startActivity(i)
    }

    loginbtn.setOnClickListener {
        val email = loginuser.text.toString().trim()
        val password = loginpassword.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(
                applicationContext, "Data is missing",Toast.LENGTH_LONG
            ).show()
            loginuser.error = "Email required"
            loginuser.requestFocus()
            return@setOnClickListener
                    }


        if (password.isEmpty()) {
            loginpassword.error = "Password required"
            loginpassword.requestFocus()
            return@setOnClickListener
        }

        RetrofitClient.instance.userLogin(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("res", "" + t)


                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    var res = response

                    Log.d("response check ", "" + response.body()?.status.toString())
                    if (res.body()?.status==200) {

                        SharedPrefManager.getInstance(applicationContext)
                            .saveUser(response.body()?.data!!)

                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        showToast(applicationContext,res.body()?.message)
                        Log.d("kjsfgxhufb",response.body()?.status.toString())
                        startActivity(intent)
                        finish()


                    }

            else
                    {
                        try {
                            val jObjError =
                                JSONObject(response.errorBody()!!.string())

                            showToast(applicationContext,jObjError.getString("user_msg"))
                        } catch (e: Exception) {
                            showToast(applicationContext,e.message)
                            Log.e("errorrr",e.message)
                        }
                    }

                }
            })

    }
}}
  private static void initializeFlipper(
      Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("com.demoapp11_dev_23213.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
}
