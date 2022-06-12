package it.polimi.ingsw.view;

import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.view.observer.*;

import java.util.ArrayList;
import java.util.List;


public class VirtualView {
    // List<Observer> observerList;

    // public sendToUser(){
    //  for( Observer obs : observerList){
    //      obs.dammiIlBean();
    //   }
    //  send();
    // }
    private List<CloudWatcher> cloudWatchers;
    private List<CharacterWatcher> characterWatchers;
    private List<IslandGroupWatcher> islandGroupWatchers;
    private List<AdvancedIslandGroupWatcher> advancedIslandGroupWatchers;
    private List<PlayerWatcher> playerWatchers;
    private List<AdvancedPlayerWatcher> advancedPlayerWatchers;
    private List<ErrorWatcher> errorWatchers;
    private SimpleGameWatcher simpleGameWatcher;
    private AdvancedGameWatcher advancedGameWatcher;

    public VirtualView(){
        cloudWatchers = new ArrayList<>();
        characterWatchers = new ArrayList<>();
        islandGroupWatchers = new ArrayList<>();
        advancedIslandGroupWatchers = new ArrayList<>();
        playerWatchers = new ArrayList<>();
        advancedPlayerWatchers = new ArrayList<>();
        errorWatchers = new ArrayList<>();
    }

    public void setAdvancedGameWatcher(AdvancedGameWatcher advancedGameWatcher) {
        this.advancedGameWatcher = advancedGameWatcher;
    }

    public void addAdvancedIslandWatcher(AdvancedIslandGroupWatcher watcher){
        advancedIslandGroupWatchers.add(watcher);
    }

    public void addPlayerWatcher(PlayerWatcher watcher){
        playerWatchers.add(watcher);
    }

    public void addErrorWatcher(ErrorWatcher watcher){
        errorWatchers.add(watcher);
    }

    public void addAdvancedPlayerWatcher(AdvancedPlayerWatcher watcher){
        advancedPlayerWatchers.add(watcher);
    }

    public void addSimpleGameWatcher(SimpleGameWatcher simpleGameWatcher) {
        this.simpleGameWatcher = simpleGameWatcher;
    }

    public void addIslandWatcher(IslandGroupWatcher watcher){
        islandGroupWatchers.add(watcher);
    }

    public void addCloudWatcher(CloudWatcher watcher){
        cloudWatchers.add(watcher);
    }

    public void addCharacterWatcher(CharacterWatcher watcher){
        characterWatchers.add(watcher);
    }

    public List<CloudBean> getCloudBean(){
        List<CloudBean> beans = new ArrayList<>();
        for(CloudWatcher watcher: cloudWatchers)
            beans.add(watcher.getBean());
        return  beans;
    }

    public List<CharacterCardBean> getCharacterBean(){
        List<CharacterCardBean> beans = new ArrayList<>();
        for(CharacterWatcher watcher: characterWatchers)
            beans.add(watcher.getBean());
        return  beans;
    }

    public List<IslandGroupBean> getIslandBean(){
        List<IslandGroupBean> beans = new ArrayList<>();
        for(IslandGroupWatcher watcher: islandGroupWatchers)
            beans.add(watcher.getBean());
        return  beans;
    }

    public List<AdvancedIslandGroupBean> getAdvancedIslandBean(){
        List<AdvancedIslandGroupBean> beans = new ArrayList<>();
        for(AdvancedIslandGroupWatcher watcher: advancedIslandGroupWatchers)
            beans.add(watcher.getBean());
        return  beans;
    }

    public List<PlayerBean> getPlayerBean(){
        List<PlayerBean> beans = new ArrayList<>();
        for(PlayerWatcher watcher: playerWatchers)
            beans.add(watcher.getBean());
        return  beans;
    }

    public List<AdvancedPlayerBean> getAdvancedPlayerBean(){
        List<AdvancedPlayerBean> beans = new ArrayList<>();
        for(AdvancedPlayerWatcher watcher: advancedPlayerWatchers)
            beans.add(watcher.getBean());
        return  beans;
    }

    public List<ErrorBean> getErrorBean(){
        List<ErrorBean> beans = new ArrayList<>();
        for(ErrorWatcher watcher: errorWatchers)
            beans.add(watcher.getBean());
        return  beans;
    }

    public AdvancedGameBoardBean getAdvancedGameBean() {
        return advancedGameWatcher.getBean();
    }

    public GameBoardBean getSimpleGameBean() {
        return simpleGameWatcher.getBean();
    }

    /**
     * Combines all currently held beans and returns them as a single virtual view bean (simple)
     * @return the combined virtual view bean
     */
    public VirtualViewBean renderSimpleView() {
        return new VirtualViewBean(
                getCloudBean(),
                null,
                getIslandBean(),
                null,
                getPlayerBean(),
                null,
                getErrorBean(),
                getSimpleGameBean(),
                null);
    }

    /**
     * Combines all currently held beans and returns them as a single virtual view bean (simple)
     * @return the combined virtual view bean
     */
    public VirtualViewBean renderAdvancedView(){
        return new VirtualViewBean(
                getCloudBean(),
                getCharacterBean(),
                null,
                getAdvancedIslandBean(),
                null,
                getAdvancedPlayerBean(),
                getErrorBean(),
                null,
                getAdvancedGameBean());
    }


    /*

    private <S extends GameElementBean, T extends Watcher> List<S> getBeansFromWatchers(List<T> watchers) {
        return watchers.stream()
                .map(Watcher::getBean)
                .map( x -> (S) x)
                .collect(Collectors.toList());
    }

     */
}



// public CloudObserver{
//  CloudBean bean;
//  update(Observable obj, Object arg){
//   this.bean = (CloudBean)obj.toBean();
//  }
// }

