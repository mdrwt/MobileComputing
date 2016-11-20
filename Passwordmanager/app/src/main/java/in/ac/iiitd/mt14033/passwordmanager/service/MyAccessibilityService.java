package in.ac.iiitd.mt14033.passwordmanager.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import in.ac.iiitd.mt14033.passwordmanager.AddLoginActivity;
import in.ac.iiitd.mt14033.passwordmanager.CommonContants;
import in.ac.iiitd.mt14033.passwordmanager.DBHelper;
import in.ac.iiitd.mt14033.passwordmanager.MatchingLoginsActivity;
import in.ac.iiitd.mt14033.passwordmanager.R;
import in.ac.iiitd.mt14033.passwordmanager.model.MatchingLogin;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
/**
 * Created by Madhur on 09/11/16.
 */
public class MyAccessibilityService extends AccessibilityService {

    private String requesting_package=null;
    private int requesting_password_et_hash;

    private NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;

    private MatchingLogin selectedMatchingLogin=null;
    private AccessibilityNodeInfo usernameET;

    private static MyAccessibilityService myAccessibilityServiceInstance;

    public DBHelper dbh=null;

    private String getEventType(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                return "TYPE_NOTIFICATION_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                return "TYPE_VIEW_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                return "TYPE_VIEW_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                return "TYPE_VIEW_LONG_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                return "TYPE_VIEW_SELECTED";
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                return "TYPE_WINDOW_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                return "TYPE_VIEW_TEXT_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
                return "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED";
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                return "TYPE_VIEW_ACCESSIBILITY_FOCUSED";
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                return "TYPE_WINDOW_CONTENT_CHANGED";


        }
        return "default";
    }

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }



    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if(event.getSource()==null) {
            return;
        }
        else {
            AccessibilityNodeInfo source  = event.getSource();
            if(source==null) {
                source.recycle();
                return;
            }
            AccessibilityNodeInfo parent = source.getParent();
            /**
             * NOTE: This is old implementation here for legacy purposes.
             * Currently using AlertDialog with transparent activity.
             * Detect popup from our app and respective button pushes within it.
             */
            if(event.getPackageName().equals(getString(R.string.selfpackagename)) &&
                    event.getClassName().equals("android.widget.Button") &&
                    event.getEventType()==AccessibilityEvent.TYPE_VIEW_CLICKED) {
                Log.v(getString(R.string.VTAG), event.getPackageName()+" "+getEventType(event)+" "+event.getClassName());
                if(source.getParent()==null || source.getParent().getContentDescription()==null)
                    return;
                if(source.getParent().getContentDescription().equals(getString(R.string.matching_login_popup_content_description))) {
                    if(source.getText().equals(getString(R.string.add_login_button_text))) {
                        Log.v(getString(R.string.VTAG), "add_login_button_text");
                        Intent intent = new Intent(this, AddLoginActivity.class);
                        intent.putExtra(CommonContants.MATCHING_LOGIN_PACKAGE_NAME, requesting_package);
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                    else if(source.getText().equals(getString(R.string.cancel))) {
                        Log.v(getString(R.string.VTAG), "cancel button tapped");
                    }
                }
            }
            /**
             * Check if field is password and packagename is not own apps.
             * If no text is filled in then show possible matching logins to choose and
             * option to add new login.
             */
            else if(event.isPassword() &&
                    !event.getPackageName().equals(getString(R.string.selfpackagename)) &&
                    event.getClassName().equals("android.widget.EditText") &&
                    event.getEventType()==AccessibilityEvent.TYPE_VIEW_CLICKED) {
                Log.v(getString(R.string.VTAG), event.getPackageName()+" "+getEventType(event)+" "+event.getClassName());
                /*
                Intent intent = new Intent(this, MatchingLoginsDialogActivity.class);
                requesting_package = event.getPackageName().toString();
                requesting_password_et_hash = source.hashCode();
                intent.putExtra(getString(R.string.matching_login_package_name), requesting_package);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                */

                mNotificationManager = (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);
                builder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(getString(R.string.matching_login_notification_title))
                                .setContentText(getString(R.string.matching_login_notification_subtitle))
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText("temporary"))
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                                .addAction (R.drawable.ic_stat_dismiss,
//                                        getString(R.string.dismiss), piDismiss)
//                                .addAction (R.drawable.ic_stat_snooze,
//                                        getString(R.string.snooze), piSnooze)
                ;
                if (Build.VERSION.SDK_INT >= 21) builder.setVibrate(new long[0]);

                Intent intent = new Intent(this, MatchingLoginsActivity.class);
                requesting_package = event.getPackageName().toString();
                requesting_password_et_hash = source.hashCode();
                intent.putExtra(CommonContants.MATCHING_LOGIN_PACKAGE_NAME, requesting_package);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                builder.setContentIntent(resultPendingIntent);
                mNotificationManager.notify(CommonContants.NOTIFICATION_ID, builder.build());


            }
            else if(event.getPackageName().equals(requesting_package) &&
                    event.getEventType()==AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                if(requesting_package!=null && selectedMatchingLogin!=null) {
                    Log.v(getString(R.string.VTAG), event.getPackageName()+" "+getEventType(event)+" "+event.getClassName());

                    AccessibilityNodeInfo passwordField = getPasswordField(source);
                    /**
                     * set the password in the password field.
                     */
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo
                                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            selectedMatchingLogin.getPassword());
                    AccessibilityNodeInfo prev=null;
                    if(passwordField!=null) {
                        passwordField.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                        prev = getSiblingEditTextUsername(passwordField);
                    }
                    /**
                     * set the is username in the username field.
                     */
                    Bundle newBundle = new Bundle();
                    newBundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            selectedMatchingLogin.getUsername());
                    if(prev!=null)
                        prev.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, newBundle);

                    Log.v(getString(R.string.VTAG), "selected match "+ selectedMatchingLogin.toString());
                    requesting_package=null;
                    selectedMatchingLogin=null;
                }
            }
        }
    }
    public boolean isUsernameField(String text) {
        String ltext = text.toLowerCase();
        if(ltext.contains("email")) {
            return true;
        }
        else {
            return false;
        }
    }
    public AccessibilityNodeInfo getUserNameField(AccessibilityNodeInfo source) {

        int nchild = source.getChildCount();
        for (int i=0; i<nchild; ++i) {
            AccessibilityNodeInfo child = source.getChild(i);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                CharSequence textchar = child.getText();
                CharSequence descchar = child.getContentDescription();
                String text = null;
                if (textchar != null) {
                    text = textchar.toString();
                    if (isUsernameField(text)) {
                        return child;
                    }
                }
                if(descchar!=null) {
                    String desc = descchar.toString();
                    if(isUsernameField(desc)) {
                        return child;
                    }
                }
                AccessibilityNodeInfo field =  getUserNameField(child);
                if(field!=null) return field;

            }
        }
        return null;
    }

    /**
     * For simple views where parent has all the username and other fields
     * simply get parent and its childs and return previous child if it is edittext
     * (which is most likely to be the username/email field)
     */
    public AccessibilityNodeInfo getSiblingEditTextUsername(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo parent = source.getParent();
        if(parent==null) {
            return null;
        }
        int nchild = parent.getChildCount();
        if (nchild>=2) {
            for (int i=0; i<nchild; ++i) {
                AccessibilityNodeInfo child = parent.getChild(i);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if(child.isPassword()) {
                        if(i>0) {
                            AccessibilityNodeInfo prevsibling = parent.getChild(i-1);
                            if(prevsibling.getClassName().equals("android.widget.EditText")) {
                                return parent.getChild(i-1);
                            }

                        }
                        else {
                            return null;
                        }
                    }
                }
            }
        }
        return getCousinEditTextUsername(source);
    }

    /**
     * http://www.geeksforgeeks.org/print-cousins-of-a-given-node-in-binary-tree/
     * For complex UI where parent does not have username field but
     * some other ancestor has username field as child subchild we try to find cousins and the preceeding cousin
     * would be out username/email field.
     *
     * Parent------LinearLayout------usernameET
     *      |------LinearLayout------PasswordET
     *      |------Button
     *      |------Button
     *      |------Button
     */
    int getLevel(AccessibilityNodeInfo root, AccessibilityNodeInfo source, int level) {
        if(root==null)
            return 0;
        if(root.hashCode()==source.hashCode()) {
            return level;
        }
        int childcount=root.getChildCount();
        for(int i=0; i<childcount; ++i) {
            int downlevel = getLevel(root.getChild(i), source, level+1);
            if(downlevel!=0)
                return downlevel;
        }
        return 0;
    }
    private AccessibilityNodeInfo getCousinEditText(AccessibilityNodeInfo root, AccessibilityNodeInfo source, int level) {
        if(root==null || level<2) {
            return null;
        }
        if(level==2) {
            int childcount=root.getChildCount();
            for(int i=0; i<childcount; ++i) {
                AccessibilityNodeInfo child = root.getChild(i);
                if(child.hashCode()==source.hashCode()) {
                    /**
                     * we wait till we get our password field.
                     * the last edittext we got as child of previous parent would be out required username field.
                     */
                    return usernameET;
                }
            }
            for(int i=0; i<childcount; ++i) {
                AccessibilityNodeInfo child = root.getChild(i);
                if(child.getClassName().equals("android.widget.EditText")) {
                    Log.v(getString(R.string.VTAG), "setting new username ET....");
                    /**
                     * Store username ET. We will return at when we find our password field.
                     * ie. We will store and return EditText found just before password field.
                     */
                    usernameET = child;
                }
            }
        }
        else if(level>2) {
            int childcount=root.getChildCount();
            for(int i=0; i<childcount; ++i) {
                AccessibilityNodeInfo cousin = getCousinEditText(root.getChild(i), source, level-1);
                if(cousin!=null)
                    return cousin;
            }
        }
        return null;
    }
    private AccessibilityNodeInfo getCousinEditTextUsername(AccessibilityNodeInfo source) {
        int level = getLevel(getRootInActiveWindow(), source, 1);

        return getCousinEditText(getRootInActiveWindow(), source, level);

    }


    /**
     * Searches for edittext field of type password in the child heirarchy and returns it.
     * @param source
     * @return password type field.
     */
    public AccessibilityNodeInfo getPasswordField(AccessibilityNodeInfo source) {
        if (source.isPassword())
            return source;
        int nchild = source.getChildCount();
        for (int i=0; i<nchild; ++i) {
            AccessibilityNodeInfo child = source.getChild(i);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {

                if(child.isPassword()) {
                    return child;
                }
                AccessibilityNodeInfo field =  getPasswordField(child);
                if(field!=null) return field;

            }
        }
        return null;
    }

    public boolean isLoginField(String text) {
        String ltext = text.toLowerCase();
        if(ltext.contains("login") || ltext.contains("log in")) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isPasswordField(String text) {
        String ltext = text.toLowerCase();
        if(ltext.contains("password") || ltext.contains("pass word")) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isLoginPage(AccessibilityNodeInfo source) {


        if(source==null)
            return false;

        int nchild = source.getChildCount();
        for (int i=0; i<nchild; ++i) {
            AccessibilityNodeInfo child = source.getChild(i);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                CharSequence textchar = child.getText();
                String text = null;
                if (textchar != null) {
                    text = textchar.toString();
                } else {
                    continue;
                }
                if (isLoginField(text)) {
                    return true;
                }

            }
            if(isLoginPage(child)) {
                return true;
            }

        }
        return false;
    }


    @Override
    public void onInterrupt() {
        Log.v(getString(R.string.VTAG), "onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.v(getString(R.string.VTAG), "onServiceConnected");
        myAccessibilityServiceInstance = this;
        if(dbh==null)
            dbh=new DBHelper(this);
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        setServiceInfo(info);
    }
    @Override
    public boolean onUnbind(Intent intent) {
        myAccessibilityServiceInstance = null;
        return super.onUnbind(intent);
    }
    @Nullable
    public static MyAccessibilityService getInstance(){
        return myAccessibilityServiceInstance;
    }

    public void onSelectMatchingLogin(MatchingLogin matchingLogin) {
        selectedMatchingLogin = matchingLogin;
    }

}