package com.example;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        if (users != null) {
            List<UserDTO> userDTOs = new ArrayList<>();
            for (User user : users) {
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
                userDTO.setUsername(user.getUsername());
                userDTOs.add(userDTO);
            }
            return userDTOs;
        } else {
            return new ArrayList<>();
        }
    }

    public String provideCurrentAge(String date) {
        Period period = providePerodDate(date);
        String age = null;
        String year = null;
        String day = null;
        String month = null;
        if (period.getYears() != 0) {
            year = string(period.getYears()) + " years ";
        }
        if (period.getMonths() != 0) {
            month = string(period.getMonths()) + " months ";
        }
        if (period.getDays() != 0) {
            day = string(period.getDays()) + " days ";
        }
        age = year + month + day;
        age = age.replaceAll("null", "");
        return age;
    }

    public static Period providePerodDate(String date) {
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.parse(date);
        Period period = Period.between(birthday, today);
        return period;
    }

    public static String string(Integer integer) {
        return Integer.toString(integer);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void changePassword(User user, UserChangePasswordDTO userChangePasswordDTO) {
        user.setPassword(bCryptPasswordEncoder.encode(userChangePasswordDTO.getNewPass()));
        Date passwordChangedTime = new Date();

        user.setPasswordChangedTime(passwordChangedTime);
        userRepository.save(user);
    }

    public boolean isPasswordValid(String databasedPass, String givenPassword) {
        if (BCrypt.checkpw(databasedPass, givenPassword)) {
            return true;
        } else {
            return false;
        }
    }

    public void signUp(UserDTO userDTO) {

    }

    public User saveUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setHomepage(userDTO.getHomepage());
        System.out.println(userDTO.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setEnabled(userDTO.getEnabled());
        System.out.println("userDTO.getEnabled()=" + userDTO.getEnabled());
        System.out.println("userDTO.isAccountNonLocked()=" + userDTO.getAccountNonLocked());
        user.setAccountNonLocked(userDTO.getAccountNonLocked());
        Date passwordChangedTime = new Date();

        user.setPasswordChangedTime(passwordChangedTime);

        return userRepository.save(user);
    }

    public User get(Long id) {
        return userRepository.findById(id).get();
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    public void save(User user) {
        encodePassword(user);
        userRepository.save(user);
    }

    private void encodePassword(User user) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public List<History> listHistorys() {
        return historyRepository.findAll();
    }
}
