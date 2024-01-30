package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.models.District;

import java.util.List;

public class ArchitecteAbilityAction extends AbilityAction {
    private  int districtsToBuild;

    public ArchitecteAbilityAction(Player currentPlayer, Game game){
        super(currentPlayer, game);
        this.districtsToBuild=0;
    }

    public void drawAdditionalCards() {

        for (int i = 0; i <=2; i++) {
            this.currentPlayer.getHand().add(this.game.getDeck().drawCard());
        }
    }


    public void buildDistricts(List<District> districts) throws IllegalActionException, DuplicatedDistrictException {
        //impossible de construire plus de 3 quartiers
        if (districtsToBuild + districts.size() > 3) {
            throw new IllegalActionException("Architecte can only build up to three districts in one turn");
        }
        //else

        for (District district : districts) {
            buildDistrict(district);
        }

        districtsToBuild += districts.size();
    }

    private void buildDistrict(District district) throws IllegalActionException, DuplicatedDistrictException {
        if (!this.currentPlayer.getHand().contains(district)) {
            throw new IllegalActionException("Le quartier n'est pas dans la main du joueur");
        }

         this.currentPlayer.getCity().addDistrict(district);
         this.currentPlayer.getHand().remove(district);
    }

    public int getDistrictsToBuild() {
        return districtsToBuild;
    }
}