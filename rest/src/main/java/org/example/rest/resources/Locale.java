package org.example.rest.resources;

import com.fasterxml.jackson.annotation.JsonValue;

import static java.util.Objects.requireNonNull;

public enum Locale {
    DA("da"),
    DE("de"),
    EN_GB("en-GB"),
    EN_US("en-US"),
    ES_ES("es-ES"),
    FR("fr"),
    HR("hr"),
    IT("it"),
    LT("lt"),
    HU("hu"),
    NL("nl"),
    NO("no"),
    PL("pl"),
    PT_BR("pt-BR"),
    RO("ro"),
    FI("fi"),
    SV_SE("sv-SE"),
    VI("vi"),
    TR("tr"),
    CS("cs"),
    EL("el"),
    BG("bg"),
    RU("ru"),
    UK("uk"),
    HI("hi"),
    TH("th"),
    ZH_CN("zh-CN"),
    JA("ja"),
    ZH_TW("zh-TW"),
    KO("ko");

    private final String value;

    Locale(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
