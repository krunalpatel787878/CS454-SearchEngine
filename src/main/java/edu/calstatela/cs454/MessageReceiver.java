package edu.calstatela.cs454;

public interface MessageReceiver {
	
	public void receiveMessage(Object theMessage, int threadId, int level);

	
	public void finished(int threadId);

	
	public void finishedAll();
}
