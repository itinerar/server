package com.itinerar.controller

import com.itinerar.controller.LoginResource
import com.itinerar.repositories.UserRepository
import com.itinerar.service.JsonSerializerService
import spock.lang.Specification
import spock.lang.Subject

import javax.persistence.EntityNotFoundException

import static com.itinerar.test_data.UserUtilData.aUser

class LoginResourceSpec extends Specification {

    def userRepository = Mock(UserRepository)
    def myJsonSerialize = Mock(JsonSerializerService)

    @Subject
    def resource = new LoginResource(userRepository, myJsonSerialize)


    def "retrieve user"() {
        given:
        def user = 'user@gmail.com'
        def password = '1234'

        when:
        resource.retriveUserLogin(user, password)

        then:
        1 * userRepository.findUserByEmail(user) >> aUser()
        1 * myJsonSerialize.prepareUserForJson(aUser()) >> aUser()
        0 * _
    }

    def "retrieve user with error"() {
        given:
        def user = 'user@gmail.com'
        def password = '1234'

        when:
        resource.retriveUserLogin(user, password)

        then:
        1 * userRepository.findUserByEmail(user) >> null
        thrown EntityNotFoundException
        0 * _
    }
}
