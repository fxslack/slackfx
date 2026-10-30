package fxslack.slackfx;

import module java.base;

/// The one entry point. No arguments starts the client; `--background` starts it in the tray only; anything else
/// (a verb, --help, --version) is the command line.
public final class Main {

    private Main() { }

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || List.of(args).equals(List.of("--background"))) SlackFx.main(args);
        else Cli.main(args);
    }
}
