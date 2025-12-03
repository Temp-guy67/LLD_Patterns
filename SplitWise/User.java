package SplitWise;

import java.lang.reflect.Array;
import java.util.Hashtable;

public class User implements Observer {
    private String userId;
    private String userName;
    private Hashtable<String, Group> activeSettlement;
    private Hashtable<String, Group> settlementHistory;

    public User(String userId, String userName) {

    }

    public void duePaymentNotification(String msg) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'duePaymentNotification'");
    }

}
