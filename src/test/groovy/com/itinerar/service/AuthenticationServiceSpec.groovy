package com.itinerar.service

import com.itinerar.entity.User
import com.itinerar.repositories.UserRepository
import spock.lang.Specification
import spock.lang.Subject

import static com.itinerar.test_data.UserUtilData.aUser

class AuthenticationServiceSpec extends Specification {

    def userID = 2L
    def userRepository = Mock(UserRepository)

    @Subject
    def service = new AuthenticationService(userRepository)

    def "get user"() {
        when:
        service.getUser()

        then:
        1 * userRepository.findUserById(userID) >> aUser()
        0 * _
    }
}
