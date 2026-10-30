package fxslack.slackfx.cmd;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import fxslack.core.*;

import static fxslack.core.Out.line;

@Command(name = "tabs", description = "Slack pages the browser has open (through cdpgate: only slack.com tabs in the approved profiles)")
public final class TabsVerb implements Runnable {

    @Option(names = "--profiles", split = ",", description = "Browser profile(s) to look in")
    java.util.List<String> profiles;

    @Override public void run() {
        try (var gate = new Gate(profiles)) {
            gate.pageTargets().forEach(t -> line(t.path("url").asText() + "   " + t.path("title").asText()));
        }
    }
}
