package ca.polymtl.inf8480.tp1.client.commands;

import ca.polymtl.inf8480.tp1.shared.AuthServerInterface;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;

public abstract class Command {
    public abstract String execute(ServerInterface serverInterface, AuthServerInterface authServerInterface);

    public abstract boolean requiresAuthentication();
}
