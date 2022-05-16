package it.polimi.ingsw.view;

import it.polimi.ingsw.view.observer.*;

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
}

// public CloudObserver{
//  CloudBean bean;
//  update(Observable obj, Object arg){
//   this.bean = (CloudBean)obj.toBean();
//  }
// }

