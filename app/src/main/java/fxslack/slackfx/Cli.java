package fxslack.slackfx;

import fxslack.core.Out;
import fxslack.slackfx.cmd.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(name = "slackfx",
    mixinStandardHelpOptions = true,
    versionProvider = Ver.class,
    subcommands = {
        AuthVerb.class, AccountsVerb.class, AccountsVerb.Use.class, WhoamiVerb.class, TabsVerb.class,
        ChannelsVerb.class, UsersVerb.class, HistoryVerb.class, ExportVerb.class, ThreadVerb.class, SearchVerb.class,
        SendVerb.class, EditVerb.class, EditVerb.Delete.class, ReactVerb.class, FileVerb.class, ScheduledVerb.class, MembersVerb.class, MarkVerb.class, RawVerb.class,
        UpdateVerb.class
    },
    description = {"slackfx: a small Slack client and command line, one program.",
        "With no arguments it starts the client (tray icon and window); with --background, in the tray only.",
        "With a verb it runs the command line, with a Slack app user token or a logged-in browser session. One account per Slack login."})
public final class Cli implements Runnable {

    @Spec CommandSpec spec;

    public static void main(String[] args) {
        xyz.jphil.windows_console_set_unicode_output.WindowsConsoleSetUnicodeOutput.enable();
        var check = args.length > 0 && args[0].equals("update") ? null : CliUpdates.start();
        var exit = new CommandLine(new Cli())
            .setExecutionExceptionHandler((e, cl, parse) -> {
                Out.err("error: " + e.getMessage());
                return 1;
            })
            .execute(args);
        CliUpdates.show(check);
        System.exit(exit);
    }

    @Override public void run() {
        spec.commandLine().usage(System.out);
    }
}
