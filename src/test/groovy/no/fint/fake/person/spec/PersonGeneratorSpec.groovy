package no.fint.fake.person.spec

import no.fint.fake.person.Adresser
import no.fint.fake.person.Navn
import no.fint.fake.person.PersonGenerator
import spock.lang.Specification

import java.util.concurrent.ThreadLocalRandom

class PersonGeneratorSpec extends Specification {

    PersonGenerator generator

    void setup() {
        def navn = new Navn()
        navn.init()
        def adresser = new Adresser()
        adresser.init()
        generator = new PersonGenerator(navn: navn, adresser: adresser)
    }

    def "Can generate three distinct boys' names"() {
        when:
        def name1 = generator.generatePersonnavn(false)
        println(name1)

        then:
        name1

        when:
        def name2 = generator.generatePersonnavn(false)
        println(name2)

        then:
        name2
        name2 != name1

        when:
        def name3 = generator.generatePersonnavn(false)
        println(name3)

        then:
        name3
        name3 != name2
    }

    def "Generates valid sequence numbers for gender"() {
        given:
        def r = Mock(ThreadLocalRandom)

        when:
        def seq1 = generator.getSequence(true, r)
        def seq2 = generator.getSequence(false, r)
        def seq3 = generator.getSequence(true, r)
        def seq4 = generator.getSequence(false, r)

        then:
        seq1 == 600
        seq2 == 601
        seq3 == 602
        seq4 == 601
        4 * r.nextInt(500, 1000) >>> [ 600, 600, 601, 601 ]
    }

    def "Can generate complete Person object"() {
        when:
        def person = generator.generatePerson(2000, 2010)
        println(person)

        then:
        person
        person.navn
        person.fodselsdato
        person.fodselsnummer
        person.kjonn.size() == 1
        person.postadresse.poststed == 'OSLO'
        person.kontaktinformasjon.mobiltelefonnummer
    }
}
