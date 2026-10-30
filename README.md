<p align="center"><img src="docs/icon.png" width="96" alt=""></p>

# slackfx

A small Slack client for Windows, and `slk`, a Slack command line, that share one login. Both act as you, with a Slack app you create in your own workspace, so there is no browser to keep open and nothing to scrape.

Status: 0.1, early. Windows only. Organisation [fxslack](https://fxslack.github.io). Not affiliated with Slack or Salesforce; "Slack" is their trademark.

## Why

The official Slack desktop app runs several processes and holds around a gigabyte of RAM. **slackfx** is one process: a tray icon that stays resident, and a chat window that opens when you want it. Measured on one machine in October 2026, the official app used about 1,000 MB a half minute after a cold start; slackfx used about 200 MB with its window open and a channel loaded, and about 40 MB a couple of minutes after the window was closed.

**slk** reads and writes Slack from a terminal or from a script, and is made to be driven by AI coding agents. Reads are free. Writes (`send`, `edit`, `delete`, `react`) go through slackfx, which shows a window with exactly what is about to be posted and waits for a person to approve it with F2.

## What it does

### slackfx

- Lives in the tray. The window opens from the tray icon and hides when closed; closing it gives the memory back.
- Channels, group chats and direct messages in one list, with a filter. Channels A-Z, then conversations by latest activity. Unread conversations are bold with a count.
- Messages with Slack's formatting: bold, italic, strike, code and code blocks, quotes, links, mentions, dates, reactions, attachments and files.
- New messages arrive within a second, through Slack's Socket Mode. A notification appears for a direct message, a group chat or an @-mention you are not already looking at.
- Write in the window and send with Enter.
- Keys, all single keys: Enter open or send, F2 conversations, F3 write, F5 older, F6 refresh, F7 copy the message, F11 maximise, Esc back or hide. The footer lists them.
- Not built yet: opening a thread, downloading a file, editing or deleting from the window.

### slk

```
slk auth            store an account
slk accounts        list accounts; the active one is starred
slk use <account>   make an account the active one
slk whoami          ask Slack who you are
slk channels        the channels you are in (or all of them with --all)
slk users           people, filtered by name, handle or email
slk history         read a channel, oldest first
slk thread          read a thread
slk search          search messages across the workspace
slk export          save a whole channel: messages, thread replies, files
slk members         who is in a channel
slk file            download a file, or read a canvas
slk send            post a message, a thread reply, or a scheduled message
slk edit, delete    change or remove one of your messages (a dry run unless --yes)
slk react           add or remove an emoji reaction
slk scheduled       list or cancel scheduled messages
slk mark            mark a channel as read
slk raw             call any Slack Web API method
```

Every verb has `--help`. Examples:

```
slk history "#general" -n 20
slk search "release notes"
slk send "#general" "Build is green"          a dry run: prints what would be posted
slk send "#general" "Build is green" --yes    posts it
slk send "#general" "Following up" --thread 1760000000.123456 --yes
```

## Set it up

1. Create the Slack app, once. At [api.slack.com/apps](https://api.slack.com/apps) choose **Create New App**, then **From a manifest**, pick your workspace and paste [`docs/slack-app-manifest.json`](docs/slack-app-manifest.json). It asks for user-token scopes only (it reads and posts as you) and turns on Socket Mode.
2. Under **OAuth & Permissions** choose **Install to Workspace** and allow it. Copy the **User OAuth Token** (it starts with `xoxp-`).
3. Under **Basic Information**, **App-Level Tokens**, generate one with the `connections:write` scope. Copy it (it starts with `xapp-`). It is what lets slackfx receive messages live; without it slackfx polls the open conversation instead.
4. Save both in `app-config.json` in the app's folder, `%USERPROFILE%\fxslack\slackfx\` (`~/fxslack/slackfx/`):

   ```json
   {
     "user_token": "xoxp-...",
     "app_token": "xapp-..."
   }
   ```

5. Run `slk auth --app`, then `slk whoami`. Start `slackfx` for the tray and the window.

An admin of your workspace may need to approve the app first. The tokens give whoever holds them access as you, so keep that file private (it is never part of this repository, and `.gitignore` lists it).

### Without creating an app

`slk auth` with no option reads the session of a Slack tab that is already logged in, in a browser that [cdpgate](https://github.com/cdpai/gate) runs. That session token rotates and needs the browser's cookie, so the app token above is the better route; this one is for workspaces that do not let you install an app.

## Install

Download `slackfx.exe` and `slk.exe` from the [latest release](https://github.com/fxslack/slackfx/releases/latest) and run them. They need Java 25 or later. Each exe fetches its program file from the same release and checks it against a checksum built into it. `slackfx.exe -Xjr:update` and `slk.exe -Xjr:update` move to a newer release. The exes are made with [jr](https://github.com/jarrunner/jr).

## Build from source

Needs JDK 25 and Maven.

```
mvn package          the jars, in core/, cli/ and app/ under target/
mvn verify           also the exes, in cli/target/jr and app/target/jr
```

`mvn -Djr.installDir=<folder> verify` copies the exes into a folder on your PATH. `mvn -Djr.source=url verify` builds the release exes, which fetch their jar from the GitHub release.

slackfx and slk depend on four libraries that are published only as snapshots, or not yet at all: `leasegate-frame`, `cdpai-gate-client`, `littlejlib-traynotify` and `xyz-jphil-windows_console_set_unicode_output`. Until they are on Maven Central, install them from their repositories with `mvn install` before building.

## Where things are

Everything slackfx and slk keep is in `%USERPROFILE%\fxslack\slackfx\`: `app-config.json` (the tokens), `sessions.json` (stored accounts), `cache/` (names, pictures, the last conversation list), `run/` (the log, and where slackfx listens). slackfx listens on `127.0.0.1` only, with a random token kept in your user profile.

## Licence

Apache-2.0. Lato (the font in the window) is by Łukasz Dziedzic, under the SIL Open Font Licence; its licence is beside the font files.
