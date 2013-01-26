/**
 * 
 */
package simpl.core;

/**
 * XML character entity names for special markup.
 * @see http://www.evolt.org/article/ala/17/21234/
 * 
 * @author andruid
 *
 */
public interface SpecialCharacterEntities
{
	// greek to you and me. 
	// well, actually, greek to the standards people, but punctuation to you and me.
	public static final char BULL	= 8226;	// bullet
	public static final char HELLIP	= 8230;	// horizontal ellipsis
	public static final char PRIME	= 8242;	// primeminutes
	public static final char PRIME_U	= 8243;	// double prime
	public static final char OLINE	= 8254;	// overline
	public static final char FRASL	= 8260;	// fraction slash
	public static final char WEIERP	= 8472;	// script capital P
	public static final char IMAGE	= 8465;	// blackletter capital I
	public static final char REAL	= 8476;	// blackletter capital R
	public static final char TRADE	= 8482;	// trade mark sign
	public static final char ALEFSYM	= 8501;	// alef symbol
	public static final char LARR	= 8592;	// leftwards arrow
	public static final char UARR	= 8593;	// upwards arrow
	public static final char RARR	= 8594;	// rightwards arrow
	public static final char DARR	= 8595;	// downwards arrow
	public static final char HARR	= 8596;	// left right arrow
	public static final char CRARR	= 8629;	// downwards arrow with corner leftwards
	public static final char LARR_D	= 8656;	// leftwards double arrow
	public static final char UARR_D	= 8657;	// upwards double arrow
	public static final char RARR_D	= 8658;	// rightwards double arrow
	public static final char DARR_D	= 8659;	// downwards double arrow
	public static final char HARR_D	= 8660;	// left right double arrow
	public static final char FORALL	= 8704;	// for all
	public static final char PART	= 8706;	// partial differential
	public static final char EXIST	= 8707;	// there exists
	public static final char EMPTY	= 8709;	// empty set
	public static final char NABLA	= 8711;	// nabla
	public static final char ISIN	= 8712;	// element of
	public static final char NOTIN	= 8713;	// not an element of
	public static final char NI	= 8715;	// contains as member
	public static final char PROD	= 8719;	// n-ary product
	public static final char SUM	= 8721;	// n-ary sumation
	public static final char MINUS	= 8722;	// minus sign
	public static final char LOWAST	= 8727;	// asterisk operator
	public static final char RADIC	= 8730;	// square root
	public static final char PROP	= 8733;	// proportional to
	public static final char INFIN	= 8734;	// infinity
	public static final char ANG	= 8736;	// angle
	public static final char AND	= 8743;	// logical and
	public static final char OR	= 8744;	// logical or
	public static final char CAP	= 8745;	// intersection
	public static final char CUP	= 8746;	// union
	public static final char INT	= 8747;	// integral
	public static final char THERE4	= 8756;	// therefore
	public static final char SIM	= 8764;	// tilde operator
	public static final char CONG	= 8773;	// approximately equal to
	public static final char ASYMP	= 8776;	// almost equal to
	public static final char NE	= 8800;	// not equal to
	public static final char EQUIV	= 8801;	// identical to
	public static final char LE	= 8804;	// less-than or equal to
	public static final char GE	= 8805;	// greater-than or equal to
	public static final char SUB	= 8834;	// subset of
	public static final char SUP	= 8835;	// superset of
	public static final char NSUB	= 8836;	// not a subset of
	public static final char SUBE	= 8838;	// subset of or equal to
	public static final char SUPE	= 8839;	// superset of or equal to
	public static final char OPLUS	= 8853;	// circled plus
	public static final char OTIMES	= 8855;	// circled times
	public static final char PERP	= 8869;	// up tack
	public static final char SDOT	= 8901;	// dot operator
	public static final char LCEIL	= 8968;	// left ceiling
	public static final char RCEIL	= 8969;	// right ceiling
	public static final char LFLOOR	= 8970;	// left floor
	public static final char RFLOOR	= 8971;	// right floor
	public static final char LANG	= 9001;	// left-pointing angle bracket
	public static final char RANG	= 9002;	// right-pointing angle bracket
	public static final char LOZ	= 9674;	// lozenge
	public static final char SPADES	= 9824;	// black spade suit
	public static final char CLUBS	= 9827;	// black club suit
	public static final char HEARTS	= 9829;	// black heart suit
	public static final char DIAMS	= 9830;	// black diamond suit
	
