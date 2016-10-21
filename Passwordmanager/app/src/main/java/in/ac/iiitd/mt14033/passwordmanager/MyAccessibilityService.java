package in.ac.iiitd.mt14033.passwordmanager;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService extends AccessibilityService {

    static final String TAG = "MyAccessibilityService";
    private static final String TASK_LIST_VIEW_CLASS_NAME =
            "in.ac.iiitd.mt14033.passwordmanager.MyAccessibilityService";
    private AccessibilityEvent oldAccessibilityEvent;
    private AccessibilityNodeInfo loginUsernameField;

    public DBHelper dbh=null;
    private boolean loginShouldSave=false;

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
            AccessibilityNodeInfo parent = source.getParent();
            Log.v(TAG, getEventType(event)+" "+event.getClassName());
            if(event.getPackageName().equals("in.ac.iiitd.mt14033.passwordmanager") &&
                    event.getClassName().equals("android.widget.Button") &&
                    event.getEventType()==AccessibilityEvent.TYPE_VIEW_CLICKED) {
                if(source.getParent().getContentDescription().equals("loginsavepasswordpopup")) {

                    if(source.getText().equals("Save")) {
                        loginSavePasswordTapped();
                    }
                    else if(source.getText().equals("Later")) {
                        loginLaterPasswordTapped();
                    }
                    else if(source.getText().equals("Never")) {
                        loginNeverPasswordTapped();
                    }
                }


            }
            else if(event.getPackageName().equals("in.ac.iiitd.mt14033.passwordmanager") &&
                    event.getClassName().equals("android.widget.Button") &&
                    event.getEventType()==AccessibilityEvent.TYPE_VIEW_CLICKED) {
                if(source.getParent().getContentDescription().equals("loginfillpasswordpopup")) {

                    if(source.getText().equals("Fill")) {

                    }
                    else if(source.getText().equals("Cancel")) {

                    }
                }


            }
            else if(isLoginPage(getRootInActiveWindow())) {
                Log.v(TAG, "This is a login page");
                if((event.getEventType()==AccessibilityEvent.TYPE_VIEW_FOCUSED
                        || event.getEventType()==AccessibilityEvent.TYPE_VIEW_CLICKED
                        || event.getEventType()==AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) &&
                        event.getEventType()!=AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED &&
                        event.getClassName().equals("android.widget.EditText")) {
                    Log.v(TAG, getEventType(event));
                    if(event.isPassword()) {
                        Log.v(TAG, "is password field");
                        AccessibilityNodeInfo usernameField = getUserNameField(getRootInActiveWindow());
                        if(usernameField!=null) {
                            Log.v(TAG,"username text is "+usernameField.getText().toString());
                            if(hasSavedPassword(usernameField)) {
                                Log.v(TAG, "Saved creds found for "+usernameField.getText().toString());
                                // Show should fill popup
                                Intent intent = new Intent(this, GetPasswordDialogActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else {
                                Log.v(TAG, "No saved creds for "+usernameField.getText().toString());
                                // Show should save popup
                            }
                        }
                    }
//                   else {
//                       AccessibilityNodeInfo field = getUserNameField(source);
//                       if(field!=null) {
//                            loginUsernameField=field;
//                           Log.v(TAG, loginUsernameField.getText().toString());
//
//                       }
//
//                   }
                }
                else if(event.getEventType()==AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    Log.v(TAG, "came her etoo");
                    if(isLoginPage(source) && loginShouldSave) {
                        loginShouldSave=false;
                        AccessibilityNodeInfo usernamefield = getUserNameField(getRootInActiveWindow());
                        AccessibilityNodeInfo passwordfield = getPasswordField(getRootInActiveWindow());
                        Log.v(TAG, "username: "+usernamefield.getText().toString());
                        String username = usernamefield.getText().toString();
                        String url = usernamefield.getPackageName().toString();
                        PasswordManager pm = new PasswordManager(0, username, url, "1234");
                        boolean res = dbh.addPassword(pm);
                        if(res)
                            Log.v(TAG, "added password successfully");


                    }
                }
                else if(event.getEventType()==AccessibilityEvent.TYPE_VIEW_CLICKED &&
                        event.getClassName().equals("android.widget.Button")) {
                    String text = event.getText().toString();
                    if(isLoginField(text)) {
                        if(isPasswordSaveAllowed(source)) {
                            Intent intent = new Intent(this, SavePasswordDialogActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }

                }


            }
            if (parent == null) {
                return;
            }

        }


    }
    public boolean showSaveCredentialPopup(AccessibilityNodeInfo nodeInfo) {
        String username = nodeInfo.getText().toString();
        String packagename = nodeInfo.getPackageName().toString();
//        PasswordManager pm new PasswordManager()
        return false;
    }

    public boolean isPasswordSaveAllowed(AccessibilityNodeInfo nodeInfo) {
        String username = nodeInfo.getText().toString();
        String packagename = nodeInfo.getPackageName().toString();
        int allowed = dbh.isPasswordSaveAllowed(username, packagename);

        return (allowed!=0);
    }

    public boolean hasSavedPassword(AccessibilityNodeInfo nodeInfo) {
        String username = nodeInfo.getText().toString();
        String packagename = nodeInfo.getPackageName().toString();
        PasswordManager pm = dbh.getPasswordForUsernameAndPackage(username, packagename);
        if(pm!=null) {
            return true;
        }
        else {
            return false;
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
    public AccessibilityNodeInfo getPasswordField(AccessibilityNodeInfo source) {

        int nchild = source.getChildCount();
        for (int i=0; i<nchild; ++i) {
            AccessibilityNodeInfo child = source.getChild(i);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                CharSequence textchar = child.getText();
                CharSequence descchar = child.getContentDescription();
                String text = null;
                if(child.isPassword()) {
                    return child;
                }
                if (textchar != null) {
                    text = textchar.toString();
                    if (isPasswordField(text)) {
                        return child;
                    }
                }
                if(descchar!=null) {
                    String desc = descchar.toString();
                    if(isPasswordField(desc)) {
                        return child;
                    }
                }

                AccessibilityNodeInfo field =  getUserNameField(child);
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

    private AccessibilityNodeInfo getListItemNodeInfo(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo current = source;
        while (true) {
            AccessibilityNodeInfo parent = current.getParent();
            if (parent == null) {
                return null;
            }
            if (TASK_LIST_VIEW_CLASS_NAME.equals(parent.getClassName())) {
                return current;
            }
            // NOTE: Recycle the infos.
            AccessibilityNodeInfo oldCurrent = current;
            current = parent;
            oldCurrent.recycle();
        }
    }

    @Override
    public void onInterrupt() {
        Log.v(TAG, "onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.v(TAG, "onServiceConnected");
        if(dbh==null)
            dbh=new DBHelper(this);
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    public void loginSavePasswordTapped() {

        Log.v(TAG, "came here");
        loginShouldSave=true;
    }
    public void loginLaterPasswordTapped() {

    }
    public void loginNeverPasswordTapped() {

    }

}