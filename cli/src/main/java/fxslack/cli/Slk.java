package fxslack.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;
import fxslack.cli.cmd.*;
import fxslack.core.Out;

@Command(name = "slk",
    mixinStandardHelpOptions = true,
    version = "slk 0.1.0",
    subcommands = {
        AuthVerb.class, AccountsVerb.class, AccountsVerb.Use.class, WhoamiVerb.class, TabsVerb.class,
        ChannelsVerb.class, UsersVerb.class, HistoryVerb.class, ExportVerb.class, ThreadVerb.class, SearchVerb.class,
        SendVerb.class, EditVerb.class, EditVerb.Delete.class, ReactVerb.class, FileVerb.class, ScheduledVerb.class, MembersVerb.class, MarkVerb.class, RawVerb.class
    },
    description = "Slack from the command line, with a Slack app user token or a logged-in browser session. One account per Slack login.")
public final class Slk implements Runnable {

    @Spec CommandSpec spec;

    public static void main(String[] args) {
        xyz.jphil.windows_console_set_unicode_output.WindowsConsoleSetUnicodeOutput.enable();
        var exit = new CommandLine(new Slk())
            .setExecutionExceptionHandler((e, cl, parse) -> {
                Out.err("error: " + e.getMessage());
                return 1;
            })
            .execute(args);
        System.exit(exit);
    }

    @Override public void run() {
        spec.commandLine().usage(System.out);
    }
}
