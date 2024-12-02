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

        @Disabled
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
    public class calculDroitEnregistrement {

        @Disabled
        @Test
        @DisplayName("Calcul du droit d'enregistrement pour un revenu cadastral inférieur à 745")
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


    @Nested
    @DisplayName("Calcul du montant total de l'achat")
    public class calculTotalProjetAchat {

        @Test
        @DisplayName("Calcul du montant total de l'achat : validation simple")
        public void calculTotalProjetAchatSimple(){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(300_000.00);
                    mockedProjet.setFraisNotaireAchat(6_000.00);
                    mockedProjet.setFraisTransformation(10_000.00);
                    Mockito.doReturn(600.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(15_600.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(332_200.00, mockedProjet.calculTotalProjetAchat());
                },
                () -> {
                    mockedProjet.setPrixHabitation(400_000.00);
                    mockedProjet.setFraisNotaireAchat(8_000.00);
                    mockedProjet.setFraisTransformation(0.00);
                    Mockito.doReturn(0.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(22_000.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(430_000.00, mockedProjet.calculTotalProjetAchat());
                },
                () -> {
                    mockedProjet.setPrixHabitation(600_000.00);
                    mockedProjet.setFraisNotaireAchat(12_000.00);
                    mockedProjet.setFraisTransformation(10_000.00);
                    Mockito.doReturn(600.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(34_800.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(657_400.00, mockedProjet.calculTotalProjetAchat());
                },
                () -> {
                    mockedProjet.setPrixHabitation(1.00);
                    mockedProjet.setFraisNotaireAchat(0.02);
                    mockedProjet.setFraisTransformation(0.00);
                    Mockito.doReturn(0.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(0.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(1.02, mockedProjet.calculTotalProjetAchat());
                },
                () -> {
                    mockedProjet.setPrixHabitation(0.00);
                    mockedProjet.setFraisNotaireAchat(0.00);
                    mockedProjet.setFraisTransformation(0.00);
                    Mockito.doReturn(0.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(0.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(0.00, mockedProjet.calculTotalProjetAchat());
                }
            );
        }
        

        @Test
        @DisplayName("Calcul du montant total de l'achat : validation des arrondis")
        public void calculTotalProjetAchatArrondi(){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(300_000.50);
                    mockedProjet.setFraisNotaireAchat(6_000.01);
                    mockedProjet.setFraisTransformation(10_000.00);
                    Mockito.doReturn(600.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(15_600.03).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(332_200.54, mockedProjet.calculTotalProjetAchat(), 0.001);
                },
                () -> {
                    mockedProjet.setPrixHabitation(400_000.20);
                    mockedProjet.setFraisNotaireAchat(8_000.00);
                    mockedProjet.setFraisTransformation(0.00);
                    Mockito.doReturn(0.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(22_000.01).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(430_000.21, mockedProjet.calculTotalProjetAchat(), 0.001);
                },
                () -> {
                    mockedProjet.setPrixHabitation(400_000.20);
                    mockedProjet.setFraisNotaireAchat(8_000.50);
                    mockedProjet.setFraisTransformation(500.95);
                    Mockito.doReturn(30.057).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(22_000.01).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(430_531.717, mockedProjet.calculTotalProjetAchat(), 0.001);
                }
            );
        }
        
        
        @ParameterizedTest
        @ValueSource(doubles = { -300_000.00, -400_000.00 })
        @DisplayName("Calcul du montant total de l'achat : prix habitation négatif")
        public void calculTotalProjetAchatPrixHabitationNegatif(double prixHabitation){

            Assertions.assertAll( 
                () -> {
                    mockedProjet.setFraisNotaireAchat(2_000.00);
                    mockedProjet.setFraisTransformation(5_000.00);
                    Mockito.doReturn(300.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(0.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculTotalProjetAchat());
                }
            );

        }


        @ParameterizedTest
        @ValueSource(doubles = { -1_000.00, -2_000.00 })
        @DisplayName("Calcul du montant total de l'achat : frais de notaire négatifs")
        public void calculTotalProjetAchatFraisNotaireNegatifs(double fraisNotaireAchat){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(300_000.00);
                    mockedProjet.setFraisTransformation(10_000.00);
                    Mockito.doReturn(600.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(15_600.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculTotalProjetAchat());
                }
            );
        }


        @ParameterizedTest
        @ValueSource(doubles = { -10_000.00, -500.00 })
        @DisplayName("Calcul du montant total de l'achat : frais de transformation négatifs")
        public void calculTotalProjetAchatFraisTransformationNegatifs(double fraisTransformation){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(300_000.00);
                    mockedProjet.setFraisNotaireAchat(8_000.00);
                    Mockito.doReturn(0.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(22_000.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculTotalProjetAchat());
                }
            );
        }


        @ParameterizedTest
        @ValueSource(doubles = { -600.00, -30.00 })
        @DisplayName("Calcul du montant total de l'achat : TVA sur les frais de transformation négative")
        public void calculTotalProjetAchatTVAFraisTransformationNegative(double valeurTVA){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(600_000.00);
                    mockedProjet.setFraisNotaireAchat(12_000.00);
                    mockedProjet.setFraisTransformation(10_000.00);
                    Mockito.doReturn(valeurTVA).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(34_800.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculTotalProjetAchat());
                }
            );
        }


        @Test
        @DisplayName("Calcul du montant total de l'achat : droit d'enregistrement négatif")
        public void calculTotalProjetAchatDroitEnregistrementNegatif(){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(300_000.00);
                    mockedProjet.setFraisNotaireAchat(6_000.00);
                    mockedProjet.setFraisTransformation(1_000.00);
                    Mockito.doReturn(60.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(-15_600.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculTotalProjetAchat());
                },
                () -> {
                    mockedProjet.setPrixHabitation(400_000.00);
                    mockedProjet.setFraisNotaireAchat(8_000.00);
                    mockedProjet.setFraisTransformation(2_000.00);
                    Mockito.doReturn(120.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(-22_000.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculTotalProjetAchat());
                }
            );
        }

    }

    @Nested
    @DisplayName("Calcul de l'apport minimal")
    public class calculApportMinimal{

        @Test
        @DisplayName("Calcul de l'apport minimal : validation simple")
        public void calculApportMinimalSimple(){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(300_000.00);
                    mockedProjet.setFraisNotaireAchat(6_000.00);
                    mockedProjet.setFraisTransformation(10_000.00);
                    Mockito.doReturn(600.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(15_600.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(52_660.00, mockedProjet.calculApportMinimal());
                },
                () -> {
                    mockedProjet.setPrixHabitation(400_000.00);
                    mockedProjet.setFraisNotaireAchat(8_000.00);
                    mockedProjet.setFraisTransformation(0.00);
                    Mockito.doReturn(0.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(22_000.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(70_000.00, mockedProjet.calculApportMinimal());
                },
                () -> {
                    mockedProjet.setPrixHabitation(600_000.00);
                    mockedProjet.setFraisNotaireAchat(12_000.00);
                    mockedProjet.setFraisTransformation(10_000.00);
                    Mockito.doReturn(600.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(34_800.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(107_860.00, mockedProjet.calculApportMinimal());
                },
                () -> {
                    mockedProjet.setPrixHabitation(0.00);
                    mockedProjet.setFraisNotaireAchat(0.00);
                    mockedProjet.setFraisTransformation(0.00);
                    Mockito.doReturn(0.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(0.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(0.00, mockedProjet.calculApportMinimal());
                }
            );
        }


        @Test
        @DisplayName("Calcul de l'apport minimal : validation des arrondis")
        public void calculApportMinimalArrondi(){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(300_000.50);
                    mockedProjet.setFraisNotaireAchat(6_000.01);
                    mockedProjet.setFraisTransformation(10_000.00);
                    Mockito.doReturn(600.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(15_600.03).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(52_660.09, mockedProjet.calculApportMinimal(), 0.001);
                },
                () -> {
                    mockedProjet.setPrixHabitation(1.00);
                    mockedProjet.setFraisNotaireAchat(0.02);
                    mockedProjet.setFraisTransformation(0.00);
                    Mockito.doReturn(0.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(0.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(0.12, mockedProjet.calculApportMinimal(), 0.001);
                },
                () -> {
                    mockedProjet.setPrixHabitation(400_000.20);
                    mockedProjet.setFraisNotaireAchat(8_000.50);
                    mockedProjet.setFraisTransformation(500.95);
                    Mockito.doReturn(30.057).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(22_000.01).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertEquals(70_053.6307, mockedProjet.calculApportMinimal());
                }
            );
        }


        @ParameterizedTest
        @ValueSource(doubles = { -300_000.00, -400_000.00 })
        @DisplayName("Calcul de l'apport minimal : prix habitation négatif")
        public void calculApportMinimalPrixHabitationNegatif(double prixHabitation){

            Assertions.assertAll( 
                () -> {
                    mockedProjet.setFraisNotaireAchat(2_000.00);
                    mockedProjet.setFraisTransformation(5_000.00);
                    Mockito.doReturn(300.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(0.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculApportMinimal());
                }
            );
        }


        @ParameterizedTest
        @ValueSource(doubles = { -1_000.00, -2_000.00 })
        @DisplayName("Calcul de l'apport minimal : frais de notaire négatifs")
        public void calculApportMinimalFraisNotaireNegatifs(double fraisNotaireAchat){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(300_000.00);
                    mockedProjet.setFraisTransformation(10_000.00);
                    Mockito.doReturn(600.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(15_600.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculApportMinimal());
                }
            );
        }


        @ParameterizedTest
        @ValueSource(doubles = { -10_000.00, -500.00 })
        @DisplayName("Calcul de l'apport minimal : frais de transformation négatifs")
        public void calculApportMinimalFraisTransformationNegatifs(double fraisTransformation){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(300_000.00);
                    mockedProjet.setFraisNotaireAchat(8_000.00);
                    Mockito.doReturn(0.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(22_000.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculApportMinimal());
                }
            );
        }


        @ParameterizedTest
        @ValueSource(doubles = { -600.00, -30.00 })
        @DisplayName("Calcul de l'apport minimal : TVA sur les frais de transformation négative")
        public void calculApportMinimalTVAFraisTransformationNegative(double valeurTVA){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(600_000.00);
                    mockedProjet.setFraisNotaireAchat(12_000.00);
                    mockedProjet.setFraisTransformation(10_000.00);
                    Mockito.doReturn(valeurTVA).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(34_800.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculApportMinimal());
                }
            );
        }


        @Test
        @DisplayName("Calcul de l'apport minimal : droit d'enregistrement négatif")
        public void calculApportMinimalDroitEnregistrementNegatif(){

            Assertions.assertAll(
                () -> {
                    mockedProjet.setPrixHabitation(300_000.00);
                    mockedProjet.setFraisNotaireAchat(6_000.00);
                    mockedProjet.setFraisTransformation(1_000.00);
                    Mockito.doReturn(60.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(-15_600.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculApportMinimal());
                },
                () -> {
                    mockedProjet.setPrixHabitation(400_000.00);
                    mockedProjet.setFraisNotaireAchat(8_000.00);
                    mockedProjet.setFraisTransformation(2_000.00);
                    Mockito.doReturn(120.00).when(mockedProjet).calculTVAFraisTransformation();
                    Mockito.doReturn(-22_000.00).when(mockedProjet).calculDroitEnregistrement();
                    Assertions.assertThrows(Exception.class, () -> projet.calculApportMinimal());
                }
            );
        }

    }

}
