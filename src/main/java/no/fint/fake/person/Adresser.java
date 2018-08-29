package no.fint.fake.person;

import lombok.Getter;
import no.fint.model.felles.kodeverk.iso.Landkode;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.kompleksedatatyper.AdresseResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class Adresser {

    @Getter
    List<AdresseResource> adresser;

    @PostConstruct
    public void init() throws IOException {
        Link no = Link.with(Landkode.class, "systemid", "NO");
        try (BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/adresser.txt")))) {
            adresser = r.lines().skip(1).map(l -> l.split(";")).map(s -> {
                AdresseResource a = new AdresseResource();
                a.setPoststed(s[28]);
                a.setPostnummer(s[27]);
                String gate = s[4];
                if (!StringUtils.isEmpty(s[6]))
                    gate = gate + " " + s[6];
                if (!StringUtils.isEmpty(s[7]))
                    gate = gate + " " + s[7];
                a.setAdresselinje(Collections.singletonList(gate));
                a.addLand(no);
                return a;
            }).collect(Collectors.toList());
        }
    }
}
