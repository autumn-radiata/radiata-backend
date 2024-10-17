package radiata.common.domain.payment.constant;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 금융결제원 기준 은행 및 증권사 코드
 */
@Getter
@RequiredArgsConstructor
public enum Bank {

    // 은행 목록
    KYONGNAMBANK("경남은행", "039"),
    GWANGJUBANK("광주은행", "034"),
    LOCALNONGHYEOP("단위농협(지역농축협)", "012"),
    BUSANBANK("부산은행", "032"),
    SAEMAUL("새마을금고", "045"),
    SANLIM("산림조합", "064"),
    SHINHAN("신한은행", "088"),
    SHINHYEOP("신협", "048"),
    CITI("씨티은행", "027"),
    WOORI("우리은행", "020"),
    POST("우체국예금보험", "071"),
    SAVINGBANK("저축은행중앙회", "050"),
    JEONBUKBANK("전북은행", "037"),
    JEJUBANK("제주은행", "035"),
    KAKAOBANK("카카오뱅크", "090"),
    KBANK("케이뱅크", "089"),
    TOSSBANK("토스뱅크", "092"),
    HANA("하나은행", "081"),
    HSBC("홍콩상하이은행", "054"),
    IBK("IBK기업은행", "003"),
    KOOKMIN("KB국민은행", "004"),
    DAEGUBANK("DGB대구은행", "031"),
    KDBBANK("KDB산업은행", "002"),
    NONGHYEOP("NH농협은행", "011"),
    SC("SC제일은행", "023"),
    SUHYEOP("Sh수협은행", "007"),

    // 증권사 목록
    KYOBO_SECURITIES("교보증권", "261"),
    DAISHIN_SECURITIES("대신증권", "267"),
    MERITZ_SECURITIES("메리츠증권", "287"),
    MIRAE_ASSET_SECURITIES("미래에셋증권", "238"),
    BOOKOOK_SECURITIES("부국증권", "290"),
    SAMSUNG_SECURITIES("삼성증권", "240"),
    SHINYOUNG_SECURITIES("신영증권", "291"),
    SHINHAN_INVESTMENT("신한금융투자", "278"),
    YUANTA_SECURITIES("유안타증권", "209"),
    EUGENE_INVESTMENT_AND_SECURITIES("유진투자증권", "280"),
    KAKAOPAY_SECURITIES("카카오페이증권", "288"),
    KIWOOM("키움증권", "264"),
    TOSS_SECURITIES("토스증권", "271"),
    KOREA_FOSS_SECURITIES("펀드온라인코리아(한국포스증권)", "294"),
    HANA_INVESTMENT_AND_SECURITIES("하나금융투자", "270"),
    HI_INVESTMENT_AND_SECURITIES("하이투자증권", "262"),
    KOREA_INVESTMENT_AND_SECURITIES("한국투자증권", "243"),
    HANHWA_INVESTMENT_AND_SECURITIES("한화투자증권", "269"),
    HYUNDAI_MOTOR_SECURITIES("현대차증권", "263"),
    DB_INVESTMENT_AND_SECURITIES("DB금융투자", "279"),
    KB_SECURITIES("KB증권", "218"),
    DAOL_INVESTMENT_AND_SECURITIES("KTB투자증권(다올투자증권)", "227"),
    LIG_INVESTMENT_AND_SECURITIES("LIG투자증권", "292"),
    NH_INVESTMENT_AND_SECURITIES("NH투자증권", "247"),
    SK_SECURITIES("SK증권", "266");

    private final String name;
    private final String code;

    public static Optional<Bank> findByCode(String code) {
        return Stream.of(values())
            .filter(bank -> bank.getCode().equals(code))
            .findAny();
    }
}