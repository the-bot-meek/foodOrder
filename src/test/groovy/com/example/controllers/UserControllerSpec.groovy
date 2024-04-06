package com.example.controllers

import spock.lang.Specification

import java.security.Principal

class UserControllerSpec extends Specification {
    def "GetUserInfo"() {
        given:
        Principal principal = Mock(Principal)
        principal.getName() >> "name"

        UserController userController = new UserController()
        when:
        Map<String,?> userInfo = userController.getUserInfo(principal)

        then:
        userInfo.keySet() == ["name"].toSet()
        userInfo.get("name") == "name"
    }
}
