package com.messenger.service;

import com.messenger.exception.UserNotFoundException;
import com.messenger.model.User;

import java.util.List;

public interface IUserService {

    void insert(final User user) throws IllegalArgumentException;

    User update(final String id, final User user) throws IllegalArgumentException, UserNotFoundException;

    void delete(final String id) throws UserNotFoundException;

    User find(final String id) throws UserNotFoundException;

    List<User> findAll();

}
