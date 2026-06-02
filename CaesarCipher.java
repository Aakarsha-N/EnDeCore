public class CaesarCipher extends EncryptionAlgorithm {
    private int shift;
    public CaesarCipher(int shift) {
        this.shift = shift;
    }
    @Override
    public String encrypt(String text, String key) {
        return process(text, shift);
    }
    @Override
    public String decrypt(String text, String key) {
        return process(text, 26 - shift);
    }
    private String process(String text, int shift) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                sb.append((char)((c - base + shift) % 26 + base));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}