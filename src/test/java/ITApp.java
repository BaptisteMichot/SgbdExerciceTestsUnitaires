import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import Pret.Pret;
import Pret.Projet;

@Nested
@DisplayName("Tests d'intégration: ensemble des composants")
public class ITApp {

    @Test
    @DisplayName("Validation du meilleur scénario")
    public void happyPath (){
        Projet projet = new Projet();
        projet.setPrixHabitation(100_000);
        projet.setRevenuCadastral(700);
        projet.setFraisNotaireAchat(4_150);
        projet.setFraisTransformation(60_000);
        double apportPersonnel = projet.calculApportMinimal(); //24_110.00
        double montantEmprunt = projet.calculResteAEmprunter(); //147_240.00
        Pret pret = new Pret();
        pret.setFraisDossierBancaire(500);
        pret.setFraisNotaireCredit(5_475);
        pret.setNombreAnnees(15);
        pret.setTauxAnnuel(0.04);

        double apportBancaire = pret.calculRestantDu(montantEmprunt);

        Assertions.assertEquals(apportBancaire + apportPersonnel, 30084.99);
    }


    @Test
    @DisplayName("Validation d'un achat élevé'")
    public void testAchatEleve() {
        Projet projet = new Projet();
        projet.setPrixHabitation(2_500_000);
        projet.setRevenuCadastral(1_200);
        projet.setFraisNotaireAchat(15_000);
        projet.setFraisTransformation(200_000);
        double apportPersonnel = projet.calculApportMinimal(); //596_200.00
        double montantEmprunt = projet.calculResteAEmprunter(); //2_440_800.00
        Pret pret = new Pret();
        pret.setFraisDossierBancaire(1_500);
        pret.setFraisNotaireCredit(4_000);
        pret.setNombreAnnees(20);
        pret.setTauxAnnuel(0.02);

        double apportBancaire = pret.calculRestantDu(montantEmprunt); //5_500.03

        Assertions.assertEquals(apportPersonnel + apportBancaire, 601_700.03);
    }

}