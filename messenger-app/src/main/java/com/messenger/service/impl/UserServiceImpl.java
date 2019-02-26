package com.messenger.service.impl;

import com.messenger.exception.UserNotFoundException;
import com.messenger.model.User;
import com.messenger.repository.UserRepository;
import com.messenger.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository repository) {
        this.userRepository = repository;
    }

    public User insert(final User user) throws IllegalArgumentException {
        checkIfUserIsValid(user);
        return userRepository.insert(user);
    }

    public User update(final String id, final User newUser) throws IllegalArgumentException, UserNotFoundException {
        checkIfUserIsValid(newUser);

        User updateUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        updateUser.setFirstName(newUser.getFirstName());
        updateUser.setLastName(newUser.getLastName());
        updateUser.setEmail(newUser.getEmail());
        updateUser.setPassword(newUser.getPassword());
        updateUser.setUserRole(newUser.getUserRole());

        userRepository.save(updateUser);
        return updateUser;
    }

    public void delete(final String id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }

    public User find(final String id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    private void checkIfUserIsValid(final User user) throws IllegalArgumentException {
        StringBuilder errors = new StringBuilder();

        checkFieldEmpty(errors, "First name", user.getFirstName());
        checkFieldEmpty(errors, "Last name", user.getLastName());
        checkFieldEmpty(errors, "Email", user.getEmail());
        checkFieldEmpty(errors, "Password", user.getPassword());

        if (!StringUtils.isEmpty(errors.toString())) {
            throw new IllegalArgumentException(errors.toString());
        }
    }

    private void checkFieldEmpty(final StringBuilder builder, final String field, final String value) {
        if (StringUtils.isEmpty(value)) {
            builder.append(field).append(" cannot be empty. ");
        }
    }
}
