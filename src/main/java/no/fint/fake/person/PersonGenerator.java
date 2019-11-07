package no.fint.fake.person;

import no.fint.model.felles.kodeverk.iso.Kjonn;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Kontaktinformasjon;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PersonGenerator {

    @Autowired
    private Navn navn;

    @Autowired
    private Adresser adresser;

    private Set<String> fodselsnummer = new ConcurrentSkipListSet<>();

    public Personnavn generatePersonnavn(boolean female) {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        Personnavn result = new Personnavn();
        result.setEtternavn(sample(navn.getEtternavn(), r));
        if (r.nextBoolean())
            result.setMellomnavn(sample(navn.getEtternavn(), r));
        List<String> collection = female ? navn.getJentenavn() : navn.getGuttenavn();
        String fornavn = sample(collection, r);
        if (r.nextBoolean()) {
            int i = r.nextInt(1,3);
            while (i-- > 0)
                fornavn = fornavn + " " + sample(collection, r);
        }
        result.setFornavn(fornavn);
        return result;
    }

    private <T> T sample(List<T> collection, ThreadLocalRandom random) {
        return collection.get(random.nextInt(collection.size()));
    }

    public int getSequence(boolean female, ThreadLocalRandom random) {
        int sequence = random.nextInt(500, 1000);
        if (female) {
            if (sequence % 2 > 0)
                return sequence + 1;
            return sequence;
        } else {
            if (sequence % 2 == 0)
                return sequence + 1;
            return sequence;
        }
    }

    public PersonResource generatePerson() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        PersonResource result = new PersonResource();
        boolean female = r.nextBoolean();
        result.setNavn(generatePersonnavn(female));
        result.addKjonn(Link.with(Kjonn.class, "systemid", female ? "2" : "1"));
        do {
            LocalDate birthDate = LocalDate.of(r.nextInt(2000, 2010), 01, 01).plusDays(r.nextInt(366));
            result.setFodselsdato(Date.from(birthDate.atStartOfDay(ZoneId.of("UTC")).toInstant()));
            result.setFodselsnummer(identifikator(String.format("%Td%<Tm%<Ty%d%d", birthDate, getSequence(female, r), r.nextInt(100))));
        } while (!fodselsnummer.add(result.getFodselsnummer().getIdentifikatorverdi()));
        result.setPostadresse(sample(adresser.getAdresser(), r));
        Kontaktinformasjon kontaktinformasjon = new Kontaktinformasjon();
        kontaktinformasjon.setMobiltelefonnummer(String.format("%s%07d", r.nextBoolean() ? "4" : "9", r.nextInt(0, 10000000)));
        kontaktinformasjon.setEpostadresse(String.format("%s%d@gmail.com", StringUtils.stripAccents(StringUtils.substringBefore(result.getNavn().getFornavn().toLowerCase(), " ")), r.nextInt(100)));
        result.setKontaktinformasjon(kontaktinformasjon);
        return result;
    }

    public static String getPersonnavnAsString(Personnavn navn) {
        if (navn == null) return null;
        String result = "";
        if (!org.springframework.util.StringUtils.isEmpty(navn.getEtternavn()))
            result += navn.getEtternavn();
        if (!org.springframework.util.StringUtils.isEmpty(navn.getFornavn())) {
            if (!result.isEmpty())
                result += ", ";
            result += navn.getFornavn();
        }
        if (!org.springframework.util.StringUtils.isEmpty(navn.getMellomnavn()))
            result += " " + navn.getMellomnavn();
        return result;
    }

    public Identifikator identifikator(String indentifikatorverdi) {
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(indentifikatorverdi);
        return identifikator;
    }
}
