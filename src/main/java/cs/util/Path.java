package cs.util;

/**
 * @author lyozniy.sergey on 28 Sep 2017.
 */
public class Path {
    public static class Web {
        public static String HOME = "/";
        public static String GET_REC_BY_USER = "/recUser";
        public static String GET_REC_BY_ITEM = "/recItem";
        public static String GET_FACE_DETECT = "/face";
        public static String UPLOAD_CATALOG = "/upload_catalog";
        public static String UPLOAD_CATALOG_FORM = "/c";
        public static String UPLOAD_USAGE = "/upload_usage";
        public static String UPLOAD_USAGE_FORM = "/u";
        public static String CREATE_MODEL = "/create_model";
        public static String UPDATE_MODEL = "/update_model";
        public static String CREATE_BUILD = "/create_build";
        public static String DELETE_BUILD = "/delete_build";
        public static String GET_BUILD = "/get_build";

        public static class Requests {
            public static String CATALOG_PARAM = "catalog";
            public static String USAGE_PARAM = "usage";
        }
        public static class Headers {
            public static String CATALOG_DISPLAY_NAME = "catalogDisplayName";
            public static String USAGE_DISPLAY_NAME = "usageDisplayName";
        }
    }

    public static class Templates {
        public static String WELCOME = "welcome.ftl";
    }

    public static class Database {
    }

}
