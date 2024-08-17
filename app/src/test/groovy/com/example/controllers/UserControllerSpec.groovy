package com.example.controllers

import io.micronaut.security.authentication.Authentication
import spock.lang.Specification

class UserControllerSpec extends Specification {
    def "GetUserInfo"() {
        given:
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> "name"

        UserController userController = new UserController()
        when:
        Map<String,?> userInfo = userController.getUserInfo(authentication)

        then:
        userInfo.keySet() == ["name"].toSet()
        userInfo.get("name") == "name"
    }
}
