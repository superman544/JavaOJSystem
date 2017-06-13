package cn.superman.system.sandbox.constant;

public final class CommunicationSignal {

    public final static class RequestSignal {
        public final static String CLOSE_SANDBOX = "CLOSE_SANDBOX";
        public final static String SANDBOX_STATUS = "SANDBOX_STATUS";
        public final static String IS_BUSY = "IS_BUSY";
        public final static String REQUSET_JUDGED_PROBLEM = "REQUSET_JUDGED_PROBLEM";
    }

    public final static class ResponseSignal {
        public final static String OK = "OK";
        public final static String NO = "NO";
        public final static String YES = "YES";
        public final static String IDLE = "IDLE";
        public final static String ERROR = "ERROR";
    }

}
