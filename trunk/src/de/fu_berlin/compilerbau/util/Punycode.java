package de.fu_berlin.compilerbau.util;

import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

// http://stackoverflow.com/questions/183485/can-anyone-recommend-a-good-free-javascript-for-punycode-to-unicode-conversion

public class Punycode {
	
	private static final String NL = "\n";
	private static final String CODE =
		"//Javascript UTF16 converter created by some@domain.name" + NL +
		"//This implementation is released to public domain" + NL +
		"var utf16 = {" + NL +
		"    decode:function(input){" + NL +
		"        var output = [], i=0, len=input.length,value,extra;" + NL +
		"        while (i < len) {" + NL +
		"                value = input.charCodeAt(i++);" + NL +
		"                if ((value & 0xF800) === 0xD800) {" + NL +
		"                        extra = input.charCodeAt(i++);" + NL +
		"                        if ( ((value & 0xFC00) !== 0xD800) || ((extra & 0xFC00) !== 0xDC00) ) {" + NL +
		"                                throw new RangeError(\"UTF-16(decode): Illegal UTF-16 sequence\");" + NL +
		"                        }" + NL +
		"                        value = ((value & 0x3FF) << 10) + (extra & 0x3FF) + 0x10000;" + NL +
		"                }" + NL +
		"                output.push(value);" + NL +
		"        }" + NL +
		"        return output;" + NL +
		"    }," + NL +
		"    encode:function(input){" + NL +
		"        var output = [], i=0, len=input.length,value;" + NL +
		"        while (i < len) {" + NL +
		"                value = input[i++];" + NL +
		"                if ( (value & 0xF800) === 0xD800 ) {" + NL +
		"                        throw new RangeError(\"UTF-16(encode): Illegal UTF-16 value\");" + NL +
		"                }" + NL +
		"                if (value > 0xFFFF) {" + NL +
		"                        value -= 0x10000;" + NL +
		"                        output.push(String.fromCharCode(((value >>>10) & 0x3FF) | 0xD800));" + NL +
		"                        value = 0xDC00 | (value & 0x3FF);" + NL +
		"                }" + NL +
		"                output.push(String.fromCharCode(value));" + NL +
		"        }" + NL +
		"        return output.join(\"\");" + NL +
		"    }" + NL +
		"}" + NL +
		" " + NL +
		"//Javascript Punycode converter derived from example in RFC3492." + NL +
		"//This implementation is created by some@domain.name and released to public domain    " + NL +
		"var punycode = new function Punycode() {" + NL +
		"    var initial_n = 0x80;" + NL +
		"    var initial_bias = 72;" + NL +
		"    var delimiter = '$';" + NL +
		"    var base = 36;" + NL +
		"    var damp = 700;" + NL +
		"    var tmin=1;" + NL +
		"    var tmax=26;" + NL +
		"    var skew=38;" + NL +
		" " + NL +
		"    var maxint = 0x7FFFFFFF;" + NL +
		"    // decode_digit(cp) returns the numeric value of a basic code " + NL +
		"    // point (for use in representing integers) in the range 0 to" + NL +
		"    // base-1, or base if cp is does not represent a value." + NL +
		" " + NL +
		"    function decode_digit(cp) {" + NL +
		"        return  cp - 48 < 10 ? cp - 22 :  cp - 65 < 26 ? cp - 65 : cp - 97 < 26 ? cp - 97 : base;" + NL +
		"    }" + NL +
		" " + NL +
		"    // encode_digit(d,flag) returns the basic code point whose value      " + NL +
		"    // (when used for representing integers) is d, which needs to be in   " + NL +
		"    // the range 0 to base-1.  The lowercase form is used unless flag is  " + NL +
		"    // nonzero, in which case the uppercase form is used.  The behavior   " + NL +
		"    // is undefined if flag is nonzero and digit d has no uppercase form. " + NL +
		" " + NL +
		"    function encode_digit(d, flag) {" + NL +
		"        return d + 22 + 75 * (d < 26) - ((flag != 0) << 5);" + NL +
		"        //  0..25 map to ASCII a..z or A..Z " + NL +
		"        // 26..35 map to ASCII 0..9         " + NL +
		"    }" + NL +
		"    //** Bias adaptation function **" + NL +
		"    function adapt(delta, numpoints, firsttime ) {" + NL +
		"        var k;" + NL +
		"        delta = firsttime ? Math.floor(delta / damp) : (delta >> 1);" + NL +
		"        delta += Math.floor(delta / numpoints);" + NL +
		" " + NL +
		"        for (k = 0;  delta > (((base - tmin) * tmax) >> 1);  k += base) {" + NL +
		"                delta = Math.floor(delta / ( base - tmin ));" + NL +
		"        }" + NL +
		"        return Math.floor(k + (base - tmin + 1) * delta / (delta + skew));" + NL +
		"    }" + NL +
		" " + NL +
		"    // encode_basic(bcp,flag) forces a basic code point to lowercase if flag is zero," + NL +
		"    // uppercase if flag is nonzero, and returns the resulting code point." + NL +
		"    // The code point is unchanged if it  is caseless." + NL +
		"    // The behavior is undefined if bcp is not a basic code point.                                                   " + NL +
		" " + NL +
		"    function encode_basic(bcp, flag) {" + NL +
		"        bcp -= (bcp - 97 < 26) << 5;" + NL +
		"        return bcp + ((!flag && (bcp - 65 < 26)) << 5);" + NL +
		"    }" + NL +
		" " + NL +
		"    // Main decode" + NL +
		"    this.decode=function(input,preserveCase) {" + NL +
		"        // Dont use uft16" + NL +
		"        var output=[];" + NL +
		"        var case_flags=[];" + NL +
		"        var input_length = input.length;" + NL +
		" " + NL +
		"        var n, out, i, bias, basic, j, ic, oldi, w, k, digit, t, len;" + NL +
		" " + NL +
		"        // Initialize the state: " + NL +
		" " + NL +
		"        n = initial_n;" + NL +
		"        i = 0;" + NL +
		"        bias = initial_bias;" + NL +
		" " + NL +
		"        // Handle the basic code points:  Let basic be the number of input code " + NL +
		"        // points before the last delimiter, or 0 if there is none, then    " + NL +
		"        // copy the first basic code points to the output.                      " + NL +
		" " + NL +
		"        basic = input.lastIndexOf(delimiter);" + NL +
		"        if (basic < 0) basic = 0;" + NL +
		" " + NL +
		"        for (j = 0;  j < basic;  ++j) {" + NL +
		"                if(preserveCase) case_flags[output.length] = ( input.charCodeAt(j) -65 < 26);" + NL +
		"                if ( input.charCodeAt(j) >= 0x80) {" + NL +
		"                        throw new RangeError(\"Illegal input >= 0x80\");" + NL +
		"                }" + NL +
		"                output.push( input.charCodeAt(j) );" + NL +
		"        }" + NL +
		" " + NL +
		"        // Main decoding loop:  Start just after the last delimiter if any  " + NL +
		"        // basic code points were copied; start at the beginning otherwise. " + NL +
		" " + NL +
		"        for (ic = basic > 0 ? basic + 1 : 0;  ic < input_length; ) {" + NL +
		" " + NL +
		"                // ic is the index of the next character to be consumed," + NL +
		" " + NL +
		"                // Decode a generalized variable-length integer into delta,  " + NL +
		"                // which gets added to i.  The overflow checking is easier   " + NL +
		"                // if we increase i as we go, then subtract off its starting " + NL +
		"                // value at the end to obtain delta." + NL +
		"                for (oldi = i, w = 1, k = base;  ;  k += base) {" + NL +
		"                        if (ic >= input_length) {" + NL +
		"                                throw RangeError (\"punycode_bad_input(1)\");" + NL +
		"                        }" + NL +
		"                        digit = decode_digit(input.charCodeAt(ic++));" + NL +
		" " + NL +
		"                        if (digit >= base) {" + NL +
		"                                throw RangeError(\"punycode_bad_input(2)\");" + NL +
		"                        }" + NL +
		"                        if (digit > Math.floor((maxint - i) / w)) {" + NL +
		"                                throw RangeError (\"punycode_overflow(1)\");" + NL +
		"                        }" + NL +
		"                        i += digit * w;" + NL +
		"                        t = k <= bias ? tmin : k >= bias + tmax ? tmax : k - bias;" + NL +
		"                        if (digit < t) { break; }" + NL +
		"                        if (w > Math.floor(maxint / (base - t))) {" + NL +
		"                                throw RangeError(\"punycode_overflow(2)\");" + NL +
		"                        }" + NL +
		"                        w *= (base - t);" + NL +
		"                }" + NL +
		" " + NL +
		"                out = output.length + 1;" + NL +
		"                bias = adapt(i - oldi, out, oldi === 0);" + NL +
		" " + NL +
		"                // i was supposed to wrap around from out to 0,   " + NL +
		"                // incrementing n each time, so we'll fix that now: " + NL +
		"                if ( Math.floor(i / out) > maxint - n) {" + NL +
		"                        throw RangeError(\"punycode_overflow(3)\");" + NL +
		"                }" + NL +
		"                n += Math.floor( i / out ) ;" + NL +
		"                i %= out;" + NL +
		" " + NL +
		"                // Insert n at position i of the output: " + NL +
		"                // Case of last character determines uppercase flag: " + NL +
		"                if (preserveCase) { case_flags.splice(i, 0, input.charCodeAt(ic -1)  -65 < 26);}" + NL +
		" " + NL +
		"                output.splice(i, 0, n);" + NL +
		"                i++;" + NL +
		"        }" + NL +
		"        if (preserveCase) {" + NL +
		"                for (i = 0, len = output.length; i < len; i++) {" + NL +
		"                        if (case_flags[i]) {" + NL +
		"                                output[i] = (String.fromCharCode(output[i]).toUpperCase()).charCodeAt(0);" + NL +
		"                        }" + NL +
		"                }" + NL +
		"        }" + NL +
		"        return utf16.encode(output);            " + NL +
		"    };" + NL +
		" " + NL +
		"    //** Main encode function **" + NL +
		" " + NL +
		"    this.encode = function (input,preserveCase) {" + NL +
		"        //** Bias adaptation function **" + NL +
		" " + NL +
		"        var n, delta, h, b, bias, j, m, q, k, t, ijv, case_flags;" + NL +
		" " + NL +
		"        if (preserveCase) {" + NL +
		"                // Preserve case, step1 of 2: Get a list of the unaltered string" + NL +
		"                case_flags = utf16.decode(input);" + NL +
		"        }" + NL +
		"        // Converts the input in UTF-16 to Unicode" + NL +
		"        input = utf16.decode(input.toLowerCase());" + NL +
		"        //input = utf16.decode(input);" + NL +
		" " + NL +
		"        var input_length = input.length; // Cache the length" + NL +
		" " + NL +
		"        if (preserveCase) {" + NL +
		"                // Preserve case, step2 of 2: Modify the list to true/false" + NL +
		"                for (j=0; j < input_length; j++) {" + NL +
		"                        case_flags[j] = input[j] != case_flags[j];" + NL +
		"                }" + NL +
		"                }" + NL +
		" " + NL +
		"        var output=[];" + NL +
		" " + NL +
		"        // Initialize the state: " + NL +
		"        n = initial_n;" + NL +
		"        delta = 0;" + NL +
		"        bias = initial_bias;" + NL +
		" " + NL +
		"        // Handle the basic code points: " + NL +
		"        for (j = 0;  j < input_length;  ++j) {" + NL +
		"                if ( input[j] < 0x80) {" + NL +
		"                        output.push(" + NL +
		"                                String.fromCharCode(" + NL +
		"                                        case_flags ? encode_basic(input[j], case_flags[j]) : input[j]" + NL +
		"                                )" + NL +
		"                        );" + NL +
		"                }" + NL +
		"        }" + NL +
		" " + NL +
		"        h = b = output.length;" + NL +
		" " + NL +
		"        // h is the number of code points that have been handled, b is the  " + NL +
		"        // number of basic code points " + NL +
		" " + NL +
		"        if (b > 0) output.push(delimiter);" + NL +
		" " + NL +
		"        // Main encoding loop: " + NL +
		"        //" + NL +
		"        while (h < input_length) {" + NL +
		"                // All non-basic code points < n have been     " + NL +
		"                // handled already. Find the next larger one: " + NL +
		" " + NL +
		"                for (m = maxint, j = 0;  j < input_length;  ++j) {" + NL +
		"                        ijv = input[j];" + NL +
		"                        if (ijv >= n && ijv < m) m = ijv;" + NL +
		"                }" + NL +
		" " + NL +
		"                // Increase delta enough to advance the decoder's    " + NL +
		"                // <n,i> state to <m,0>, but guard against overflow: " + NL +
		" " + NL +
		"                if (m - n > Math.floor((maxint - delta) / (h + 1))) {" + NL +
		"                        throw RangeError(\"punycode_overflow (1)\");" + NL +
		"                }" + NL +
		"                delta += (m - n) * (h + 1);" + NL +
		"                n = m;" + NL +
		" " + NL +
		"                for (j = 0;  j < input_length;  ++j) {" + NL +
		"                        ijv = input[j];" + NL +
		" " + NL +
		"                        if (ijv < n ) {" + NL +
		"                                if (++delta > maxint) return Error(\"punycode_overflow(2)\");" + NL +
		"                        }" + NL +
		" " + NL +
		"                        if (ijv == n) {" + NL +
		"                                // Represent delta as a generalized variable-length integer: " + NL +
		"                                for (q = delta, k = base;  ;  k += base) {" + NL +
		"                                        t = k <= bias ? tmin : k >= bias + tmax ? tmax : k - bias;" + NL +
		"                                        if (q < t) break;" + NL +
		"                                        output.push( String.fromCharCode(encode_digit(t + (q - t) % (base - t), 0)) );" + NL +
		"                                        q = Math.floor( (q - t) / (base - t) );" + NL +
		"                                }" + NL +
		"                                output.push( String.fromCharCode(encode_digit(q, preserveCase && case_flags[j] ? 1:0 )));" + NL +
		"                                bias = adapt(delta, h + 1, h == b);" + NL +
		"                                delta = 0;" + NL +
		"                                ++h;" + NL +
		"                        }" + NL +
		"                }" + NL +
		" " + NL +
		"                ++delta, ++n;" + NL +
		"        }" + NL +
		" " + NL +
		"        if(output.length == input.length+1) {" + NL +
		"            output.pop();" + NL +
		"            return output.join(\"\");" + NL +
		"        } else {" + NL +
		"            output.unshift(\"$Z\");" + NL +
		"            return output.join(\"\");" + NL +
		"        }" + NL +
		"    }" + NL +
		"}();" + NL +
		" " + NL +
		"function encode(s) {" + NL +
		"    return punycode.encode(s, true);" + NL +
		"}" + NL +
		" " + NL +
		"function decode(s) {" + NL +
		"    return punycode.decode(s, true);" + NL +
		"}" + NL;
		
	private static final ScriptEngine javascriptEngine;
	static {
		try {
			final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
			javascriptEngine = scriptEngineManager.getEngineByMimeType("text/javascript");
			javascriptEngine.eval(CODE);
		} catch(Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static final Map<String,String> encoded = new HashMap<String,String>();
	public static String encode(String str) {
		String result = encoded.get(str);
		if(result == null) {
			try {
				result = ((Invocable)javascriptEngine).invokeFunction("encode", str).toString();
			} catch (ScriptException e) {
				return null;
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
			encoded.put(str, result);
		}
		return result;
	}
	
}