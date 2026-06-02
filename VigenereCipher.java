public class VigenereCipher extends EncryptionAlgorithm {
    @Override
    public String encrypt(String text, String key) {
        return process(text, key, true);
    }
    @Override
    public String decrypt(String text, String key) {
        return process(text, key, false);
    }
    private String process(String text, String key, boolean enc) {
        StringBuilder sb = new StringBuilder();
        key = key.toLowerCase();
        int j = 0;
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int shift = key.charAt(j % key.length()) - 'a';
                if (!enc) shift = 26 - shift;
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                sb.append((char)((c - base + shift) % 26 + base));
                j++;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}