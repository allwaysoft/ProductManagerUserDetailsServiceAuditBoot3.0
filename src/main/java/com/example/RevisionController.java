package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RevisionController {

    @Autowired
    ProductRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("/revision/user")

    public String getAll(Model model, @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {

        List<UserRevisionDTO> listUserRevisionDTOs = new ArrayList();
        AuditReader reader = AuditReaderFactory.get(entityManager);

        int numberOfElements;
        Long totalItems;
        Long totalPages;
        AuditQuery queryCount;
        AuditQuery query;
        if (keyword == null || keyword.isBlank()) {
            System.out.println("!!keyword=null");
            queryCount = reader.createQuery()
                    .forRevisionsOfEntity(User.class, false, true)
                    .addProjection(AuditEntity.revisionNumber().count());
            totalItems = (Long) queryCount.getSingleResult();
            System.out.println("!!totalItems=" + totalItems);

            if (totalItems % size == 0) {
                totalPages = totalItems / size;
            } else {
                totalPages = totalItems / size + 1;
            }

            System.out.println("!!totalPages=" + totalPages);
            query = reader.createQuery()
                    .forRevisionsOfEntity(User.class, false, true)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .addOrder(AuditEntity.revisionNumber().desc());

//        query.add(AuditEntity.revisionProperty("username").eq("admin"));
            //This return a list of array triplets of changes concerning the specified revision.
            // The array triplet contains the entity, entity revision information and at last the revision type.
            List<Object[]> revs = (List<Object[]>) query.getResultList();
            numberOfElements = revs.size();
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

        } else {
            System.out.println("!!keyword!=null");
            queryCount = reader.createQuery()
                    .forRevisionsOfEntity(User.class, false, true)
                    .add(AuditEntity.property("username").like(keyword))
                    .addProjection(AuditEntity.revisionNumber().count());
            totalItems = (Long) queryCount.getSingleResult();
            System.out.println("!!totalItems=" + totalItems);

            if (totalItems % size == 0) {
                totalPages = totalItems / size;
            } else {
                totalPages = totalItems / size + 1;
            }

            System.out.println("!!totalPages=" + totalPages);
            query = reader.createQuery()
                    .forRevisionsOfEntity(User.class, false, true)
                    .add(AuditEntity.property("username").like(keyword))
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .addOrder(AuditEntity.revisionNumber().desc());

//        query.add(AuditEntity.revisionProperty("username").eq("admin"));
            //This return a list of array triplets of changes concerning the specified revision.
            // The array triplet contains the entity, entity revision information and at last the revision type.
            List<Object[]> revs = (List<Object[]>) query.getResultList();
            numberOfElements = revs.size();
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

            model.addAttribute("keyword", keyword);
        }

        System.out.println();
        model.addAttribute("listUserRevisionDTOs", listUserRevisionDTOs);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("numberOfElements", numberOfElements);

        return "revision/list_user_revision";
    }

}
