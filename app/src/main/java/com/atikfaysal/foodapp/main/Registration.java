package com.atikfaysal.foodapp.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atikfaysal.foodapp.R;
import com.atikfaysal.foodapp.backend.BackgroundTask;
import com.atikfaysal.foodapp.backend.CheckInternetConnection;
import com.atikfaysal.foodapp.interfaces.InitialMethods;
import com.atikfaysal.foodapp.interfaces.OnResponseTask;
import com.atikfaysal.foodapp.others.AdditionalMethods;
import com.atikfaysal.foodapp.others.DataValidation;
import com.atikfaysal.foodapp.others.DesEncryptionAlgo;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Registration extends AppCompatActivity implements InitialMethods,View.OnClickListener,DatePickerDialog.OnDateSetListener
{

    protected TextInputEditText inUsername,inName,inEmail,inPassword,inConPass,inAdress;
    protected TextView txtSignIn,txtDob;
    protected DataValidation validation;
    protected CheckInternetConnection internetConnection;
    protected AdditionalMethods additionalMethods;
    protected ProgressDialog progressDialog;
    protected ProgressBar progressBar;
    protected Map<String,String>map;
    protected DesEncryptionAlgo encryptionAlgo;

    protected Button bProceed;
    protected int day,month,year;

    protected RadioGroup radioGender;
    protected String name,email,userName,password,conPassword,gender,address,dob;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        initComponent();//initialize all component
    }

    @Override
    public void initComponent() {
        inUsername = findViewById(R.id.inputUsername);
        inName = findViewById(R.id.inputName);
        inEmail = findViewById(R.id.inputEmail);
        inPassword = findViewById(R.id.inputPass);
        inConPass = findViewById(R.id.inputConPass);
        radioGender = findViewById(R.id.radioGender);
        bProceed = findViewById(R.id.bProceed);
        txtSignIn = findViewById(R.id.txtSingIn);
        inAdress = findViewById(R.id.inputAddress);
        progressBar = findViewById(R.id.progress);
        txtDob = findViewById(R.id.inputDob);
        ImageView iconBack = findViewById(R.id.iconBack);


        Calendar calendar = Calendar.getInstance();//calender
        validation = new DataValidation();//User data validation
        internetConnection = new CheckInternetConnection(this);//internet connection
        additionalMethods = new AdditionalMethods(this);//Some additional methods
        progressDialog = new ProgressDialog(this);//progress dialog
        encryptionAlgo = new DesEncryptionAlgo(this);//encrypt user password


        day = calendar.get(Calendar.DAY_OF_MONTH);//getting current date
        month = calendar.get(Calendar.MONTH);//getting current month
        year = calendar.get(Calendar.YEAR);//getting current year


        //set click listener
        bProceed.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
        txtDob.setOnClickListener(this);
        iconBack.setOnClickListener(this);

        //calling required method
        chooseGender();//choose gender from radio button
        infoValidation();//checking user information
    }

    @Override
    public void setToolbar() {}

    //button click action
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.bProceed://if all information of user are valid then go to next page for phone number verification

                if (checkingUserInfo())//checking user is connect to internet
                {
                    if(internetConnection.isOnline())//checking user's internet connection is enable
                    {
                        Map<String,String> map = new HashMap<>();

                        map.put("cases","registration");//passing case
                        map.put("username",userName);//passing username
                        map.put("email",email);//passing email

                        progressDialog.setTitle("Please wait");//set dialog title
                        progressDialog.setMessage("Checking your information....");//set dialog message
                        progressDialog.show();

                        BackgroundTask backgroundTask = new BackgroundTask(this,responseTask);//server response
                        backgroundTask.serverResponse(getResources().getString(R.string.dataValidation),map);//receive server response



                    }else additionalMethods.errorMessage(getResources().getString(R.string.offline));//if user does not access to internet
                }else additionalMethods.errorMessage(getResources().getString(R.string.invalidInfo));//if info of user are not valid


                break;
            case R.id.txtSingIn://if user has already account then go to sign in page
                finish();//close current activity,go to sign in activity
                break;
            case R.id.inputDob://select user birth date from calender
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, Registration.this, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;
            case R.id.iconBack://finish current page
                finish();//close current activity
                break;
        }
    }

    //checking all information of user are match our information policy
    private boolean checkingUserInfo()
    {
        name = Objects.requireNonNull(inName.getText()).toString();//getting name
        userName = Objects.requireNonNull(inUsername.getText()).toString();//getting username
        email = Objects.requireNonNull(inEmail.getText()).toString();//getting email
        address = Objects.requireNonNull(inAdress.getText()).toString();//getting address
        password = Objects.requireNonNull(inPassword.getText()).toString();//getting password
        conPassword = Objects.requireNonNull(inConPass.getText()).toString();//getting conPassword
        dob = txtDob.getText().toString();//getting date of birth


        return validation.nameValidation(name) && validation.emailValidation(email) &&
                validation.userNameValidation(userName) && validation.passwordValidation(password) &&
                address.length() > 15 && !TextUtils.isEmpty(address) && password.equals(conPassword) && !TextUtils.isEmpty(dob);//return true or false
    }

    //choose gender
    protected void chooseGender()
    {
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id)
                {
                    case R.id.rMale://select male
                        gender = "male";
                        break;
                    case R.id.rFemale://select female
                        gender = "female";
                        break;
                    case R.id.rOthers://select others
                        gender = "other";
                        break;
                        default:
                            gender = null;
                            break;
                }
            }
        });
    }

    //validation of user information
    protected void infoValidation()
    {
        final Drawable iconValid = getResources().getDrawable(R.drawable.icon_valid);//valid icon
        iconValid.setBounds(0,0,iconValid.getIntrinsicWidth(),iconValid.getIntrinsicHeight());
        final Drawable iconInvalid = getResources().getDrawable(R.drawable.icon_invalid);//invalid icon
        iconInvalid.setBounds(0,0,iconInvalid.getIntrinsicWidth(),iconInvalid.getIntrinsicHeight());

        //username field checking
        inUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = Objects.requireNonNull(inUsername.getText()).toString();
                if(validation.userNameValidation(text))
                    inUsername.setError("valid",iconValid);
                else inUsername.setError("invalid",iconInvalid);
            }
        });

        //name field checking
        inName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = Objects.requireNonNull(inName.getText()).toString();

                if(validation.nameValidation(text))//with regx char ,name length is greater than 5 and less than 25
                    inName.setError("valid",iconValid);
                else inName.setError("invalid name",iconInvalid);
            }
        });

        //email field checking
        inEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = inEmail.getText().toString();

                if(validation.emailValidation(text))//with regx char ,email length is greater than 5 and less than 25
                    inEmail.setError("valid",iconValid);
                else inEmail.setError("invalid email",iconInvalid);
            }
        });

        //password field checking
        inPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = Objects.requireNonNull(inPassword.getText()).toString();

                if(validation.passwordValidation(text))
                    inPassword.setError("valid",iconValid);
                else inPassword.setError("invalid password",iconInvalid);
            }
        });

        //confirm password field checking
        inConPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = Objects.requireNonNull(inConPass.getText()).toString();
                if(text.equals(Objects.requireNonNull(inPassword.getText()).toString())&&(text.length()>=8&&text.length()<=16))
                    inConPass.setError("Password match",iconValid);
                else inConPass.setError("Password does not match",iconInvalid);
            }
        });

        inAdress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = Objects.requireNonNull(inAdress.getText()).toString();
                if(text.length()>15 && !TextUtils.isEmpty(text))
                    inAdress.setError("Valid",iconValid);
                else inAdress.setError("Invalid address",iconInvalid);
            }
        });

    }

    //saving user info into database
    protected void savingUserInfo(String phone)
    {
        map = new HashMap<>();

        map.put("cases","registration");//passing cases
        map.put("name",name);//passing user full name
        map.put("username",userName);//passing username
        map.put("email",email);//passing email
        map.put("address",address);//passing address
        map.put("phone",phone);//passing user phone number
        map.put("gender",gender);//passing gender
        map.put("password",password);//passing password
        map.put("dob",dob);//passing date of birth
        map.put("date",additionalMethods.getDate());//passing current date

        if(internetConnection.isOnline())//checking internet connection is enable
        {
            BackgroundTask backgroundTask = new BackgroundTask(this,onResponseTask);
            backgroundTask.serverResponse(getResources().getString(R.string.registration),map);//server response
        }else additionalMethods.errorMessage(getResources().getString(R.string.offline));//if user has no internet connection
    }

    @Override
    public void processJsonData(String json) {}

    @Override
    public List<?> processJsonValue(String json) {return null;}

    //date set listener,select a date from datePicker
    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
        day = dd;month = mm+1;year=yy;
        String d,m;
        d = day+"";m = month+"";
        if(day<10)
            d="0"+day;
        if(month<10)
            m = "0"+month;

        txtDob.setText(year+"-"+m+"-"+d);
    }


    //take action against server response
    private OnResponseTask responseTask = new OnResponseTask() {
        @Override
        public void onResultSuccess(final String value) {

            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);//sleep for 1s

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();//dismiss progress dialog
                                switch (value)
                                {
                                    case "success"://if username and email does not exist in database
                                        onLogin(LoginType.PHONE);//phone number verification by facebook account kit
                                        break;
                                    default:
                                        bProceed.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_sign_in));//if any failed ,enable this button
                                        switch (value)
                                        {
                                            case "username"://if username is already exist in database
                                                additionalMethods.errorMessage(getResources().getString(R.string.invalidUserName));
                                                break;
                                            case "email"://if email is already exist in database
                                                additionalMethods.errorMessage(getResources().getString(R.string.invalidEmail));
                                                break;
                                            case "both"://if email and username are exist in database
                                                additionalMethods.errorMessage(getResources().getString(R.string.invalidBoth));
                                                break;
                                            default://if another error
                                                additionalMethods.errorMessage(getResources().getString(R.string.unknown));
                                                break;
                                        }
                                        break;
                                }
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }
    };

    //getting server response for insert user info
    private OnResponseTask onResponseTask = new OnResponseTask() {
        @Override
        public void onResultSuccess(String value) {
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);//sleep for 1s

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                progressBar.setVisibility(View.INVISIBLE);//invisible progress bar

                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();//start thread
        }
    };


    /*
     ******************************************************************************
     *                                                                            *
     *           PHONE NUMBER VERIFICATION WITH FACEBOOK ACCOUNT KIT API          *
     *                                                                            *
     ******************************************************************************

     */



    protected static final int FRAMEWORK_REQUEST_CODE = 1;
    private int nextPermissionsRequestCode = 4000;
    @SuppressLint("UseSparseArrays")
    private final Map<Integer, OnCompleteListener> permissionsListeners = new HashMap<>();

    private interface OnCompleteListener {
        void onComplete();
    }

    @Override
    public void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != FRAMEWORK_REQUEST_CODE) {
            return;
        }

        final String toastMessage;
        final AccountKitLoginResult loginResult = AccountKit.loginResultWithIntent(data);
        if (loginResult == null || loginResult.wasCancelled())
        {
            toastMessage = "Cancelled";
            Toast.makeText(this,toastMessage,Toast.LENGTH_SHORT).show();
        }
        else if (loginResult.getError() != null) {
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        } else {
            final AccessToken accessToken = loginResult.getAccessToken();
            if (accessToken != null) {

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        String phone = account.getPhoneNumber().toString();

                        if(phone.length()==14)
                            phone = phone.substring(3);

                        savingUserInfo(phone);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(final AccountKitError error) {
                    }
                });
            } else {
                toastMessage = "Unknown response type";
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onLogin(final LoginType loginType)
    {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        final AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,
                AccountKitActivity.ResponseType.TOKEN);
        final AccountKitConfiguration configuration = configurationBuilder.build();
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        Registration.OnCompleteListener completeListener = new Registration.OnCompleteListener() {
            @Override
            public void onComplete() {
                startActivityForResult(intent, FRAMEWORK_REQUEST_CODE);
            }
        };
        if (configuration.isReceiveSMSEnabled() && !canReadSmsWithoutPermission()) {
            final Registration.OnCompleteListener receiveSMSCompleteListener = completeListener;
            completeListener = new Registration.OnCompleteListener() {
                @Override
                public void onComplete() {
                    requestPermissions(
                            Manifest.permission.RECEIVE_SMS,
                            com.atikfaysal.foodapp.R.string.permissions_receive_sms_title,
                            com.atikfaysal.foodapp.R.string.permissions_receive_sms_message,
                            receiveSMSCompleteListener);
                }
            };
        }
        if (configuration.isReadPhoneStateEnabled() && !isGooglePlayServicesAvailable()) {
            final Registration.OnCompleteListener readPhoneStateCompleteListener = completeListener;
            completeListener = new Registration.OnCompleteListener() {
                @Override
                public void onComplete() {
                    requestPermissions(
                            Manifest.permission.READ_PHONE_STATE,
                            com.atikfaysal.foodapp.R.string.permissions_read_phone_state_title,
                            com.atikfaysal.foodapp.R.string.permissions_read_phone_state_message,
                            readPhoneStateCompleteListener);
                }
            };
        }
        completeListener.onComplete();
    }

    private boolean isGooglePlayServicesAvailable() {
        final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int googlePlayServicesAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
        return googlePlayServicesAvailable == ConnectionResult.SUCCESS;
    }

    private boolean canReadSmsWithoutPermission() {
        final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int googlePlayServicesAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
        return googlePlayServicesAvailable == ConnectionResult.SUCCESS;
    }

    private void requestPermissions(
            final String permission,
            final int rationaleTitleResourceId,
            final int rationaleMessageResourceId,
            final OnCompleteListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        checkRequestPermissions(permission, rationaleTitleResourceId, rationaleMessageResourceId, listener);
    }

    @TargetApi(23)
    private void checkRequestPermissions(final String permission, final int rationaleTitleResourceId, final int rationaleMessageResourceId,
                                         final Registration.OnCompleteListener listener) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        final int requestCode = nextPermissionsRequestCode++;
        permissionsListeners.put(requestCode, listener);

        if (shouldShowRequestPermissionRationale(permission)) {
            new AlertDialog.Builder(this).setTitle(rationaleTitleResourceId).setMessage(rationaleMessageResourceId).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    requestPermissions(new String[] { permission }, requestCode);
                }
            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    permissionsListeners.remove(requestCode);
                }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        } else requestPermissions(new String[]{ permission }, requestCode);

    }

    @TargetApi(23)
    @SuppressWarnings("unused")
    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String permissions[], final @NonNull int[] grantResults) {
        final Registration.OnCompleteListener permissionsListener = permissionsListeners.remove(requestCode);
        if (permissionsListener != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsListener.onComplete();
        }
    }


}
