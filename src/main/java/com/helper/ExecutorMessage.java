package com.helper;

/**
 * Created by noahispas on 22.06.16.
 */
public class ExecutorMessage {

    public enum Command {
        dothis,
        dothat
    }

    private Command command;
    private String payload;

    public ExecutorMessage(Command command, String payload){
        this.command = command;
        this.payload = payload;
    }

    public Command getCommand() {
        return command;
    }
    public void setCommand(Command command) {
        this.command = command;
    }

    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "ExecutorMessage{" +
                "command=" + command +
                ", payload='" + payload + '\'' +
                '}';
    }
}
