package com.dyma.tennis.data;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerEntityList {

    public static PlayerEntity RAFAEL_NADAL = new PlayerEntity(
            "Nadal",
            "Rafael",
            UUID.fromString("b466c6f7-52c6-4f25-b00d-c562be41311e"),
            LocalDate.of(1986, Month.JUNE, 3),
            5000,
            1
    );

    public static PlayerEntity NOVAK_DJOKOVIC = new PlayerEntity(
            "Djokovic",
            "Novak",
            UUID.fromString("d27aef45-51cd-401b-a04a-b78a1327b793"),
            LocalDate.of(1987, Month.MAY, 22),
            4000,
            2
    );

    public static PlayerEntity ROGER_FEDERER = new PlayerEntity(
            "Federer",
            "Roger",
            UUID.fromString("79d52b6a-7b4f-4111-8aaa-d6a48717f5a3"),
            LocalDate.of(1981, Month.AUGUST, 8),
            3000,
            3
    );

    public static PlayerEntity ANDY_MURRAY = new PlayerEntity(
            "Murray",
            "Andy",
            UUID.fromString("d461b0db-e4d2-4bdf-9dd3-fd23f9914838"),
            LocalDate.of(1987, Month.MAY, 15),
            2000,
            4
    );

    public static List<PlayerEntity> ALL = Arrays.asList(ROGER_FEDERER, ANDY_MURRAY, NOVAK_DJOKOVIC, RAFAEL_NADAL);
}
