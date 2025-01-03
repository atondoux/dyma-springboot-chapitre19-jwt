package com.dyma.tennis.data;

import com.dyma.tennis.model.Player;
import com.dyma.tennis.model.PlayerDescription;
import com.dyma.tennis.model.Rank;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerList {

    public static Player RAFAEL_NADAL = new Player(
            new PlayerDescription(
                    UUID.fromString("b466c6f7-52c6-4f25-b00d-c562be41311e"),
                    "Rafael",
                    "Nadal",
                    LocalDate.of(1986, Month.JUNE, 3),
                    new Rank(1, 5000)
            ),
            Collections.emptySet()
    );

    public static Player NOVAK_DJOKOVIC = new Player(
            new PlayerDescription(
                    UUID.fromString("d27aef45-51cd-401b-a04a-b78a1327b793"),
                    "Novak",
                    "Djokovic",
                    LocalDate.of(1987, Month.MAY, 22),
                    new Rank(2, 4000)
            ),
            Collections.emptySet()
    );

    public static Player ROGER_FEDERER = new Player(
            new PlayerDescription(
                    UUID.fromString("79d52b6a-7b4f-4111-8aaa-d6a48717f5a3"),
                    "Roger",
                    "Federer",
                    LocalDate.of(1981, Month.AUGUST, 8),
                    new Rank(3, 3000)
            ),
            Collections.emptySet()
    );

    public static Player ANDY_MURRAY = new Player(
            new PlayerDescription(
                    UUID.fromString("d461b0db-e4d2-4bdf-9dd3-fd23f9914838"),
                    "Andy",
                    "Murray",
                    LocalDate.of(1987, Month.MAY, 15),
                    new Rank(4, 2000)
            ),
            Collections.emptySet()
    );

    public static List<Player> ALL = Arrays.asList(RAFAEL_NADAL, NOVAK_DJOKOVIC, ROGER_FEDERER, ANDY_MURRAY);
}
