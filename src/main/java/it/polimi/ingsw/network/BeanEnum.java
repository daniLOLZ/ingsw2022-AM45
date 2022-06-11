package it.polimi.ingsw.network;

public enum BeanEnum {

    LOBBY_BEAN("LobbyBean"),
    GAME_INIT_BEAN("GameInitBean"),
    ADVANCED_GAMEBOARD_BEAN("AdvancedGameBoardBean"),
    ADVANCED_ISLANDGROUP_BEAN("AdvancedIslandGroupBean"),
    ADVANCED_PLAYER_BEAN("AdvancedPlayerBean"),
    CHARACTER_CARD_BEAN("CharacterBean"),
    CLOUD_BEAN("CloudBean"),
    ERROR_BEAN("ErrorBean"),
    GAMEBOARD_BEAN("GameBoardBean"),
    ISLANDGROUP_BEAN("IslandGroupBean"),
    PLAYER_BEAN("PlayerBean"),
    VIRTUAL_VIEW_BEAN("VirtualViewBean");

    public final String name;

    BeanEnum(String name){
        this.name = name;
    }

}
