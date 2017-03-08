/*
 * Copyright 2017 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sbgapps.simplenumberpicker.utils;

public class HexaUtil {

    final private static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String intToHex(int value, int length) {
        String ret = Integer.toHexString(value);
        while (ret.length() < length) ret = "0" + ret;
        return ret.toUpperCase();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    static public byte[] hexToBytes(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static String hexToAscii(String hex) {
        int length = hex.length();
        StringBuilder ascii = new StringBuilder(length / 2);
        for (int i = 0; i < length; i += 2) {
            String str = hex.substring(i, i + 2);
            if (str.equals("00")) break;
            ascii.append((char) Integer.parseInt(str, 16));
        }
        return ascii.toString();
    }

    public static String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) hex.append(intToHex((int) aChar, 2));
        return hex.toString().toUpperCase();
    }
}
