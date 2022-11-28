package com.example;

import lombok.Data;
import org.hibernate.envers.RevisionType;

@Data
public class UserRevisionDTO {

    private User user;
    private UserRevEntity userRevEntity;
    private RevisionType revisionType;

}
