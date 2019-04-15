# leericos
An android library to parse LRC files easily.

## Usage

```kotlin
val leericos = Leericos("adele-someonelikeyou.lrc")
val lrc : LRC = leericos.get(timestamp)
println("Lyrics : ${lrc.line}")
```
