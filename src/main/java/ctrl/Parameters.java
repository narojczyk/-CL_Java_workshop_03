package ctrl;

import model.UserDao;

public class Parameters {
    public static final int DAO_UPDATE_FAILED = 0;
    public static final int DAO_UPDATE_WRONG_INDEX_PASSED = -1;
    public static final int DAO_CREATE_FAILED_SQL_CONSTRAINT_VIOLATION = -99;
    public static final int DAO_CREATE_FAILED = -666;
    public static final int DAO_GETRECORDSCOUNT_FAILED = 0;
    public static final String SQL_DATABASE_NAME = "workshop3";
    public static final String SQL_TABLE_NAME = "users";
    public static final int SQL_COL_NAME_LENGTH = UserDao.getSize_name();
    public static final String SERVLET_CONTEXT = "/adminpanel";
}