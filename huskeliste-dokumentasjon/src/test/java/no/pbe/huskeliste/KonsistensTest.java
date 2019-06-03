package no.pbe.huskeliste;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.assertj.core.api.JUnitSoftAssertions;
import org.assertj.core.api.PathAssert;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class KonsistensTest {
        @Rule
        public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

        private final List<Path> alleDokumentasjonsfiler = new ArrayList<>();

        private final Set<Path> besøkte = new HashSet<>();

        private final Parser parser = Parser.builder().build();

        private Path rotKatalog;

        @Before
        public void _before() throws URISyntaxException, IOException {
            final URL url = getClass().getResource("/.anker");
            assertThat(url)
                    .as("Filen .anker skal ligge plassert i roten av huskeliste-dokumentasjon sin classpath")
                    .isNotNull();
            rotKatalog = Paths.get(
                    url
                            .toURI()
            )
                    .resolve("../../../..")
                    .normalize();
            assertThat(rotKatalog).isDirectory();

            Files.walk(
                    rotKatalog.resolve("dokumentasjon")
            )
                    .filter(path -> path.toFile().isFile())
                    .forEach(alleDokumentasjonsfiler::add);
        }

        @Test
        public void skal_verifisere_at_alle_linker_fungerer_og_at_alle_filer_under_dokumentasjonskatalogen_er_linka_inn_i_dokumentasjonen() {
            sjekkSidenOgTraverser(
                    rotKatalog.resolve("README.md")
            );
            softly
                    .assertThat(
                            alleDokumentasjonsfiler
                                    .stream()
                                    .filter(erIkkeLinketInnIDokumentasjonen())
                                    .sorted()
                    )
                    .as("filer under dokumentasjonskatalogen som ikkje har nokon link som peikar til seg")
                    .isEmpty();
        }

        private Predicate<Path> erIkkeLinketInnIDokumentasjonen() {
            return ((Predicate<Path>) besøkte::contains).negate();
        }

        private void sjekkSidenOgTraverser(final Path gjeldendeFil, final Reader content) throws IOException {
            final Node document = parser.parseReader(content);
            document.accept(new AbstractVisitor() {
                @Override
                public void visit(final Link link) {
                    super.visit(link);
                    if (skalBesøke(link.getDestination())) {
                        sjekkPeker(gjeldendeFil, fjernAnkerreferanseFraLink(link.getDestination()));
                    }

                    if (erInternReferanseInnenforSammeFil(link.getDestination())) {
                        sjekkInternPeker(gjeldendeFil, link.getDestination());
                    }
                }

                @Override
                public void visit(final Image image) {
                    if (skalBesøke(image.getDestination())) {
                        sjekkPeker(gjeldendeFil, image.getDestination());
                    }
                }
            });
        }

        private void sjekkInternPeker(final Path gjeldendeFil, final String peker) {
            final String utenRelativLinkPrefix = peker
                    .substring(1)
                    .toLowerCase();
            les(
                    gjeldendeFil,
                    reader -> assertThat(
                            reader
                                    .lines()
                                    .map(String::toLowerCase)
                                    .anyMatch(line -> line.matches("#+ *" + utenRelativLinkPrefix))
                    )
                            .withFailMessage(
                                    "Den interne linken '%s' pekerr ut i det store intet, det eksisterer ikke noe avsnitt med tittelen '%s' i filen '%s'",
                                    peker,
                                    utenRelativLinkPrefix,
                                    gjeldendeFil
                            )
                            .isTrue()
            );

        }

        private void sjekkAtPekerenEksisterer(final Path destinasjonsFil, final Path gjeldendeFil, final String peker) {
            final PathAssert assertion = softly.assertThat(destinasjonsFil);

            if (destinasjonsFil.toFile().exists()) {
                verifiserAtLinkIkkePekerPåKatalogKatalog(assertion);
            } else {
                assertion
                        .withFailMessage(
                                "Den lokale linken '%s' i dokumentasjonssiden '%s' peker ut i det store intet, det eksisterer ingen fil på stien '%s'",
                                peker,
                                gjeldendeFil,
                                destinasjonsFil
                        )
                        .exists();
            }
        }

        private void verifiserAtLinkIkkePekerPåKatalogKatalog(final PathAssert assertion) {
            assertion.isRegularFile();
        }

        private void sjekkPeker(final Path gjeldendeFil, final String peker) {
            softly.assertThat(peker)
                    .withFailMessage(
                            "Den lokale linken '%s' i dokumentasjonssiden '%s' inneholder en eller flere backslash som katalogseparator, det fungerer svært dårlig på linux, bytt det derfor ut med vanlig slash (/)",
                            peker,
                            gjeldendeFil
                    )
                    .doesNotContain("\\");
            final Path destinasjonsFil = resolve(gjeldendeFil, peker);
            sjekkAtPekerenEksisterer(
                    destinasjonsFil,
                    gjeldendeFil,
                    peker
            );

            if (skalBehandleLinkensInnhold(destinasjonsFil)) {
                sjekkSidenOgTraverser(destinasjonsFil);
            }
        }

        private boolean skalBehandleLinkensInnhold(final Path path) {
            return besøkte.add(path) &&
                    path.toFile().isFile() &&
                    path
                            .toFile()
                            .getName()
                            .toLowerCase()
                            .matches(".+\\.(md|feature)");
        }

        private Path resolve(final Path gjeldendeFil, final String peker) {
            return gjeldendeFil
                    .getParent()
                    .resolve(peker)
                    .normalize();
        }

        private void sjekkSidenOgTraverser(final Path gjeldendeFil) {
            les(gjeldendeFil, content -> sjekkSidenOgTraverser(gjeldendeFil, content));
        }

        private boolean skalBesøke(final String peker) {
            return !erInternReferanseInnenforSammeFil(peker) &&
                    !peker.matches("https?.*") &&
                    !peker.matches(".*\\.java");
        }

        private boolean erInternReferanseInnenforSammeFil(final String peker) {
            return peker.trim().startsWith("#");
        }

        private void les(final Path gjeldendeFil, final IOConsumer consumer) {
            try {
                try (final BufferedReader content = Files.newBufferedReader(gjeldendeFil, StandardCharsets.UTF_8)) {
                    consumer.accept(content);
                }
            } catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private String fjernAnkerreferanseFraLink(String destination) {
            return Arrays.stream(destination.split("#"))
                    .findFirst().orElseThrow(
                            () -> new IllegalStateException(
                                    String.format("Prøver å fjerne ankerreferanse fra linken %s, men resterende link mangler", destination))
                    );
        }


        @FunctionalInterface
        private interface IOConsumer {
            void accept(final BufferedReader reader) throws IOException;
        }
}
