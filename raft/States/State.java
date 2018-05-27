package raft.States;

import raft.Raft;

public abstract class State {
    protected StateID stateID;
    protected Raft raft;
    State(StateID stateID, Raft raft){
        this.stateID = stateID;
        this.raft = raft;
    }

    public StateID getStateID() {
        return stateID;
    }

    public abstract void receiveMessage(StateID stateID, String msg);

    public abstract void handleLeaderHeartBeat();
    public abstract void handleLeaderHeartBeatFailure();
}