package com.github.jobop.anylog.tools.encoders;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64 {
  private static final byte[] encodingTable = {
      (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F',
      (byte) 'G',
      (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M',
      (byte) 'N',
      (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T',
      (byte) 'U',
      (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z',
      (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
      (byte) 'g',
      (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm',
      (byte) 'n',
      (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't',
      (byte) 'u',
      (byte) 'v',
      (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
      (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
      (byte) '6',
      (byte) '7', (byte) '8', (byte) '9',
      (byte) '+', (byte) '/'
  };
  
  public static String toBase64String(byte[] data) {
	  return new String(encode(data));
  }

  /**
   * encode the input data producing a base 64 encoded byte array.
   *
   * @return a byte array containing the base 64 encoded data.
   */
  public static byte[] encode(byte[] data) {
    byte[] bytes;

    int modulus = data.length % 3;
    if (modulus == 0) {
      bytes = new byte[4 * data.length / 3];
    }
    else {
      bytes = new byte[4 * ( (data.length / 3) + 1)];
    }

    int dataLength = (data.length - modulus);
    int a1, a2, a3;
    for (int i = 0, j = 0; i < dataLength; i += 3, j += 4) {
      a1 = data[i] & 0xff;
      a2 = data[i + 1] & 0xff;
      a3 = data[i + 2] & 0xff;

      bytes[j] = encodingTable[ (a1 >>> 2) & 0x3f];
      bytes[j + 1] = encodingTable[ ( (a1 << 4) | (a2 >>> 4)) & 0x3f];
      bytes[j + 2] = encodingTable[ ( (a2 << 2) | (a3 >>> 6)) & 0x3f];
      bytes[j + 3] = encodingTable[a3 & 0x3f];
    }

    /*
     * process the tail end.
     */
    int b1, b2, b3;
    int d1, d2;

    switch (modulus) {
      case 0: /* nothing left to do */
        break;
      case 1:
        d1 = data[data.length - 1] & 0xff;
        b1 = (d1 >>> 2) & 0x3f;
        b2 = (d1 << 4) & 0x3f;

        bytes[bytes.length - 4] = encodingTable[b1];
        bytes[bytes.length - 3] = encodingTable[b2];
        bytes[bytes.length - 2] = (byte) '=';
        bytes[bytes.length - 1] = (byte) '=';
        break;
      case 2:
        d1 = data[data.length - 2] & 0xff;
        d2 = data[data.length - 1] & 0xff;

        b1 = (d1 >>> 2) & 0x3f;
        b2 = ( (d1 << 4) | (d2 >>> 4)) & 0x3f;
        b3 = (d2 << 2) & 0x3f;

        bytes[bytes.length - 4] = encodingTable[b1];
        bytes[bytes.length - 3] = encodingTable[b2];
        bytes[bytes.length - 2] = encodingTable[b3];
        bytes[bytes.length - 1] = (byte) '=';
        break;
    }

    return bytes;
  }

  /*
   * set up the decoding table.
   */
  private static final byte[] decodingTable;

  static {
    decodingTable = new byte[128];

    for (int i = 'A'; i <= 'Z'; i++) {
      decodingTable[i] = (byte) (i - 'A');
    }

    for (int i = 'a'; i <= 'z'; i++) {
      decodingTable[i] = (byte) (i - 'a' + 26);
    }

    for (int i = '0'; i <= '9'; i++) {
      decodingTable[i] = (byte) (i - '0' + 52);
    }

    decodingTable['+'] = 62;
    decodingTable['/'] = 63;
  }

  /**
       * decode the base 64 encoded input data. It is assumed the input data is valid.
   *
   * @return a byte array representing the decoded data.
   */
  public static byte[] decode(
      byte[] data) {
    byte[] bytes;
    byte b1, b2, b3, b4;

    if (data[data.length - 2] == '=') {
      bytes = new byte[ ( ( (data.length / 4) - 1) * 3) + 1];
    }
    else if (data[data.length - 1] == '=') {
      bytes = new byte[ ( ( (data.length / 4) - 1) * 3) + 2];
    }
    else {
      bytes = new byte[ ( (data.length / 4) * 3)];
    }

    for (int i = 0, j = 0; i < data.length - 4; i += 4, j += 3) {
      b1 = decodingTable[data[i]];
      b2 = decodingTable[data[i + 1]];
      b3 = decodingTable[data[i + 2]];
      b4 = decodingTable[data[i + 3]];

      bytes[j] = (byte) ( (b1 << 2) | (b2 >> 4));
      bytes[j + 1] = (byte) ( (b2 << 4) | (b3 >> 2));
      bytes[j + 2] = (byte) ( (b3 << 6) | b4);
    }

    if (data[data.length - 2] == '=') {
      b1 = decodingTable[data[data.length - 4]];
      b2 = decodingTable[data[data.length - 3]];

      bytes[bytes.length - 1] = (byte) ( (b1 << 2) | (b2 >> 4));
    }
    else if (data[data.length - 1] == '=') {
      b1 = decodingTable[data[data.length - 4]];
      b2 = decodingTable[data[data.length - 3]];
      b3 = decodingTable[data[data.length - 2]];

      bytes[bytes.length - 2] = (byte) ( (b1 << 2) | (b2 >> 4));
      bytes[bytes.length - 1] = (byte) ( (b2 << 4) | (b3 >> 2));
    }
    else {
      b1 = decodingTable[data[data.length - 4]];
      b2 = decodingTable[data[data.length - 3]];
      b3 = decodingTable[data[data.length - 2]];
      b4 = decodingTable[data[data.length - 1]];

      bytes[bytes.length - 3] = (byte) ( (b1 << 2) | (b2 >> 4));
      bytes[bytes.length - 2] = (byte) ( (b2 << 4) | (b3 >> 2));
      bytes[bytes.length - 1] = (byte) ( (b3 << 6) | b4);
    }

    return bytes;
  }

  private static boolean ignore(
      char c) {
    return (c == '\n' || c == '\r' || c == '\t' || c == ' ');
  }

  /**
   * decode the base 64 encoded String data - whitespace will be ignored.
   *
   * @return a byte array representing the decoded data.
   */
  public static byte[] decode(
      String data) {
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();

    try {
      decode(data, bOut);
    }
    catch (IOException e) {
      throw new RuntimeException("exception decoding base64 string: " + e);
    }

    return bOut.toByteArray();
  }

  /**
       * decode the base 64 encoded String data writing it to the given output stream,
   * whitespace characters will be ignored.
   *
   * @return the number of bytes produced.
   */
  public static int decode(
      String data,
      OutputStream out) throws IOException {
    byte[] bytes;
    byte b1, b2, b3, b4;
    int length = 0;

    int end = data.length();

    while (end > 0) {
      if (!ignore(data.charAt(end - 1))) {
        break;
      }

      end--;
    }

    for (int i = 0; i < end - 4; i += 4) {
      if (ignore(data.charAt(i))) {
        continue;
      }

      b1 = decodingTable[data.charAt(i)];
      b2 = decodingTable[data.charAt(i + 1)];
      b3 = decodingTable[data.charAt(i + 2)];
      b4 = decodingTable[data.charAt(i + 3)];

      out.write( (b1 << 2) | (b2 >> 4));
      out.write( (b2 << 4) | (b3 >> 2));
      out.write( (b3 << 6) | b4);

      length += 3;
    }

    if (data.charAt(end - 2) == '=') {
      b1 = decodingTable[data.charAt(end - 4)];
      b2 = decodingTable[data.charAt(end - 3)];

      out.write( (b1 << 2) | (b2 >> 4));

      length += 1;
    }
    else if (data.charAt(end - 1) == '=') {
      b1 = decodingTable[data.charAt(end - 4)];
      b2 = decodingTable[data.charAt(end - 3)];
      b3 = decodingTable[data.charAt(end - 2)];

      out.write( (b1 << 2) | (b2 >> 4));
      out.write( (b2 << 4) | (b3 >> 2));

      length += 2;
    }
    else {
      b1 = decodingTable[data.charAt(end - 4)];
      b2 = decodingTable[data.charAt(end - 3)];
      b3 = decodingTable[data.charAt(end - 2)];
      b4 = decodingTable[data.charAt(end - 1)];

      out.write( (b1 << 2) | (b2 >> 4));
      out.write( (b2 << 4) | (b3 >> 2));
      out.write( (b3 << 6) | b4);

      length += 3;
    }

    return length;
  }
}