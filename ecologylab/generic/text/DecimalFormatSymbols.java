/*
 * @(#)DecimalFormatSymbols.java	1.41 04/05/10
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * (C) Copyright Taligent, Inc. 1996, 1997 - All Rights Reserved
 * (C) Copyright IBM Corp. 1996 - 1998 - All Rights Reserved
 *
 *   The original version of this source code and documentation is copyrighted
 * and owned by Taligent, Inc., a wholly-owned subsidiary of IBM. These
 * materials are provided under terms of a License Agreement between Taligent
 * and Sun. This technology is protected by multiple US and International
 * patents. This notice and attribution to Taligent may not be removed.
 *   Taligent is a registered trademark of Taligent, Inc.
 *
 */

package ecologylab.generic.text;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Hashtable;

/**
 * This class represents the set of symbols (such as the decimal separator, the
 * grouping separator, and so on) needed by <code>DecimalFormat</code> to
 * format numbers. <code>DecimalFormat</code> creates for itself an instance
 * of <code>DecimalFormatSymbols</code> from its locale data. If you need to
 * change any of these symbols, you can get the
 * <code>DecimalFormatSymbols</code> object from your
 * <code>DecimalFormat</code> and modify it.
 * 
 * @see java.util.Locale
 * @see DecimalFormat
 * @version 1.41, 05/10/04
 * @author Mark Davis
 * @author Alan Liu
 */

final public class DecimalFormatSymbols implements Cloneable, Serializable
{

	/**
	 * Create a DecimalFormatSymbols object.
	 */
	public DecimalFormatSymbols()
	{
		initialize();
	}

	/**
	 * Gets the character used for zero. Different for Arabic, etc.
	 */
	public char getZeroDigit()
	{
		return zeroDigit;
	}

	/**
	 * Sets the character used for zero. Different for Arabic, etc.
	 */
	public void setZeroDigit(char zeroDigit)
	{
		this.zeroDigit = zeroDigit;
	}

	/**
	 * Gets the character used for thousands separator. Different for French,
	 * etc.
	 */
	public char getGroupingSeparator()
	{
		return groupingSeparator;
	}

	/**
	 * Sets the character used for thousands separator. Different for French,
	 * etc.
	 */
	public void setGroupingSeparator(char groupingSeparator)
	{
		this.groupingSeparator = groupingSeparator;
	}

	/**
	 * Gets the character used for decimal sign. Different for French, etc.
	 */
	public char getDecimalSeparator()
	{
		return decimalSeparator;
	}

	/**
	 * Sets the character used for decimal sign. Different for French, etc.
	 */
	public void setDecimalSeparator(char decimalSeparator)
	{
		this.decimalSeparator = decimalSeparator;
	}

	/**
	 * Gets the character used for per mille sign. Different for Arabic, etc.
	 */
	public char getPerMill()
	{
		return perMill;
	}

	/**
	 * Sets the character used for per mille sign. Different for Arabic, etc.
	 */
	public void setPerMill(char perMill)
	{
		this.perMill = perMill;
	}

	/**
	 * Gets the character used for percent sign. Different for Arabic, etc.
	 */
	public char getPercent()
	{
		return percent;
	}

	/**
	 * Sets the character used for percent sign. Different for Arabic, etc.
	 */
	public void setPercent(char percent)
	{
		this.percent = percent;
	}

	/**
	 * Gets the character used for a digit in a pattern.
	 */
	public char getDigit()
	{
		return digit;
	}

	/**
	 * Sets the character used for a digit in a pattern.
	 */
	public void setDigit(char digit)
	{
		this.digit = digit;
	}

	/**
	 * Gets the character used to separate positive and negative subpatterns in a
	 * pattern.
	 */
	public char getPatternSeparator()
	{
		return patternSeparator;
	}

	/**
	 * Sets the character used to separate positive and negative subpatterns in a
	 * pattern.
	 */
	public void setPatternSeparator(char patternSeparator)
	{
		this.patternSeparator = patternSeparator;
	}

	/**
	 * Gets the string used to represent infinity. Almost always left unchanged.
	 */
	public String getInfinity()
	{
		return infinity;
	}

	/**
	 * Sets the string used to represent infinity. Almost always left unchanged.
	 */
	public void setInfinity(String infinity)
	{
		this.infinity = infinity;
	}

	/**
	 * Gets the string used to represent "not a number". Almost always left
	 * unchanged.
	 */
	public String getNaN()
	{
		return NaN;
	}

	/**
	 * Sets the string used to represent "not a number". Almost always left
	 * unchanged.
	 */
	public void setNaN(String NaN)
	{
		this.NaN = NaN;
	}

	/**
	 * Gets the character used to represent minus sign. If no explicit negative
	 * format is specified, one is formed by prefixing minusSign to the positive
	 * format.
	 */
	public char getMinusSign()
	{
		return minusSign;
	}

