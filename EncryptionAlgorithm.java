public abstract class EncryptionAlgorithm {
    public abstract String encrypt(String text, String key);
    public abstract String decrypt(String text, String key);
}