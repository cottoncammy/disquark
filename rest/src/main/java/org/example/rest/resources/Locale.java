package org.example.rest.resources;

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

    public static Locale create(String value) {
        switch (requireNonNull(value)) {
            case "da":
                return DA;
            case "de":
                return DE;
            case "en-GB":
                return EN_GB;
            case "en-US":
                return EN_US;
            case "es-ES":
                return ES_ES;
            case "fr":
                return FR;
            case "hr":
                return HR;
            case "it":
                return IT;
            case "lt":
                return LT;
            case "hu":
                return HU;
            case "nl":
                return NL;
            case "no":
                return NO;
            case "pl":
                return PL;
            case "pt-BR":
                return PT_BR;
            case "ro":
                return RO;
            case "fi":
                return FI;
            case "sv-SE":
                return SV_SE;
            case "vi":
                return VI;
            case "tr":
                return TR;
            case "cs":
                return CS;
            case "el":
                return EL;
            case "bg":
                return BG;
            case "ru":
                return RU;
            case "uk":
                return UK;
            case "hi":
                return HI;
            case "th":
                return TH;
            case "zh-CN":
                return ZH_CN;
            case "ja":
                return JA;
            case "zh-TW":
                return ZH_TW;
            case "ko":
                return KO;
            default:
                throw new IllegalArgumentException();
        }
    }

    Locale(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
