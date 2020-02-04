package no.fint.fake.person;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class Navn {

    @Getter
    private List<String> guttenavn;

    @Getter
    private List<String> jentenavn;

    @Getter
    private List<String> etternavn;

    @PostConstruct
    public void init() throws IOException {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/etternavn.txt"), StandardCharsets.UTF_8))) {
            etternavn = r.lines().map(l -> l.split(";")[0]).collect(Collectors.toList());
        }
        try (BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/guttenavn.txt"), StandardCharsets.UTF_8))) {
            guttenavn = r.lines().map(l -> l.split(";")[0]).collect(Collectors.toList());
        }
        try (BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/jentenavn.txt"), StandardCharsets.UTF_8))) {
            jentenavn = r.lines().map(l -> l.split(";")[0]).collect(Collectors.toList());
        }

        log.info("Lastet {} etternavn, {} guttenavn og {} jentenavn.", etternavn.size(), guttenavn.size(), jentenavn.size());
    }
}
