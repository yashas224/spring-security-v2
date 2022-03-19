package com.example.demo1.auth;

import com.example.demo1.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository("fake")
@Primary
public class FakeApplicationUserDaoIml implements ApplicationUserDao {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<ApplicationUser> selectApplicationByUserName(String name) {
        return getUser().stream().filter(user -> user.getUsername().equals(name)).findFirst();
    }


    private List<ApplicationUser> getUser() {
        List<ApplicationUser> list = Arrays.asList(new ApplicationUser("annasmith", passwordEncoder.encode("samaga"),
                        ApplicationUserRole.STUDENT.getGrantedAuthority(), true, true, true, true),

                new ApplicationUser("linda", passwordEncoder.encode("samaga"),
                        ApplicationUserRole.ADMIN.getGrantedAuthority(), true, true, true, true),

                new ApplicationUser("tom", passwordEncoder.encode("samaga"),
                        ApplicationUserRole.ADMINTRAINEE.getGrantedAuthority(), true, true, true, true)
        );
        return list;
    }

}

