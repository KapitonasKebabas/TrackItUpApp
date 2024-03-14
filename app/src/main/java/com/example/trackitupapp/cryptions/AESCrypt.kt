import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AESCrypt {
    private const val ALGORITHM = "AES"
    private const val KEY = "1Hbfh667adfDEJ78"

    fun encrypt(value: String): String {
        val key = generateKey()
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedByteValue = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedByteValue, Base64.DEFAULT)
    }

    fun decrypt(value: String): String {
        val key = generateKey()
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decryptedValue64 = Base64.decode(value, Base64.DEFAULT)
        val decryptedByteValue = cipher.doFinal(decryptedValue64)
        return String(decryptedByteValue, Charsets.UTF_8)
    }

    private fun generateKey(): SecretKeySpec {
        val key = KEY.toByteArray(Charsets.UTF_8)
        return SecretKeySpec(key, ALGORITHM)
    }
}
