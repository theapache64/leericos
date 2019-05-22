![poster](https://dummyimage.com/800x400/000/fff.png&text=leericos)

# leericos
An android library to parse LRC files easily.

## Installation

```groovy
implementation 'com.theapache64.leericos:leericos:0.0.1-alpha02'
```

## Usage

```kotlin
val leericos = Leericos("adele-someonelikeyou.lrc")
val lrc : LRC = leericos.get(timestamp)
println("Lyrics : ${lrc.line}")
```
