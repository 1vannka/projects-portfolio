public interface IDisplayDriver
{
    void Clear();

    void SetColor(byte r, byte g, byte b);

    void ShowText(string text);
}