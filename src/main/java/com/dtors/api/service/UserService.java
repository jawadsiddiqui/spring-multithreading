package com.dtors.api.service;


import com.dtors.api.entity.User;
import com.dtors.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    Object target;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Async
    public CompletableFuture<List<User>> saveUser(MultipartFile multipartFile) throws Exception {
        long startTime = System.currentTimeMillis();
        List<User> users = parseCSVFile(multipartFile);
        logger.info("saving list of size {}", users.size() + "" + Thread.currentThread().getName());
        users = userRepository.saveAll(users);

        long endTime = System.currentTimeMillis();
        logger.info("Total time {}", endTime - startTime);
        return CompletableFuture.completedFuture(users);

    }


    @Async
    public CompletableFuture<List<User>> findAllUsers(){
        logger.info("get list of user {}", Thread.currentThread().getName());
        List<User> user = userRepository.findAll();
        return CompletableFuture.completedFuture(user);
    }

    private List<User> parseCSVFile(final MultipartFile file) throws Exception {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return br.lines()
                    .map(line -> line.split(","))
                    .map(data -> {
                        User user = new User();
                        user.setName(data[0]);
                        user.setEmail(data[1]);
                        user.setGender(data[2]);
                        return user;
                    })
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            logger.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }
}