	/**
	 * Sets the character used to represent minus sign. If no explicit negative
	 * format is specified, one is formed by prefixing minusSign to the positive
	 * format.
	 */
	public void setMinusSign(char minusSign)
	{
		this.minusSign = minusSign;
	}

	/**
	 * Returns the currency symbol for the currency of these DecimalFormatSymbols
	 * in their locale.
	 * 
	 * @since 1.2
	 */
	public String getCurrencySymbol()
	{
		return currencySymbol;
	}

	/**
	 * Sets the currency symbol for the currency of these DecimalFormatSymbols in
	 * their locale.
	 * 
	 * @since 1.2
	 */
	public void setCurrencySymbol(String currency)
	{
		currencySymbol = currency;
	}

	/**
	 * Returns the ISO 4217 currency code of the currency of these
	 * DecimalFormatSymbols.
	 * 
	 * @since 1.2
	 */
	public String getInternationalCurrencySymbol()
	{
		return intlCurrencySymbol;
	}

	/**
	 * Sets the ISO 4217 currency code of the currency of these
	 * DecimalFormatSymbols. If the currency code is valid (as defined by
	 * {@link java.util.Currency#getInstance(java.lang.String) Currency.getInstance}),
	 * this also sets the currency attribute to the corresponding Currency
	 * instance and the currency symbol attribute to the currency's symbol in the
	 * DecimalFormatSymbols' locale. If the currency code is not valid, then the
	 * currency attribute is set to null and the currency symbol attribute is not
	 * modified.
	 * 
	 * @see #setCurrency
	 * @see #setCurrencySymbol
	 * @since 1.2
	 */
	public void setInternationalCurrencySymbol(String currencyCode)
	{
		intlCurrencySymbol = currencyCode;
		currency = null;
		if (currencyCode != null)
		{
			try
			{
				currency = Currency.getInstance(currencyCode);
				currencySymbol = currency.getSymbol();
			}
			catch (IllegalArgumentException e)
			{
			}
		}
	}

	/**
	 * Gets the currency of these DecimalFormatSymbols. May be null if the
	 * currency symbol attribute was previously set to a value that's not a valid
	 * ISO 4217 currency code.
	 * 
	 * @return the currency used, or null
	 * @since 1.4
	 */
	public Currency getCurrency()
	{
		return currency;
	}

	/**
	 * Returns the monetary decimal separator.
	 * 
	 * @since 1.2
	 */
	public char getMonetaryDecimalSeparator()
	{
		return monetarySeparator;
	}

	/**
	 * Sets the monetary decimal separator.
	 * 
	 * @since 1.2
	 */
	public void setMonetaryDecimalSeparator(char sep)
	{
		monetarySeparator = sep;
	}

	// ------------------------------------------------------------
	// BEGIN Package Private methods ... to be made public later
	// ------------------------------------------------------------

	/**
	 * Returns the character used to separate the mantissa from the exponent.
	 */
	char getExponentialSymbol()
	{
		return exponential;
	}

	/**
	 * Sets the character used to separate the mantissa from the exponent.
	 */
	void setExponentialSymbol(char exp)
	{
		exponential = exp;
	}

	// ------------------------------------------------------------
	// END Package Private methods ... to be made public later
	// ------------------------------------------------------------

