package com.itinerar.test_data

import com.itinerar.entity.User

import java.text.SimpleDateFormat

class UserUtilData {
    static aUser(Map overrides = [:]) {
        Map values = [
                id           : '1',
                name         : '1',
                password     : '1',
                email        : 'user@gmail.com',
                address      : '1',
                comments     : [],
                birthDate    : (new SimpleDateFormat("yyyy-MM-dd")).parse("2019-04-25"),
                journeys     : [],
                activeJourney: null //aJourney()
        ]
        values << overrides

        new User(values)
    }
}
