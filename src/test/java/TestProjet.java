import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import Pret.Projet;

public class TestProjet {

    private static Projet mockedProjet ;
    private static Projet projet;

    @BeforeAll
    static void setup() {
        TestProjet.projet = new Projet();
        // Préparation d'un objet mocked pour les fonctions intégrées.
        TestProjet.mockedProjet = Mockito.spy(projet);
    }


    @Nested
    @DisplayName("Calcul de la TVA sur les frais de transformation")
    public class calculTVAFraisDeTransformation {

        @Test
        @DisplayName("Calcul de la tva sur les frais de transformation: Validation simple")
        public void calculTVAFraisTransformationSimple() {
            // Test de valeurs simples, le test doit les verifier toutes:
            
            Assertions.assertAll(
                () -> {
                    projet.setFraisTransformation(90_000.00);
                    Assertions.assertEquals(5_400.00, projet.calculTVAFraisTransformation());
                },
                () -> {
                    projet.setFraisTransformation(0.00);
                    Assertions.assertEquals(0.00, projet.calculTVAFraisTransformation());
                },
                () -> {
                    projet.setFraisTransformation(100_000.00);
                    Assertions.assertEquals(6_000.00, projet.calculTVAFraisTransformation());
                },
                () -> {
                    projet.setFraisTransformation(59_595.00);
                    Assertions.assertEquals(3_575.70, projet.calculTVAFraisTransformation());
                },
                () -> {
                    projet.setFraisTransformation(1.00);
                    Assertions.assertEquals(0.06, projet.calculTVAFraisTransformation());
                }
            );
            
            //Assertions.assertThrows(Exception.class,() -> {projet.setFraisTransformation(-1.00);});
        }


        @Disabled
        @Test
        @DisplayName("Calcul de la tva sur les frais de transformation : Validation des arrondis")
        public void calculTVAFraisTransformationArrondi() {
        // Test de problème d'arrondis
            projet.setFraisTransformation(92_123.89);
            Assertions.assertEquals(5_527.44, projet.calculTVAFraisTransformation());
            //Assertions.assertEquals(5_527.44, projet.calculTVAFraisTransformation(), 0.0001);
            //delta (marge d'erreur) à la fin mais pas ici pcq fiscalité
        }


        @Disabled
        @ParameterizedTest
        @ValueSource(doubles = { -90_000.00, -25_000.00 })
        @DisplayName("Calcul de la tva sur les frais de transformation: Validation des frais negatifs")
        public void calculTVAFraisTransformationNegatif(double fraisTransformation) {
        // Test de valeurs négatives
            projet.setFraisTransformation(fraisTransformation);
            Assertions.assertThrows(Exception.class, () -> projet.calculTVAFraisTransformation());
        }

    }


    @Nested
    @DisplayName("Calcul du droit d'enregistrement")
    class calculDroitEnregistrement {

        @Disabled
        @Test
        @DisplayName("Calcul du droit d'enregistrement pour un revenu cadastral inférieur à 750")
        public void calculDroitEnregistrementRevenuCadastralInferieur745(){
        // Changement du comportement du calcul d'abattement.
            Assertions.assertAll( 
                () -> {
                    mockedProjet.setPrixHabitation(350_000.00);
                    Mockito.doReturn(40_000.00).when(mockedProjet).calculAbattement();
                    mockedProjet.setRevenuCadastral(740);
                    Assertions.assertEquals(18_600.00, mockedProjet.calculDroitEnregistrement());
                }
            );

        }
    }

}
