package com.lv.api;

import com.lv.api.storage.model.Account;
import com.lv.api.storage.model.Group;
import com.lv.api.storage.model.Permission;
import com.lv.api.storage.repository.AccountRepository;
import com.lv.api.storage.repository.GroupRepository;
import com.lv.api.storage.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
@Slf4j
public class Application {

    @Autowired
    AccountRepository qrCodeStorageService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PermissionRepository permissionRepository;

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private void createAdminUserIfNotExist(){
        Account account = qrCodeStorageService.findAccountByUsername("admin");
        if(account == null){
            List<Permission> defaultPermission = addPermission();
            Group group = initGroupDefault(defaultPermission);



            account = new Account();
            account.setUsername("admin");
            account.setPassword(passwordEncoder.encode("admin"));
            account.setStatus(1);
            account.setKind(1);
            account.setFullName("Root account");
            account.setGroup(group);
            account.setIsSuperAdmin(true);
            qrCodeStorageService.save(account);
        }

    }

    private List<Permission> addPermission(){
        List<Permission> results = new ArrayList<>();
        //ACCOUNT
        results.add(Permission.builder().action("/v1/account/create_admin").description("Create admin").name("Create admin").nameGroup("Account").showMenu(false).build());
        results.add(Permission.builder().action("/v1/account/delete").description("Delete admin").name("Delete admin").nameGroup("Account").showMenu(false).build());
//        results.add(Permission.builder().action("/v1/account/forget_password").description("Forget Password").name("Forget Password").nameGroup("Account").showMenu(false).build());
        results.add(Permission.builder().action("/v1/account/get").description("Get account").name("Get account").nameGroup("Account").showMenu(false).build());
        results.add(Permission.builder().action("/v1/account/list").description("List account").name("List account").nameGroup("Account").showMenu(false).build());
//        results.add(Permission.builder().action("/v1/account/login").description("Login").name("Login").nameGroup("Account").showMenu(false).build());
        results.add(Permission.builder().action("/v1/account/logout").description("Logout").name("Logout").nameGroup("Account").showMenu(false).build());
        results.add(Permission.builder().action("/v1/account/profile").description("Get profile").name("Get profile").nameGroup("Account").showMenu(false).build());
        results.add(Permission.builder().action("/v1/account/request_forget_password").description("Request forget password").name("Request forget password").nameGroup("Account").showMenu(false).build());
        results.add(Permission.builder().action("/v1/account/update_admin").description("Update admin").name("Update admin").nameGroup("Account").showMenu(false).build());
        results.add(Permission.builder().action("/v1/account/update_profile").description("Update profile user").name("Update profile user").nameGroup("Account").showMenu(false).build());
        results.add(Permission.builder().action("/v1/account/verify_account").description("Verify account").name("Verify account").nameGroup("Account").showMenu(false).build());
        //CATEGORY
        results.add(Permission.builder().action("/v1/category/auto-complete").description("Category auto complete").name("Category auto complete").nameGroup("Category").showMenu(false).build());
        results.add(Permission.builder().action("/v1/category/create").description("Create category").name("Create category").nameGroup("Category").showMenu(false).build());
        results.add(Permission.builder().action("/v1/category/delete").description("Delete category").name("Delete category").nameGroup("Category").showMenu(false).build());
        results.add(Permission.builder().action("/v1/category/get").description("Get category by id").name("Get category").nameGroup("Category").showMenu(false).build());
        results.add(Permission.builder().action("/v1/category/list").description("Get list category").name("Get list category").nameGroup("Category").showMenu(false).build());
        results.add(Permission.builder().action("/v1/category/update").description("Update category").name("Update category").nameGroup("Category").showMenu(false).build());
        //FILE
        results.add(Permission.builder().action("/v1/file/download").description("Download file").name("Download file").nameGroup("File").showMenu(false).build());
        results.add(Permission.builder().action("/v1/file/upload").description("Upload file").name("Upload file").nameGroup("File").showMenu(false).build());
        //GROUP
        results.add(Permission.builder().action("/v1/group/create").description("Create group").name("Create group").nameGroup("Group").showMenu(false).build());
        results.add(Permission.builder().action("/v1/group/delete").description("Delete group").name("Delete group").nameGroup("Group").showMenu(false).build());
        results.add(Permission.builder().action("/v1/group/get").description("Get group by id").name("Get group").nameGroup("Group").showMenu(false).build());
        results.add(Permission.builder().action("/v1/group/list").description("Get list group").name("List group").nameGroup("List group").showMenu(false).build());
        results.add(Permission.builder().action("/v1/group/list_combobox").description("Get list group combobox").name("List group combobox").nameGroup("Group").showMenu(false).build());
        results.add(Permission.builder().action("/v1/group/update").description("Update group").name("Update group").nameGroup("Group").showMenu(false).build());
        //NEWS
        results.add(Permission.builder().action("/v1/news/create").description("Create news").name("Create news").nameGroup("News").showMenu(false).build());
        results.add(Permission.builder().action("/v1/news/delete").description("Delete news").name("Delete news").nameGroup("News").showMenu(false).build());
        results.add(Permission.builder().action("/v1/news/get").description("Get news by id").name("Get news by id").nameGroup("News").showMenu(false).build());
        results.add(Permission.builder().action("/v1/news/list").description("Get list news").name("Get list news").nameGroup("News").showMenu(false).build());
        results.add(Permission.builder().action("/v1/news/update").description("Update news").name("Update news").nameGroup("News").showMenu(false).build());
        //PERMISSION
        results.add(Permission.builder().action("/v1/permission/create").description("Create permission").name("Create permission").nameGroup("Permission").showMenu(false).build());
        results.add(Permission.builder().action("/v1/permission/list").description("List permission").name("List permission").nameGroup("Permission").showMenu(false).build());
        //LOCATION
        results.add(Permission.builder().action("/v1/locations/list").description("Chill list location").name("Chill list location").nameGroup("Locations").showMenu(false).build());
        results.add(Permission.builder().action("/v1/locations/get").description("Get location").name("Get location").nameGroup("Locations").showMenu(false).build());
        results.add(Permission.builder().action("/v1/locations/auto-complete").description("Auto complete location").name("Auto complete location").nameGroup("Locations").showMenu(false).build());
        results.add(Permission.builder().action("/v1/locations/create").description("Create location").name("Create location").nameGroup("Locations").showMenu(false).build());
        results.add(Permission.builder().action("/v1/locations/update").description("Update location").name("Update location").nameGroup("Locations").showMenu(false).build());
        results.add(Permission.builder().action("/v1/locations/delete").description("Delete location").name("Delete location").nameGroup("Locations").showMenu(false).build());
        //RANK
        results.add(Permission.builder().action("/v1/ranks/list").description("Get list rank").name("Get list rank").nameGroup("Ranks").showMenu(false).build());
        results.add(Permission.builder().action("/v1/ranks/auto-complete").description("Auto complete rank").name("Auto complete rank").nameGroup("Ranks").showMenu(false).build());
        results.add(Permission.builder().action("/v1/ranks/get").description("Get rank").name("Get location").nameGroup("Ranks").showMenu(false).build());
        results.add(Permission.builder().action("/v1/ranks/create").description("Create rank").name("Create rank").nameGroup("Ranks").showMenu(false).build());
        results.add(Permission.builder().action("/v1/ranks/update").description("Update rank").name("Update rank").nameGroup("Ranks").showMenu(false).build());
        results.add(Permission.builder().action("/v1/ranks/delete").description("Delete rank").name("Delete rank").nameGroup("Ranks").showMenu(false).build());


        return permissionRepository.saveAll(results);
    }

    private Group initGroupDefault(List<Permission> defaultPermission){
        Group superAdminGroup = new Group();
        superAdminGroup.setKind(1);
        superAdminGroup.setName("ROLE SUPPER ADMIN");
        superAdminGroup.setId(1L);
        superAdminGroup.setPermissions(defaultPermission);
        return groupRepository.save(superAdminGroup);

    }

    @PostConstruct
    public void initialize() {
        createAdminUserIfNotExist();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
