package raft;

import raft.States.CandidateState;
import raft.States.FollowerState;
import raft.States.State;
import raft.net.ssl.SSLChannel;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class Raft<T extends Serializable> {

    enum ServerState {
        INITIALIZING, WAITING, RUNNING, TERMINATING;
    }
    enum ClusterState {
        INITIALIZING, RUNNING, TERMINATING;
    }

	UUID ID = UUID.randomUUID();
	Integer port;
	ConcurrentHashMap<UUID, RaftCommunication> cluster = new ConcurrentHashMap<>();
	private AtomicReference<ServerState> serverState = new AtomicReference<>(ServerState.INITIALIZING);
	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            System.out.println("Timeout");
            state.get().handleLeaderHeartBeatFailure();
        }
    };

    public void setState(State state) {
        this.state.set(state);
    }

    private AtomicReference<State> state = new AtomicReference<>(new FollowerState(this));
    private boolean isTimeout = false;



	//	Persistent serverState (save this to stable storage)
	Long currentTerm = 0L;
	UUID votedFor;
	RaftLog<T>[] log;

	//	Volatile serverState
	Long commitIndex = 0L;
	Long lastApplied = 0L;

	//	Leader serverState (this is in RaftCommunication now)
/*	Long[] nextIndex;
	Long[] matchIndex; */

	// TODO Create class (runnable) to redirect client requests (RaftForward? RaftRedirect?) Also create RPC for that (because why the hell not)

	public Raft(Integer port, InetSocketAddress cluster) {
		this.port = port;

		// Connect to known cluster
		{
			SSLChannel channel = new SSLChannel(cluster);
			if (channel.connect()) {
				this.executor.execute(new RaftDiscover(this, channel, true));
			} else {
				System.out.println("Connection failed!"); // DEBUG
				return; // Show better error message
			}
		}

		// Listen for new connections
		this.executor.execute(() -> {
			while (serverState.get() != ServerState.TERMINATING) {
				SSLChannel channel = new SSLChannel(port);
				if (channel.accept()) {
					executor.execute(new RaftDiscover(this, channel, false));
				}
			}
		});
	}

	public Raft(Integer port) {
		this.port = port;

		// Listen for new connections
		this.executor.execute(() -> {
			while (serverState.get() != ServerState.TERMINATING) {
				SSLChannel channel = new SSLChannel(port);
				if (channel.accept()) {
					executor.execute(new RaftDiscover(this, channel, false));
				}
			}
		});
	}

	public void start() {
        scheduleTimeout();
	}

    public void scheduleTimeout() {
	    timer.schedule(timerTask, new Random().nextInt(RaftProtocol.maxRandomDelay-RaftProtocol.minRandomDelay) + RaftProtocol.minRandomDelay);
    }


}
