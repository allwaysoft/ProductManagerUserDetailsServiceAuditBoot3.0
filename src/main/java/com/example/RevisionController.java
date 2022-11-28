package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RevisionController {

    @Autowired
    ProductRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("/revision/user")
    public String indexPermissions(Model model) {
        List<UserRevisionDTO> listUserRevisionDTOs = new ArrayList();

        AuditReader reader = AuditReaderFactory.get(entityManager);
        AuditQuery query = reader.createQuery()
                .forRevisionsOfEntity(User.class, false, true);

//        query.add(AuditEntity.revisionProperty("username").eq("admin"));
        //This return a list of array triplets of changes concerning the specified revision.
        // The array triplet contains the entity, entity revision information and at last the revision type.
        List<Object[]> revs = (List<Object[]>) query.getResultList();
        for (Object[] rev : revs) {
            UserRevisionDTO userRevisionDTO = new UserRevisionDTO();
            userRevisionDTO.setUser((User) rev[0]);
            userRevisionDTO.setUserRevEntity((UserRevEntity) rev[1]);
            userRevisionDTO.setRevisionType((RevisionType) rev[2]);
            listUserRevisionDTOs.add(userRevisionDTO);
            System.out.println(((User) rev[0]).getUsername());
            User user = (User) rev[0];
            UserRevEntity userRevEntity = (UserRevEntity) rev[1];
            RevisionType type = (RevisionType) rev[2];
            System.out.println(user);
            System.out.println(userRevEntity);
            System.out.println(userRevEntity.getId());
            System.out.println(userRevEntity.getRevisionDate());
            System.out.println(userRevEntity.getUsername());
            System.out.println(type.name());
        }

        System.out.println();
        model.addAttribute("listUserRevisionDTOs", listUserRevisionDTOs);

        return "revision/list_user_revision";
    }

}
