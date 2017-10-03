package cs.util;

/**
 * @author lyozniy.sergey on 28 Sep 2017.
 */
public class Path {
    public static class Web {
        public static String HOME = "/";
        public static String GET_REC_BY_USER = "/recUser";
        public static String GET_FACE_RECOGNIZE = "/face";
        public static String UPLOAD_CATALOG = "/upload_catalog";
        public static String UPLOAD_USAGE = "/upload_usage";

        public static class Headers {
            public static String CATALOG = "catalogDisplayName";
            public static String USAGE = "usageDisplayName";
        }
    }

    public static class Templates {
    }

    public static class Database {
    }

}
