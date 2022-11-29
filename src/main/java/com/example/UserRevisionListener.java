package com.example;

import java.util.Optional;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        String username;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            username = Optional.of(principal.getUsername()).orElse(null);
        } else {
            username = "unauthenticated";
        }
        UserRevEntity exampleRevEntity = (UserRevEntity) revisionEntity;

        exampleRevEntity.setUsername(username);
    }
}
