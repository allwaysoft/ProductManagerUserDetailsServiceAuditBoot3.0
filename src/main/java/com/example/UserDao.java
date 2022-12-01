package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
class UserDao {

    @PersistenceContext
    EntityManager em;

    @Transactional
    List<User> findUsersByUsername(String username) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        Predicate usernamePredicate = cb.equal(user.get("username"), username);
        cq.where(usernamePredicate);
        TypedQuery<User> query = em.createQuery(cq);

        CriteriaUpdate<User> criteriaUpdate = cb.createCriteriaUpdate(User.class);
        Root<User> root = criteriaUpdate.from(User.class);
        criteriaUpdate.set("name", "adminnew");
        criteriaUpdate.where(cb.equal(root.get("username"), username));
//        em.getTransaction().begin();
        em.createQuery(criteriaUpdate).executeUpdate();
//        em.getTransaction().commit();

// get entity from database
        User user2 = em.find(User.class, 1);

        // do changes
        user2.setName("Ram");

        // update the student object
        em.persist(user2);
        return query.getResultList();
    }

}
