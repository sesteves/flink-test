/**
  * Created by Sergio on 12/04/2016.
  */

object EventTimeData {

  val personWithAge = Seq(
    Tuple3(1448892364000L, "Miko", 15), Tuple3(1448892367000L, "Jace", 25),
    Tuple3(1448892368000L, "Rose", 25), Tuple3(1448892372000L, "Hari", 43),
    Tuple3(1448892374000L, "Jack", 35), Tuple3(1448892378000L, "Jason", 37),
    Tuple3(1448892382000L, "Mick", 21), Tuple3(1448892389000L, "Kk", 25),
    Tuple3(1448892390000L, "Duke", 25), Tuple3(1448892399000L, "Duma", 28),
    Tuple3(1448892404000L, "Anna", 22), Tuple3(1448892406000L, "Erik", 30),
    Tuple3(1448892412000L, "Jarn", 35), Tuple3(1448892414000L, "Nick", 37),
    Tuple3(1448892414040L, "Kuke", 23), Tuple3(1448892415100L, "Json", 39),
    Tuple3(1448892417400L, "Deak", 28), Tuple3(1448892423010L, "Jim", 29),
    Tuple3(1448892425110L, "Obama", 55), Tuple3(1448892425810L, "Busi", 65),
    Tuple3(1448892427010L, "Jook", 34), Tuple3(1448892429110L, "Nkin", 25),
    Tuple3(1448892430110L, "Mari", 25), Tuple3(1448892432110L, "Mike", 26),
    Tuple3(1448892436110L, "Uoks", 28), Tuple3(1448892446110L, "Duli", 95),
    Tuple3(1448892504000L, "Mkke", 25), Tuple3(1448892506500L, "Kom", 45),
    Tuple3(1448892508200L, "Niok", 32), Tuple3(1448892509000L, "Jeep", 34),
    Tuple3(1448892512300L, "Ikls", 25), Tuple3(1448892515300L, "Lksa", 12),
    Tuple3(1448892517300L, "LJks", 22), Tuple3(1448892537300L, "Luks", 15)
  )

  // Rose 10s later, Hari 5s later, Anna 3s earlier, Ikls 30s later
  // Make missing
  val personWithInterest = Seq(
    Tuple3(1448892363000L, "Miko", "Apple"), Tuple3(1448892368000L, "Jace", "Game"),
    Tuple3(1448892375000L, "Rose", "Organge"), Tuple3(1448892375000L, "Hari", "Banana"),
    Tuple3(1448892375200L, "Jack", "Chatting"), Tuple3(1448892380000L, "Jason", "Walking"),
    Tuple3(1448892382000L, "Mick", "Egg"), Tuple3(1448892389000L, "Kk", "Cabbage"),
    Tuple3(1448892390400L, "Duke", "Chilli"), Tuple3(1448892401000L, "Duma", "Reading"),
    Tuple3(1448892401000L, "Anna", "Cycling"), Tuple3(1448892406000L, "Erik", "Kayka"),
    Tuple3(1448892412000L, "Jarn", "Singing"), Tuple3(1448892414000L, "Nick", "Reading"),
    Tuple3(1448892414040L, "Kuke", "iPhone"), Tuple3(1448892415100L, "Json", "Mac"),

    Tuple3(1448892415240L, "Kuse", "OnePlus"), Tuple3(1448892415400L, "Jsonn", "iMac"),

    Tuple3(1448892419400L, "Deak", "Stories"), Tuple3(1448892423910L, "Jim", "Pating"),
    Tuple3(1448892425110L, "Obama", "Dawing"), Tuple3(1448892425910L, "Busi", "Coding"),
    Tuple3(1448892427310L, "Jook", "Smiles"), Tuple3(1448892429110L, "Nkin", "Jump"),
    Tuple3(1448892434110L, "Mari", "Shopping"), Tuple3(1448892439110L, "Mike", "Forest"),
    Tuple3(1448892446110L, "Uoks", "Lake"), Tuple3(1448892446310L, "Duli", "Boat"),
    Tuple3(1448892504000L, "Mkke", "Music"), Tuple3(1448892506900L, "Kom", "Food"),
    Tuple3(1448892509200L, "Niok", "Learing"), Tuple3(1448892511000L, "Jeep", "Games"),
    Tuple3(1448892532300L, "Ikls", "Warcraft"), Tuple3(1448892534300L, "Lksa", "Dota"),
    Tuple3(1448892536300L, "LJks", "Age3"), Tuple3(1448892568300L, "Luks", "nothing")
  )
}