	/**
	 * Standard override.
	 */
	public Object clone()
	{
		try
		{
			return (DecimalFormatSymbols) super.clone();
			// other fields are bit-copied
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Override equals.
	 */
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		DecimalFormatSymbols other = (DecimalFormatSymbols) obj;
		return (zeroDigit == other.zeroDigit
				&& groupingSeparator == other.groupingSeparator
				&& decimalSeparator == other.decimalSeparator
				&& percent == other.percent && perMill == other.perMill
				&& digit == other.digit && minusSign == other.minusSign
				&& patternSeparator == other.patternSeparator
				&& infinity.equals(other.infinity) && NaN.equals(other.NaN)
				&& currencySymbol.equals(other.currencySymbol)
				&& intlCurrencySymbol.equals(other.intlCurrencySymbol)
				&& currency == other.currency
				&& monetarySeparator == other.monetarySeparator);
	}

	/**
	 * Override hashCode.
	 */
	@Override public int hashCode()
	{
		int result = zeroDigit;
		result = result * 37 + groupingSeparator;
		result = result * 37 + decimalSeparator;
		return result;
	}

	/**
	 * Initializes the symbols from the LocaleElements resource bundle.
	 */
	private void initialize()
	{
		decimalSeparator = '.';
		groupingSeparator = ',';
		patternSeparator = ';';
		percent = '%';
		zeroDigit = '0';
		digit = '#';
		minusSign = '-';
		exponential = 'E';
		perMill = 'ä';
		infinity = "°";
		NaN = "NaN";
		currencySymbol = "$";
		intlCurrencySymbol = "USD";
	}

	/**
	 * Character used for zero.
	 * 
	 * @serial
	 * @see #getZeroDigit
	 */
	private char							zeroDigit;

	/**
	 * Character used for thousands separator.
	 * 
	 * @serial
	 * @see #getGroupingSeparator
	 */
	private char							groupingSeparator;

	/**
	 * Character used for decimal sign.
	 * 
	 * @serial
	 * @see #getDecimalSeparator
	 */
	private char							decimalSeparator;

	/**
	 * Character used for per mille sign.
	 * 
	 * @serial
	 * @see #getPerMill
	 */
	private char							perMill;

	/**
	 * Character used for percent sign.
	 * 
	 * @serial
	 * @see #getPercent
	 */
	private char							percent;

	/**
	 * Character used for a digit in a pattern.
	 * 
	 * @serial
	 * @see #getDigit
	 */
	private char							digit;

	/**
	 * Character used to separate positive and negative subpatterns in a pattern.
	 * 
	 * @serial
	 * @see #getPatternSeparator
	 */
	private char							patternSeparator;

	/**
	 * String used to represent infinity.
	 * 
	 * @serial
	 * @see #getInfinity
	 */
	private String							infinity;

	/**
	 * String used to represent "not a number".
	 * 
	 * @serial
	 * @see #getNaN
	 */
	private String							NaN;

	/**
	 * Character used to represent minus sign.
	 * 
	 * @serial
	 * @see #getMinusSign
	 */
	private char							minusSign;

	/**
	 * String denoting the local currency, e.g. "$".
	 * 
	 * @serial
	 * @see #getCurrencySymbol
	 */
	private String							currencySymbol;

	/**
	 * ISO 4217 currency code denoting the local currency, e.g. "USD".
	 * 
	 * @serial
	 * @see #getInternationalCurrencySymbol
	 */
	private String							intlCurrencySymbol;

	/**
	 * The decimal separator used when formatting currency values.
	 * 
	 * @serial
	 * @since JDK 1.1.6
	 * @see #getMonetaryDecimalSeparator
	 */
	private char							monetarySeparator;										// Field
																												// new
																												// in
																												// JDK
																												// 1.1.6

	/**
	 * The character used to distinguish the exponent in a number formatted in
	 * exponential notation, e.g. 'E' for a number such as "1.23E45".
	 * <p>
	 * Note that the public API provides no way to set this field, even though it
	 * is supported by the implementation and the stream format. The intent is
	 * that this will be added to the API in the future.
	 * 
	 * @serial
	 * @since JDK 1.1.6
	 */
	private char							exponential;												// Field
																												// new
																												// in
																												// JDK
																												// 1.1.6

	// currency; only the ISO code is serialized.
	private transient Currency			currency;

	// Proclaim JDK 1.1 FCS compatibility
	static final long						serialVersionUID			= 5772796243397350300L;

	// The internal serial version which says which version was written
	// - 0 (default) for version up to JDK 1.1.5
	// - 1 for version from JDK 1.1.6, which includes two new fields:
	// monetarySeparator and exponential.
	// - 2 for version from J2SE 1.4, which includes locale field.
	private static final int			currentSerialVersion		= 2;

	/**
	 * Describes the version of <code>DecimalFormatSymbols</code> present on
	 * the stream. Possible values are:
	 * <ul>
	 * <li><b>0</b> (or uninitialized): versions prior to JDK 1.1.6.
	 * 
	 * <li><b>1</b>: Versions written by JDK 1.1.6 or later, which include two
	 * new fields: <code>monetarySeparator</code> and <code>exponential</code>.
	 * <li><b>2</b>: Versions written by J2SE 1.4 or later, which include a new
	 * <code>locale</code> field.
	 * </ul>
	 * When streaming out a <code>DecimalFormatSymbols</code>, the most recent
	 * format (corresponding to the highest allowable
	 * <code>serialVersionOnStream</code>) is always written.
	 * 
	 * @serial
	 * @since JDK 1.1.6
	 */
	private int								serialVersionOnStream	= currentSerialVersion;

	/**
	 * cache to hold the NumberElements and the Currency of a Locale.
	 */
	private static final Hashtable	cachedLocaleData			= new Hashtable(3);
	
	public static void main (String[] args)
	{
		DecimalFormatSymbols s = new DecimalFormatSymbols();

		System.out.println(s.currencySymbol);
		System.out.println(s.decimalSeparator);
		System.out.println(s.digit);
		System.out.println(s.exponential);
		System.out.println(s.groupingSeparator);
		System.out.println(s.infinity);
		System.out.println(s.intlCurrencySymbol);
		System.out.println(s.minusSign);
		System.out.println(s.monetarySeparator);
		System.out.println(s.NaN);
		System.out.println(s.patternSeparator);
		System.out.println(s.percent);
		System.out.println(s.perMill);
		System.out.println(s.zeroDigit);
	}
}
