package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.network.BeanEnum;

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

}
