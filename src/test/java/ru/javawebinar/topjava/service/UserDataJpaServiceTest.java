package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = {"datajpa"})
public class UserDataJpaServiceTest extends UserServiceTest {
}
