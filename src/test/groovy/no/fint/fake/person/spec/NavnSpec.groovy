package no.fint.fake.person.spec

import no.fint.fake.person.Navn
import spock.lang.Specification

class NavnSpec extends Specification {

    def "Can parse names properly"() {
        given:
        def names = new Navn()

        when:
        names.init()

        then:
        !names.etternavn.isEmpty()
        names.etternavn[0] == 'Hansen'

        then:
        !names.guttenavn.isEmpty()
        names.guttenavn[0] == 'Jakob'

        then:
        !names.jentenavn.isEmpty()
        names.jentenavn[0] == 'Sofie'
    }
}
