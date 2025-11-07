# Retro Crash Screen

Mod ID: `retrocrash`

Brings back the old-school beta crash screen.

## Source code / how it works
This tiny mod is [licensed under LGPL3](./LICENSE).

All this mod does is inject a Swing window right before the game closes down for a crash,
hiding Minecraft's window while halting its ability to shut down until the window closes.

[Jump directly to the Java source folder](./src/main/java/com/flooferland/retrocrash)

Normally this crash window was embedded inside the old-school Minecraft launcher,
but as that no longer exists yet _(and I'm unsure [Betacraft](https://github.com/betacraftuk/betacraft-launcher)'s provides this feature)_
I've decided to make this mod straight into the game so its more easily accessible over making a whole launcher for it.

_Excuse the very messy Swing code, this UI library is worse than Python's tkinter, but I noticed Minecraft's launcher appeared to be using it
so I am for the sake of accuracy_

## Contributing

This mode uses [Stonecutter](https://stonecutter.kikugie.dev) for multi-version / multi-loader.

If you'd like to port this mod to a new version / new loader, please [submit an issue](https://github.com/FlooferLand/retrocrash/issues)
as I can somewhat easily do it myself.

If you'd like to add a new version yourself or add any new features,
feel free to do so by making a [pull request](https://github.com/FlooferLand/retrocrash/pulls).

If your change/addition is huge, please consider making an issue before-hand as I might not merge it if it doesn't match the project's vision.
_(ex: if you're trying to add a more sophisticated crash screen, see the [Crash Assistant](https://github.com/KostromDan/Crash-Assistant) mod and/or contribute to it instead)_

If you'd like to test the crashing, you can either trigger it yourself by holding down the vanilla
F3+C, or add a `.dev_crash` file at the root of the repo and the loader will crash immediately.

