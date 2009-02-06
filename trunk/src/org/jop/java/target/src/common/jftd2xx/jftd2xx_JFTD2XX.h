/* DO NOT EDIT THIS FILE - it is machine generated */
#include "jni.h"
/* Header for class jftd2xx_JFTD2XX */

#ifndef _Included_jftd2xx_JFTD2XX
#define _Included_jftd2xx_JFTD2XX
#ifdef __cplusplus
extern "C" {
#endif
#undef jftd2xx_JFTD2XX_FT_OK
#define jftd2xx_JFTD2XX_FT_OK 0L
#undef jftd2xx_JFTD2XX_FT_INVALID_HANDLE
#define jftd2xx_JFTD2XX_FT_INVALID_HANDLE 1L
#undef jftd2xx_JFTD2XX_FT_DEVICE_NOT_FOUND
#define jftd2xx_JFTD2XX_FT_DEVICE_NOT_FOUND 2L
#undef jftd2xx_JFTD2XX_FT_DEVICE_NOT_OPENED
#define jftd2xx_JFTD2XX_FT_DEVICE_NOT_OPENED 3L
#undef jftd2xx_JFTD2XX_FT_IO_ERROR
#define jftd2xx_JFTD2XX_FT_IO_ERROR 4L
#undef jftd2xx_JFTD2XX_FT_INSUFFICIENT_RESOURCES
#define jftd2xx_JFTD2XX_FT_INSUFFICIENT_RESOURCES 5L
#undef jftd2xx_JFTD2XX_FT_INVALID_PARAMETER
#define jftd2xx_JFTD2XX_FT_INVALID_PARAMETER 6L
#undef jftd2xx_JFTD2XX_FT_INVALID_BAUD_RATE
#define jftd2xx_JFTD2XX_FT_INVALID_BAUD_RATE 7L
#undef jftd2xx_JFTD2XX_FT_DEVICE_NOT_OPENED_FOR_ERASE
#define jftd2xx_JFTD2XX_FT_DEVICE_NOT_OPENED_FOR_ERASE 8L
#undef jftd2xx_JFTD2XX_FT_DEVICE_NOT_OPENED_FOR_WRITE
#define jftd2xx_JFTD2XX_FT_DEVICE_NOT_OPENED_FOR_WRITE 9L
#undef jftd2xx_JFTD2XX_FT_FAILED_TO_WRITE_DEVICE
#define jftd2xx_JFTD2XX_FT_FAILED_TO_WRITE_DEVICE 10L
#undef jftd2xx_JFTD2XX_FT_EEPROM_READ_FAILED
#define jftd2xx_JFTD2XX_FT_EEPROM_READ_FAILED 11L
#undef jftd2xx_JFTD2XX_FT_EEPROM_WRITE_FAILED
#define jftd2xx_JFTD2XX_FT_EEPROM_WRITE_FAILED 12L
#undef jftd2xx_JFTD2XX_FT_EEPROM_ERASE_FAILED
#define jftd2xx_JFTD2XX_FT_EEPROM_ERASE_FAILED 13L
#undef jftd2xx_JFTD2XX_FT_EEPROM_NOT_PRESENT
#define jftd2xx_JFTD2XX_FT_EEPROM_NOT_PRESENT 14L
#undef jftd2xx_JFTD2XX_FT_EEPROM_NOT_PROGRAMMED
#define jftd2xx_JFTD2XX_FT_EEPROM_NOT_PROGRAMMED 15L
#undef jftd2xx_JFTD2XX_FT_INVALID_ARGS
#define jftd2xx_JFTD2XX_FT_INVALID_ARGS 16L
#undef jftd2xx_JFTD2XX_FT_OTHER_ERROR
#define jftd2xx_JFTD2XX_FT_OTHER_ERROR 17L
/*
 * Class:     jftd2xx_JFTD2XX
 * Method:    open
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_jftd2xx_JFTD2XX_open
  (JNIEnv *, jobject, jint);

/*
 * Class:     jftd2xx_JFTD2XX
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jftd2xx_JFTD2XX_close
  (JNIEnv *, jobject);

/*
 * Class:     jftd2xx_JFTD2XX
 * Method:    read
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_jftd2xx_JFTD2XX_read
  (JNIEnv *, jobject, jbyteArray, jint, jint);

/*
 * Class:     jftd2xx_JFTD2XX
 * Method:    write
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_jftd2xx_JFTD2XX_write
  (JNIEnv *, jobject, jbyteArray, jint, jint);

/*
 * Class:     jftd2xx_JFTD2XX
 * Method:    getQueueStatus
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_jftd2xx_JFTD2XX_getQueueStatus
  (JNIEnv *, jobject);

/*
 * Class:     jftd2xx_JFTD2XX
 * Method:    setLatencyTimer
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_jftd2xx_JFTD2XX_setLatencyTimer
  (JNIEnv *, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif