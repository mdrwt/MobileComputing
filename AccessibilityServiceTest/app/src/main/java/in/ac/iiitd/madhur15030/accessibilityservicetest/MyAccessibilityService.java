package in.ac.iiitd.madhur15030.accessibilityservicetest;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService extends AccessibilityService {

    static final String TAG = "MyAccessibilityService";
    private static final String TASK_LIST_VIEW_CLASS_NAME =
            "in.ac.iiitd.mt14033.passwordmanager.MyAccessibilityService";
    private AccessibilityEvent oldAccessibilityEvent;
    private AccessibilityNodeInfo loginUsernameField;

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


            if(isLoginPage(source)) {
                Log.v(TAG, "This is a login page");
                if((event.getEventType()==AccessibilityEvent.TYPE_VIEW_FOCUSED
                        || event.getEventType()==AccessibilityEvent.TYPE_VIEW_CLICKED
                        || event.getEventType()==AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) &&
                        event.getEventType()!=AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
                    Log.v(TAG, getEventType(event));
                   if(event.isPassword()) {
                       Log.v(TAG, "is password field");
                       AccessibilityNodeInfo usernameField = getUserNameField(source);
                       if(usernameField!=null) {
                           Log.v(TAG,"username text is "+usernameField.getText().toString());
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


            }
            if (parent == null) {
                return;
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
        AccessibilityNodeInfo parent = source.getParent();
        int nchild = parent.getChildCount();
        for (int i=0; i<nchild; ++i) {
            AccessibilityNodeInfo child = parent.getChild(i);
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
    public boolean isLoginPage(AccessibilityNodeInfo source) {


        AccessibilityNodeInfo parent = source.getParent();
        if(parent==null)
            return false;
        int nchild = parent.getChildCount();
        for (int i=0; i<nchild; ++i) {
            AccessibilityNodeInfo child = parent.getChild(i);
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
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

}