public interface IFileManager
{
    string? Directory { get; set; }

    Stack<ICommand> Commands { get; }

    void Connect(string path, string mode);

    void Disconnect();

    void GotoPath(string path);

    void ListTree(int depth);

    void CopyFile(string sourcePath, string destinationPath);

    void DeleteFile(string path);

    void MoveFile(string sourcePath, string destinationPath);

    void RenameFile(string path, string newName);

    void ShowFile(string path, string flag, string mode);
}