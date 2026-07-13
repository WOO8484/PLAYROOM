package com.starlight.expedition.core.model

/**
 * 폴더 자동 분류가 인식하는 플랫폼입니다. 표시용 이름만 가지며,
 * 실제 실행 연동은 이번 업데이트 범위에 포함되지 않습니다.
 */
enum class Platform(val displayName: String) {
    NES("NES"),
    SNES("SNES"),
    GAME_BOY("Game Boy"),
    GAME_BOY_COLOR("Game Boy Color"),
    GAME_BOY_ADVANCE("Game Boy Advance"),
    NINTENDO_DS("Nintendo DS"),
    NINTENDO_3DS("Nintendo 3DS"),
    NINTENDO_64("Nintendo 64"),

    PSP("PSP"),
    PLAYSTATION_1("PlayStation"),
    PLAYSTATION_2("PlayStation 2"),

    GAMECUBE("GameCube"),
    WII("Wii"),

    MASTER_SYSTEM("Master System"),
    GAME_GEAR("Game Gear"),
    GENESIS("Mega Drive / Genesis"),
    SEGA_CD("Mega CD / Sega CD"),
    SEGA_SATURN("Sega Saturn"),
    DREAMCAST("Dreamcast"),

    PC_ENGINE("PC Engine"),
    NEO_GEO_POCKET("Neo Geo Pocket"),
    ARCADE("Arcade"),

    /** 미리보기·테스트 전용 표시입니다. 실제 검색 결과에는 사용하지 않습니다. */
    NATIVE("자체 게임"),
    RETRO("레트로 콘솔"),
    PORTABLE("휴대용 콘솔"),

    UNKNOWN("미분류")
}