	// special special charaters
	public static final char OELIG	= 338;	// latin capital ligature OE
	public static final char OELIG_S	= 339;	// latin small ligature oe
	public static final char SCARON	= 352;	// latin capital letter S with caron
	public static final char SCARON_S	= 353;	// latin small letter s with caron
	public static final char YUML	= 376;	// latin capital letter Y with diaeresis
	public static final char CIRC	= 710;	// modifier letter circumflex accent
	public static final char TILDE	= 732;	// small tilde
	public static final char ENSP	= 8194;	// space
	public static final char EMSP	= 8195;	// space
	public static final char THINSP	= 8201;	// n space
	public static final char ZWNJ	= 8204;	// zero width non-joiner
	public static final char ZWJ	= 8205;	// zero width joiner
	public static final char LRM	= 8206;	// left-to-right mark
	public static final char RLM	= 8207;	// right-to-left mark
	public static final char NDASH	= 8211;	// en dash
	public static final char MDASH	= 8212;	// em dash
	public static final char LSQUO	= 8216;	// left single quotation mark
	public static final char RSQUO	= 8217;	// right single quotation mark
	public static final char SBQUO	= 8218;	// single low-9 quotation mark
	public static final char LDQUO	= 8220;	// left double quotation mark
	public static final char RDQUO	= 8221;	// right double quotation mark
	public static final char BDQUO	= 8222;	// double low-9 quotation mark
	public static final char DAGGER	= 8224;	// dagger
	public static final char DAGGER_D	= 8225;	// double dagger
	public static final char PERMIL	= 8240;	// per mille sign
	public static final char LSAQUO	= 8249;	// single left-pointing angle quotation mark
	public static final char RSAQUO	= 8250;	// single right-pointing angle quotation mark
	public static final char EURO	= 8364;	// euro sign
	public static final char DBLRARR = 187; // double right arrow (ex: as found in imdb titles after More at IMDBPro)
	public static final char ANOTHER_DBL_QUOTE = 34; //Double quotation mark 
	public static final char ANOTHER_QUOTE = 39; //Single Quotation mark
	
	public static final char SPECIAL_CHARACTER_ENTITIES[]	=
	{
		BULL, HELLIP, PRIME, PRIME_U, OLINE, FRASL, WEIERP, IMAGE, REAL,
		TRADE, ALEFSYM, LARR, UARR, RARR, DARR, HARR, CRARR, LARR_D, UARR_D,
		RARR_D, DARR_D, HARR_D, FORALL, PART, EXIST, EMPTY, NABLA, ISIN,
		NOTIN, NI, PROD, SUM, MINUS, LOWAST, RADIC, PROP, INFIN, ANG, AND, OR,
		CAP, CUP, INT, THERE4, SIM, CONG, ASYMP, NE, EQUIV, LE, GE, SUB, SUP,
		NSUB, SUBE, SUPE, OPLUS, OTIMES, PERP, SDOT, LCEIL, RCEIL, LFLOOR,
		RFLOOR, LANG, RANG, LOZ, SPADES, CLUBS, HEARTS, DIAMS, OELIG, OELIG_S,
		SCARON, SCARON_S, YUML, CIRC, TILDE, ENSP, EMSP, THINSP, ZWNJ, ZWJ,
		LRM, RLM, NDASH, MDASH, LSQUO, RSQUO, SBQUO, LDQUO, RDQUO, BDQUO,
		DAGGER, DAGGER_D, PERMIL, LSAQUO, RSAQUO, EURO,  DBLRARR, ANOTHER_DBL_QUOTE, ANOTHER_QUOTE
		
	};
}
