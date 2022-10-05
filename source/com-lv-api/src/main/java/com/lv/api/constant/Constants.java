package com.lv.api.constant;


import com.lv.api.utils.ConfigurationService;

public class Constants {
    public static final String ROOT_DIRECTORY =  ConfigurationService.getInstance().getString("file.upload-dir","/tmp/upload");

    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_CUSTOMER = 2;
    public static final Integer USER_KIND_EMPLOYEE = 3;
    public static final Integer USER_KIND_COLLABORATOR = 4;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    public static final Integer GROUP_KIND_SUPER_ADMIN = 1;
    public static final Integer GROUP_KIND_CUSTOMER = 2;
    public static final Integer GROUP_KIND_EMPLOYEE = 3;
    public static final Integer GROUP_KIND_COLLABORATOR = 4;

    public static final Integer MAX_ATTEMPT_FORGET_PWD = 5;
    public static final Integer MAX_TIME_FORGET_PWD = 5 * 60 * 1000; //5 minutes
    public static final Integer MAX_ATTEMPT_LOGIN = 5;
    public static final Integer MAX_TIME_VERIFY_ACCOUNT = 5 * 60 * 1000; //5 minutes

    public static final int CATEGORY_KIND_NEWS = 1;
    public static final int CATEGORY_KIND_JOB= 2;
    public static final int CATEGORY_KIND_DEPARTMENT = 3;


    public static final Integer GENDER_MALE = 1;
    public static final Integer GENDER_FEMALE = 2;
    public static final Integer GENDER_OTHER = 3;

    public static final int LOCATION_KIND_PROVINCE = 1;
    public static final int LOCATION_KIND_DISTRICT = 2;
    public static final int LOCATION_KIND_WARD = 3;

    public static final int APP_CUSTOMER = 1;
    public static final int APP_CMS = 2;

    private Constants(){
        throw new IllegalStateException("Utility class");
    }

}
