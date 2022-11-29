package com.example;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    HistoryRepository historyRepository;

    public static final int MAX_FAILED_ATTEMPTS = 3;

//    private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours
    private static final long LOCK_TIME_DURATION = 5 * 60 * 1000; // 5 mins

//    public void increaseFailedAttempts(User user) {
//        int newFailAttempts = user.getFailedAttempt() + 1;
//        userRepository.updateFailedAttempts(newFailAttempts, user.getUsername());
//    }
//
    public void increaseFailedAttempts(User user) {
        user.setFailedAttempt(user.getFailedAttempt() + 1);
        userRepository.save(user);
    }

    public void resetFailedAttempts(User user) {
        user.setFailedAttempt(0);
        userRepository.save(user);
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());

        userRepository.save(user);
    }

    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);

            userRepository.save(user);

            return true;
        }

        return false;
    }

}
