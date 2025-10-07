This README is available in other languages:
[🇷🇺 version](README.ru.md)

# File System Management Application

## Purpose
To demonstrate mastery of **SOLID principles** and **design patterns** (behavioral, structural, and creational) through a file system management application.  
The system supports console-based interaction while keeping business logic decoupled from input/output mechanisms.

---

## Overview
The application models interaction with a file system, providing navigation, file and directory management, and flexible output options.  
It supports relative and absolute paths, directory tree visualization, and switching between file systems.  
All operations are performed without relying on third-party libraries, and the design ensures separation of concerns and extendability.

---

## Core Features
- **Navigation**: browse file system using relative and absolute paths.  
- **Directory Listing**: display contents of directories in the console.  
- **File Viewing**: display contents of files in the console.  
- **File Operations**: move, copy, delete, rename files.  
- **Console Interface**: accept commands with flags; parsing is decoupled from file system logic.  
- **Filesystem Abstraction**: supports switching between different file systems (initially only local FS).  
- **Tree Display**: visualize directories as a tree with configurable depth and symbols.  
- **Collision Handling**: handle file name conflicts gracefully.  

---

## Command Semantics
- `connect [Address] [-m Mode]` — connect to a filesystem (`Mode`: local).  
- `disconnect` — disconnect from the filesystem.  
- `tree goto [Path]` — navigate to a directory (relative or absolute path).  
- `tree list {-d Depth}` — list directory contents up to specified depth (default 1).  
- `file show [Path] {-m Mode}` — display file contents (`Mode`: console).  
- `file move [SourcePath] [DestinationPath]` — move a file to a directory.  
- `file copy [SourcePath] [DestinationPath]` — copy a file to a directory.  
- `file delete [Path]` — delete a file.  
- `file rename [Path] [Name]` — rename a file.

---

## Technologies
- **C# (.NET 8)**
- **xUnit** — unit testing  
- **Patterns Used**: SOLID, behavioral, structural, creational  

---

## Result
The application provides a flexible, console-driven interface for managing files and directories, with operations decoupled from I/O.  
It supports tree visualization, relative and absolute paths, collision handling, and can be extended to additional file systems.
