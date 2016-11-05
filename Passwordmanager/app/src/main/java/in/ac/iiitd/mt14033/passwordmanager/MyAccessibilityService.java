package in.ac.iiitd.mt14033.passwordmanager;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyAccessibilityService extends AccessibilityService {

    private static final String TASK_LIST_VIEW_CLASS_NAME =
            "in.ac.iiitd.mt14033.passwordmanager.MyAccessibilityService";
    private String requesting_package=null;

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
            AccessibilityNodeInfo parent = source.getParent();
            /**
             * Detect popup from our app and respective button pushes within it.
             */

            if(event.getPackageName().equals(getString(R.string.selfpackagename)) &&
                    event.getClassName().equals("android.widget.Button") &&
                    event.getEventType()==AccessibilityEvent.TYPE_VIEW_CLICKED) {
                Log.v(getString(R.string.VTAG), event.getPackageName()+" "+getEventType(event)+" "+event.getClassName());
                if(source.getParent()==null || source.getParent().getContentDescription()==null)
                    return;
                if(source.getParent().getContentDescription().equals("matchingpasswordpopup")) {
                    if(source.getText().equals(getString(R.string.add_login_button_text))) {
                        Log.v(getString(R.string.VTAG), "add_login_button_text");
                        Intent intent = new Intent(this, AddLoginActivity.class);
                        intent.putExtra(getString(R.string.matching_login_package_name), requesting_package);
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
            else if(event.isPassword() && !event.getPackageName().equals(getString(R.string.selfpackagename))) {
                Log.v(getString(R.string.VTAG), "Event is password");

                Intent intent = new Intent(this, MatchingLoginsDialogActivity.class);
                requesting_package = event.getPackageName().toString();
                intent.putExtra(getString(R.string.matching_login_package_name), requesting_package);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

//                DialogMatchingLogins dialogMatchingLogins = new DialogMatchingLogins();
//                dialogMatchingLogins.show(getSupportFragmentManager(), "matching_logins");
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
        return pm != null;

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

    @Override
    public void onInterrupt() {
        Log.v(getString(R.string.VTAG), "onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.v(getString(R.string.VTAG), "onServiceConnected");
        if(dbh==null)
            dbh=new DBHelper(this);
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        setServiceInfo(info);
    }


}