package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.network.BeanEnum;

import java.util.ArrayList;
import java.util.List;

public class VirtualViewBean extends GameElementBean{

    private List<CloudBean> cloudBeans;
    private List<CharacterCardBean> characterCardBeans;
    private List<IslandGroupBean> islandGroupBeans;
    private List<AdvancedIslandGroupBean> advancedIslandGroupBeans;
    private List<PlayerBean> playerBeans;
    private List<AdvancedPlayerBean> advancedPlayerBeans;
    private List<ErrorBean> errorBeans;
    private GameBoardBean gameBoardBean;
    private AdvancedGameBoardBean advancedGameBoardBean;

    public VirtualViewBean(List<CloudBean> cloudBeans, List<CharacterCardBean> characterCardBeans, List<IslandGroupBean> islandGroupBeans, List<AdvancedIslandGroupBean> advancedIslandGroupBeans, List<PlayerBean> playerBeans, List<AdvancedPlayerBean> advancedPlayerBeans, List<ErrorBean> errorBeans, GameBoardBean gameBoardBean, AdvancedGameBoardBean advancedGameBoardBean) {
        this.cloudBeans = cloudBeans;
        this.characterCardBeans = characterCardBeans;
        this.islandGroupBeans = islandGroupBeans;
        this.advancedIslandGroupBeans = advancedIslandGroupBeans;
        this.playerBeans = playerBeans;
        this.advancedPlayerBeans = advancedPlayerBeans;
        this.errorBeans = errorBeans;
        this.gameBoardBean = gameBoardBean;
        this.advancedGameBoardBean = advancedGameBoardBean;
    }

    public List<CloudBean> getCloudBeans() {
        return cloudBeans;
    }

    public List<CharacterCardBean> getCharacterCardBeans() {
        return characterCardBeans;
    }

    public List<IslandGroupBean> getIslandGroupBeans() {
        return islandGroupBeans;
    }

    public List<AdvancedIslandGroupBean> getAdvancedIslandGroupBeans() {
        return advancedIslandGroupBeans;
    }

    public List<PlayerBean> getPlayerBeans() {
        return playerBeans;
    }

    public List<AdvancedPlayerBean> getAdvancedPlayerBeans() {
        return advancedPlayerBeans;
    }

    public List<ErrorBean> getErrorBeans() {
        return errorBeans;
    }

    public GameBoardBean getGameBoardBean() {
        return gameBoardBean;
    }

    public AdvancedGameBoardBean getAdvancedGameBoardBean() {
        return advancedGameBoardBean;
    }

    @Override
    public String toString() {
        //todo stub
        StringBuilder retString = new StringBuilder();

        retString.append(cloudBeans.toString());
        if(characterCardBeans!=null) retString.append(characterCardBeans.toString());
        if(islandGroupBeans!=null) retString.append(islandGroupBeans.toString());
        if(advancedIslandGroupBeans!=null) retString.append(advancedIslandGroupBeans.toString());
        if(playerBeans!=null) retString.append(playerBeans.toString());
        if(advancedPlayerBeans!=null) retString.append(advancedPlayerBeans.toString());
        retString.append(errorBeans.toString());
        if(gameBoardBean!=null) retString.append(gameBoardBean.toString());
        if(advancedGameBoardBean!=null) retString.append(advancedGameBoardBean.toString());

        return retString.toString();
    }

    @Override
    public BeanEnum getBeanType() {
        return BeanEnum.VIRTUAL_VIEW_BEAN;
    }

    public static VirtualViewBean getMockBean(){

        CloudBean cloud = new CloudBean(1, new ArrayList<>());
        //CharacterCardBean character = new CharacterCardBean(1,"mock", "mock",new ArrayList<>(), 69);
        IslandGroupBean island = new IslandGroupBean(1, List.of(1), new ArrayList<>(), false, TeamEnum.NOTEAM, 0, 0);
        AdvancedIslandGroupBean advancedIsland = AdvancedIslandGroupBean.getPromotedBean(island);
        PlayerBean player = new PlayerBean("mock", PlayerEnum.PLAYER1, true, TeamEnum.WHITE, 8, new ArrayList<>(), List.of(0,0,0,0,0), new ArrayList<>(), List.of(new Assistant(1,1,1)), null, 0);
        AdvancedPlayerBean advancedPlayer = AdvancedPlayerBean.getPromotedBean(player);
        ErrorBean errorBean = new ErrorBean("No Information received from the server!!!");
        GameBoardBean gameBoard = new GameBoardBean(List.of(0), new ArrayList<>(), List.of(0), 0, 0, PhaseEnum.PLANNING);
        AdvancedGameBoardBean advancedGameBoard = new AdvancedGameBoardBean(gameBoard.getIdIslandGroups(), gameBoard.getIdAssistantsPlayed(), gameBoard.getIdPlayers(), gameBoard.getCurrentPlayerId(), gameBoard.getTurn(), gameBoard.getPhase(),20, List.of(1,1,1));

        return new VirtualViewBean(
                List.of(cloud, cloud),
                null,
                List.of(island, island, island, island), //required at least 4 island so the game state is somewhat valid
                List.of(advancedIsland, advancedIsland, advancedIsland, advancedIsland),
                List.of(player, player),
                List.of(advancedPlayer, advancedPlayer),
                List.of(errorBean),
                gameBoard,
                advancedGameBoard);
    }
}
