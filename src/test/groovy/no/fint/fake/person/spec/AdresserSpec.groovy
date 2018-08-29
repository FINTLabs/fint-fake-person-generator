package no.fint.fake.person.spec

import no.fint.fake.person.Adresser
import spock.lang.Specification

class AdresserSpec extends Specification {
    Adresser generator

    void setup() {
        generator = new Adresser()
        generator.init()
    }

    def "Generates valid AdresseResource"() {
        when:
        def a = generator.adresser[0]
        println(a)

        then:
        a.poststed == 'OSLO'
        a.postnummer == '0876'
        a.adresselinje[0] == 'Nils Bays vei 19 B'
        a.land.size() == 1
    }
}
